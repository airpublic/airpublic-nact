package eu.airpublic;

// This file is copypasta'd from fr.cea.sna

import com.sun.net.httpserver.*;
import fr.cea.sna.gateway.generic.core.SnaProcessor;
import fr.cea.sna.gateway.protocol.http.server.HttpContent;
import fr.cea.sna.gateway.protocol.http.server.RequestHandler;
import fr.cea.sna.gateway.sthbnd.http.HttpPacket;
import fr.cea.sna.gateway.util.IOUtils;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Server<H extends HttpContent> {
    private static final String ROOT_CONTEXT = "/";
    private static final int DEFAULT_PORT = 8789;
    private final HttpServer server;
    private AbstractMediator mediator;
    private final AtomicBoolean running;
    private int handled;
    private int binded;
    private RequestHandler<H> requestHandler;

    public Server(AbstractMediator mediator, RequestHandler<H> requestHandler, SnaProcessor<HttpPacket> processor) throws IOException {
        this.mediator = mediator;
        this.requestHandler = requestHandler;
        this.server = HttpServer.create();
        this.server.setExecutor((Executor)null);
        this.running = new AtomicBoolean(false);
        this.handled = 0;
        this.binded = 0;
    }

    private void running(boolean running) {
        synchronized(this) {
            this.running.set(running);
        }
    }

    boolean running() {
        boolean running = false;
        synchronized(this) {
            running = this.running.get();
            return running;
        }
    }

    protected void handle() {
        this.handle((String)null);
    }

    protected void handle(String path) {
        if(path == null) {
            path = "/";
        }

        HttpContext context = this.server.createContext(path);
        context.setHandler(new HttpHandler() {
            public void handle(HttpExchange httpExchange) throws IOException {
                int responseCode = !eu.airpublic.Server.this.running()?404:202;
                Headers headers = httpExchange.getRequestHeaders();
                byte[] content = IOUtils.read(eu.airpublic.Server.this.mediator, httpExchange.getRequestBody());
                responseCode = this.close(httpExchange, responseCode);
                if(responseCode == 202) {
                    HttpContent httpContent = eu.airpublic.Server.this.requestHandler.createHttpContent();
                    httpContent.addHeaders(headers);
                    httpContent.setContent(content);

                    try {
                        eu.airpublic.Server.this.requestHandler.process((H) httpContent);
                    } catch (Exception var7) {
                        if(eu.airpublic.Server.this.mediator.isErrorLoggable()) {
                            eu.airpublic.Server.this.mediator.error("this exception should never be thrown");
                        }
                    }

                }
            }

            private int close(HttpExchange httpExchange, int responseCode) {
                int code = responseCode;

                try {
                    httpExchange.sendResponseHeaders(code, 0L);
                } catch (IOException var8) {
                    code = 500;
                    if(eu.airpublic.Server.this.mediator.isErrorLoggable()) {
                        eu.airpublic.Server.this.mediator.error(var8.getMessage(), var8);
                    }
                } finally {
                    httpExchange.close();
                }

                return code;
            }
        });
        ++this.handled;
    }

    public void start() throws IOException {
        if(this.handled == 0) {
            this.handle();
        }

        if(this.binded == 0) {
            this.bind();
        }

        this.running(true);
        this.server.start();
    }

    protected void start(String clientURL) throws IOException {
        this.bind(new URL(clientURL));
        this.start();
    }

    public void start(String ipAddress, int port) throws IOException {
        this.bind(ipAddress, port);
        this.start();
    }

    protected void bind() throws IOException {
        Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while(true) {
            NetworkInterface networkInterface;
            do {
                if(!networkInterfaces.hasMoreElements()) {
                    return;
                }

                networkInterface = (NetworkInterface)networkInterfaces.nextElement();
            } while(networkInterface.isLoopback());

            Enumeration inetAddresses = networkInterface.getInetAddresses();

            while(inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = (InetAddress)inetAddresses.nextElement();
                this.bind(inetAddress.getHostAddress(), 8789);
            }
        }
    }

    public void bind(URL url) throws IOException {
        InetAddress remote = InetAddress.getByName(url.getHost());
        Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while(true) {
            NetworkInterface networkInterface;
            do {
                if(!networkInterfaces.hasMoreElements()) {
                    return;
                }

                networkInterface = (NetworkInterface)networkInterfaces.nextElement();
            } while(!remote.isReachable(networkInterface, 0, 1000));

            Enumeration inetAddresses = networkInterface.getInetAddresses();

            while(inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = (InetAddress)inetAddresses.nextElement();
                this.bind(inetAddress.getHostAddress(), 8789);
            }
        }
    }

    public void bind(String ipAddress, int port) throws IOException {
        this.server.bind(new InetSocketAddress(ipAddress, port), 0);
        ++this.binded;
        if(this.mediator.isInfoLoggable()) {
            this.mediator.info("server binded on " + ipAddress + " / port " + port);
        }

    }

    public void stop() {
        this.running(false);
        this.server.stop(0);
    }
}