package com.oneapm.log.agent.flume.parser.transform;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oneapm.log.agent.flume.parser.LogFormatter;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.utils.JsonUtil;
import com.oneapm.log.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformParser implements Parser {

  private static final Logger logger = LoggerFactory.getLogger(TransformParser.class);

  public static TransformParser parser;

  public static TransformParser getParser() {
    if (parser == null)
      parser = new TransformParser();
    return parser;
  }

  /**
   * 用途:把统一之后的Json字符串里面对应的key,value提取出来,放到一个LinkedHashMap当中.
   * 
   * @param content
   * @return LinkedHashMap对象
   */
  public static LinkedHashMap<String, String> getTransformMap(String content) {
    Matcher m = Pattern.compile(".*(\\{.*\\}).*").matcher(content);
    LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
    String metrics = "";
    if (m.find()) {
      metrics = content.substring(m.start(1), m.end(1));
      content = content.replace(metrics, "\"0\"");
    } else {
      
    }

    LinkedHashMap<String, String> map = JsonUtil.getMap(content);
    result.put("timestamp", map.get("timestamp"));
    LinkedHashMap<String, String> metircsMap =
        JsonUtil.getMap(metrics.substring(1, metrics.length() - 1));
    for (String string : metircsMap.keySet()) {
      String[] meStrings = string.split("#&");
      LogFormatter[] logFormatter = LogFormatter.values();
      String keyString = map.get("key").toUpperCase();
      for (LogFormatter l : logFormatter) {
        if (keyString.equals(l.name())) {
          String[] re = l.getMetricContext();
          for (int i = 0; i < meStrings.length - 1; i++) {
            if (result.get(re[i + 1]) == null)
              result.put(re[i + 1], meStrings[i]);
          }
          result.put(meStrings[meStrings.length - 1], metircsMap.get(string));
        }
      }
    }
    return result;
  }

  @Override
  public String parser(String content) {
    LogFormatter[] typeArrays = LogFormatter.values();
    for (LogFormatter logFormatter : typeArrays) {
      String[] jsonArgs = logFormatter.getMetricContext();
      String name = logFormatter.name().toLowerCase();
      boolean system = false;
      if (name.equals("system"))
        system = StringUtil.containStrPart(content, jsonArgs, 3);
      if (StringUtil.containStrAll(content, jsonArgs) || system) {
        content = content.substring(1, content.length() - 1);
        String key = name;
        if (!logFormatter.name().equals("TRANSFORM")) {
          LinkedHashMap<String, String> map = JsonUtil.getMap(content);
          // 1.从形如{"k1":"v1","k2":"v2","k3":"v3"...}转化成形如{"key":"kafka","eventCategory":"RawMetricEvent","metrics":{"XXX#&YYY#&offset":"654","XXX#&YYY#&lag":"12"...},"ttl":"2400","timestamp":"1444705149182"}
          String temp = "";
          LinkedHashMap<String, Object> tran =
              new LinkedHashMap<String, Object>();
          tran.put("key", key);
          tran.put("eventCategory", "RawMetricEvent");
          LinkedHashMap<String, String> metric =
              new LinkedHashMap<String, String>();
          for (String string : map.keySet()) {
            if (string.equals("timestamp") == false) {
              Matcher m =
                  Pattern.compile("[0-9]\\d*\\.?\\d*").matcher(map.get(string));

              if (!m.matches()) {
                temp += map.get(string) + "#&";
              } else {
                String keyString = temp + string;
                metric.put(keyString, map.get(string));
              }
            }
          }

          if (metric.isEmpty()) {
            logger.error("the error content is :" + content);
          }

          tran.put("metrics", metric);
          tran.put("ttl", "2400");
          tran.put("timestamp", map.get("timestamp"));
          String result = JsonUtil.getJacksonDeepString(tran);
          return result;
        } else {
          // 2.和1相反
          if (content == "{}" || content.length() < 4) {
            return null;
          }

          String tempString = "";
          try {
            tempString = JsonUtil.getJacksonString(getTransformMap(content));
            String[] temps =
                tempString.substring(1, tempString.length() - 1).split(":");
            for (String firstLevel : temps) {
              String[] firstTemps = firstLevel.split(",");
              for (String secondLevel : firstTemps) {
                Matcher m =
                    Pattern.compile("\"[0-9]\\d*\\.?\\d*\"").matcher(
                        secondLevel);
                if (m.find()) {
                  String replaceString =
                      secondLevel.substring(1, secondLevel.length() - 1);
                  tempString =
                      tempString.replaceAll(secondLevel, replaceString);
                }

              }
            }
          } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("the error content is :" + content);
            return null;
          }

          return tempString;
        }
      }
    }
    return null;
  }

  public static String parser1(String content) {
    LogFormatter[] typeArrays = LogFormatter.values();
    for (LogFormatter logFormatter : typeArrays) {
      String[] jsonArgs = logFormatter.getMetricContext();
      String name = logFormatter.name().toLowerCase();
      boolean system = false;
      if (name.equals("system"))
        system = StringUtil.containStrPart(content, jsonArgs, 3);
      if (StringUtil.containStrAll(content, jsonArgs) || system) {
        content = content.substring(1, content.length() - 1);
        String key = name;
        if (!logFormatter.name().equals("TRANSFORM")) {
          LinkedHashMap<String, String> map = JsonUtil.getMap(content);
          // 1.从形如{"k1":"v1","k2":"v2","k3":"v3"...}转化成形如{"key":"kafka","eventCategory":"RawMetricEvent","metrics":{"XXX#&YYY#&offset":"654","XXX#&YYY#&lag":"12"...},"ttl":"2400","timestamp":"1444705149182"}
          String temp = "";
          LinkedHashMap<String, Object> tran =
              new LinkedHashMap<String, Object>();
          tran.put("key", key);
          tran.put("eventCategory", "RawMetricEvent");
          LinkedHashMap<String, String> metric =
              new LinkedHashMap<String, String>();
          for (String string : map.keySet()) {
            if (string.equals("timestamp") == false) {
              Matcher m =
                  Pattern.compile("[0-9]\\d*\\.?\\d*").matcher(map.get(string));
              if (!m.matches()) {
                temp += map.get(string) + "#&";
              } else {
                String keyString = temp + string;
                metric.put(keyString, map.get(string));
              }
            }
          }
          tran.put("metrics", metric);
          tran.put("ttl", "2400");
          tran.put("timestamp", map.get("timestamp"));
          String result = JsonUtil.getJacksonDeepString(tran);
          return result;
        } else {
          System.out.println("the result going to es" + content);
          // 2.和1相反
          String tempString =
              JsonUtil.getJacksonString(getTransformMap(content));
          String[] temps =
              tempString.substring(1, tempString.length() - 1).split(":");
          for (String firstLevel : temps) {
            String[] firstTemps = firstLevel.split(",");
            for (String secondLevel : firstTemps) {
              Matcher m =
                  Pattern.compile("\"[0-9]\\d*\\.?\\d*\"").matcher(secondLevel);
              if (m.matches()) {
                String replaceString =
                    secondLevel.substring(1, secondLevel.length() - 1);
                tempString = tempString.replaceAll(secondLevel, replaceString);
              }

            }
          }

          return tempString;
        }
      }
    }
    return null;
  }

  public static void main(String[] args) {
    String string =
        "{\"key\":\"system\",\"eventCategory\":\"RawMetricEvent\",\"metrics\":{\"iZ25979ub3rZ#&maxDiskUsage\":\"0.45\"},\"ttl\":\"2400\",\"timestamp\":\"1465368478064\"}";
    String reString = "{}";
    String resulString =
        "system/memory timestamp:1465205427942,hostname:will-desktop,memUsage:-16%";

    String errString =
        "{\"timestamp\":\"1466842884\",\"hostname\":\"will-desktop\",\"fullGCTime\":\"0.12\",\"GCMem\":\"116736\"}";

    System.out.println(parser1(errString));
  }

}