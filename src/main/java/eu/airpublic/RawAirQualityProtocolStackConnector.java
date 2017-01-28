package eu.airpublic;

import fr.cea.sna.gateway.core.model.ResourceConfig;
import fr.cea.sna.gateway.generic.core.ProtocolStackConnector;
import fr.cea.sna.gateway.generic.core.Task;
import fr.cea.sna.gateway.generic.core.packet.Packet;
import fr.cea.sna.gateway.generic.local.ServicesEnumeration;
import fr.cea.sna.gateway.sthbnd.http.HttpProtocolStackConnector;
import fr.cea.sna.gateway.sthbnd.http.HttpRequest;
import fr.cea.sna.gateway.sthbnd.http.chain.json.JSONHttpChainedTask;
import fr.cea.sna.gateway.sthbnd.http.chain.json.JSONHttpChainedTasks;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by john on 27/01/17.
 */
public class RawAirQualityProtocolStackConnector extends HttpProtocolStackConnector {

    public RawAirQualityProtocolStackConnector(AbstractMediator mediator) throws ParserConfigurationException, SAXException, IOException {
        super(mediator);
    }

    public Task.Get createGetTask(AbstractMediator abstractMediator, String s, ResourceConfig resourceConfig, Object[] objects) {
        return null;
    }

    public Task.Set createSetTask(AbstractMediator abstractMediator, String s, ResourceConfig resourceConfig, Object[] objects) {
        return null;
    }

    public Task.Act createActTask(AbstractMediator abstractMediator, String s, ResourceConfig resourceConfig, Object[] objects) {
        return null;
    }

    public Task.Subscribe createSubscribeTask(AbstractMediator abstractMediator, String s, ResourceConfig resourceConfig, Object[] objects) {
        return null;
    }

    public Task.Unsubscribe createUnsubscribeTask(AbstractMediator abstractMediator, String s, ResourceConfig resourceConfig, Object[] objects) {
        return null;
    }

//    class ServicesEnumerationTask extends JSONHttpChainedTask<HttpRequest> {
//
//    }

    public Task.ServicesEnumeration createServicesEnumerationTask(AbstractMediator abstractMediator, String s, Object[] objects) {
//        return new JSONHttpChainedTask<HttpRequest>(mediator, HttpRequest.class, null, null) {
//
//        };
//        return new ServicesEnumeration(this.mediator, this, null, new Object[0]) {
//            public void execute() {
//                super.execute();
//                this.result = new String[]{"airQuality"};
//            }
//        };
        return null;
    }
}
