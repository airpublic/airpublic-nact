package eu.airpublic;

import fr.cea.sna.gateway.generic.core.InvalidPacketException;
import fr.cea.sna.gateway.generic.core.SnaProcessor;
import fr.cea.sna.gateway.protocol.http.server.RequestHandler;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;

/**
 * Created by john on 27/01/17.
 */
public class RawAirQualityReadingHttpRequestHandler implements RequestHandler<RawAirQualityReadingPacket> {

    AbstractMediator mediator;
    SnaProcessor processor;

    public RawAirQualityReadingHttpRequestHandler(AbstractMediator mediator, SnaProcessor processor) {
        this.mediator = mediator;
        this.processor = processor;
    }

    public void process(RawAirQualityReadingPacket packet) {
        this.mediator.warn("Airpublic: process HTTP request");
        this.mediator.warn(packet.toString());
        try {
            this.processor.process(packet);
        } catch (InvalidPacketException e) {
            this.mediator.warn("Loooool! TIFU!");
            e.printStackTrace();
        }
    }

    public RawAirQualityReadingPacket createHttpContent() {
        this.mediator.warn("Airpublic: LOOOL! I have been asked to respond!");
        return new RawAirQualityReadingPacket(new byte[0]);
    }
}
