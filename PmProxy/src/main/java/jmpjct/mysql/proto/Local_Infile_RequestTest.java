package jmpjct.mysql.proto;

import org.junit.*;
import static org.junit.Assert.*;

public class Local_Infile_RequestTest {
    @Test
    public void test1() {
        byte[] packet = Proto.packet_string_to_bytes(""
            + "0c 00 00 01 fb 2f 65 74    63 2f 70 61 73 73 77 64"
        );

        Local_Infile_Request pkt = Local_Infile_Request.loadFromPacket(packet);
        assertArrayEquals(packet, pkt.toPacket());
        assertEquals(pkt.filename, "/etc/passwd");
    }
}
