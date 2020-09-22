package org.traccar.protocol;

import io.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.Protocol;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class G15cProtocolDecoder extends BaseProtocolDecoder {

    private SimpleDateFormat dtmFormatter = new SimpleDateFormat("HHmmssddMMyy");

    public G15cProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {
        Object result = null;

        // prepare message as channel buffer
        String str = (String) msg;

        // make sure it is clean string
        if ((str == null) || (str.equals(""))) {
            return result;
        }

        // split
        String[] arr = str.split(",", -1);

        // the length must not less than 3
        if (arr.length < 3) {
            return result;
        }

        // manufacture
        String manufacture = arr[0];
        if ((manufacture == null) || (manufacture.equals(""))) {
            return result;
        }

        // imei
        String imei = arr[1];
        if ((imei == null) || (imei.equals(""))) {
            return result;
        }

        // version
        String version = arr[2];
        if ((version == null) || (version.equals(""))) {
            return result;
        }

        // decode based on version
        if (version.equalsIgnoreCase("V1")) {
            result = decodeV1(channel, remoteAddress, arr, manufacture, imei);
        }

        return result;
    }

    private Position decodeV1(Channel channel, SocketAddress remoteAddress, String[] arr, String manufacture, String imei) throws Exception {

        // device time
        String deviceTime = arr[3];  // HHMMSS
        String deviceDate = arr[11]; // DDMMYY
        Date deviceDtm = dtmFormatter.parse(deviceTime.concat(deviceDate));

        // data significance
        String dataSignificance = arr[4];
        boolean valid = dataSignificance.equalsIgnoreCase("A");

        // latitude
        String latitudeStr = arr[5];
        String latitudeDegree = latitudeStr.substring(0, 2);
        String latitudeMins = latitudeStr.substring(2);
        String latitudeFlag = arr[6];
        double latitude = Integer.parseInt(latitudeDegree);
        latitude += Double.parseDouble(latitudeMins) / 60.0;
        latitude *= latitudeFlag.equalsIgnoreCase("N") ? 1 : -1;

        // longitude
        String longitudeStr = arr[7];
        String longitudeDegree = longitudeStr.substring(0, 3);
        String longitudeMins = longitudeStr.substring(3);
        String longitudeFlag = arr[8];
        double longitude = Integer.parseInt(longitudeDegree);
        longitude += Double.parseDouble(longitudeMins) / 60.0;
        longitude *= longitudeFlag.equalsIgnoreCase("E") ? 1 : -1;

        // speed
        String speedStr = arr[9];
        double speedDbl = Double.parseDouble(speedStr);

        // direction
        String direction = arr[10];
        double directionDbl = Double.parseDouble(direction);

        // vehicle status
        String vehicleStatus = arr[12];

        // register device and store position
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);
        if (deviceSession == null) {
            return null;
        }

        // create position
        Position position = new Position(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());
        position.setValid(valid);
        position.setTime(deviceDtm);
        position.setLatitude(latitude);
        position.setLongitude(longitude);
        position.setSpeed(speedDbl);
        position.setCourse(directionDbl);
        position.set(Position.KEY_EVENT, vehicleStatus);
        return position;
    }

}
