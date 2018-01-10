package jmpjct.mysql.proto;

import org.junit.*;
import static org.junit.Assert.*;

public class Com_SleepTest {
    @Test
    public void test1() {
        byte[] packet = Proto.packet_string_to_bytes(""
            + "01 00 00 00 00"
        );

        Com_Sleep pkt = Com_Sleep.loadFromPacket(packet);
        assertArrayEquals(packet, pkt.toPacket());
    }
}
