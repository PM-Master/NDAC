package jmpjct.mysql.proto;

import java.io.*;
import java.util.ArrayList;

import org.apache.commons.io.HexDump;
import org.apache.log4j.Logger;

public abstract class Packet {
    public long sequenceId = 0;

    public abstract ArrayList<byte[]> getPayload();
    
    public byte[] toPacket() {
        ArrayList<byte[]> payload = this.getPayload();
        
        int size = 0;
        for (byte[] field: payload)
            size += field.length;
        
        byte[] packet = new byte[size+4];
        
        System.arraycopy(Proto.build_fixed_int(3, size), 0, packet, 0, 3);
        System.arraycopy(Proto.build_fixed_int(1, this.sequenceId), 0, packet, 3, 1);
        
        int offset = 4;
        for (byte[] field: payload) {
            System.arraycopy(field, 0, packet, offset, field.length);
            offset += field.length;
        }
        
        return packet;
    }
    
    public static int getSize(byte[] packet) {
        int size = (int) new Proto(packet).get_fixed_int(3);
        return size;
    }
    
    public static byte getType(byte[] packet) {
        return packet[4];
    }
    
    public static long getSequenceId(byte[] packet) {
        return new Proto(packet, 3).get_fixed_int(1);
    }
    
    public static final void dump(byte[] packet) {
        Logger logger = Logger.getLogger("MySQL.Packet");
        
        if (!logger.isTraceEnabled())
            return;
        
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            HexDump.dump(packet, 0, out, 0);
            logger.trace("Dumping packet\n"+out.toString());
        }
        catch (IOException e) {
            return;
        }
    }
    
    public static final void dump_stderr(byte[] packet) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            HexDump.dump(packet, 0, out, 0);
            System.err.println("Dumping packet\n" + out.toString());
        }
        catch (IOException e) {
            return;
        }
    }

    public static byte[] read_packet(InputStream in) throws IOException {
        int b = 0;
        int size = 0;
        byte[] packet = new byte[3];
        
        // Read size (3)
        int offset = 0;
        int target = 3;
        do {
            b = in.read(packet, offset, (target - offset));
            if (b == -1) {
                throw new IOException();
            }
            offset += b;
        } while (offset != target);
        
        size = Packet.getSize(packet);
        
        byte[] packet_tmp = new byte[size+4];
        System.arraycopy(packet, 0, packet_tmp, 0, 3);
        packet = packet_tmp;
        packet_tmp = null;
        
        target = packet.length;
        String x = new String(packet);
        do {
            b = in.read(packet, offset, (target - offset));
            if (b == -1) {
                throw new IOException();
            }
            offset += b;
        } while (offset != target);
        
        return packet;
    }
    
    public static ArrayList<byte[]> read_full_result_set(InputStream in, OutputStream out, ArrayList<byte[]> buffer, boolean bufferResultSet) throws IOException {
        // Assume we have the start of a result set already
        
        byte[] packet = buffer.get((buffer.size()-1));
        long colCount = ColCount.loadFromPacket(packet).colCount;
        
        // Read the columns and the EOF field
        for (int i = 0; i < (colCount+1); i++) {
            packet = Packet.read_packet(in);
            if (packet == null) {
                throw new IOException();
            }
            buffer.add(packet);
            
            // Evil optimization
            if (!bufferResultSet) {
                Packet.write(out, buffer);
                out.flush();
                buffer.clear();
            }
        }
        
        int packedPacketSize = 65535;
        byte[] packedPacket = new byte[packedPacketSize];
        int position = 0;
        
        while (true) {
            packet = Packet.read_packet(in);
            
            if (packet == null) {
                throw new IOException();
            }
            
            int packetType = Packet.getType(packet);
            
            if (packetType == Flags.EOF || packetType == Flags.ERR) {
                byte[] newPackedPacket = new byte[position];
                System.arraycopy(packedPacket, 0, newPackedPacket, 0, position);
                buffer.add(newPackedPacket);
                packedPacket = packet;
                break;
            }
            
            if (position+packet.length > packedPacketSize) {
                int subsize = packedPacketSize - position;
                System.arraycopy(packet, 0, packedPacket, position, subsize);
                buffer.add(packedPacket);
                
                // Evil optimization
                if (!bufferResultSet) {
                    Packet.write(out, buffer);
                    out.flush();
                    buffer.clear();
                }
                
                packedPacket = new byte[packedPacketSize];
                position = 0;
                System.arraycopy(packet, subsize, packedPacket, position, packet.length-subsize);
                position += packet.length-subsize;
            }
            else {
                System.arraycopy(packet, 0, packedPacket, position, packet.length);
                position += packet.length;
            }
        }
        buffer.add(packedPacket);
        
        // Evil optimization
        if (!bufferResultSet) {
            Packet.write(out, buffer);
            buffer.clear();
            out.flush();
        }
        
        if (Packet.getType(packet) == Flags.ERR) {
            return buffer;
        }
        
        if (EOF.loadFromPacket(packet).hasStatusFlag(Flags.SERVER_MORE_RESULTS_EXISTS)) {
            buffer.add(Packet.read_packet(in));
            buffer = Packet.read_full_result_set(in, out, buffer, bufferResultSet);
        }
        return buffer;
    }
    
    public static void write(OutputStream out, ArrayList<byte[]> buffer) throws IOException {
        for (byte[] packet: buffer) {
            //System.out.println(">" + new String(packet));
            out.write(packet);
        }
    }
    
}
