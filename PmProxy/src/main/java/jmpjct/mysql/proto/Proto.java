package jmpjct.mysql.proto;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.DecoderException;
import org.apache.log4j.Logger;
import java.util.ArrayList;

public class Proto {
    public byte[] packet = null;
    public int offset = 0;

    public Proto(byte[] packet) {
        this.packet = packet;
    }

    public Proto(byte[] packet, int offset) {
        this.packet = packet;
        this.offset = offset;
    }

    public boolean has_remaining_data() {
        return this.packet.length - this.offset > 0;
    }

    public static byte[] build_fixed_int(int size, long value) {
        byte[] packet = new byte[size];

        if (size == 8) {
            packet[0] = (byte) ((value >>  0) & 0xFF);
            packet[1] = (byte) ((value >>  8) & 0xFF);
            packet[2] = (byte) ((value >> 16) & 0xFF);
            packet[3] = (byte) ((value >> 24) & 0xFF);
            packet[4] = (byte) ((value >> 32) & 0xFF);
            packet[5] = (byte) ((value >> 40) & 0xFF);
            packet[6] = (byte) ((value >> 48) & 0xFF);
            packet[7] = (byte) ((value >> 56) & 0xFF);
        }
        else if (size == 6) {
            packet[0] = (byte) ((value >>  0) & 0xFF);
            packet[1] = (byte) ((value >>  8) & 0xFF);
            packet[2] = (byte) ((value >> 16) & 0xFF);
            packet[3] = (byte) ((value >> 24) & 0xFF);
            packet[4] = (byte) ((value >> 32) & 0xFF);
            packet[5] = (byte) ((value >> 40) & 0xFF);
        }
        else if (size == 4) {
            packet[0] = (byte) ((value >>  0) & 0xFF);
            packet[1] = (byte) ((value >>  8) & 0xFF);
            packet[2] = (byte) ((value >> 16) & 0xFF);
            packet[3] = (byte) ((value >> 24) & 0xFF);
        }
        else if (size == 3) {
            packet[0] = (byte) ((value >>  0) & 0xFF);
            packet[1] = (byte) ((value >>  8) & 0xFF);
            packet[2] = (byte) ((value >> 16) & 0xFF);
        }
        else if (size == 2) {
            packet[0] = (byte) ((value >>  0) & 0xFF);
            packet[1] = (byte) ((value >>  8) & 0xFF);
        }
        else if (size == 1) {
            packet[0] = (byte) ((value >>  0) & 0xFF);
        }
        else {
            Logger.getLogger("MySQL.Proto").fatal("Encoding int["+size+"] "+value+" failed!");
            return null;
        }
        return packet;
    }

    public static byte[] build_lenenc_int(long value) {
        byte[] packet = null;

        if (value < 251) {
            packet = new byte[1];
            packet[0] = (byte) ((value >>  0) & 0xFF);
        }
        else if (value < 65535) {
            packet = new byte[3];
            packet[0] = (byte) 0xFC;
            packet[1] = (byte) ((value >>  0) & 0xFF);
            packet[2] = (byte) ((value >>  8) & 0xFF);
        }
        else if (value < 16777215) {
            packet = new byte[4];
            packet[0] = (byte) 0xFD;
            packet[1] = (byte) ((value >>  0) & 0xFF);
            packet[2] = (byte) ((value >>  8) & 0xFF);
            packet[3] = (byte) ((value >> 16) & 0xFF);
        }
        else {
            packet = new byte[9];
            packet[0] = (byte) 0xFE;
            packet[1] = (byte) ((value >>  0) & 0xFF);
            packet[2] = (byte) ((value >>  8) & 0xFF);
            packet[3] = (byte) ((value >> 16) & 0xFF);
            packet[4] = (byte) ((value >> 24) & 0xFF);
            packet[5] = (byte) ((value >> 32) & 0xFF);
            packet[6] = (byte) ((value >> 40) & 0xFF);
            packet[7] = (byte) ((value >> 48) & 0xFF);
            packet[8] = (byte) ((value >> 56) & 0xFF);
        }

        return packet;
    }

    public static byte[] build_lenenc_str(String str) {
        return Proto.build_lenenc_str(str, false);
    }

    public static byte[] build_lenenc_str(String str, boolean base64) {
        if (str.equals("")) {
            byte[] packet = new byte[1];
            packet[0] = 0x00;
            return packet;
        }

        int strsize = str.length();
        if (base64)
            strsize = Base64.decodeBase64(str).length;

        byte[] size = Proto.build_lenenc_int(strsize);
        byte[] strByte = Proto.build_fixed_str(strsize, str, base64);
        byte[] packet = new byte[size.length + strByte.length];
        System.arraycopy(size, 0, packet, 0, size.length);
        System.arraycopy(strByte, 0, packet, size.length, strByte.length);
        return packet;
    }

    public static byte[] build_null_str(String str) {
        return Proto.build_null_str(str, false);
    }

    public static byte[] build_null_str(String str, boolean base64) {
        int size = str.length() + 1;
        if (base64)
            size = Base64.decodeBase64(str).length + 1;
        return Proto.build_fixed_str(str.length() + 1, str, base64);
    }

    public static byte[] build_fixed_str(long size, String str) {
        return Proto.build_fixed_str((int)size, str);
    }

    public static byte[] build_fixed_str(long size, String str, boolean base64) {
        return Proto.build_fixed_str((int)size, str, base64);
    }

    public static byte[] build_fixed_str(int size, String str) {
        return Proto.build_fixed_str(size, str, false);
    }

    public static byte[] build_fixed_str(int size, String str, boolean base64) {
        byte[] packet = new byte[size];
        byte[] strByte = null;

        if (base64)
            strByte = Base64.decodeBase64(str);
        else
            strByte = str.getBytes();

        if (strByte.length < packet.length)
            size = strByte.length;
        System.arraycopy(strByte, 0, packet, 0, size);
        return packet;
    }

    public static byte[] build_eop_str(String str) {
        return Proto.build_eop_str(str, false);
    }

    public static byte[] build_eop_str(String str, boolean base64) {
        int size = str.length();
        if (base64)
            size = Base64.decodeBase64(str).length;
        return Proto.build_fixed_str(size, str, base64);
    }

    public static byte[] build_filler(int len) {
        return Proto.build_filler(len, (byte)0x00);
    }

    public static byte[] build_filler(int len, int filler_value) {
        return Proto.build_filler(len, (byte)filler_value);
    }

    public static byte[] build_filler(int len, byte filler_value) {
        byte[] filler = new byte[len];
        for (int i = 0; i < len; i++)
            filler[i] = filler_value;
        return filler;
    }

    public static byte[] build_byte(byte value) {
        byte[] field = new byte[1];
        field[0] = value;
        return field;
    }

    public static char int2char(byte i) {
        return (char)i;
    }

    public static byte char2int(char i) {
        return (byte)i;
    }

    public long get_fixed_int(int size) {
        byte[] bytes = null;

        if ( this.packet.length < (size + this.offset))
            return -1;

        bytes = new byte[size];
        System.arraycopy(packet, offset, bytes, 0, size);
        this.offset += size;
        return this.get_fixed_int(bytes);
    }

    public static long get_fixed_int(byte[] bytes) {
        long value = 0;

        for (int i = bytes.length-1; i > 0; i--) {
            value |= bytes[i] & 0xFF;
            value <<= 8;
        }
        value |= bytes[0] & 0xFF;

        return value;
    }

    public void get_filler(int size) {
        this.offset += size;
    }

    public long get_lenenc_int() {
        int size = 0;

        // 1 byte int
        if (this.packet[offset] < 251) {
            size = 1;
        }
        // 2 byte int
        else if (this.packet[offset] == 252) {
            this.offset += 1;
            size = 2;
        }
        // 3 byte int
        else if (this.packet[offset] == 253) {
            this.offset += 1;
            size = 3;
        }
        // 8 byte int
        else if (this.packet[offset] == 254) {
            this.offset += 1;
            size = 8;
        }

        if (size == 0) {
            Logger.getLogger("MySQL.Proto").fatal("Decoding int at offset "+offset+" failed!");
            return -1;
        }

        return this.get_fixed_int(size);
    }

    public String get_fixed_str(long len) {
        return this.get_fixed_str((int)len);
    }

    public String get_fixed_str(long len, boolean base64) {
        return this.get_fixed_str((int)len, base64);
    }

    public String get_fixed_str(int len) {
        int start = this.offset;
        int end = this.offset+len;

        if (end > this.packet.length) {
            end = this.packet.length;
            len = end - start;
        }

        StringBuilder str = new StringBuilder(len);

        for (int i = start; i < end; i++) {
            str.append(Proto.int2char(packet[i]));
            this.offset += 1;
        }

        return str.toString();
    }

    public String get_fixed_str(int len, boolean base64) {
        int start = this.offset;
        int end = this.offset+len;

        if (end > this.packet.length) {
            end = this.packet.length;
            len = end - start;
        }

        if (!base64)
            return this.get_fixed_str(len);

        byte[] chunk = new byte[len];
        System.arraycopy(this.packet, start, chunk, 0, len);
        this.offset += len;
        return Base64.encodeBase64String(chunk);
    }

    public String get_null_str() {
        return this.get_null_str(false);
    }

    public String get_null_str(boolean base64) {
        int len = 0;

        for (int i = this.offset; i < this.packet.length; i++) {
            if (packet[i] == 0x00)
                break;
            len += 1;
        }

        String str = this.get_fixed_str(len, base64);
        this.offset += 1;
        return str;
    }

    public String get_eop_str() {
        return this.get_eop_str(false);
    }

    public String get_eop_str(boolean base64) {
        int len = this.packet.length - this.offset;
        return this.get_fixed_str(len, base64);
    }

    public String get_lenenc_str() {
        return this.get_lenenc_str(false);
    }

    public String get_lenenc_str(boolean base64) {
        int len = (int)this.get_lenenc_int();
        return this.get_fixed_str(len, base64);
    }

    public static byte[] arraylist_to_array(ArrayList<byte[]> input) {
        int size = 0;
        for (byte[] field: input)
            size += field.length;

        byte[] result = new byte[size];

        int offset = 0;
        for (byte[] field: input) {
            System.arraycopy(field, 0, result, offset, field.length);
            offset += field.length;
        }

        return result;
    }

    public static byte[] packet_string_to_bytes(String str) {
        byte[] res = null;
        str = str.replaceAll("\\s","").toUpperCase();
        try {
            res = Hex.decodeHex(str.toCharArray());
        } catch (DecoderException e) {}
        return res;
    }
}
