package jmpjct.mysql.proto;

import java.util.ArrayList;

public class Com_Shutdown extends Packet {
    public long shutdownType = Flags.SHUTDOWN_DEFAULT;
    
    public ArrayList<byte[]> getPayload() {
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_byte(Flags.COM_SHUTDOWN));
        if (this.shutdownType != Flags.SHUTDOWN_DEFAULT)
            payload.add(Proto.build_fixed_int(1, this.shutdownType));
        
        return payload;
    }
    
    public static Com_Shutdown loadFromPacket(byte[] packet) {
        Com_Shutdown obj = new Com_Shutdown();
        Proto proto = new Proto(packet, 3);
        
        obj.sequenceId = proto.get_fixed_int(1);
        proto.get_filler(1);
        obj.shutdownType = proto.get_fixed_int(1);
        
        return obj;
    }
}
