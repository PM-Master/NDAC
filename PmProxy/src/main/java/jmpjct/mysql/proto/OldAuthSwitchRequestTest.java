package jmpjct.mysql.proto;

import org.junit.*;
import static org.junit.Assert.*;

public class OldAuthSwitchRequestTest {
    @Test
    public void test1() {
        byte[] packet = Proto.packet_string_to_bytes(""
            + "01 00 00 02 fe"
        );

        OldAuthSwitchRequest pkt = OldAuthSwitchRequest.loadFromPacket(packet);
        assertArrayEquals(packet, pkt.toPacket());
    }
}
