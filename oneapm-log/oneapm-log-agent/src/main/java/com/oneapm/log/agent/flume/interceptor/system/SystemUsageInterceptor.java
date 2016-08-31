package com.oneapm.log.agent.flume.interceptor.system;

import java.util.*;

import com.oneapm.log.common.utils.InterceptorEventUtil;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.Constants;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oneapm.log.agent.flume.interceptor.system.SystemUsageInterceptor.DEFAULT_SYSTEM_USAGE_TYPE;
import static com.oneapm.log.agent.flume.interceptor.system.SystemUsageInterceptor.SYSTEM_USAGE_TYPES;
import static com.oneapm.log.agent.flume.parser.transform.TransformParser.parser;


/*
# Name the components on this agent
a1.sources = src-1
a1.sinks = sk-1
a1.channels = ch-1

# Describe/configure the source
a1.sources.src-1.type = exec
a1.sources.src-1.shell = /bin/bash -c
a1.sources.src-1.command = sh ./sbin/system.sh

#System Interceptor
a1.sources.src-1.interceptors=i1
a1.sources.src-1.interceptors.i1.type=com.oneapm.log.agent.flume.interceptor.system.SystemUsageInterceptor$SystemUsageInterceptorBuilder
a1.sources.src-1.interceptors.i1.systemUsageTypes=system/diskFileSystem, system/memory, system/cpuUsage


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

 */

/*
执行语句: bin/flume-ng agent -n a1 -c conf -f conf/filename.conf -Dflume.root.logger=INFO,console
 */
public class SystemUsageInterceptor implements Interceptor, Constants {

  public static final String SYSTEM_USAGE_TYPES = "systemUsageTypes";
  public static final String DEFAULT_SYSTEM_USAGE_TYPE = "system/diskFileSystem";
  private String[] systemUsage;
  private static final Logger logger = LoggerFactory.getLogger(SystemUsageInterceptor.class);
  Map<String,String> map = new HashMap<>();
  public SystemUsageInterceptor(String systemUsageTypes) {
    this.systemUsage = StringUtil.getArray(systemUsageTypes, INTERCEPTOR_DEFAULT_SYSTEM_USAGE_SEPERATOR);
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
    String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
    try {
      Parser parser = LogParserFactory.getInstance().getParserByClass(this.getClass(),map);

      if (parser != null) {
        String parserResult = parser.parser(eventBodyStr);
        if("".equals(parserResult)){
          logger.debug("parserResult is null");
          return null;
        }
        event.setBody(parserResult.getBytes(Charsets.UTF_8));
      } else {
        logger.error("No parser is found");
        return null;
      }
    }catch(Exception e){
      logger.debug(e.getMessage());
      return null;
    }

    return event;
  }

  @Override
  public List<Event> intercept(List<Event> events) {
    List<Event> eventList = new ArrayList<>();
    for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {

      Event next = iterator.next();
      Event current = intercept(next);

      if (current == null) {
        iterator.remove();
      }else{
        eventList.addAll(InterceptorEventUtil.eventTransformer(current));
      }
    }
    return eventList;
  }

  public static class SystemUsageInterceptorBuilder implements Interceptor.Builder {

    private String systemUsageTypes;

    @Override
    public void configure(Context content) {
      systemUsageTypes = content.getString(SYSTEM_USAGE_TYPES, DEFAULT_SYSTEM_USAGE_TYPE);
    }

    @Override
    public Interceptor build() {
      return new SystemUsageInterceptor(systemUsageTypes);
    }

  }

}