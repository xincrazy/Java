package com.oneapm.log.agent.flume.parser.tomcat;

import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.constants.GrokConstants;
import com.oneapm.log.common.grok.GrokBean;
import com.oneapm.log.common.utils.JsonUtil;
import com.oneapm.log.common.utils.StringUtil;
import com.oneapm.log.common.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TomcatLogParser implements Parser {
    private static final Logger logger             = LoggerFactory.getLogger(TomcatLogParser.class);
    private String logType;
    private static Map<Integer, TomcatLogParser> parserInstancePool = new HashMap<>();


    public TomcatLogParser(String logType) {
        this.logType = logType;
    }

    @Override
    public String parser(String content) {
        GrokBean gb = new GrokBean(
                this.getClass().getResource("/").getPath() + GrokConstants.DEFAULT_GROK_PATTERN_FILE_PATH,
                logType);

        logger.debug("content before parsing: " + content);

        TomcatLogResult tomcatLogResult = gb.matchLog(content).getObject(TomcatLogResult.class);
        String resultWithQuotations = convertToJsonStr(tomcatLogResult);

        logger.debug("content after parsing: " + StringUtil.removeQuotationForValues(resultWithQuotations));

        return StringUtil.removeQuotationForValues(resultWithQuotations);
    }

    /**
     *
     * @return the {@link TomcatLogParser}. Construct a new one if the parser is null.
     */
    public static TomcatLogParser getParser(String logType) {
        int hashCode = logType.hashCode();

        if (parserInstancePool.get(hashCode) != null) {
            return parserInstancePool.get(hashCode);
        } else {
            TomcatLogParser parser = new TomcatLogParser(logType);
            parserInstancePool.put(hashCode, parser);
            return parser;
        }
    }

    /**
     * Private class used to store the Grok match result.
     */
    @ToString
    @Getter
    @Setter
    private static class TomcatLogResult {
        String method;
        String httpstatuscode;
        String remotehostname;
        String requesturi;
        String version;
        String timetoken;
        String bytessent;
        String year;
        String month;
        String day;
        String hour;
        String minute;
        String second;
        String mills;
    }

    /**
     * Convert the grok result to the required json string.
     *
     * @param tomcatLogResult which is returned from the grok match();
     *
     * @return a key value json which represents the Grok result.
     */
    private String convertToJsonStr(TomcatLogResult tomcatLogResult) {
        if (tomcatLogResult == null) {
            logger.debug("Grok match result is null...");
            return "";
        }
        LinkedHashMap<String, String> finalJsonMap = new LinkedHashMap<>();
        finalJsonMap.put("timestamp", getTimeStringFromGrokOutput(tomcatLogResult));
        finalJsonMap.put("hostname", tomcatLogResult.getRemotehostname());
        finalJsonMap.put("servicename", "Tomcat");
        finalJsonMap.put("servicetype", tomcatLogResult.getMethod());
        if (tomcatLogResult.getRequesturi() != null) {
            finalJsonMap.put("servicesubtype", tomcatLogResult.getRequesturi());
        }
        if (tomcatLogResult.getHttpstatuscode() != null) {
            finalJsonMap.put("metricname1", "HttpStatusCode");
            finalJsonMap.put("metricdata1", tomcatLogResult.getHttpstatuscode());
        }
        if (tomcatLogResult.getBytessent() != null) {
            finalJsonMap.put("metricname2", "BytesSent");
            finalJsonMap.put("metricdata2", tomcatLogResult.getBytessent());
        }
        if (tomcatLogResult.getTimetoken() != null) {
            finalJsonMap.put("metricname3", "TimeToken");
            finalJsonMap.put("metricdata3", tomcatLogResult.getTimetoken());
        }
        return JsonUtil.getJacksonString(finalJsonMap);
    }

    /**
     * Will construct a time format like "yyyy-MM-dd'T'HH:mm:ss.SSSX",
     * basing on Grok output.
     *
     * @return a timestamp for the given Grok output.
     */
    private String getTimeStringFromGrokOutput(TomcatLogResult tomcatLogResult) {
        StringBuffer time = new StringBuffer();
        time.append(tomcatLogResult.getYear());
        time.append("-");
        time.append(convertMonth(tomcatLogResult.getMonth()));
        time.append("-");
        time.append(tomcatLogResult.getDay());
        time.append(" ");
        time.append(tomcatLogResult.getHour());
        time.append(":");
        time.append(tomcatLogResult.getMinute());
        time.append(":");
        time.append(tomcatLogResult.getSecond());
        time.append(".");
        time.append(tomcatLogResult.getMills() == null ? "000" : tomcatLogResult.getMills());

        return TimeUtil.formatTZ(time.toString(), "yyyy-MM-dd hh:mm:ss.SSS");
    }

    private String convertMonth(String month) {
        switch (month) {
            case "Jan":
                return "01";
            case "Feb":
                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
            default:
                break;
        }
        return "";
    }
}
