package jmpjct.plugin.cache.eh;

import java.util.ArrayList;
import java.util.List;

import jmpjct.Engine;
import jmpjct.JMP;
import jmpjct.mysql.proto.*;
import jmpjct.plugin.Base;
import org.apache.log4j.Logger;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Statistics;
import net.sf.ehcache.terracotta.TerracottaNotRunningException;

public class Eh extends Base {
    private static Ehcache cache = null;
    private static CacheManager cachemanager = null;
    
    public Logger logger = Logger.getLogger(Eh.class.getName());//"Plugin.Cache.Eh");
    private int TTL = 0;
    private String key = "";
    
    public Eh() {
        if (Eh.cachemanager == null) {
            this.logger.trace("Eh - CacheManager: Loading "+ JMP.config.getProperty("ehcacheConf"));
            Eh.cachemanager = CacheManager.create(JMP.config.getProperty("ehcacheConf").trim());
        }
        
        if (Eh.cache == null) {
            this.logger.trace("Eh - cache: Getting "+JMP.config.getProperty("ehcacheCacheName"));
            Eh.cache = Eh.cachemanager.getEhcache(JMP.config.getProperty("ehcacheCacheName").trim());
            Eh.cache.setSampledStatisticsEnabled(true);
        }
        
        if (Eh.cache == null) {
            this.logger.fatal("Eh is null! Does instance '"+JMP.config.getProperty("ehcacheCacheName")+"' exist?");
        }
    }
    
    @SuppressWarnings("unchecked")
    public void read_query(Engine context) {
        if (Eh.cache == null)
            return;
        
        String query = context.query;
        //this.logger.info(query);
        String command = "";
        String value = "";
        
        // Reset all values on a new query
        this.TTL = 0;
        this.key = "";
        
        if (!query.startsWith("/* "))
            return;
        
        // Extract out the command
        command = query.substring(3, query.indexOf("*/")).trim();
        
        if (command.indexOf(":") != -1) {
            value = command.substring(command.indexOf(":")+1).trim();
            command = command.substring(0, command.indexOf(":")).trim();
        }
        
        query = query.substring(query.indexOf("*/")+2).trim();
        this.key = context.schema+":"+query;
        
        this.logger.info("Cache Key: '"+this.key+"'");
        this.logger.trace("Command: '"+command+"'"+" value: '"+value+"'");
        
        if (command.equalsIgnoreCase("CACHE")) {
            this.logger.trace("CACHE");
            this.TTL = Integer.parseInt(value);
            context.buffer_result_set();
            
            Eh.cache.acquireWriteLockOnKey(this.key);
            
            Element element = Eh.cache.get(this.key);
            
            if (element != null) {
                this.logger.trace("Cache Hit!");
                Eh.cache.releaseWriteLockOnKey(this.key);
                
                context.clear_buffer();
                context.buffer = (ArrayList<byte[]>) element.getValue();
                context.nextMode = Flags.MODE_SEND_QUERY_RESULT;
                
                if (context.buffer.size() == 0) {
                    ERR err = new ERR();
                    err.sequenceId = context.sequenceId+1;
                    err.errorCode = 1032;
                    err.sqlState = "HY000";
                    err.errorMessage = "Can't find record in ehcache";
                    
                    context.clear_buffer();
                    context.buffer.add(err.toPacket());
                    context.nextMode = Flags.MODE_SEND_QUERY_RESULT;
                    
                    this.logger.fatal("Cache hit but invalid result!");
                }
            }
        }
        else if (command.equalsIgnoreCase("FLUSH")) {
            this.logger.trace("FLUSH");
            OK ok = new OK();
            
            boolean removed = Eh.cache.remove(this.key);
            if (removed)
                ok.affectedRows = 1;
            ok.sequenceId = context.sequenceId+1;
            
            context.clear_buffer();
            context.buffer.add(ok.toPacket());
            context.nextMode = Flags.MODE_SEND_QUERY_RESULT;
        }
        else if (command.equalsIgnoreCase("FLUSHALL")) {
            this.logger.trace("FLUSHALL");
            OK ok = new OK();
            
            Eh.cache.removeAll();
            ok.sequenceId = context.sequenceId+1;
            
            context.clear_buffer();
            context.buffer.add(ok.toPacket());
            context.nextMode = Flags.MODE_SEND_QUERY_RESULT;
        }
        else if (command.equalsIgnoreCase("REFRESH")) {
            this.logger.trace("REFRESH");
            Eh.cache.remove(this.key);
            this.TTL = Integer.parseInt(value);
            context.buffer_result_set();
        }
        else if (command.equalsIgnoreCase("STATS")) {
            this.logger.trace("STATS");
            ResultSet rs = new ResultSet();
            Row row = null;
            
            rs.addColumn(new Column("Key"));
            rs.addColumn(new Column("Value"));
            
            Statistics stats = Eh.cache.getStatistics();
            
            rs.addRow(new Row("AverageGetTime", stats.getAverageGetTime()));
            rs.addRow(new Row("AverageSearchTime", stats.getAverageSearchTime()));
            
            rs.addRow(new Row("ObjectCount", stats.getObjectCount()));
            rs.addRow(new Row("MemoryStoreObjectCount", stats.getMemoryStoreObjectCount()));
            rs.addRow(new Row("OffHeapStoreObjectCount", stats.getOffHeapStoreObjectCount()));
            rs.addRow(new Row("DiskStoreObjectCount", stats.getDiskStoreObjectCount()));

            rs.addRow(new Row("CacheHits", stats.getCacheHits()));
            rs.addRow(new Row("CacheMisses", stats.getCacheMisses()));

            rs.addRow(new Row("InMemoryHits", stats.getInMemoryHits()));
            rs.addRow(new Row("InMemoryMisses", stats.getInMemoryMisses()));

            rs.addRow(new Row("OffHeapHits", stats.getOffHeapHits()));
            rs.addRow(new Row("OffHeapMisses", stats.getOffHeapMisses()));

            rs.addRow(new Row("OnDiskHits", stats.getOnDiskHits()));
            rs.addRow(new Row("OnDiskMisses", stats.getOnDiskMisses()));

            rs.addRow(new Row("EvictionCount", stats.getEvictionCount()));

            rs.addRow(new Row("SearchesPerSecond", stats.getSearchesPerSecond()));
            rs.addRow(new Row("WriterQueueSize", stats.getWriterQueueSize()));
            
            context.clear_buffer();
            context.buffer = rs.toPackets();
            context.nextMode = Flags.MODE_SEND_QUERY_RESULT;
        }
        else if (command.equalsIgnoreCase("INFO")) {
            this.logger.trace("INFO");
            ResultSet rs = new ResultSet();
            Row row = null;
            
            rs.addColumn(new Column("Key"));
            rs.addColumn(new Column("Value"));
            
            rs.addRow(new Row("getGuid", Eh.cache.getGuid()));
            rs.addRow(new Row("getName", Eh.cache.getName()));
            rs.addRow(new Row("getStatus", Eh.cache.getStatus().toString()));
            rs.addRow(new Row("isDisabled", Eh.cache.isDisabled()));
            rs.addRow(new Row("isSearchable", Eh.cache.isSearchable()));
            
            try {
                rs.addRow(new Row("isNodeBulkLoadEnabled", Eh.cache.isNodeBulkLoadEnabled()));
                rs.addRow(new Row("isClusterBulkLoadEnabled", Eh.cache.isClusterBulkLoadEnabled()));
            }
            catch (UnsupportedOperationException e) {}
            catch (TerracottaNotRunningException e) {}
            
            rs.addRow(new Row("isStatisticsEnabled", Eh.cache.isStatisticsEnabled()));
            rs.addRow(new Row("isSampledStatisticsEnabled", Eh.cache.isSampledStatisticsEnabled()));
            
            switch (Eh.cache.getStatisticsAccuracy()) {
                case Statistics.STATISTICS_ACCURACY_BEST_EFFORT:
                    rs.addRow(new Row("getStatisticsAccuracy", "STATISTICS_ACCURACY_BEST_EFFORT"));
                    break;
                case Statistics.STATISTICS_ACCURACY_GUARANTEED:
                    rs.addRow(new Row("getStatisticsAccuracy", "STATISTICS_ACCURACY_GUARANTEED"));
                    break;
                case Statistics.STATISTICS_ACCURACY_NONE:
                    rs.addRow(new Row("getStatisticsAccuracy", "STATISTICS_ACCURACY_NONE"));
                    break;
                default:
                    rs.addRow(new Row("getStatisticsAccuracy", "STATISTICS_ACCURACY_UNKNOWN"));
                    break;
            }
            
            rs.addRow(new Row("hasAbortedSizeOf", Eh.cache.hasAbortedSizeOf()));
            
            context.clear_buffer();
            context.buffer = rs.toPackets();
            context.nextMode = Flags.MODE_SEND_QUERY_RESULT;
        }
        else if (command.equalsIgnoreCase("DUMP KEYS")) {
            this.logger.trace("DUMP KEYS");
            List keys = this.cache.getKeysWithExpiryCheck();
            
            ResultSet rs = new ResultSet();
            rs.addColumn(new Column("Key"));
            
            for (Object k: keys) {
                this.logger.trace("Key: '"+k+"'");
                rs.addRow(new Row(k.toString())); 
            }
            
            context.clear_buffer();
            context.buffer = rs.toPackets();
            context.nextMode = Flags.MODE_SEND_QUERY_RESULT;
        }
        else {
            /*this.logger.trace("FAIL");
            ERR err = new ERR();
            err.sequenceId = context.sequenceId+1;
            err.errorCode = 1047;
            err.sqlState = "08S01";
            err.errorMessage = "Unknown command '"+command+"'";
            
            context.clear_buffer();
            context.buffer.add(err.toPacket());
            context.nextMode = Flags.MODE_SEND_QUERY_RESULT;
            
            this.logger.fatal(command+" is unknown!");*/
        }
    }
    
    public void read_query_result(Engine context) {
        if (Eh.cache == null)
            return;
        
        // Cache this key?
        if (this.TTL == 0 || context.buffer.size() == 0)
            return;
        
        Element element = new Element(this.key, context.buffer);
        element.setTimeToLive(this.TTL);
        Eh.cache.put(element);
        this.logger.info(Com_Query.loadFromPacket(context.buffer.get(0)).query);
        this.logger.info(element.toString());
        Eh.cache.releaseWriteLockOnKey(this.key);
    }
}
