package jmpjct;

/*
 * Java Mysql Proxy
 * Main binary. Just listen for connections and pass them over
 * to the proxy module
 */

import java.io.IOException;
import java.util.ArrayList;
import java.net.ServerSocket;

import jmpjct.JMP;
import jmpjct.plugin.Base;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JMP_Thread implements Runnable {
    public int port;
    public boolean listening = true;
    public ServerSocket listener = null;
    public ArrayList<Base> plugins = new ArrayList<Base>();
    public Logger logger = Logger.getLogger("JMP_Thread");
    
    public JMP_Thread(int port) {
        Thread.currentThread().setName("Listener: "+port);
        this.port = port;
    }
    
    public void run() {
        try {
            this.listener = new ServerSocket(this.port);
        }
        catch (IOException e) {
            this.logger.fatal("Could not listen on port "+this.port);
            System.exit(-1);
        }
        
        this.logger.info("Listening on "+this.port);
        System.out.println("Listening on "+this.port);
        
        String[] ps = new String[0];
        ExecutorService tp = Executors.newCachedThreadPool();
        
        if (JMP.config.getProperty("plugins") != null)
            ps = JMP.config.getProperty("plugins").split(",");
        
        while (this.listening) {
            plugins = new ArrayList<Base>();
            for (String p: ps) {
                try {
                    plugins.add((Base) Base.class.getClassLoader().loadClass(p.trim()).newInstance());
                    this.logger.info("Loaded plugin "+p);
                }
                catch (ClassNotFoundException e) {
                    this.logger.error("["+p+"] "+e);
                    continue;
                }
                catch (InstantiationException e) {
                    this.logger.error("["+p+"] "+e);
                    continue;
                }
                catch (IllegalAccessException e) {
                    this.logger.error("["+p+"] "+e);
                    continue;
                }
            }
            try {
                tp.submit(new Engine(this.port, this.listener.accept(), plugins));
            }
            catch (IOException e) {
                this.logger.fatal("Accept fatal "+e);
                this.listening = false;
            }
        }
    
        try {
            tp.shutdown();
            this.listener.close();
        }
        catch (IOException e) {}
    }
}
