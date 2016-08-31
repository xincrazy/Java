/**
 * Project Name:oneapm-log-common
 * File Name:TestGrok.java
 * Package Name:com.oneapm.log.common.grok
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.common.grok;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.Test;

/**
 * ClassName:TestGrok <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public class TestGrok {
    
    /**
     * test01: <br/>
     * 
     * @author hadoop
     * @since JDK 1.7
     */
    @Test
    public void test01() {
        //
        String log = "INFO [main] 2016-05-31 16:21:05,886 YamlConfigurationLoader.java:92 - Loading settings from file:/home/hadoop/software/cassandra-2.1.14/conf/cassandra.yaml";
        //
        GrokBean gb = new GrokBean(this.getClass()
                                       .getResource("/")
                                       .getPath() +
                                           "grok/patterns/log.grok",
                                   "%{CASSANDRALOG:cassandra}");
        System.out.println(gb.matchLog(log)
                             .toJson(true)
                             .orElse(""));
    }
    
    @ToString
    @Getter
    @Setter
    public static class Test02 {
        String  timestamp;
        String  codelocation;
        String  threadname;
        String  logmessage;
        String  loglevel;
        Integer year;
    }
    
    /**
     * test02: <br/>
     * 
     * @author hadoop
     * @since JDK 1.7
     */
    @Test
    public void test02() {
        //
        String log = "INFO [main] 2016-05-31 16:21:05,886 YamlConfigurationLoader.java:92 - Loading settings from file:/home/hadoop/software/cassandra-2.1.14/conf/cassandra.yaml";
        //
        GrokBean gb = new GrokBean(this.getClass()
                                       .getResource("/")
                                       .getPath() +
                                           "grok/patterns/log.grok",
                                   "%{CASSANDRALOG:cassandra}");
        Test02 t = gb.matchLog(log)
                     .getObject(Test02.class);
        System.out.println(t.toString());
    }
    
    /**
     * test03: <br/>
     * 
     * @author hadoop
     * @since JDK 1.7
     */
    @Test
    public void test03() {
        //
        String log = "2016-06-24 15:17:34.816 [Thread-3] INFO c.o.research.alert.data.cassandra.EventServiceImpl - Event[{\"feed\":\"metrics\",\"timestamp\":\"2016-06-24 15:17:34:816\",\"service\":\"alert\",\"host\":\"10.128.34.137\",\"metric\":\"event/failure/speed\",\"value\":0}] ";
        //
        GrokBean gb = new GrokBean(this.getClass()
                                       .getResource("/")
                                       .getPath() +
                                           "grok/patterns/log.grok",
                                   "%{AKKALOG:akka}");
        System.out.println(gb.matchLog(log)
                             .toJson(true)
                             .orElse(""));
    }
    
    /**
     * test04: <br/>
     * 
     * @author hadoop
     * @since JDK 1.7
     */
    @Test
    public void test04() {
        //DRUIDLOG (?<timestamp>%{YEAR:year}-%{MONTHNUM:month}-%{DAYNUM:day} %{HOUR:hour}:%{MINUTE:minute}:%{SECOND:second},%{MILLSECOND:mills})\s+?%{LOGLEVEL:loglevel}\s+?(?<codelocation>[\w\-:.\d]+?)\s+?\[%{DRUIDTHREADNAME:threadname}\]\s+?Event\s+?\[%{ANY:logmessage}\]
        String log = "2016-06-27 03:50:09,304 INFO c.m.e.c.LoggingEmitter [MonitorScheduler-0] Event [{\"feed\":\"metrics\",\"timestamp\":\"2016-06-27T03:50:09.304Z\",\"service\":\"realtime\",\"host\":\"10.165.113.43:8084\",\"metric\":\"events/thrownAway\",\"value\":6641,\"user2\":\" metricData_druid_oneHour_longterm\"}]";
        //
        GrokBean gb = new GrokBean(this.getClass()
                                       .getResource("/")
                                       .getPath() +
                                           "grok/patterns/log.grok",
                                   "%{DRUIDLOG:druid}");
        System.out.println(gb.matchLog(log)
                             .toJson(true)
                             .orElse(""));
    }

    /**
     * @author zhaoxin
     * test druid partern for all druid logs
     */

    @Test
    public void testxin() {
        //DRUIDLOGCOMMON (?<timestamp>%{YEAR:year}-%{MONTHNUM:month}-%{DAYNUM:day} %{HOUR:hour}:%{MINUTE:minute}:%{SECOND:second},%{MILLSECOND:mills})\s+?%{LOGLEVEL:loglevel}\s+?%{ANY:logmessage}
        String log = "2016-08-24 08:42:39,100 WARN i.d.s.c.r.LoadRule [Coordinator-Exec--0] Not enough [_default_tier] servers or node capacity to assign segment[druid_metric_10minute_2016-06-22T17:00:00.000Z_2016-06-22T18:00:00.000Z_2016-06-22T17:00:00.000Z]! Expected Replicants[2]";
        //
        GrokBean gb = new GrokBean(this.getClass()
                .getResource("/")
                .getPath() +
                "grok/patterns/log.grok",
                "%{ALLCOMMON:druid}");
        System.out.println(gb.matchLog(log)
                .toJson(true)
                .orElse(""));
    }
    
    /**
     * test05: <br/>
     * 
     * @author hadoop
     * @since JDK 1.8
     */
    @Test
    public void test05() {
        //
        String log = "10.128.6.60 - - [04/Jul/2016:10:51:44 +0800] \"POST /E_Menu/userlogin/login HTTP/1.1\" 302 -";
        //
        GrokBean gb = new GrokBean(this.getClass()
                                       .getResource("/")
                                       .getPath() +
                                           "grok/patterns/log.grok",
                                   "%{TOMCATLOG:tomcatlog}");
        System.out.println(gb.matchLog(log)
                             .toJson(true)
                             .orElse(""));
    }
    
    /**
     * test06: <br/>
     * 
     * @author hadoop
     * @since JDK 1.8
     */
    @Test
    public void test06() {
        //
        String log = "10.128.6.60 - - [04/Jul/2016:10:51:44 +0800] \"GET /E_Menu/userlogin/restaurant.action HTTP/1.1\" 200 6736";
        //
        GrokBean gb = new GrokBean(this.getClass()
                                       .getResource("/")
                                       .getPath() +
                                           "grok/patterns/log.grok",
                                   "%{TOMCATLOG:tomcatlog}");
        System.out.println(gb.matchLog(log)
                             .toJson(true)
                             .orElse(""));
    }
    
    /**
     * test07: <br/>
     * 
     * @author hadoop
     * @since JDK 1.8
     */
    @Test
    public void test07() {
        //
        String log = "10.128.6.60 - - [04/Jul/2016:10:51:44 +0800] \"GET /E_Menu/userlogin/restaurant.action HTTP/1.1\" 200 6736 34";
        //
        GrokBean gb = new GrokBean(this.getClass()
                                       .getResource("/")
                                       .getPath() +
                                           "grok/patterns/log.grok",
                                   "%{TOMCATLOG:tomcatlog}");
        System.out.println(gb.matchLog(log)
                             .toJson(true)
                             .orElse(""));
    }
    
    /**
     * test08: <br/>
     * 
     * @author hadoop
     * @since JDK 1.8
     */
    @Test
    public void test08() {
        //
        String log = "2016-06-25T16:30:48.107+0800: 1016.343: [Full GC (Ergonomics) [PSYoungGen: 14848K->14848K(29184K)] [ParOldGen: 87426K->87424K(87552K)] 102274K->102272K(116736K), [Metaspace: 101003K->101003K(1144832K)], 0.1125602 secs] [Times: user=0.29 sys=0.00, real=0.11 secs]";
        //
        
        String log2= "2016-07-11T10:39:19.507+0800: 8614.295: [Full GC (System.gc()) [PSYoungGen: 20421K->9218K(36864K)] [ParOldGen: 87508K->87161K(87552K)] 107929K->96379K(124416K), [Metaspace: 104403K->104403K(1148928K)], 0.1829835 secs] [Times: user=0.55 sys=0.00, real=0.18 secs]";
        GrokBean gb = new GrokBean(this.getClass()
                                       .getResource("/")
                                       .getPath() +
                                           "grok/patterns/log.grok",
                                   "%{GCLOG:gclog}");
        System.out.println(gb.matchLog(log2)
                             .toJson(true)
                             .orElse(""));
    }

    /**
     * test08: <br/>
     *
     * @author hadoop
     * @since JDK 1.8
     */
    @Test
    public void test09() {

        String log =  "2016-07-29T11:20:44.202+0800: 866847.417: [GC (Allocation Failure) 2016-07-29T11:20:44.202+0800: 866847.417: [ParNew: 340765K->10629K(368640K), 0.0351048 secs] 3664353K->3337336K(6250496K), 0.0353582 secs] [Times: user=0.14 sys=0.00, real=0.04 secs] ";
        String log3 = "2016-08-01T10:40:24.709+0800: 3038.253: [GC (Allocation Failure) 2016-08-01T10:40:24.709+0800: 3038.253: [ParNew: 337530K->12445K(368640K), 0.0222301 secs] 3226912K->2909080K(6250496K), 0.0223375 secs] [Times: user=0.09 sys=0.00, real=0.02 secs] ";
        String log2 = "2016-07-26T11:25:34.359+0800: 13.681: [GC (Allocation Failure) [PSYoungGen: 42458K->1268K(63488K)] 153219K->112802K(238592K), 0.0035722 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] ";
        //String log2= "2016-07-11T10:39:19.507+0800: 8614.295: [Full GC (System.gc()) [PSYoungGen: 20421K->9218K(36864K)] [ParOldGen: 87508K->87161K(87552K)] 107929K->96379K(124416K), [Metaspace: 104403K->104403K(1148928K)], 0.1829835 secs] [Times: user=0.55 sys=0.00, real=0.18 secs]";
        String log4 =  "157146.855: [GC157146.855: [ParNew: 3409741K->50880K(3774912K), 0.0268110 secs] 4197275K->842322K(7969216K), 0.0269480 secs] [Times: user=0.14 sys=0.00, real=0.03 secs]";
        String log5 = "97043.775: [GC (Allocation Failure) 97043.776: [ParNew: 5048712K->64087K(5662336K), 0.0291789 secs] 5165316K->180713K(11953792K), 0.0293851 secs] [Times: user=0.27 sys=0.01, real=0.03 secs]";
        GrokBean gb = new GrokBean(this.getClass()
                .getResource("/")
                .getPath() +
                "grok/patterns/log.grok",
                "%{YOUNGGC_TEST:younggc}");
        System.out.println(gb.matchLog(log5)
                .toJson(true)
                .orElse(""));
    }

}
