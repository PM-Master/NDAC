package jmpjct.mysql.proto;

import org.junit.*;
import static org.junit.Assert.*;

public class ColCountTest {
    @Test
    public void test1() {
        byte[] packet = new byte[] {
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x05
        };

        ColCount colcount = ColCount.loadFromPacket(packet);

        assertArrayEquals(packet, colcount.toPacket());
        assertEquals(colcount.colCount, 5);
        assertEquals(colcount.sequenceId, 1);
    }
}
