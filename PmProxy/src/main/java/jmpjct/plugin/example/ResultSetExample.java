package jmpjct.plugin.example;

/*
 * Example plugin. Return a fake result set for every query
 */

import jmpjct.Engine;
import jmpjct.mysql.proto.Column;
import jmpjct.mysql.proto.Flags;
import jmpjct.mysql.proto.ResultSet;
import jmpjct.mysql.proto.Row;
import jmpjct.plugin.Base;
import org.apache.log4j.Logger;

public class ResultSetExample extends Base {

    public void init(Engine context) {
        this.logger = Logger.getLogger("Plugin.Example.ResultSetExample");
    }
    
    public void read_query(Engine context) {
        this.logger.info("Plugin->read_query");
        
        ResultSet rs = new ResultSet();
        
        Column col = new Column("Fake Data");
        rs.addColumn(col);
        
        rs.addRow(new Row("1"));
        
        context.clear_buffer();
        context.buffer = rs.toPackets();
        context.nextMode = Flags.MODE_SEND_QUERY_RESULT;
    }
}
