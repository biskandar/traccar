package org.traccar.protocol;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.traccar.BaseProtocol;
import org.traccar.CharacterDelimiterFrameDecoder;
import org.traccar.PipelineBuilder;
import org.traccar.TrackerServer;

public class G15cProtocol extends BaseProtocol {

    public G15cProtocol() {
        addServer(new TrackerServer(false, getName()) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline) {
                pipeline.addLast(new CharacterDelimiterFrameDecoder(1024, "*#", "*", "#"));
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new G15cProtocolEncoder(G15cProtocol.this));
                pipeline.addLast(new G15cProtocolDecoder(G15cProtocol.this));
            }
        });
    }

}
