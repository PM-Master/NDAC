package jmpjct.plugin.debug;

/*
 * Debug plugin
 * Output packet debugging information
 */

import java.util.ArrayList;

import jmpjct.Engine;
import jmpjct.mysql.proto.Flags;
import jmpjct.mysql.proto.Packet;
import jmpjct.plugin.Base;
import org.apache.log4j.Logger;

public class Debug extends Base {
    public Logger logger = Logger.getLogger("Plugin.Debug");
    
    public void read_handshake(Engine context) {
        this.logger.debug("<- HandshakePacket");
        this.logger.debug("   Server Version: "+context.handshake.serverVersion);
        this.logger.debug("   Connection Id: "+context.handshake.connectionId);
        this.logger.debug("   Server Capability Flags: "
                          + Debug.dump_capability_flags(context.handshake.capabilityFlags));
    }
    
    public void read_auth(Engine context) {
        this.logger.debug("-> AuthResponsePacket");
        this.logger.debug("   Max Packet Size: "+context.authReply.maxPacketSize);
        this.logger.debug("   User: "+context.authReply.username);
        this.logger.debug("   Schema: "+context.authReply.schema);
        
        this.logger.debug("   Client Capability Flags: "
                          + Debug.dump_capability_flags(context.authReply.capabilityFlags));
    }
    
    public void read_query(Engine context) {
        switch (Packet.getType(context.buffer.get(context.buffer.size()-1))) {
            case Flags.COM_QUIT:
                this.logger.info("-> COM_QUIT");
                break;
            
            // Extract out the new default schema
            case Flags.COM_INIT_DB:
                this.logger.info("-> USE "+context.schema);
                break;
            
            // Query
            case Flags.COM_QUERY:
                this.logger.info("-> "+context.query);
                break;
            
            default:
                this.logger.debug("Packet is "+Packet.getType(context.buffer.get(context.buffer.size()-1))+" type.");
                Debug.dump_buffer(context);
                break;
        }
        context.buffer_result_set();
    }
    
    public void read_query_result(Engine context) {
        if (!context.bufferResultSet)
            return;
        
        switch (Packet.getType(context.buffer.get(context.buffer.size()-1))) {
            case Flags.OK:
                this.logger.info("<- OK");
                break;
            
            case Flags.ERR:
                this.logger.info("<- ERR");
                break;
            
            default:
                this.logger.debug("Result set or Packet is "+Packet.getType(context.buffer.get(context.buffer.size()-1))+" type.");
                break;
        }
    }
    
    public static final void dump_buffer(ArrayList<byte[]> buffer) {
        Logger logger = Logger.getLogger("Plugin.Debug");
        
        if (!logger.isTraceEnabled())
            return;
        
        for (byte[] packet: buffer) {
            Packet.dump(packet);
        }
    }
    
    public static final void dump_buffer(Engine context) {
        Logger logger = Logger.getLogger("Plugin.Debug");
        
        if (!logger.isTraceEnabled())
            return;
        
        for (byte[] packet: context.buffer) {
            Packet.dump(packet);
        }
    }
    
    public static final String dump_capability_flags(long capabilityFlags) {
        String out = "";
        if ((capabilityFlags & Flags.CLIENT_LONG_PASSWORD) != 0)
            out += " CLIENT_LONG_PASSWORD";
        if ((capabilityFlags & Flags.CLIENT_FOUND_ROWS) != 0)
            out += " CLIENT_FOUND_ROWS";
        if ((capabilityFlags & Flags.CLIENT_LONG_FLAG) != 0)
            out += " CLIENT_LONG_FLAG";
        if ((capabilityFlags & Flags.CLIENT_CONNECT_WITH_DB) != 0)
            out += " CLIENT_CONNECT_WITH_DB";
        if ((capabilityFlags & Flags.CLIENT_NO_SCHEMA) != 0)
            out += " CLIENT_NO_SCHEMA";
        if ((capabilityFlags & Flags.CLIENT_COMPRESS) != 0)
            out += " CLIENT_COMPRESS";
        if ((capabilityFlags & Flags.CLIENT_ODBC) != 0)
            out += " CLIENT_ODBC";
        if ((capabilityFlags & Flags.CLIENT_LOCAL_FILES) != 0)
            out += " CLIENT_LOCAL_FILES";
        if ((capabilityFlags & Flags.CLIENT_IGNORE_SPACE) != 0)
            out += " CLIENT_IGNORE_SPACE";
        if ((capabilityFlags & Flags.CLIENT_PROTOCOL_41) != 0)
            out += " CLIENT_PROTOCOL_41";
        if ((capabilityFlags & Flags.CLIENT_INTERACTIVE) != 0)
            out += " CLIENT_INTERACTIVE";
        if ((capabilityFlags & Flags.CLIENT_SSL) != 0)
            out += " CLIENT_SSL";
        if ((capabilityFlags & Flags.CLIENT_IGNORE_SIGPIPE) != 0)
            out += " CLIENT_IGNORE_SIGPIPE";
        if ((capabilityFlags & Flags.CLIENT_TRANSACTIONS) != 0)
            out += " CLIENT_TRANSACTIONS";
        if ((capabilityFlags & Flags.CLIENT_RESERVED) != 0)
            out += " CLIENT_RESERVED";
        if ((capabilityFlags & Flags.CLIENT_SECURE_CONNECTION) != 0)
            out += " CLIENT_SECURE_CONNECTION";
        return out;
    }
    
    public static final String dump_status_flags(long statusFlags) {
        String out = "";
        if ((statusFlags & Flags.SERVER_STATUS_IN_TRANS) != 0)
            out += " SERVER_STATUS_IN_TRANS";
        if ((statusFlags & Flags.SERVER_STATUS_AUTOCOMMIT) != 0)
            out += " SERVER_STATUS_AUTOCOMMIT";
        if ((statusFlags & Flags.SERVER_MORE_RESULTS_EXISTS) != 0)
            out += " SERVER_MORE_RESULTS_EXISTS";
        if ((statusFlags & Flags.SERVER_STATUS_NO_GOOD_INDEX_USED) != 0)
            out += " SERVER_STATUS_NO_GOOD_INDEX_USED";
        if ((statusFlags & Flags.SERVER_STATUS_NO_INDEX_USED) != 0)
            out += " SERVER_STATUS_NO_INDEX_USED";
        if ((statusFlags & Flags.SERVER_STATUS_CURSOR_EXISTS) != 0)
            out += " SERVER_STATUS_CURSOR_EXISTS";
        if ((statusFlags & Flags.SERVER_STATUS_LAST_ROW_SENT) != 0)
            out += " SERVER_STATUS_LAST_ROW_SENT";
        if ((statusFlags & Flags.SERVER_STATUS_LAST_ROW_SENT) != 0)
            out += " SERVER_STATUS_LAST_ROW_SENT";
        if ((statusFlags & Flags.SERVER_STATUS_DB_DROPPED) != 0)
            out += " SERVER_STATUS_DB_DROPPED";
        if ((statusFlags & Flags.SERVER_STATUS_NO_BACKSLASH_ESCAPES) != 0)
            out += " SERVER_STATUS_NO_BACKSLASH_ESCAPES";
        if ((statusFlags & Flags.SERVER_STATUS_METADATA_CHANGED) != 0)
            out += " SERVER_STATUS_METADATA_CHANGED";
        if ((statusFlags & Flags.SERVER_QUERY_WAS_SLOW) != 0)
            out += " SERVER_QUERY_WAS_SLOW";
        if ((statusFlags & Flags.SERVER_PS_OUT_PARAMS) != 0)
            out += " SERVER_PS_OUT_PARAMS";
        return out;
    }
}
