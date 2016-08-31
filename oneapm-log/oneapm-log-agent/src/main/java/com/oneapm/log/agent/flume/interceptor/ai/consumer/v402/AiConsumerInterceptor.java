package com.oneapm.log.agent.flume.interceptor.ai.consumer.v402;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * # Name the components on this agent
a1.sources = src-1
a1.sinks = sk-1
a1.channels = ch-1
 
# Describe/configure the source
a1.sources.src-1.type = exec
a1.sources.src-1.shell = /bin/bash -c
a1.sources.src-1.command =  tail -f /oneapm/log/consumer/tps-stat.log;


#System Interceptor
a1.sources.src-1.interceptors=i1 
a1.sources.src-1.interceptors.i1.type=AiConsumerInterceptor$AiConsumerInterceptorBuilder

#当前AIconsumer的type类型只有一种，不需要配置改参数，也可以顺利执行
#a1.sources.src-1.interceptors.i1.aiConsumerTypes=
#a1.sources.src-1.interceptors.i2.type=com.oneapm.log.agent.flume.interceptor.transform.TransformInterceptor$TransformInterceptorBuilder


# Use a channel which buffers events in memory
a1.channels.ch-1.type = memory
a1.channels.ch-1.capacity = 100000
a1.channels.ch-1.transactionCapacity = 10000

# b) File Roll Sink
a1.sinks.sk-1.type = file_roll
a1.sinks.sk-1.sink.directory = /home/will/loggs/output
a1.sinks.sk-1.sink.rollInterval = 0

# Bind the source and sink to the channel
a1.sources.src-1.channels = ch-1
a1.sinks.sk-1.channel = ch-1



 * @author will
 *
 */

public class AiConsumerInterceptor  implements Interceptor{
	private static final Logger logger             = LoggerFactory.getLogger(AiConsumerInterceptor.class);

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
		String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
		logger.debug("event processed by AiConsumerInterceptor: " + eventBodyStr);
		//改部分的工厂模式建议需要修改，后续商榷之后再做更改
		Parser parser = null;
		try {
			parser = LogParserFactory.getInstance().getParserByClass(this.getClass(), null);
		} catch (Exception e) {
			logger.error("error when getting parser by class." + e.getMessage());
		}
		String parserResult = parser.parser(eventBodyStr);
		if (parserResult != null) {
			logger.debug("event after parsing by AiConsumerInterceptor: " + parserResult);
			event.setBody(parserResult.getBytes(Charsets.UTF_8));
		} else {
			return null;
		}
		return event;
	}

	@Override
	public List<Event> intercept(List<Event> events) {
		// TODO Auto-generated method stub
		for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {

			Event next = iterator.next();
			Event current = intercept(next);

			if (current == null) {
				iterator.remove();
			}
		}
		return events;
	}

	public static class AiConsumerInterceptorBuilder implements Interceptor.Builder {

		@Override
		public void configure(Context content) {
			// TODO Auto-generated method stub

		}

		@Override
		public Interceptor build() {
			logger.debug("inside AiConsumerInterceptorBuilder");
			return new AiConsumerInterceptor();
		}
	}
}