package jmpjct.plugin.proxy;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.google.gson.Gson;
import jmpjct.Engine;
import jmpjct.JMP;
import jmpjct.mysql.proto.*;
import jmpjct.plugin.Base;
import jmpjct.plugin.model.PmResponse;
import jmpjct.plugin.model.TranslateRequest;
import org.apache.log4j.Logger;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.apache.commons.codec.CharEncoding.UTF_8;

public class Proxy extends Base {
    public Logger logger = Logger.getLogger(Proxy.class.getName());//"Plugin.Proxy");

    // MySql server stuff
    public String mysqlHost = "";
    public int mysqlPort = 0;
    public Socket mysqlSocket = null;
    public InputStream mysqlIn = null;
    public OutputStream mysqlOut = null;

    public void init(Engine context) throws IOException, UnknownHostException {
        //System.out.println("init");
        this.logger.trace("init");

        String[] phs = JMP.config.getProperty("proxyHosts").split(",");
        for(int i = 0; i < phs.length; i++){
            this.logger.info(i + ": " + phs[i]);
        }
        for (String ph: phs) {
            String[] hi = ph.split(":");
            if (context.port == Integer.parseInt(hi[0].trim())) {
                this.mysqlHost = hi[1].trim();
                this.mysqlPort = Integer.parseInt(hi[2].trim());
                break;
            }
        }

        // Connect to the mysql server on the other side
        this.mysqlSocket = new Socket(this.mysqlHost, this.mysqlPort);
        this.mysqlSocket.setPerformancePreferences(0, 2, 1);
        this.mysqlSocket.setTcpNoDelay(true);
        this.mysqlSocket.setTrafficClass(0x10);
        this.mysqlSocket.setKeepAlive(true);

        this.logger.info("Connected to mysql server at " + this.mysqlHost + ":" + this.mysqlPort);
        this.mysqlIn = new BufferedInputStream(this.mysqlSocket.getInputStream(), 16384);
        this.mysqlOut = this.mysqlSocket.getOutputStream();
    }

    public void read_handshake(Engine context) throws IOException {
        //System.out.println("read_handshake");
        this.logger.trace("read_handshake");
        byte[] packet = Packet.read_packet(this.mysqlIn);
        context.handshake = Handshake.loadFromPacket(packet);
        this.logger.info("connectionId: " + context.handshake.connectionId);

        // Remove some flags from the reply
        context.handshake.removeCapabilityFlag(Flags.CLIENT_COMPRESS);
        context.handshake.removeCapabilityFlag(Flags.CLIENT_SSL);
        context.handshake.removeCapabilityFlag(Flags.CLIENT_LOCAL_FILES);

        // Set the default result set creation to the server's character set
        ResultSet.characterSet = context.handshake.characterSet;

        // Set Replace the packet in the buffer
        context.buffer.add(context.handshake.toPacket());
    }

    public void send_handshake(Engine context) throws IOException {
        //System.out.println("send_handshake");
        this.logger.trace("send_handshake");
        this.logger.info("send hand shake: " + new String(context.buffer.get(0)));
        Packet.write(context.clientOut, context.buffer);
        context.clear_buffer();
    }

    public void read_auth(Engine context) throws IOException {
        //System.out.println("read_auth");
        this.logger.trace("read_auth");
        byte[] packet = Packet.read_packet(context.clientIn);
        context.buffer.add(packet);
        this.logger.info("context.buffer: " + new String(context.buffer.get(0)));

        context.authReply = HandshakeResponse.loadFromPacket(packet);
        this.logger.info("authReply: " + context.authReply);

        if (!context.authReply.hasCapabilityFlag(Flags.CLIENT_PROTOCOL_41)) {
            this.logger.fatal("We do not support Protocols under 4.1");
            context.halt();
            return;
        }

        context.authReply.removeCapabilityFlag(Flags.CLIENT_COMPRESS);
        context.authReply.removeCapabilityFlag(Flags.CLIENT_SSL);
        context.authReply.removeCapabilityFlag(Flags.CLIENT_LOCAL_FILES);

        context.schema = context.authReply.schema;
    }

    public void send_auth(Engine context) throws IOException {
        //System.out.println("send_auth");
        this.logger.trace("send_auth");
        this.logger.info(new String(context.buffer.get(0)));
        Packet.write(this.mysqlOut, context.buffer);
        context.clear_buffer();
    }

    public void read_auth_result(Engine context) throws IOException {
        this.logger.trace("read_auth_result");
        byte[] packet = Packet.read_packet(this.mysqlIn);
        context.buffer.add(packet);
        this.logger.info("buffer in read_auth_result " + new String(context.buffer.get(0)));
        if (Packet.getType(packet) != Flags.OK) {
            this.logger.fatal("Auth is not okay!");
        }
    }

    public void send_auth_result(Engine context) throws IOException {
        this.logger.trace("read_auth_result");
        //this.logger.info(new String(context.buffer.get(0)));
        Packet.write(context.clientOut, context.buffer);
        context.clear_buffer();
    }

    public void read_query(Engine context) throws IOException {
        //System.out.println("read_query");
        this.logger.trace("read_query");
        context.bufferResultSet = false;

        byte[] packet = Packet.read_packet(context.clientIn);
        context.buffer.add(packet);


        context.sequenceId = Packet.getSequenceId(packet);
        this.logger.trace("Client sequenceId: " + context.sequenceId);

        switch (Packet.getType(packet)) {
            case Flags.COM_QUIT:
                this.logger.trace("COM_QUIT");
                context.halt();
                break;

            // Extract out the new default schema
            case Flags.COM_INIT_DB:
                this.logger.trace("COM_INIT_DB");
                context.schema = Com_Initdb.loadFromPacket(packet).schema;
                //translator.setDatabase(context.schema);
                break;

            // Query
            case Flags.COM_QUERY:
                this.logger.trace("COM_QUERY");
                context.query = Com_Query.loadFromPacket(packet).query;
                this.logger.info(context.query);

                String comment = "";
                try{
                    comment = context.query.substring(context.query.indexOf("/*"), context.query.indexOf("*/") + 2);
                }catch(Exception e){

                }
                String[] pieces = context.query.split("/\\*|\\*//*");
                if(pieces.length > 1) {
                    String[] properties = pieces[1].split(" ");
                    for(String s : properties){
                        if(s.startsWith("user=")){
                            context.user = s.split("=")[1];
                        }
                    }
                }

                // check if a user has been sent with the query
                if(context.user != null) {
                    HttpURLConnection connection = null;
                    try {
                        //Create connection to translator in policy machine
                        URL url = new URL("http://localhost:8080/pm/api/translate");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type",
                                "application/json");

                        //password is hard coded
                        String json = "{" +
                                "\"sql\":\"" + context.query + "\"," +
                                "\"username\":\"" + context.user + "\"," +
                                "\"host\":\"" + this.mysqlHost + "\"," +
                                "\"dbUsername\":\"" + context.authReply.username + "\"," +
                                "\"dbPassword\":\"root\"," +
                                "\"port\":" + this.mysqlPort + "," +
                                "\"database\":\"" + context.schema + "\"" +
                                "}";
                        connection.setRequestProperty("Content-Length",
                                Integer.toString(json.getBytes().length));
                        connection.setRequestProperty("Content-Language", "en-US");

                        connection.setUseCaches(false);
                        connection.setDoOutput(true);

                        //Send request
                        DataOutputStream wr = new DataOutputStream (
                                connection.getOutputStream());
                        wr.writeBytes(json);
                        wr.close();


                        //Get Response
                        InputStream is = connection.getInputStream();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                        StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
                        String line;
                        while ((line = rd.readLine()) != null) {
                            response.append(line);
                            response.append('\r');
                        }
                        rd.close();


                        json = response.toString();

                        json = json.replaceAll("\\{|\\}", "");
                        String[] split = json.split("\"entity\":");
                        String sql = split[1];

                        if(sql.equals("null")) {
                            context.error = true;
                            return;
                        }
                        sql = sql.replace("\"", "");
                        System.out.println("new query: " + sql);


                        byte[] bytes = sql.getBytes();

                        byte[] newBytes = new byte[bytes.length + 5];
                        byte[] start = BigInteger.valueOf(bytes.length + 1).toByteArray();
                        newBytes[0] = start.length > 1 ? start[1] : start[0];
                        newBytes[1] = start.length > 1 ? start[0] : (byte)0;
                        newBytes[2] = (byte) 0;
                        newBytes[3] = (byte) 0;
                        newBytes[4] = (byte) 3;
                        for (int i = 5; i < newBytes.length; i++) {
                            newBytes[i] = bytes[i - 5];
                        }
                        //System.out.println(Arrays.toString(newBytes));

                        context.buffer.clear();
                        context.buffer.add(newBytes);
                        context.query = Com_Query.loadFromPacket(bytes).query;
                    }catch(Exception e){
                        e.printStackTrace();
                        context.error = true;
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }

                }else{
                    context.query = Com_Query.loadFromPacket(packet).query;
                }
                break;

            default:
                break;
        }
    }

    public void send_query(Engine context) throws IOException {
        //System.out.println("send_query");
        this.logger.trace("send_query");
        byte[] sending = context.buffer.get(0);
        int c = 0;
        int i = sending.length - 1;
        while (i >= 0 && sending[i] == 0)
        {
            --i;
        }

        sending =  Arrays.copyOf(sending, i + 1);
        context.buffer.clear();
        context.buffer.add(sending);

        //System.out.println("sending: " + Arrays.toString(context.buffer.get(0)));

        Packet.write(this.mysqlOut, context.buffer);
        context.clear_buffer();
    }

    public void read_query_result(Engine context) throws IOException {
        //System.out.println("read_query_result");
        this.logger.trace("read_query_result");

        byte[] packet = Packet.read_packet(this.mysqlIn);
        //System.out.println("packet: " + new String(packet));
        context.buffer.add(packet);
        //this.logger.info("\t" + Com_Query.loadFromPacket(packet).query);

        context.sequenceId = Packet.getSequenceId(packet);

        if(context.pmAccessDenied) {
            byte[] bytes = context.pmErrorMessage.getBytes();

            byte[] newBytes = new byte[bytes.length + 5];
            byte[] start = BigInteger.valueOf(bytes.length + 1).toByteArray();
            newBytes[0] = start.length > 1 ? start[1] : start[0];
            newBytes[1] = start.length > 1 ? start[0] : (byte)0;
            newBytes[2] = (byte) 0;
            newBytes[3] = (byte) 1;
            newBytes[4] = (byte) -1;
            for (int i = 5; i < newBytes.length; i++) {
                newBytes[i] = bytes[i - 5];
            }

            context.buffer.clear();
            context.buffer.add(newBytes);
            System.out.println("PM ERROR: " + new String(newBytes));
        } else {
            switch (Packet.getType(packet)) {
                case Flags.OK:
                case Flags.ERR:
                    break;

                default:
                    context.buffer = Packet.read_full_result_set(this.mysqlIn, context.clientOut, context.buffer, context.bufferResultSet);
                    break;
            }
        }
    }

    public void send_query_result(Engine context) throws IOException {
        //System.out.println("send_query_result");
        this.logger.trace("send_query_result");
        //System.out.println("send_query_result: " + new String(context.buffer.get(0)));
        Packet.write(context.clientOut, context.buffer);
        context.clear_buffer();
    }

    public void cleanup(Engine context) {
        //System.out.println("cleanup");
        this.logger.trace("cleanup");
        if (this.mysqlSocket == null) {
            return;
        }

        try {
            this.mysqlSocket.close();
        }
        catch(IOException e) {}
    }
}
