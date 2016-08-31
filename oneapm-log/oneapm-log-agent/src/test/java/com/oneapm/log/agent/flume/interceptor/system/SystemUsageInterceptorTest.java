package com.oneapm.log.agent.flume.interceptor.system;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * PcakgeName is com.oneapm.log.agent.flume.interceptor.system.
 * Created by will on 16-8-2.
 */
public class SystemUsageInterceptorTest {

    Interceptor systemInterceptor = null;

    @Before
    public void setUp()throws Exception{
        Map<String, String> context = new HashMap<>();
        context.put("systemUsageTypes", "system/diskFileSystem, system/memory, system/cpuUsage");
        Context content = new Context(context);

        Interceptor.Builder builder = new SystemUsageInterceptor.SystemUsageInterceptorBuilder();
        builder.configure(content);
        systemInterceptor = builder.build();
    }

    @Test
    public void test(){
        String cpu = "{\"system\":\"cpuUsage\",\"timestamp\":\"1470136246868\",\"hostname\":\"will-desktop\",\"SYS_IDLE\":\"130\",\"Total\":\"401\"}";
        String mem = "{\"system\":\"memory\",\"timestamp\":\"1470136246868\",\"hostname\":\"will-desktop\",\"bc3\":\"2953968\",\"memTotal\":\"16127992\"}";
        String disk = "{\"system\":\"diskFileSystem\",\"timestamp\":\"1470136246868\",\"hostname\":\"will-desktop\",\"maxDiskUsage\":\"25\"}";
        String netIo = "{\"system\":\"netIo\",\"timestamp\":\"1470387077818\",\"hostname\":\"will-desktop\",\"netUploadSpeed\":\"66\",\"netDownloadSpeed\":\"186\"}";
        String diskIo = "{\"system\":\"diskIo\",\"timestamp\":\"1470387077818\",\"hostname\":\"will-desktop\",\"ioWriteSpeed\":\"1000\",\"ioReadSpeed\":\"1000\"}";
        String wrongDiskIo = "{\"system\":\"diskIo\",\"timestamp\":\"1470387077818\",\"hostname\":\"will-desktop\",\"ioWriteSpeed\":\"-1\",\"ioReadSpeed\":\"-1\"}";
        String wrong = "";

        Event cpuEvent = EventBuilder.withBody(cpu, Charset.defaultCharset());
        Event memEvent = EventBuilder.withBody(mem, Charset.defaultCharset());
        Event diskEvent = EventBuilder.withBody(disk, Charset.defaultCharset());
        Event wrongEvent = EventBuilder.withBody(wrong, Charset.defaultCharset());
        Event netIoEvent = EventBuilder.withBody(netIo, Charset.defaultCharset());
        Event diskIoEvent = EventBuilder.withBody(diskIo, Charset.defaultCharset());
        Event wrongDiskIoEvent = EventBuilder.withBody(wrongDiskIo, Charset.defaultCharset());

        List<Event> eventList = new ArrayList<>();
        eventList.add(cpuEvent);
        eventList.add(memEvent);
        eventList.add(diskEvent);
        eventList.add(wrongEvent);
        eventList.add(netIoEvent);
        eventList.add(diskIoEvent);
        eventList.add(wrongDiskIoEvent);

        assertEquals(7, systemInterceptor.intercept(eventList).size());
    }
}
