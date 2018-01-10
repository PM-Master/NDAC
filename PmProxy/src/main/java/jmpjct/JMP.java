package jmpjct;

/*
 * Java Mysql Proxy
 * Main binary. Just listen for connections and pass them over
 * to the proxy module
 */

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.InputStream;
import java.util.Properties;
import java.io.FileInputStream;

public class JMP {
    public static Properties config = new Properties();

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("user.dir"));

        InputStream resourceAsStream = JMP.class.getClassLoader().getResourceAsStream("conf/jmp.properties");
        JMP.config.load(resourceAsStream);

        Logger logger = Logger.getLogger("JMP");

        resourceAsStream = JMP.class.getClassLoader().getResourceAsStream("conf/log.conf");
        PropertyConfigurator.configure(resourceAsStream);

        String[] ports = JMP.config.getProperty("ports").split(",");
        for (String port: ports) {
            new jmpjct.JMP_Thread(Integer.parseInt(port.trim())).run();
        }
    }
}
