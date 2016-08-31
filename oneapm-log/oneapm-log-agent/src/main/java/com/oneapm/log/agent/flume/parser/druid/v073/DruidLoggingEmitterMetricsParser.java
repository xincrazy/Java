package com.oneapm.log.agent.flume.parser.druid.v073;

import com.oneapm.log.agent.flume.parser.LogFormatter;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.constants.GrokConstants;
import com.oneapm.log.common.grok.GrokBean;
import com.oneapm.log.common.utils.HostNameUtil;
import com.oneapm.log.common.utils.JsonUtil;
import com.oneapm.log.common.utils.StringUtil;
import com.oneapm.log.common.utils.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DruidLoggingEmitterMetricsParser implements Parser {
	private static final Logger logger             = LoggerFactory.getLogger(DruidLoggingEmitterMetricsParser.class);
	private static final String DRUIDLOG_GROK_PATTERN = "%{DRUIDLOG:druid}";
	private static final String DRUIDQUERY_GROK_PATTERN = "%{DRUIDQUERYLOG:druidQuery}";
	private static final String DRUIDUNANNOUNCING_GROK_PATTERN = "%{DRUIDUNANNOUNCING:druidUnannoucing}";
	private static final String DRUIDWALKTHROUGHROWS_GROK_PATTERN = "%{DRUIDWALKTHROUGHLOG:druidWalkThroughLog}";
	private static final String[] DRUIDQUERYNODES = {"broker-query", "coordinator-query", "realtime-query", "historical-query"};
	private static Map<Integer, DruidLoggingEmitterMetricsParser> parserInstancePool = new HashMap<>();
	private String druidNode;
	private String tolerableRequestTime;
	private long thrownAwayValue = 0;
	private long unparseableValue = 0;
	private long processedValue = 0;
	private boolean thrownAwayValueSet = false;
	private boolean unparseableValueSet = false;
	private boolean processedValueSet = false;

	private DruidLoggingEmitterMetricsParser(String druidNode, String tolerableRequestTime) {
		this.druidNode = druidNode;
		this.tolerableRequestTime = tolerableRequestTime;
	}

	public static DruidLoggingEmitterMetricsParser getParser(String druidNode, String tolerableRequestTime) {
		int hashCode = druidNode.hashCode() ^ tolerableRequestTime.hashCode();
		if (parserInstancePool.get(hashCode) != null) {
			return parserInstancePool.get(hashCode);
		} else {
			DruidLoggingEmitterMetricsParser parser = new DruidLoggingEmitterMetricsParser(druidNode, tolerableRequestTime);
			parserInstancePool.put(hashCode, parser);
			return parser;
		}
	}

	@Override
	public String parser(String content) {
		logger.debug("content before parsing: " + content);
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		DruidLogResult druidLogResult = null;
		if (content.contains("Unannouncing segment")) {
			GrokBean gb = new GrokBean(
					this.getClass().getResource("/").getPath() + GrokConstants.DEFAULT_GROK_PATTERN_FILE_PATH,
					DRUIDUNANNOUNCING_GROK_PATTERN);
			druidLogResult = gb.matchLog(content).getObject(DruidLogResult.class);
			map.put("timestamp", String.valueOf(System.currentTimeMillis()));
			map.put("hostname", HostNameUtil.getHostName());
			map.put("servicename", "druid");
			map.put("servicetype", this.druidNode);
			map.put("servicesubtype", druidLogResult.getDatasource());
			map.put("metricname", "Unannouncing segment");
			map.put("metricdata", String.valueOf(1));
		}else if (content.contains("completed walk through of")) {
			GrokBean gb = new GrokBean(
					this.getClass().getResource("/").getPath() + GrokConstants.DEFAULT_GROK_PATTERN_FILE_PATH,
					DRUIDWALKTHROUGHROWS_GROK_PATTERN);
			druidLogResult = gb.matchLog(content).getObject(DruidLogResult.class);
			map.put("timestamp", getTimeStringFromGrokOutput(druidLogResult));
			map.put("hostname", HostNameUtil.getHostName());
			map.put("servicename", "druid");
			map.put("servicetype", this.druidNode);
			map.put("servicesubtype", druidLogResult.getDatasource());
			map.put("metricname", "indexMerger/walked/rate");
			map.put("metricdata",
					String.valueOf(
							Long.valueOf(druidLogResult.getWalkthroughrows().replaceAll(",", ""))
									/Long.valueOf(druidLogResult.getTotaltime().replaceAll(",", ""))));
		}else if (Arrays.asList(DRUIDQUERYNODES).contains(this.druidNode)) {
			GrokBean gb = new GrokBean(
					this.getClass().getResource("/").getPath() + GrokConstants.DEFAULT_GROK_PATTERN_FILE_PATH,
					DRUIDQUERY_GROK_PATTERN);
			druidLogResult = gb.matchLog(content).getObject(DruidLogResult.class);
			map = formatMap(druidLogResult);
		}else {
			GrokBean gb = new GrokBean(
					this.getClass().getResource("/").getPath() + GrokConstants.DEFAULT_GROK_PATTERN_FILE_PATH,
					DRUIDLOG_GROK_PATTERN);
			druidLogResult = gb.matchLog(content).getObject(DruidLogResult.class);
			map = formatMap(druidLogResult);
		}
		String result = JsonUtil.getJacksonString(map);
		result = StringUtil.removeQuotationForValues(result);
		logger.debug("content after parsing: " + result);
		return result;
	}

	/**
	 * 将metric的结果按照StringUtil.jsonArgument()的次序排好,返回值为一个正确形式的LinkedHashMap
	 *
	 * @param druidLogResult
	 * @return result
	 */
	private LinkedHashMap<String, String> formatMap(DruidLogResult druidLogResult) {
		LinkedHashMap<String, String> result = new LinkedHashMap<>();
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("hostname", HostNameUtil.getHostName());
		map.put("timestamp",getTimeStringFromGrokOutput(druidLogResult));
		map.put("servicename", "druid");
		map.put("servicetype", this.druidNode);
		if (Arrays.asList(DRUIDQUERYNODES).contains(this.druidNode)) {
			map.put("hostname", HostNameUtil.getHostName());
//			if (Integer.valueOf(druidLogResult.getRequesttime()) >= Integer.valueOf(this.tolerableRequestTime)) {
//				map.put("servicesubtype", druidLogResult.getQuerymessage());
//			}
			map.put("servicesubtype", druidLogResult.getDatasource());
			map.put("servicesubtype1", druidLogResult.getQueryType());
			map.put("metricname", "request/time");
			map.put("metricdata", druidLogResult.getRequesttime());
		} else {
			map = JsonUtil.getMap(druidLogResult.getLogmessage().replaceAll("\\{", "").replaceAll("}", ""));
			map.put("hostname", HostNameUtil.getHostName());
			map.put("timestamp",getTimeStringFromGrokOutput(druidLogResult));
			map.put("servicename", "druid");
			map.put("servicetype", this.druidNode);
			map.put("servicesubtype", map.get("dataSource"));
			map.put("metricname", map.get("metric"));
			map.put("metricdata", map.get("value"));
			if ("events/thrownAway".equals(map.get("metricname"))) {
				this.thrownAwayValue = Long.valueOf(map.get("metricdata"));
				this.thrownAwayValueSet = true;
			}
			if ("events/unparseable".equals(map.get("metricname"))) {
				this.unparseableValue = Long.valueOf(map.get("metricdata"));
				this.unparseableValueSet = true;
			}
			if ("events/processed".equals(map.get("metricname"))) {
				this.processedValue = Long.valueOf(map.get("metricdata"));
				this.processedValueSet = true;
			}
			if (this.thrownAwayValueSet && this.unparseableValueSet && this.processedValueSet) {
				map.put("metricname1", "timeout/rate");
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				double timeoutrate = this.thrownAwayValue*1.0 / (this.thrownAwayValue + this.processedValue);
				map.put("metricdata1", String.valueOf(decimalFormat.format(timeoutrate*100)));
				map.put("metricname2", "baddata/rate");
				double baddatarate = this.unparseableValue*1.0 / (this.thrownAwayValue + this.unparseableValue + this.processedValue);
				map.put("metricdata2", String.valueOf(decimalFormat.format(baddatarate*100)));
				this.processedValueSet = false;
				this.unparseableValueSet = false;
				this.thrownAwayValueSet = false;
			}
 		}
		for (String string : LogFormatter.DRUID.getMetricContext()) {
			String value = map.get(string);
			if (value != null) {
				result.put(string, value);
			}
		}
		return result;
	}

	/**
	 * Private class used to store the Grok match result.
	 */
	@ToString
	@Getter
	@Setter
	private static class DruidLogResult {
		String year;
		String month;
		String day;
		String hour;
		String minute;
		String second;
		String mills;
		String logmessage;
		String remotehostname;
		String querymessage;
		String requesttime;
		String querystatus;
		String datasource;
		String walkthroughrows;
		String totaltime;
		String queryType;
	}

	/**
	 * Will construct a time format like "yyyy-MM-dd'T'HH:mm:ss.SSSX",
	 * basing on Grok output.
	 *
	 * @return a timestamp for the given Grok output.
	 */
	private String getTimeStringFromGrokOutput(DruidLoggingEmitterMetricsParser.DruidLogResult druidLogResult) {
		StringBuffer time = new StringBuffer();
		time.append(druidLogResult.getYear());
		time.append("-");
		time.append(druidLogResult.getMonth());
		time.append("-");
		time.append(druidLogResult.getDay());
		time.append("T");
		time.append(druidLogResult.getHour());
		time.append(":");
		time.append(druidLogResult.getMinute());
		time.append(":");
		time.append(druidLogResult.getSecond());
		time.append(".");
		time.append(druidLogResult.getMills() == null ? "000" : druidLogResult.getMills());
		time.append("Z");

		return TimeUtil.formatTZ(time.toString());
	}
}