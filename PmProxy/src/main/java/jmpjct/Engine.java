package jmpjct;

/*
 * Base proxy code. This should really just move data back and forth
 * Calling plugins as needed
 */

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;

import jmpjct.mysql.proto.Flags;
import jmpjct.mysql.proto.Handshake;
import jmpjct.mysql.proto.HandshakeResponse;
import jmpjct.plugin.Base;
import org.apache.log4j.Logger;

public class Engine implements Runnable {
    public Logger logger = Logger.getLogger("Engine");
    
    public int port = 0;

    public String user;
    public boolean pmAccessDenied = false;
    public String pmErrorMessage = "";

    public Socket clientSocket = null;
    public InputStream clientIn = null;
    public OutputStream clientOut = null;
    
    // Plugins
    public ArrayList<Base> plugins = new ArrayList<Base>();
    
    // Packet Buffer. ArrayList so we can grow/shrink dynamically
    public ArrayList<byte[]> buffer = new ArrayList<byte[]>();
    public int offset = 0;
    
    // Stop the thread?
    public boolean running = true;

    // What sorta of result set should we expect?
    public int expectedResultSet = Flags.RS_OK;
    
    // Connection info
    public Handshake handshake = null;
    public HandshakeResponse authReply = null;
    
    public String schema = "";
    public String query = "";
    public long statusFlags = 0;
    public long sequenceId = 0;
    
    // Buffer or directly pass though the data
    public boolean bufferResultSet = true;
    public boolean packResultSet = true;
    
    // Modes
    public int mode = Flags.MODE_INIT;
    
    // Allow plugins to muck with the modes
    public int nextMode = Flags.MODE_INIT;

    public Engine(int port, Socket clientSocket, ArrayList<Base> plugins) throws IOException {
        this.port = port;
        this.plugins = plugins;
        
        this.clientSocket = clientSocket;
        this.clientSocket.setPerformancePreferences(0, 2, 1);
        this.clientSocket.setTcpNoDelay(true);
        this.clientSocket.setTrafficClass(0x10);
        this.clientSocket.setKeepAlive(true);
        
        this.clientIn = new BufferedInputStream(this.clientSocket.getInputStream(), 16384);
        this.clientOut = this.clientSocket.getOutputStream();
    }

    public boolean error = false;
    public void run() {
        try {
            while (this.running) {
                switch (this.mode) {
                    case Flags.MODE_INIT:
                        this.logger.trace("MODE_INIT");
                        this.nextMode = Flags.MODE_READ_HANDSHAKE;
                        for (Base plugin : this.plugins)
                            plugin.init(this);
                        break;
                    
                    case Flags.MODE_READ_HANDSHAKE:
                        this.logger.trace("MODE_READ_HANDSHAKE");
                        this.nextMode = Flags.MODE_SEND_HANDSHAKE;
                        for (Base plugin : this.plugins)
                            plugin.read_handshake(this);
                        break;
                    
                    case Flags.MODE_SEND_HANDSHAKE:
                        this.logger.trace("MODE_SEND_HANDSHAKE");
                        this.nextMode = Flags.MODE_READ_AUTH;
                        for (Base plugin : this.plugins)
                            plugin.send_handshake(this);
                        break;
                    
                    case Flags.MODE_READ_AUTH:
                        this.logger.trace("MODE_READ_AUTH");
                        this.nextMode = Flags.MODE_SEND_AUTH;
                        for (Base plugin : this.plugins)
                            plugin.read_auth(this);
                        break;
                    
                    case Flags.MODE_SEND_AUTH:
                        this.logger.trace("MODE_SEND_AUTH");
                        this.nextMode = Flags.MODE_READ_AUTH_RESULT;
                        for (Base plugin : this.plugins)
                            plugin.send_auth(this);
                        break;
                    
                    case Flags.MODE_READ_AUTH_RESULT:
                        this.logger.trace("MODE_READ_AUTH_RESULT");
                        this.nextMode = Flags.MODE_SEND_AUTH_RESULT;
                        for (Base plugin : this.plugins)
                            plugin.read_auth_result(this);
                        break;
                    
                    case Flags.MODE_SEND_AUTH_RESULT:
                        this.logger.trace("MODE_SEND_AUTH_RESULT");
                        this.nextMode = Flags.MODE_READ_QUERY;
                        for (Base plugin : this.plugins)
                            plugin.send_auth_result(this);
                        break;
                    
                    case Flags.MODE_READ_QUERY:
                        this.logger.trace("MODE_READ_QUERY");
                        this.nextMode = Flags.MODE_SEND_QUERY;
                        for (Base plugin : this.plugins)
                            plugin.read_query(this);
                        if(error){
                            halt();
                        }
                        break;
                    
                    case Flags.MODE_SEND_QUERY:
                        this.logger.trace("MODE_SEND_QUERY");
                        this.nextMode = Flags.MODE_READ_QUERY_RESULT;
                        for (Base plugin : this.plugins)
                            plugin.send_query(this);
                        break;
                    
                    case Flags.MODE_READ_QUERY_RESULT:
                        this.logger.trace("MODE_READ_QUERY_RESULT");
                        this.nextMode = Flags.MODE_SEND_QUERY_RESULT;
                        for (Base plugin : this.plugins)
                            plugin.read_query_result(this);
                        break;
                    
                    case Flags.MODE_SEND_QUERY_RESULT:
                        this.logger.trace("MODE_SEND_QUERY_RESULT");
                        this.nextMode = Flags.MODE_READ_QUERY;
                        for (Base plugin : this.plugins)
                            plugin.send_query_result(this);
                        break;
                    
                    case Flags.MODE_CLEANUP:
                        this.logger.trace("MODE_CLEANUP");
                        this.nextMode = Flags.MODE_CLEANUP;
                        for (Base plugin : this.plugins)
                            plugin.cleanup(this);
                        this.halt();
                        break;
                    
                    default:
                        this.logger.fatal("UNKNOWN MODE "+this.mode);
                        this.halt();
                        break;
                }
                this.mode = this.nextMode;
            }
            
            this.logger.info("Exiting thread.");            
            this.clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                this.clientSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            
            try {
                for (Base plugin : this.plugins)
                            plugin.cleanup(this);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void buffer_result_set() {
        if (!this.bufferResultSet)
            this.bufferResultSet = true;
    }
    
    public void halt() {
        this.logger.trace("Halting!");
        this.running = false;
    }
    
    public void clear_buffer() {
        this.logger.trace("Clearing Buffer.");
        this.offset = 0;
        
        // With how ehcache works, if we clear the buffer via .clear(), it also
        // clears the cached value. Create a new ArrayList and count on java
        // cleaning up after ourselves.
        this.buffer = new ArrayList<byte[]>();
    }
}
