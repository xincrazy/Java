package com.oneapm.log.agent.flume.parser.ai.consumer.v402;

import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.constants.GrokConstants;
import com.oneapm.log.common.grok.GrokBean;
import com.oneapm.log.common.utils.HostNameUtil;
import com.oneapm.log.common.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AIConsumerParser implements Parser {
  private static final String AICONSUMER_GROK_PATTERN = "%{AICONSUMER:aiconsumer}";
  private static final Logger logger             = LoggerFactory.getLogger(AIConsumerParser.class);
  private static AIConsumerParser parser;

  @Override
  public String parser(String content) {
    GrokBean gb = new GrokBean(
            this.getClass().getResource("/").getPath() + GrokConstants.DEFAULT_GROK_PATTERN_FILE_PATH,
            AICONSUMER_GROK_PATTERN);

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String today = format.format(new Date());
    AIConsumerGrokResult aiConsumerGrokResult = gb.matchLog(content).getObject(AIConsumerGrokResult.class);
    logger.debug("grok parse result: " + gb.matchLog(content).toJson(true).orElse(""));
    if (aiConsumerGrokResult.getTimestamp() == null) {
      return null;
    }
    String time = today + " " + aiConsumerGrokResult.getTimestamp();
    String unixTimeStamp = TimeUtil.formatTZ(time,"yyyy-MM-dd hh:mm:ss.SSS");
    return buildResult(unixTimeStamp, aiConsumerGrokResult.getDataworker(), aiConsumerGrokResult.getValue());
  }

  /**
   * Private class used to store the Grok match result.
   */
  @ToString
  @Getter
  @Setter
  private static class AIConsumerGrokResult
  {
      String timestamp;
      String dataworker;
      String value;
  }

  /**
   * Build a key value format result string based on the given keys and values.
   */
  private String buildResult(String timestamp, String dataworker, String value) {
    return "{\"timestamp\":" + timestamp + ",\"hostname\":\"" + HostNameUtil.getHostName()
            +"\",\"servicename\":\"AIConsumer\",\"metricname\":\""
            + dataworker + "\",\"metricdata\":" + value.replaceAll(" ","") +"}";
  }

  public static AIConsumerParser getParser() {
    if (parser == null) {
      parser = new AIConsumerParser();
    }
    return parser;
  }
}
