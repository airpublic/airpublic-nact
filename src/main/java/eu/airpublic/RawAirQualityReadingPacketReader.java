package eu.airpublic;

import fr.cea.sna.gateway.generic.core.InvalidPacketException;
import fr.cea.sna.gateway.generic.core.packet.Packet;
import fr.cea.sna.gateway.generic.core.packet.SimplePacketReader;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;
import org.json.JSONObject;

import static jdk.nashorn.internal.objects.Global.print;

/**
 * Created by john on 25/01/17.
 */
public class RawAirQualityReadingPacketReader extends SimplePacketReader{

    protected RawAirQualityReadingPacketReader(AbstractMediator mediator) {
        super(mediator);
    }

    public void parse(Packet packet) throws InvalidPacketException {


        //JSONObject jsonObject = new JSONObject(new String(packet.getBytes()));

        this.mediator.warn("Parsing air quality packet");
        super.setServiceProviderId("null");
        super.setServiceId("airQuality");
        super.setResourceId("temperature");
        //super.setData(jsonObject.opt("temperature"));
        super.setData(3.4);
        super.configure();
    }
}
