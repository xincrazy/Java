package com.oneapm.log.agent.flume.parser.system.linux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneapm.log.agent.flume.parser.Parser;
import com.oneapm.log.common.constants.GrokConstants;
import com.oneapm.log.common.grok.GrokBean;
import com.oneapm.log.common.utils.JsonUtil;
import com.oneapm.log.common.utils.LinuxPiper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ProcessAliveCheckerParser implements Parser {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(ProcessAliveCheckerParser.class);
    private String processName;
    private String processKeyword;

    public static Map<Integer, ProcessAliveCheckerParser> parserInstancePool = new HashMap<Integer, ProcessAliveCheckerParser>();

    @ToString
    @Getter
    @Setter
    public static class ProcessInfo {
        String pid;
        String cmd;
    }

    public ProcessAliveCheckerParser(String processName, String processKeyword) {
        this.processName = processName;
        this.processKeyword = processKeyword;
    }

    /**
     * 将进程相关信息放入content中
     *
     * @param content
     * @return
     */
    @Override
    public String parser(String content) {
        //抛弃原有event内容（仅有前缀）
        String hostname = null; //获取hostname
        String timestamp = String.valueOf(System.currentTimeMillis());//获取ts
        String processDetail = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String info = null;
        BufferedReader bufferedReader = null;
        //使用进程pid
        try {
            Process process1 = Runtime.getRuntime().exec("ps -efww");
            Process process2 = Runtime.getRuntime().exec("grep " + processKeyword);
            InputStream in = null;
            try {
                in = LinuxPiper.pipe(process1, process2);
            } catch (InterruptedException e) {
                logger.error("exec pipe error", e);
            }

            bufferedReader = new BufferedReader(new InputStreamReader(in));
            info = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> proceessAliveMap = new LinkedHashMap<>();

        proceessAliveMap.put("timestamp", timestamp);
        proceessAliveMap.put("hostname", hostname);
        proceessAliveMap.put("servicename", "processAlive");
        proceessAliveMap.put("servicetype", processName);
        proceessAliveMap.put("subservicetype", processKeyword);
        proceessAliveMap.put("metricname","alive");
        //可能在第一行显示grep信息
        if (StringUtils.contains(String.valueOf(info), "grep ")) {
            try {
                //如果还有数据往下读取一行
                if((info = bufferedReader.readLine())!=null) {
                    proceessAliveMap.put("metricdata", Integer.valueOf("1"));
                }else {
                    proceessAliveMap.put("metricdata", Integer.valueOf("0"));
                }
            }catch (IOException e) {
                logger.error("read line error",e);
            }
        } else {
            //可能第一行显示包含keyword的信息
            proceessAliveMap.put("metricdata", Integer.valueOf("1"));
        }
        String processAliveStr = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            processAliveStr = mapper.writeValueAsString(proceessAliveMap);
        } catch (JsonProcessingException e) {
            logger.error("parse json error", e);
        }
        return processAliveStr;
    }

    public static ProcessAliveCheckerParser getParser(String processName, String processKeyword) {
        int hashCode = processName.hashCode() ^ processKeyword.hashCode();
        if (parserInstancePool.get(hashCode) != null) {
            return parserInstancePool.get(hashCode);
        } else {
            ProcessAliveCheckerParser parser = new ProcessAliveCheckerParser(processName, processKeyword);
            parserInstancePool.put(Integer.valueOf(hashCode),parser);
            return parser;
        }
    }
}
