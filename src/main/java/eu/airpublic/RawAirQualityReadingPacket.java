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

    public byte[] getContent() {
        return new byte[0];
    }

    public void setContent(byte[] bytes) {
        int length = content==null?0:content.length;

        this.content = new byte[length];

        if(length > 0)
        {
            System.arraycopy(content, 0, this.content, 0, length);
        }

    }

    public byte[] getcontent() {
        int length = content==null?0:content.length;

        byte[] content = new byte[length];

        if(length > 0)
        {
            System.arraycopy(this.content, 0, content, 0, length);
        }

        return content;
    }

}
