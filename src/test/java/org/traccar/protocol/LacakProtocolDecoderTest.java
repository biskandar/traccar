package org.traccar.protocol;

import io.netty.handler.codec.http.HttpMethod;
import org.junit.Test;
import org.traccar.ProtocolTest;

public class LacakProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        LacakProtocolDecoder decoder = new LacakProtocolDecoder(null);

        verifyPositions(decoder, request(HttpMethod.POST, "/",
                buffer("{\"location\":{\"event\":\"motionchange\",\"is_moving\":true,\"uuid\":\"f01b376b-a937-4bbf-b056-4dad09230dc9\",\"timestamp\":\"2020-09-15T14:17:16.000Z\",\"odometer\":0,\"coords\":{\"latitude\":-6.1075334,\"longitude\":106.7776274,\"accuracy\":17.7,\"speed\":0.01,\"heading\":304.39,\"altitude\":53.9},\"activity\":{\"type\":\"still\",\"confidence\":100},\"battery\":{\"is_charging\":true,\"level\":0.76},\"extras\":{}}}")));

        verifyPositions(decoder, request(HttpMethod.POST, "/",
                buffer("{\"location\":{\"event\":\"motionchange\",\"is_moving\":true,\"uuid\":\"f01b376b-a937-4bbf-b056-4dad09230dc9\",\"timestamp\":\"2020-09-15T14:17:16.000Z\",\"odometer\":0,\"coords\":{\"latitude\":-6.1075334,\"longitude\":106.7776274,\"accuracy\":17.7,\"speed\":0.01,\"heading\":304.39,\"altitude\":53.9},\"activity\":{\"type\":\"still\",\"confidence\":100},\"battery\":{\"is_charging\":true,\"level\":0.76},\"extras\":{}}}")));

    }

}