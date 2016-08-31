package com.oneapm.log.agent.flume.interceptor.system;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import com.google.common.base.Charsets;
import com.oneapm.log.agent.flume.interceptor.Constants;
import com.oneapm.log.common.utils.JsonUtil;

/**
 * AI-1404 监控指定目录的容量变化，默认每小时一次
 * @author tangyang
 *
 *flume配置如下
 *
# Name the components on this agent
a1.sources = src-1
a1.sinks = sk-1
a1.channels = ch-1

# Describe/configure the source
a1.sources.src-1.type = exec
a1.sources.src-1.shell = /bin/bash -c
a1.sources.src-1.command =directory=/home/tang;while true;do capacity=`du $directory -cBM --max-depth=1 2>/dev/null|tail -n 1|awk '{print $1}'|sed 's/[a-zA-Z]//g'`;echo "direcitory:\""$directory"\",capacity:"$capacity;sleep 1h;done;


#System Interceptor
a1.sources.src-1.interceptors=i1 i2
a1.sources.src-1.interceptors.i1.type=com.oneapm.log.agent.flume.interceptor.system.DirectoryCapacityInterceptor$DirectoryCapacityInterceptorBuilder
a1.sources.src-1.interceptors.i2.type=com.oneapm.log.agent.flume.interceptor.transform.TransformInterceptor$TransformInterceptorBuilder


# Use a channel which buffers events in memory
a1.channels.ch-1.type = memory
a1.channels.ch-1.capacity = 100000
a1.channels.ch-1.transactionCapacity = 10000

# b) File Roll Sink
a1.sinks.sk-1.type = file_roll
a1.sinks.sk-1.sink.directory = /home/tang/loggs/output/directoryCapacity
a1.sinks.sk-1.sink.rollInterval = 0

# Bind the source and sink to the channel
a1.sources.src-1.channels = ch-1
a1.sinks.sk-1.channel = ch-1
 *
 */

/**
 * flume 的启动命令如下 bin/flume-ng agent -n a1 -c conf -f conf/directoryCapacity.conf -Dflume.root.logger=INFO,console
 * @author tang
 *
 */

/**
 * 输出格式：{"direcitory":"/home/tang","capacity":"4969"}
 * @author tangyang
 *
 */
public class DirectoryCapacityInterceptor implements Constants,Interceptor{

  private DirectoryCapacityInterceptor(){
    
  }
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Event intercept(Event event) {
    String eventBodyStr = new String(event.getBody(), Charsets.UTF_8);
    LinkedHashMap<String, String> eventHashMap = JsonUtil.getMap(eventBodyStr);
    String jsonBody = JsonUtil.getJacksonString(eventHashMap);
    String directoryCapacityJsonStr = jsonBody;
    event.setBody(directoryCapacityJsonStr.getBytes(Charsets.UTF_8));
    return event;
  }

  @Override
  public List<Event> intercept(List<Event> events) {
    Iterator<Event> i$ = events.iterator();
    while (i$.hasNext()) {
      intercept((Event) i$.next());
    }
    return events;
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub
    
  }
  
  public  static class  DirectoryCapacityInterceptorBuilder  implements Builder {

    @Override
    public void configure(Context context) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public Interceptor build() {
        return new DirectoryCapacityInterceptor();
    }
  }
  

}
