package eu.airpublic;

import fr.cea.sna.gateway.generic.core.InvalidPacketException;
import fr.cea.sna.gateway.generic.core.SnaManager;
import fr.cea.sna.gateway.generic.core.impl.PacketReaderFactory;
import fr.cea.sna.gateway.generic.core.packet.Packet;
import fr.cea.sna.gateway.generic.core.packet.PacketReader;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;

/**
 * Created by john on 25/01/17.
 */
public class RawAirQualityReadingPacketReaderFactory implements PacketReaderFactory<RawAirQualityReadingPacket> {
    public boolean handle(Class<? extends Packet> packetType) {
        return RawAirQualityReadingPacket.class.isAssignableFrom(packetType);
    }

    public PacketReader<RawAirQualityReadingPacket> newInstance(AbstractMediator mediator, SnaManager manager, RawAirQualityReadingPacket packet) throws InvalidPacketException {
        RawAirQualityReadingPacketReader packetReader = new RawAirQualityReadingPacketReader(mediator);
        packetReader.parse(packet);
        return packetReader;
    }
}
