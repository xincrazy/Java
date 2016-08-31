package com.oneapm.log.agent.flume.interceptor.system;

import java.util.*;

import com.google.common.base.Preconditions;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.agent.flume.parser.factory.LogParserFactory;
import com.oneapm.log.common.constants.OverallConstants;
import com.oneapm.log.common.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import com.oneapm.log.agent.flume.interceptor.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tangyang
 * 执行语句: bin/flume-ng agent -n a1 -c conf -f conf/filename.conf  -Dflume.root.logger=INFO,console
 *
 */

public class ProcessAliveCheckerInterceptor implements Constants, Interceptor, OverallConstants {

    private Logger logger = LoggerFactory.getLogger(ProcessAliveCheckerInterceptor.class);
    public static final String PROCESS_ALIVE_TYPES = "ProcessAliveType";
    private static final String PROCESS_ALIVE_TYPE_DFLT = "system/processAlive";
    private String processName;
    private String processKeyword;
    private String[] process;

    private Map<String, String> map = new HashMap<String, String>();

    private ProcessAliveCheckerInterceptor(String processName, String processKeyword) {
        this.processName = processName;
        this.processKeyword = processKeyword;
        this.process = StringUtil.getArray(PROCESS_ALIVE_TYPES, INTERCEPTOR_DEFAULT_PROCESS_ALIVE_SEPERATOR);

        map.put("processName", processName);
        map.put("processKeyword", processKeyword);
    }

    @Override
    public void close() {
    }

    @Override
    public void initialize() {
    }

    @Override
    public Event intercept(Event event) {
        String eventBodyStr = new String(event.getBody(), DEFAULT_CHARSET);
        String matchedProcessAlive = StringUtil.containStr(eventBodyStr, process);
        Parser parser = null;
        try {
            parser = LogParserFactory.getInstance().getParserByClass(this.getClass(), map);
        } catch (Exception e) {
            logger.error("can't found processAlive parser", e);
            return null;
        }
        if (parser != null) {
            String parserResult = parser.parser(eventBodyStr);
            event.setBody(parserResult.getBytes(DEFAULT_CHARSET));
        } else {
            event.setBody("NO parser is found! ".getBytes(DEFAULT_CHARSET));
            return null;
        }
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

    public static class ProcessAliveCheckerInterceptorBuilder
            implements Interceptor.Builder {
        private static final String PROCESS_NAME = "processName";//进程名称
        private static final String PROCESS_KEYWORD = "processKeyword";//进程关键字
        private static final String PROCESS_ALIVE_TYPE = "processAliveTypes";
        private static final String PROCESS_ALIVE_TYPE_DFLT = "system/processAlive";

        private String processName;
        private String processKeyword;
        private String ProcessAliveType;

        @Override
        public void configure(Context context) {
            this.processName = context.getString(PROCESS_NAME);
            Preconditions.checkArgument(!StringUtils.isEmpty(processName), "must provide process name argument");
            this.processKeyword = context.getString(PROCESS_KEYWORD);
            Preconditions.checkArgument(!StringUtils.isEmpty(processKeyword), "must provide unique process keyword argument");
            this.ProcessAliveType = context.getString(PROCESS_ALIVE_TYPE, PROCESS_ALIVE_TYPE_DFLT);
        }

        @Override
        public Interceptor build() {
            //使用进程id
            Preconditions.checkArgument(!StringUtils.isEmpty(processName), "must provid process name argument");
            Preconditions.checkArgument(!StringUtils.isEmpty(processKeyword), "must provide process keyword argument");
            return new ProcessAliveCheckerInterceptor(this.processName, this.processKeyword);
        }
    }

}
