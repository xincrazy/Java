package com.oneapm.log.agent.flume.parser.cassandra.v37;

import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.utils.DiskUtil;
import com.oneapm.log.common.utils.HostNameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.oneapm.log.agent.flume.parser.cassandra.CassandraParserUtil.*;

public class CassandraLogParser implements Parser {
    private static final Logger logger             = LoggerFactory.getLogger(CassandraLogParser.class);
    public static final String[] CLIENT_REQUEST_LATENCY = {"ClientReadRequestLatency", "ClientWriteRequestLatency"};
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
        String[] allValues = content.split(",");
        if (Arrays.asList(CLIENT_REQUEST_LATENCY).contains(metricType)) {
            return buildResult(allValues[0], getResultKeyByMetricType(metricType), allValues[13]);
        }
        else if (Arrays.asList(CASSANDRA_ONE_MINUTE_RATE_METRIC_TYPES).contains(metricType)) {
            return buildResult(allValues[0], getResultKeyByMetricType(metricType), allValues[3]);
        }
        else if (Arrays.asList(CASSANDRA_LOAD_AND_PENDING_TASKS_METRIC_TYPES).contains(metricType)) {
            return buildResult(allValues[0], getResultKeyByMetricType(metricType), allValues[1]);
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
     * Build a key value format result string based on the given key and value.
     */
    private String buildResult(String timestamp, String key, String value) {
        String result = "{\"timestamp\":" + timestamp + "000,\"hostname\":\"" + HostNameUtil.getHostName()
                + "\",\"servicename\":\"Cassandra\",\"metricname\":\"" + key + "\",\"metricdata\":" + value;
        if ("DiskSpaceUsed".equals(key)) {
            result = result + ",\"metricname1\":\"DiskFreeSpaceRate\",\"metricdata1\":" + DiskUtil.getFreeDiskSpaceRate(this.cassandraPath);
        }
        result += "}";
        logger.debug("cassandra parser result: " + result);
        return result;
    }
}
