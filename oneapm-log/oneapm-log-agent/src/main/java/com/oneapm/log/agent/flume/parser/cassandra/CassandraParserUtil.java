package com.oneapm.log.agent.flume.parser.cassandra;

/**
 * Util class used for cassandra parser.
 *
 */
public class CassandraParserUtil {
    /**
     * Get a key value based on the given metric type for kibana display.
     *
     * @param metricType
     *      The metric type represents what log file is being intercepted.
     * @return
     *      a string represents the meaning of the result value.
     */
    public static String getResultKeyByMetricType (String metricType) {
        switch (metricType) {
            case "ClientReadRequestLatency":
                return "CR_R_Latency_1min";
            case "ClientWriteRequestLatency":
                return "CR_W_Latency_1min";
            case "ClientReadRequestTimeouts":
                return "CR_R_Timeouts_1min";
            case "ClientWriteRequestTimeouts":
                return "CR_W_Timeouts_1min";
            case "ClientReadRequestUnavailables":
                return "CR_R_Unavailables_1min";
            case "ClientWriteRequestUnavailables":
                return "CR_W_Unavailables_1min";
            case "TotalCompactionsCompleted":
                return "Compaction_Completed_1min";
            case "StorageLoad":
                return "DiskSpaceUsed";
            case "CompactionPendingTask":
                return "Compaction_Pending";
            default:
                return "";
        }
    }
}
