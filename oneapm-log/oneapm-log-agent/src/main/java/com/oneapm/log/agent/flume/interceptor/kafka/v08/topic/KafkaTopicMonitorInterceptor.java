package com.oneapm.log.agent.flume.interceptor.kafka.v08.topic;

import java.nio.charset.Charset;
import java.util.*;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oneapm.log.agent.flume.interceptor.Constants;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.constants.OverallConstants;
import com.oneapm.log.common.utils.StringUtil;
/**
 * 此拦截器用于获取kafka的监控数据
 * 执行语句:bin/flume-ng agent --conf conf  --file  conf/kafka.conf  --name a1  -Dflume.root.logger=INFO,console
 * 具体的配置见template/flume/tasks/kafkaTopicMonitor.task.conf
 * edit by  tangyang 2016-7-8
 *
 */
///// TODO: 16-7-18 现在为修改kafka提供的ConsumerOffsetChecker获取offset，logsize和lag，如果能找到java原生获取的方法，则修改为原生获取的方式

public class KafkaTopicMonitorInterceptor implements OverallConstants, Interceptor,Constants {
	private static final Logger logger = LoggerFactory.getLogger(KafkaTopicMonitorInterceptor.class);
	private  String topic;
	private  String zookeeperConnect;
	private  String consumerGroup;
	private  String clusterName;
	private  String kafkaTypes;
	public String[] kafka;

	private Map<String, String> map = new HashMap<>();

	public KafkaTopicMonitorInterceptor(String kafkaTypes,String topic,String zookeeperConnect,String consumerGroup,String clusterName) {
		this.kafka = StringUtil.getArray(kafkaTypes, INTERCEPTOR_DEFAULT_KAFKA_SEPERATOR);
		this.topic = topic;
		this.zookeeperConnect = zookeeperConnect;
		this.consumerGroup = consumerGroup;
		this.clusterName = clusterName;

        map.put("topic",topic);
        map.put("zookeeperConnect",zookeeperConnect);
	    map.put("consumerGroup",consumerGroup);
	    map.put("clusterName",clusterName);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Event intercept(Event event) {
		String eventBodyStr = new String(event.getBody(), DEFAULT_CHARSET);
		Parser parser = null;
		try {
		    parser = LogParserFactory.getInstance().getParserByClass(this.getClass(),map);
        }catch (Exception e) {
            logger.error("no parser get",e);
        }
        if (parser != null) {
			String parserResult = parser.parser(eventBodyStr);
            event.setBody(parserResult.getBytes(DEFAULT_CHARSET));
		} else {
			event.setBody("NO parser is found!".getBytes(DEFAULT_CHARSET));
			return null;
		}
		return event;
	}

	@Override
	public List<Event> intercept(List<Event> events) {
		List<Event> eventList = new ArrayList<>();
		for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
			Event next = iterator.next();
			if (next == null) {
				iterator.remove();
			}
			Event current = intercept(next);
			if (current == null) {
				iterator.remove();
			}else {
				String eventBodyStr = new String(current.getBody(),DEFAULT_CHARSET);
				eventBodyStr = eventBodyStr.replace("[","").replace("]","");
				eventBodyStr += ", ";
				String[] eventDataArray = eventBodyStr.split("}, ");
				for(String singleEventBody:eventDataArray) {
					singleEventBody += "}";
					Event event = EventBuilder.withBody(singleEventBody,DEFAULT_CHARSET,current.getHeaders());
					eventList.add(event);
				}
			}
		}
		return eventList;
	}

	public static class KafkaTopicMonitorInterceptorBuilder implements Builder {
		private static final String TOPIC = "topic";
		private static final String ZOOKEEPER_CONNECT = "zookeeperConnect";
		private static final String ZOOKEEPER_CONNNECT_DFLT = "localhost:2181";
		private static final String CONSUMER_GROUP = "consumerGroup";
		private static final String KAFKA_TYPES = "kafkaTypes";
		private static final String DEFAULT_KAFKA_TYPE = "kafka/kafkaTopicMonitor";
		private static final String CLUSTER_NAME = "clusterName";

		private  String topic;
		private  String zookeeperConnect;
		private  String consumerGroup;
		private  String kafkaTypes;
		private  String clusterName;

		@Override
		public void configure(Context content) {
			kafkaTypes = content.getString(KAFKA_TYPES, DEFAULT_KAFKA_TYPE);
			topic = content.getString(TOPIC);
			Preconditions.checkArgument(!StringUtils.isEmpty(topic),"must supply a  kafka topic(may not be empty)");
			zookeeperConnect = content.getString(ZOOKEEPER_CONNECT,ZOOKEEPER_CONNNECT_DFLT);
			Preconditions.checkArgument(!StringUtils.isEmpty(zookeeperConnect),"must supply a zookeeper connect string,eg.localhost:2181 (may not be empty)");
			consumerGroup = content.getString(CONSUMER_GROUP);
			Preconditions.checkArgument(!StringUtils.isEmpty(CONSUMER_GROUP),"must supply a kafka consumer group(may not be empty)");
			clusterName = content.getString(CLUSTER_NAME);
			Preconditions.checkArgument(!StringUtils.isEmpty(clusterName),"must supply a kafka cluster name(may not be empty)");
		}

		@Override
		public Interceptor build() {
			Preconditions.checkNotNull(topic,"must supply a  kafka topic(may not be empty)");
			Preconditions.checkNotNull(zookeeperConnect,"must supply a zookeeper connect string,eg.localhost:2181 (may not be empty)");
			Preconditions.checkNotNull(consumerGroup,"must supply a kafka consumer group(may not be empty)");
			Preconditions.checkNotNull(clusterName,"must supply a kafka cluster name(may not be empty)");
			return new KafkaTopicMonitorInterceptor(kafkaTypes,topic,zookeeperConnect,consumerGroup,clusterName);
		}

	}

}