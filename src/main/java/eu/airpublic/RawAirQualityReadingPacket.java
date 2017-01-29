package eu.airpublic;

import fr.cea.sna.gateway.protocol.http.server.HttpContent;
import fr.cea.sna.gateway.sthbnd.http.HttpRequestPacket;

/**
 * Created by john on 25/01/17.
 */
public class RawAirQualityReadingPacket extends HttpRequestPacket implements HttpContent {

    public RawAirQualityReadingPacket(byte[] content) {
        super(content);
    }

    public void setContent(byte[] bytes) {
        System.out.println("Setting content to "+bytes.toString());
        int length = bytes==null?0:bytes.length;

        this.content = new byte[length];

        if(length > 0)
        {
            System.arraycopy(bytes, 0, this.content, 0, length);
        }

        System.out.println("Content is now "+this.content.toString());

    }

    public byte[] getContent() {
        System.out.println("Getting content as "+this.content.toString());

        int length = content==null?0:content.length;

        byte[] content = new byte[length];

        if(length > 0)
        {
            System.arraycopy(this.content, 0, content, 0, length);
        }

        return content;
    }

}
