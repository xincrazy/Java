package com.oneapm.log.agent.flume.parser.jvm.hotspot;

import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.constants.GrokConstants;
import com.oneapm.log.common.grok.GrokBean;
import com.oneapm.log.common.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JvmParser implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(JvmParser.class);

    private static final String logType = "%{GCLOG:gclog}";

    private static final String youngGcType = "%{YOUNGGC:younggc}";

    private static Map<Integer, JvmParser> parserInstancePool = new LinkedHashMap<>();

    private String appName;

    private JvmParser(String appName) {
        this.appName = appName;
    }

    @Override
    public String parser(String content) {
        GrokBean gb =
                new GrokBean(this.getClass().getResource("/").getPath()
                        + GrokConstants.DEFAULT_GROK_PATTERN_FILE_PATH, logType);

        GrokBean gbYoung = new GrokBean(this.getClass().getResource("/").getPath() + GrokConstants.DEFAULT_GROK_PATTERN_FILE_PATH, youngGcType);

        logger.debug("content before parsing: " + content);

        //String youngGcString = gbYoung.matchLog(content).toJson(true).orElse("");

        YoungGCResult youngGCResult = gbYoung.matchLog(content).getObject(YoungGCResult.class);

        String resultWithQuotaions = "";

        if(youngGCResult.getGcallafter()!=null){
            resultWithQuotaions = convertToJsonStr(youngGCResult);
        }else{
            JvmParserResult jvmParserResult = gb.matchLog(content).getObject(JvmParserResult.class);
            if(jvmParserResult.getGcallafter()==null){
                logger.debug("The input content can not match the grok bean,the input string is : "+content);
                return null;
            }
            resultWithQuotaions = convertToJsonStr(jvmParserResult);
        }
        return removeQuotationForValues(resultWithQuotaions);
    }

    private String convertToJsonStr(YoungGCResult jvmParserResult) {
        if (jvmParserResult == null) {
            logger.debug("Grok match result is null...");
            return "";
        }

        LinkedHashMap<String, String> finalJsonMap = new LinkedHashMap<>();

        String hostname = "";
        try {
            hostname = (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
            return "";
        }

        BigDecimal bgGcALlBefore = new BigDecimal(jvmParserResult.getGcallbefore());
        BigDecimal bgGcALlAfter = new BigDecimal(jvmParserResult.getGcallafter());
        BigDecimal bgGcAllMem = new BigDecimal(jvmParserResult.getGcallafter2());
        BigDecimal bgGcMemRate = bgGcALlBefore.subtract(bgGcALlAfter).divide(bgGcAllMem,2,BigDecimal.ROUND_HALF_EVEN);

        String timeStamp = String.valueOf(System.currentTimeMillis());

        finalJsonMap.put("timestamp", timeStamp);

        finalJsonMap.put("hostname", hostname);

        finalJsonMap.put("servicename", "jvm");

        finalJsonMap.put("servicetype",appName);

        finalJsonMap.put("servicesubtype","youngGc");

        finalJsonMap.put("metricname1","GCTime");

        finalJsonMap.put("metricdata1", jvmParserResult.timesreal);

        finalJsonMap.put("metricname2","beforGcMem");

        finalJsonMap.put("metricdata2", jvmParserResult.gcallbefore);

        finalJsonMap.put("metricname3","afterGcMem");

        finalJsonMap.put("metricdata3", jvmParserResult.gcallafter);

        finalJsonMap.put("metricname4","gcMemRate");

        finalJsonMap.put("metricdata4", String.valueOf(bgGcMemRate.doubleValue()));

        return JsonUtil.getJacksonString(finalJsonMap);
    }


    private String convertToJsonStr(JvmParserResult jvmParserResult) {
        if (jvmParserResult == null) {
            logger.debug("Grok match result is null...");
            return "";
        }

        LinkedHashMap<String, String> finalJsonMap = new LinkedHashMap<>();

        String hostname = "";
        try {
            hostname = (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
            return "";
        }

        BigDecimal bgGcALlBefore = new BigDecimal(jvmParserResult.getGcallbefore());
        BigDecimal bgGcALlAfter = new BigDecimal(jvmParserResult.getGcallafter());
        BigDecimal bgGcAllMem = new BigDecimal(jvmParserResult.getGcallafter2());
        BigDecimal bgGcMemRate = bgGcALlBefore.subtract(bgGcALlAfter).divide(bgGcAllMem,2,BigDecimal.ROUND_HALF_EVEN);

        String timeStamp = String.valueOf(System.currentTimeMillis());

        finalJsonMap.put("timestamp", timeStamp);

        finalJsonMap.put("hostname", hostname);

        finalJsonMap.put("servicename", "jvm");

        finalJsonMap.put("servicetype",appName);

        finalJsonMap.put("servicesubtype","fullGc");

        finalJsonMap.put("metricname1","GCTime");

        finalJsonMap.put("metricdata1", jvmParserResult.timesreal);

        finalJsonMap.put("metricname2","beforGcMem");

        finalJsonMap.put("metricdata2", jvmParserResult.gcallbefore);

        finalJsonMap.put("metricname3","afterGcMem");

        finalJsonMap.put("metricdata3", jvmParserResult.gcallafter);

        finalJsonMap.put("metricname4","gcMemRate");

        finalJsonMap.put("metricdata4", String.valueOf(bgGcMemRate.doubleValue()));

        return JsonUtil.getJacksonString(finalJsonMap);
    }

    /**
     * @param result
     * @return
     */
    private String removeQuotationForValues(String result) {
        String[] temps =
                result.substring(1, result.length() - 1).split(":");
        for (String firstLevel : temps) {
            String[] firstTemps = firstLevel.split(",");
            for (String secondLevel : firstTemps) {
                Matcher m =
                        Pattern.compile("\"[0-9]\\d*\\.?\\d*\"").matcher(secondLevel);
                if (m.matches()) {
                    String replaceString =
                            secondLevel.substring(1, secondLevel.length() - 1);
                    result = result.replaceAll(secondLevel, replaceString);
                }

            }
        }

        return result;
    }

    /**
     * private class used the store the young gc Grok match result.
     */
    @ToString
    @Getter
    @Setter
    private static class YoungGCResult{
        String day;

        String ergonomics;

        String gcallafter;

        String gcallafter2;

        String gcallafter2unit;

        String gcallafterunit;

        String gcallbefore;

        String gcallbeforeunit;

        String gcnumber;

        String gctype;

        String gcyoungafter;

        String gcyoungafter2;

        String gcyoungafter2unit;

        String gcyoungafterunit;

        String gcyoungbefore;

        String gcyoungbeforeunit;

        String hour;

        String metatime;

        String metatimeunit;

        String mills;

        String minute;

        String month;

        String real;

        String second;

        String sys;

        String times;

        String timesreal;

        String timessys;

        String timestamp;

        String timesunit;

        String timesuser;

        String timezone;

        String user;

        String year;

        String young;

    }

    /**
     * Private class used to store the Grok match result.
     */
    @ToString
    @Getter
    @Setter
    private static class JvmParserResult {

        String day;

        String ergonomics;

        String gcallafter;

        String gcallafter2;

        String gcallafter2unit;

        String gcallafterunit;

        String gcallbefore;

        String gcallbeforeunit;

        String gclog;

        String gcmetaafter;

        String gcmetaafter2;

        String gcmetaafter2unit;

        String gcmetaafterunit;

        String gcmetabefore;

        String gcmetabeforeunit;

        String gcnumber;

        String gcoldafter;

        String gcoldafter2;

        String gcoldafter2unit;

        String gcoldafterunit;

        String gcoldbefore;

        String gcoldbeforeunit;

        String gctype;

        String gcyoungafter;

        String gcyoungafter2;

        String gcyoungafter2unit;

        String gcyoungafterunit;

        String gcyoungbefore;

        String gcyoungbeforeunit;

        String hour;

        String metaspace;

        String metatime;

        String metatimeunit;

        String mills;

        String minute;

        String month;

        String old;

        String real;

        String second;

        String sys;

        String times;

        String timesreal;

        String timessys;

        String timestamp;

        String timesunit;

        String timesuser;

        String timezone;

        String user;

        String year;

        String young;

    }

    public static JvmParser getParser(String appName) {
        int hashCode = appName.hashCode();
        if (parserInstancePool.containsKey(hashCode)) {
            return parserInstancePool.get(hashCode);
        } else {
            JvmParser parser = new JvmParser(appName);
            parserInstancePool.put(hashCode, parser);
            return parser;
        }
    }


}