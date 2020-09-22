package org.traccar.protocol;

import org.traccar.Protocol;
import org.traccar.StringProtocolEncoder;
import org.traccar.model.Command;

public class G15cProtocolEncoder extends StringProtocolEncoder {

    public G15cProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        // nothing to do ...

        return null;
    }

}
