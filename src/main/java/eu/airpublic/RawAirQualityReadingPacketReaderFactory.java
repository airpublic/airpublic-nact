package eu.airpublic;

import fr.cea.sna.gateway.generic.core.InvalidPacketException;
import fr.cea.sna.gateway.generic.core.SnaManager;
import fr.cea.sna.gateway.generic.core.impl.PacketReaderFactory;
import fr.cea.sna.gateway.generic.core.packet.Packet;
import fr.cea.sna.gateway.generic.core.packet.PacketReader;
import fr.cea.sna.gateway.sthbnd.http.HttpPacket;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;

/**
 * Created by john on 25/01/17.
 */
public class RawAirQualityReadingPacketReaderFactory implements 
PacketReaderFactory<HttpPacket>
{
    public boolean handle(Class<? extends Packet> packetType)
    {
        return HttpPacket.class.isAssignableFrom(packetType);
    }

    public PacketReader<HttpPacket> newInstance(AbstractMediator mediator, 
    		SnaManager manager, HttpPacket packet)
    				throws InvalidPacketException 
    {
        RawAirQualityReadingPacketReader packetReader =
        		new RawAirQualityReadingPacketReader(mediator);
        
        packetReader.parse(packet);
        return packetReader;
    }
}
