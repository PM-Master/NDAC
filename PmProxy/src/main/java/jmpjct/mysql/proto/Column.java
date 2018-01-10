package jmpjct.mysql.proto;

import java.util.ArrayList;

public class Column extends Packet {
    public String catalog = "def";
    public String schema = "";
    public String table = "";
    public String org_table = "";
    public String name = "";
    public String org_name = "";
    public long characterSet = 0;
    public long columnLength = 0;
    public long type = Flags.MYSQL_TYPE_VAR_STRING;
    public long flags = 0;
    public long decimals = 31;
    
    public Column() {}
    
    public Column(String name) {
        // Set this up by default. Allow overrides if needed
        this.characterSet = ResultSet.characterSet;
        this.name = name;
    }
    
    public ArrayList<byte[]> getPayload() {
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_lenenc_str(this.catalog));
        payload.add(Proto.build_lenenc_str(this.schema));
        payload.add(Proto.build_lenenc_str(this.table));
        payload.add(Proto.build_lenenc_str(this.org_table));
        payload.add(Proto.build_lenenc_str(this.name));
        payload.add(Proto.build_lenenc_str(this.org_name));
        payload.add(Proto.build_filler(1, (byte)0x0c));
        payload.add(Proto.build_fixed_int(2, this.characterSet));
        payload.add(Proto.build_fixed_int(4, this.columnLength));
        payload.add(Proto.build_fixed_int(1, this.type));
        payload.add(Proto.build_fixed_int(2, this.flags));
        payload.add(Proto.build_fixed_int(1, this.decimals));
        payload.add(Proto.build_filler(2));
        
        return payload;
    }
    
    public static Column loadFromPacket(byte[] packet) {
        Column obj = new Column();
        Proto proto = new Proto(packet, 3);
        
        obj.sequenceId = proto.get_fixed_int(1);
        obj.catalog = proto.get_lenenc_str();
        obj.schema = proto.get_lenenc_str();
        obj.table = proto.get_lenenc_str();
        obj.org_table = proto.get_lenenc_str();
        obj.name = proto.get_lenenc_str();
        obj.org_name = proto.get_lenenc_str();
        proto.get_filler(1);
        obj.characterSet = proto.get_fixed_int(2);
        obj.columnLength = proto.get_fixed_int(4);
        obj.type = proto.get_fixed_int(1);
        obj.flags = proto.get_fixed_int(2);
        obj.decimals = proto.get_fixed_int(1);
        proto.get_filler(2);
        
        return obj;
    }
}
