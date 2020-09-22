package org.traccar.protocol;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.traccar.BaseHttpProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.Protocol;
import org.traccar.model.Position;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.StringReader;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class LacakProtocolDecoder extends BaseHttpProtocolDecoder {

    private static final SimpleDateFormat DF_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public LacakProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {
        List<Position> positions = new LinkedList<>();
        FullHttpRequest request = (FullHttpRequest) msg;
        JsonObject root = Json.createReader(new StringReader(request.content().toString(StandardCharsets.UTF_8)))
                .readObject();
        JsonObject location = root.getJsonObject("location");
        if (location != null) {

            String deviceId = location.getString("device_id");
            if (StringUtils.isBlank(deviceId)) {
                return positions;
            }

            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, deviceId);
            if (deviceSession == null) {
                return positions;
            }

            // [ISO-8601 UTC], eg:  "2015-05-05T04:31:54.123Z"
            String textTimestamp = location.getString("timestamp");
            Calendar calendarTimestamp = javax.xml.bind.DatatypeConverter.parseDateTime(textTimestamp);
            if (calendarTimestamp == null) {
                return positions;
            }

            // create position
            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());
            position.setTime(calendarTimestamp.getTime());
            position.setValid(true);

            JsonObject coords = location.getJsonObject("coords");
            if (coords != null) {

                // latitude is a must value
                if (!coords.isNull("latitude")) {
                    double latitude = coords.getJsonNumber("latitude").doubleValue();
                    position.setLatitude(latitude);
                } else {
                    return positions;
                }

                // longitude is a must value
                if (!coords.isNull("longitude")) {
                    double longitude = coords.getJsonNumber("longitude").doubleValue();
                    position.setLongitude(longitude);
                } else {
                    return positions;
                }

                if (!coords.isNull("accuracy")) {
                    double accuracy = coords.getJsonNumber("accuracy").doubleValue();
                    position.setAccuracy(accuracy);
                }
                if (!coords.isNull("speed")) {
                    double speed = coords.getJsonNumber("speed").doubleValue();
                    position.setSpeed(speed);
                }
                if (!coords.isNull("heading")) {
                    double heading = coords.getJsonNumber("heading").doubleValue();
                    position.setCourse(heading);
                }
                if (!coords.isNull("altitude")) {
                    double altitude = coords.getJsonNumber("altitude").doubleValue();
                    position.setAltitude(altitude);
                }

            } else {
                return positions;
            }

            String uuid = location.getString("uuid");
            position.set("uuid", uuid);

            JsonObject extras = location.getJsonObject("extras");
            if (extras != null) {

            }
            JsonObject activity = location.getJsonObject("activity");
            if (activity != null) {

            }
            JsonObject geofence = location.getJsonObject("geofence");
            if (geofence != null) {

            }
            JsonObject battery = location.getJsonObject("battery");
            if (battery != null) {

            }

            String event = location.getString("event");
            position.set(Position.KEY_EVENT, event);

            if (!location.isNull("is_moving")) {
                boolean moving = location.getBoolean("is_moving");
                position.set(Position.KEY_MOTION, moving);
            }

            // if (!location.isNull("is_heartbeat")) {
            //     boolean heartbeat = location.getBoolean("is_heartbeat");
            // }

            if (!location.isNull("odometer")) {
                double odometer = location.getJsonNumber("odometer").doubleValue();
                position.set(Position.KEY_ODOMETER, odometer);
            }

            positions.add(position);

        }
        sendResponse(channel, HttpResponseStatus.OK);
        return positions;
    }

}
