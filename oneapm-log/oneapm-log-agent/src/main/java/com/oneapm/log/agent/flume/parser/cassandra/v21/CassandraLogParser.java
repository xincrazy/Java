package com.oneapm.log.agent.flume.parser.cassandra.v21;

import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.cassandra.CassandraParserUtil;
import com.oneapm.log.common.utils.DiskUtil;
import com.oneapm.log.common.utils.HostNameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CassandraLogParser implements Parser {
    private static final Logger logger             = LoggerFactory.getLogger(CassandraLogParser.class);
    private static final String[] CLIENT_REQUEST_LATENCY = {"ClientReadRequestLatency", "ClientWriteRequestLatency"};
    private static final String[] CASSANDRA_ONE_MINUTE_RATE_METRIC_TYPES
            = {"ClientReadRequestTimeouts", "ClientWriteRequestTimeouts", "ClientReadRequestUnavailables",
            "ClientWriteRequestUnavailables", "TotalCompactionsCompleted"};
    private static final String[] CASSANDRA_LOAD_AND_PENDING_TASKS_METRIC_TYPES = {"StorageLoad", "CompactionPendingTask"};
    private static Map<Integer, CassandraLogParser> parserInstancePool = new HashMap<>();
    private String metricType;
    private String cassandraPath;

    public CassandraLogParser(Map<String, String> paramMap) {
        this.metricType = paramMap.get("metricType");
        this.cassandraPath = paramMap.get("cassandraPath");
    }

    @Override
    public String parser(String content) {
        logger.debug("content before parsing: " + content);
        String currentTimeStamp = getCurrentTimeStamp();
        String[] allValues = content.split(",");
        if (Arrays.asList(CLIENT_REQUEST_LATENCY).contains(metricType)) {
            return buildResult(currentTimeStamp, CassandraParserUtil.getResultKeyByMetricType(metricType), allValues[4]);
        }
        else if (Arrays.asList(CASSANDRA_ONE_MINUTE_RATE_METRIC_TYPES).contains(metricType)) {
            return buildResult(currentTimeStamp, CassandraParserUtil.getResultKeyByMetricType(metricType), allValues[2]);
        }
        else if (Arrays.asList(CASSANDRA_LOAD_AND_PENDING_TASKS_METRIC_TYPES).contains(metricType)) {
            return buildResult(currentTimeStamp, CassandraParserUtil.getResultKeyByMetricType(metricType), allValues[1]);
        }
        return null;
    }

    /**
     *
     * @return the {@link CassandraLogParser}.
     */
    public static CassandraLogParser getParser(Map<String, String> paramMap) {
        int hashCode = paramMap.get("metricType").hashCode();

        if (parserInstancePool.get(hashCode) != null) {
            return parserInstancePool.get(hashCode);
        } else {
            CassandraLogParser parser = new CassandraLogParser(paramMap);
            parserInstancePool.put(hashCode, parser);
            return parser;
        }
    }

    /**
     * 将现在的时间转化成UNIX时间1462752000007.
     */
    public static String getCurrentTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date d = null;
        try {
            d = formatter.parse(formatter.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Long.toString(d.getTime());
    }

    /**
     * Build a key value format result string based on the given key and value.
     */
    private String buildResult(String timestamp, String key, String value) {
        String result = "{\"timestamp\":" + timestamp + ",\"hostname\":\"" + HostNameUtil.getHostName()
                + "\",\"servicename\":\"Cassandra\",\"metricname\":\"" + key + "\",\"metricdata\":" + value;
        if ("DiskSpaceUsed".equals(key)) {
            result = result + ",\"metricname1\":\"DiskFreeSpaceRate\",\"metricdata1\":" + DiskUtil.getFreeDiskSpaceRate(this.cassandraPath);
        }
        result += "}";
        logger.debug("cassandra parser result: " + result);
        return result;
    }
}
