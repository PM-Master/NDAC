package jmpjct.mysql.proto;

import org.junit.*;
import static org.junit.Assert.*;

public class Com_QuitTest {
    @Test
    public void test1() {
        byte[] packet = Proto.packet_string_to_bytes(""
            + "01 00 00 00 01"
        );

        Com_Quit pkt = Com_Quit.loadFromPacket(packet);
        assertArrayEquals(packet, pkt.toPacket());
    }
}
