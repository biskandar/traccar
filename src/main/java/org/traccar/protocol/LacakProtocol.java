package org.traccar.protocol;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.traccar.BaseProtocol;
import org.traccar.PipelineBuilder;
import org.traccar.TrackerServer;

public class LacakProtocol extends BaseProtocol {

    public LacakProtocol() {
        addServer(new TrackerServer(false, getName()) {
            @Override
            protected void addProtocolHandlers(PipelineBuilder pipeline) {
                pipeline.addLast(new HttpResponseEncoder());
                pipeline.addLast(new HttpRequestDecoder(8192, 8192, 128 * 1024));
                pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
                pipeline.addLast(new LacakProtocolDecoder(LacakProtocol.this));
            }
        });
    }

}
