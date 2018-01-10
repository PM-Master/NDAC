package jmpjct.mysql.proto;

import org.junit.*;
import static org.junit.Assert.*;

public class Local_Infile_ResponseTest {
    @Test
    public void test1() {
        byte[] packet = Proto.packet_string_to_bytes(""
            + "05 00 00 00 00 FF FE FD FC"
        );

        Local_Infile_Response pkt = Local_Infile_Response.loadFromPacket(packet);
        
        assertArrayEquals(packet, pkt.toPacket());
    }
}
