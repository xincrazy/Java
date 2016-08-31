package com.oneapm.log.agent.flume.parser.system.linux;

import com.alibaba.fastjson.JSONObject;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SystemUsageParser implements Parser {

  private static final Logger logger = LoggerFactory.getLogger(SystemUsageParser.class);

  private static SystemUsageParser parser;
  private static double aDouble;

  public static SystemUsageParser getParser() {
    if (parser == null)
      parser = new SystemUsageParser();

    return parser;
  }


  @Override
  public String parser(String content) {
    JSONObject jsonObject = JSONObject.parseObject(content);
    String systemType = jsonObject.get("system").toString();
    switch (systemType) {
      case "diskFileSystem":
        return removeQuotationForValues(getDiskResult(jsonObject));
      case "memory":
        return removeQuotationForValues(getMemResult(jsonObject));
      case "cpuUsage":
        return removeQuotationForValues(getCpuResult(jsonObject));
      case "netIo":
        return removeQuotationForValues(getNetResult(jsonObject));
      case "diskIo":
          try {
              return  removeQuotationForValues(getDiskIOResult(jsonObject));
          } catch (Exception e) {
              return "";
          }
        default:
        logger.debug("cannot analyze this type,please check it.");
        return "";

    }

  }

  private static String getDiskIOResult(JSONObject jsonObject) throws Exception {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    String hostname = jsonObject.get("hostname").toString();
    String timestamp = jsonObject.get("timestamp").toString();
    String diskIoSpeed = jsonObject.get("ioWriteSpeed").toString();

    BigDecimal bg1 = new BigDecimal(diskIoSpeed);
    aDouble = bg1.doubleValue();
    if(aDouble < 0){
        throw new Exception("");
    }
    BigDecimal bg2 = new BigDecimal(String.valueOf(30));
    BigDecimal bgNew = bg1.divide(bg2,2,BigDecimal.ROUND_HALF_EVEN);

    String diskIoSpeedResult =  String.valueOf(bgNew.doubleValue());

    String ioRead = jsonObject.get("ioReadSpeed").toString();

    BigDecimal bg3 = new BigDecimal(ioRead);
    BigDecimal bg4 = new BigDecimal(String.valueOf(30));
    BigDecimal bgNew1 = bg3.divide(bg4,2,BigDecimal.ROUND_HALF_EVEN);

    String ioReadSpeed =  String.valueOf(bgNew1.doubleValue());



    map.put("timestamp", timestamp);
    map.put("hostname", hostname);
    map.put("servicename", "system");
    map.put("servicetype", "diskIo");
    map.put("servicesubtype", "");
    map.put("metricname1", "ioWriteSpeed");
    map.put("metricdata1", diskIoSpeedResult);
    map.put("metricname2", "ioReadSpeed");
    map.put("metricdata2", ioReadSpeed);


    return JsonUtil.getJacksonString(map);
  }



  private static String getNetResult(JSONObject jsonObject){
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    String hostname = jsonObject.get("hostname").toString();
    String timestamp = jsonObject.get("timestamp").toString();
    String netUpload = jsonObject.get("netUploadSpeed").toString();

    BigDecimal bg1 = new BigDecimal(netUpload);
    BigDecimal bg2 = new BigDecimal(String.valueOf(30*1024));
    BigDecimal bgNew = bg1.divide(bg2,2,BigDecimal.ROUND_HALF_EVEN);


    String netUploadSpeed =  String.valueOf(bgNew.doubleValue());

    String netDownload = jsonObject.get("netUploadSpeed").toString();

    BigDecimal bg3 = new BigDecimal(netDownload);
    BigDecimal bg4 = new BigDecimal(String.valueOf(30*1024));
    BigDecimal bgNew1 = bg3.divide(bg4,2,BigDecimal.ROUND_HALF_EVEN);

    String netDownloadSpeed =  String.valueOf(bgNew1.doubleValue());

    map.put("timestamp", timestamp);
    map.put("hostname", hostname);
    map.put("servicename", "system");
    map.put("servicetype", "netIo");
    map.put("servicesubtype", "");
    map.put("metricname1", "netUploadSpeed");
    map.put("metricdata1", netUploadSpeed);
    map.put("metricname2", "netDownloadSpeed");
    map.put("metricdata2", netDownloadSpeed);



    return JsonUtil.getJacksonString(map);
  }


  private static String getDiskResult(JSONObject jsonObject) {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    String hostname = jsonObject.get("hostname").toString();
    String timestamp = jsonObject.get("timestamp").toString();
    String maxDiskUsage = jsonObject.get("maxDiskUsage").toString();

    BigDecimal bg1 = new BigDecimal(maxDiskUsage);
    BigDecimal bg2 = new BigDecimal(String.valueOf(0.01));
    BigDecimal bgNew = bg1.multiply(bg2);

    String diskUsage =  String.valueOf(bgNew.setScale(2,BigDecimal.ROUND_HALF_EVEN).doubleValue());

    map.put("timestamp", timestamp);
    map.put("hostname", hostname);
    map.put("servicename", "system");
    map.put("servicetype", "diskFileSystem");
    map.put("servicesubtype", "");
    map.put("metricname", "maxDiskUsage");
    map.put("metricdata", diskUsage);


    return JsonUtil.getJacksonString(map);
  }

  private static String getCpuResult(JSONObject jsonObject) {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    String hostname = jsonObject.get("hostname").toString();
    String timestamp = jsonObject.get("timestamp").toString();
    String sys_idle = jsonObject.get("SYS_IDLE").toString();
    String total = jsonObject.get("Total").toString();

    BigDecimal bgIdle = new BigDecimal(sys_idle);
    BigDecimal bgTotal = new BigDecimal(total);

    BigDecimal bgUsed = bgTotal.subtract(bgIdle);

    BigDecimal bgRes = bgUsed.divide(bgTotal, 2, BigDecimal.ROUND_HALF_EVEN);


    String cpuUsage = String.valueOf(bgRes.doubleValue());

    map.put("timestamp", timestamp);
    map.put("hostname", hostname);
    map.put("servicename", "system");
    map.put("servicetype", "cpuUsage");
    map.put("servicesubtype", "");
    map.put("metricname", "cpuUsage");
    map.put("metricdata", cpuUsage);


    return JsonUtil.getJacksonString(map);
  }

  private static String getMemResult(JSONObject jsonObject) {
    LinkedHashMap<String, String> map = new LinkedHashMap<>();
    String hostname = jsonObject.get("hostname").toString();
    String timestamp = jsonObject.get("timestamp").toString();
    String bc = jsonObject.get("bc3").toString();
    String memTotal = jsonObject.get("memTotal").toString();

    BigDecimal bgIdle = new BigDecimal(bc);
    BigDecimal bgTotal = new BigDecimal(memTotal);

    BigDecimal bgRes = bgIdle.divide(bgTotal, 2, BigDecimal.ROUND_HALF_EVEN);


    String memUsage = String.valueOf(bgRes.doubleValue());

    map.put("timestamp", timestamp);
    map.put("hostname", hostname);
    map.put("servicename", "system");
    map.put("servicetype", "memory");
    map.put("servicesubtype", "");
    map.put("metricname", "memUsage");
    map.put("metricdata", memUsage);


    return JsonUtil.getJacksonString(map);

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


}