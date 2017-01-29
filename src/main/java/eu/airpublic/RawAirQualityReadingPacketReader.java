package eu.airpublic;

import fr.cea.sna.gateway.generic.core.InvalidPacketException;
import fr.cea.sna.gateway.generic.core.packet.Packet;
import fr.cea.sna.gateway.generic.core.packet.SimplePacketReader;
import fr.cea.sna.gateway.util.mediator.AbstractMediator;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by john on 25/01/17.
 */
public class RawAirQualityReadingPacketReader extends SimplePacketReader{

    protected RawAirQualityReadingPacketReader(AbstractMediator mediator) {
        super(mediator);
    }

    private Map<String, String> parseFormEncodedData(String content) throws InvalidPacketException {

        String[] pairs = content.split("\\&");

        Map<String, String> attrs = new HashMap<String, String>();

        for (int i = 0; i < pairs.length; i++) {
            String[] fields = pairs[i].split("=");
            String name = null;

            try {
                name = URLDecoder.decode(fields[0], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new InvalidPacketException();
            }

            String value = null;

            try {
                value = URLDecoder.decode(fields[1], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new InvalidPacketException();
            }

            attrs.put(name, value);

            this.mediator.warn("pair: " + name + ": " + value);
        }

        return attrs;
    }

    private String processId(Map<String, String> attrs) throws InvalidPacketException {

        // Get sensor id, a +ve int
        String sensorIdString = attrs.get("id");

        if (sensorIdString == null) {
            throw new InvalidPacketException();
        }

        int sensorId = Integer.parseInt(sensorIdString);

        if (sensorId < 1) {
            throw new InvalidPacketException();
        }

        String spid = "airpublic_"+sensorId;
        super.setServiceProviderId(spid);
        super.isHelloMessage(true);
        super.setServiceId("admin");
//        super.setResourceId("id");
//        super.setData(sensorId);
        super.configure();

        return spid;
    }

    private void processLocation(String spid, Map<String, String> attrs) throws InvalidPacketException {
        // Get latitude and longitude

        super.setServiceProviderId(spid);
        super.setServiceId("admin");
        super.setResourceId("location");


        String gpsFixStr = attrs.get("gps_fix");
        boolean gpsFix = false;

        System.out.println("gps_fix "+gpsFixStr);

        if (gpsFixStr != null) {
            gpsFix = (Integer.parseInt(gpsFixStr) != 0);
        }

        if (gpsFix) {
            String lonStr = attrs.get("longitude");
            String latStr = attrs.get("latitude");

            if (lonStr == null || latStr == null) {
                throw new InvalidPacketException();
            }

            float lon = Float.parseFloat(lonStr);
            float lat = Float.parseFloat(latStr);

            String location = String.format("%f:%f", lat, lon);
            System.out.println("SET location "+location);

            super.setData(location);
            super.configure();

        } else {
            System.out.println("SET location null");

            super.setData(null);
            super.configure();
        }
    }

    public void parse(Packet packet) throws InvalidPacketException {

        Map<String, String> attrs = parseFormEncodedData(new String(packet.getBytes()));
        try {
            String content = new String(packet.getBytes());

            this.mediator.warn("Content Follows:");
            this.mediator.warn(content);

            this.mediator.warn("Parsing air quality packet");

            String floatKeys[] = {"pm1", "pm2_5", "pm10", "w_pm1", "w_pm10", "w_pm2_5", "sample_flow_rate", "sampling_period", "speed", "altitude", "heading", "humidity", "temp", "no2_a", "no2_w", "co_a", "co_w", "pt", "latitude", "longitude"};
            String intKeys[] = {"lonet_bat", "id"};

            // Get sensor id
            String spid = processId(attrs);

            // Get location
            processLocation(spid, attrs);

            // Get all other keys

            String sid = "airpublicUncalibrated";
            // Uncalibrated Airpublic data

            processOtherFloats(spid, sid, attrs, floatKeys);
            processOtherInts(spid, sid, attrs, intKeys);

        } catch (InvalidPacketException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidPacketException();
        }

    }

    private void processOtherInts(String spid, String sid, Map<String, String> attrs, String[] intKeys) {
        for (String s : intKeys) {
            String svalue = attrs.get(s);
            if (svalue != null) {
                super.setServiceProviderId(spid);
                super.setServiceId(sid);
                super.setResourceId(s);

                int value = Integer.parseInt(svalue);

                super.setData(value);

                super.configure();
            }
        }
    }

    private void processOtherFloats(String spid, String sid, Map<String, String> attrs, String[] floatKeys) {
        for (String s : floatKeys) {
            String svalue = attrs.get(s);

            if (svalue != null) {
                super.setServiceProviderId(spid);
                super.setServiceId(sid);
                super.setResourceId(s);

                float value = Float.parseFloat(svalue);
                super.setData(value);
                
                System.out.println(String.format("Setting %s:%s:%s:%f", spid, sid, s, value));

                super.configure();
            }
        }
    }
}

