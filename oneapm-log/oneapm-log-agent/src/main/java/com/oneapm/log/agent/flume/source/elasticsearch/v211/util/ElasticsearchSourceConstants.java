/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.lang3.StringUtils;

import com.oneapm.log.agent.flume.source.elasticsearch.v211.ElasticsearchSource;

/**
 * ClassName: ElasticsearchSourceConstants <br/>
 * Function: <br/>
 * date: <br/>
 *
 * @author hadoop
 * @version
 * @since JDK 1.7
 */
public class ElasticsearchSourceConstants {
    // params
    public static final String CLUSTER_NAME              = "cluster.name";
    public static final String HOST_NAME                 = "host.name";
    public static final String PORT                      = "port";
    public static final String BATCH_SIZE                = "batch.size";
    public static final String DURATION                  = "duration";
    public static final String BUFFER_SIZE               = "buffer.size";
    public static final String BUILDER_TYPE              = "builder.type";
    
    public static final String NODES_FILTERS             = "nodes.filters";
    public static final String NODES_METRICS             = "nodes.metrics";
    
    public static final String INDICES_FILTERS           = "indices.filters";
    public static final String INDICES_METRICS           = "indices.metrics";
    public static final String INDICES_INDEXNAME         = "indices.indexname";
    // for elasticsearch
    public static final String PROPERTY_PREFIX           = "elasticsearch.";
    // default
    public static final int    DEFAULT_BATCH_SIZE        = 50;
    public static final long   DEFAULT_DURATION          = 60000;
    public static final String DEFAULT_CLUSTER_NAME      = "elasticsearch";
    public static final String DEFAULT_HOST_NAME         = "127.0.0.1";
    public static final int    DEFAULT_PORT              = 9300;
    public static final int    DEFAULT_BUFFER_SIZE       = 5000;
    public static final String DEFAULT_BUILDER_TYPE      = "com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceUniformBuilder";
    
    public static final String DEFAULT_NODES_FILTERS     = "breaker,fs,http,jvm,os,process,script,threadpool,indices,transport";
    public static final String DEFAULT_NODES_METRICS     = "nodes.indices.indexing.index_total,"
                                                           + "nodes.indices.indexing.index_time_in_millis,"
                                                           + "nodes.indices.indexing.index_current,"
                                                           + "nodes.indices.indexing.index_failed,"
                                                           + "nodes.indices.indexing.delete_total,"
                                                           + "nodes.indices.indexing.delete_time_in_millis,"
                                                           + "nodes.indices.indexing.delete_current,"
                                                           + "nodes.indices.indexing.noop_update_total,"
                                                           + "nodes.indices.indexing.throttle_time_in_millis,"
                                                           + "nodes.indices.search.open_contexts,"
                                                           + "nodes.indices.search.query_total,"
                                                           + "nodes.indices.search.query_time_in_millis,"
                                                           + "nodes.indices.search.query_current,"
                                                           + "nodes.indices.search.fetch_total,"
                                                           + "nodes.indices.search.fetch_time_in_millis,"
                                                           + "nodes.indices.search.fetch_current,"
                                                           + "nodes.indices.search.scroll_total,"
                                                           + "nodes.indices.search.scroll_time_in_millis,"
                                                           + "nodes.indices.search.scroll_current,"
                                                           + "nodes.indices.indexing.index_speed,"
                                                           + "nodes.indices.search.search_latency,"
                                                           + "nodes.os.load_average,"
                                                           + "nodes.os.mem.total_in_bytes,"
                                                           + "nodes.os.mem.free_in_bytes,"
                                                           + "nodes.os.mem.used_in_bytes,"
                                                           + "nodes.os.mem.free_percent,"
                                                           + "nodes.os.mem.used_percent,"
                                                           + "nodes.process.open_file_descriptors,"
                                                           + "nodes.process.max_file_descriptors,"
                                                           + "nodes.process.cpu.percent,"
                                                           + "nodes.process.cpu.total_in_millis,"
                                                           + "nodes.fs.total.total_in_bytes,"
                                                           + "nodes.fs.total.free_in_bytes,"
                                                           + "nodes.fs.total.available_in_bytes,"
                                                           + "nodes.jvm.gc.collectors.young.collection_count,"
                                                           + "nodes.jvm.gc.collectors.young.collection_time_in_millis,"
                                                           + "nodes.jvm.gc.collectors.young.collection_latency,"
                                                           + "nodes.jvm.gc.collectors.old.collection_count,"
                                                           + "nodes.jvm.gc.collectors.old.collection_time_in_millis,"
                                                           + "nodes.jvm.gc.collectors.old.collection_latency,"
                                                           + "nodes.jvm.threads.count,"
                                                           + "nodes.jvm.threads.peak_count,"
                                                           + "nodes.jvm.mem.heap_used_in_bytes,"
                                                           + "nodes.jvm.mem.heap_used_percent,"
                                                           + "nodes.jvm.mem.heap_committed_in_bytes,"
                                                           + "nodes.jvm.mem.heap_max_in_bytes,"
                                                           + "nodes.jvm.mem.non_heap_used_in_bytes,"
                                                           + "nodes.jvm.mem.non_heap_committed_in_bytes,"
                                                           + "nodes.jvm.mem.pools.young.used_in_bytes,"
                                                           + "nodes.jvm.mem.pools.young.max_in_bytes,"
                                                           + "nodes.jvm.mem.pools.young.peak_used_in_bytes,"
                                                           + "nodes.jvm.mem.pools.young.peak_max_in_bytes,"
                                                           + "nodes.jvm.mem.pools.old.used_in_bytes,"
                                                           + "nodes.jvm.mem.pools.old.max_in_bytes,"
                                                           + "nodes.jvm.mem.pools.old.peak_used_in_bytes,"
                                                           + "nodes.jvm.mem.pools.old.peak_max_in_bytes,"
                                                           + "nodes.jvm.mem.pools.survivor.used_in_bytes,"
                                                           + "nodes.jvm.mem.pools.survivor.max_in_bytes,"
                                                           + "nodes.jvm.mem.pools.survivor.peak_used_in_bytes,"
                                                           + "nodes.jvm.mem.pools.survivor.peak_max_in_bytes,"
                                                           + "nodes.jvm.buffer_pools.direct.count,"
                                                           + "nodes.jvm.buffer_pools.direct.used_in_bytes,"
                                                           + "nodes.jvm.buffer_pools.direct.total_capacity_in_bytes,"
                                                           + "nodes.jvm.buffer_pools.mapped.count,"
                                                           + "nodes.jvm.buffer_pools.mapped.used_in_bytes,"
                                                           + "nodes.jvm.buffer_pools.mapped.total_capacity_in_bytes,"
                                                           + "nodes.transport.server_open,"
                                                           + "nodes.transport.rx_count,"
                                                           + "nodes.transport.rx_count.throughput,"
                                                           + "nodes.transport.rx_size_in_bytes,"
                                                           + "nodes.transport.rx_size_in_bytes.throughput,"
                                                           + "nodes.transport.tx_count,"
                                                           + "nodes.transport.tx_count.throughput,"
                                                           + "nodes.transport.tx_size_in_bytes,"
                                                           + "nodes.transport.tx_size_in_bytes.throughput";
    
    public static final String DEFAULT_INDICES_FILTERS   = "completion,docs,fielddata,flush,get,indexing,merge,percolate,querycache,recovery,refresh,requestcache,search,segments,store,suggest,translog,warmer";
    public static final String DEFAULT_INDICES_METRICS   = "indices.total.search.query_total,"
                                                           + "indices.total.search.fetch_total,"
                                                           + "indices.total.search.scroll_total,"
                                                           + "indices.total.search.query_latency,"
                                                           + "indices.total.search.fetch_latency,"
                                                           + "indices.total.search.scroll_latency,"
                                                           + "indices.primaries.indexing.index_total,"
                                                           + "indices.primaries.indexing.index_speed,"
                                                           + "indices.total.refresh.total,"
                                                           + "indices.total.refresh.total_latency,"
                                                           + "indices.total.flush.total,"
                                                           + "indices.total.flush.total_latency,"
                                                           + "indices.total.segments.count,"
                                                           + "indices.total.merges.total,"
                                                           + "indices.total.merges.total_latency";
    public static final String DEFAULT_INDICES_INDEXNAME = "_all";
    // others
    public static final String DEFAULT_CHARACTER         = "UTF-8";
    public static final String DEFAULT_SERVICE_TYPE      = "elasticsearch";
    public static final String DEFAULT_SPLITS            = "\\s*,\\s*";
    public static final String JVM_GC_COLLECTOR_YOUNG    = "young";
    public static final String JVM_GC_COLLECTOR_OLD      = "old";
    public static final String JVM_MEM_POOLS_YOUNG       = "young";
    public static final String JVM_MEM_POOLS_OLD         = "old";
    public static final String JVM_MEM_POOLS_SURVIVOR    = "survivor";
    public static final String JVM_BUFFER_POOLS_DIRECT   = "direct";
    public static final String JVM_BUFFER_POOLS_MAPPED   = "mapped";
    
    /**
     * main: <br/>
     * generate flume-conf template file for this source <br>
     * 
     * @author hadoop
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     * @since JDK 1.8
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        //
        String agent;
        String source;
        String channel;
        if (args.length < 3 ||
            StringUtils.isBlank(args[0]) ||
            StringUtils.isBlank(args[1]) ||
            StringUtils.isBlank(args[2])) {
            agent = "test";
            source = "esSource";
            channel = "memoryChannel";
        } else {
            agent = args[0];
            source = args[1];
            channel = args[2];
        }
        // base path
        Path base = Paths.get(ElasticsearchSourceConstants.class.getProtectionDomain()
                                                                .getCodeSource()
                                                                .getLocation()
                                                                .toURI());
        // template conf path
        Path p = Paths.get(base.getParent()
                               .getParent()
                               .getParent()
                               .toString(),
                           "oneapm-log-dist",
                           "target",
                           "oneapm-log-0.1.0",
                           "oneapm-log-0.1.0",
                           "conf");
        // create file
        if (!Files.exists(p)) {
            p = Paths.get(base.getParent()
                              .getParent()
                              .getParent()
                              .toString(),
                          "conf");
        }
        // template file
        Path f = Paths.get(p.toString(),
                           "flume-conf-elastic-source-" +
                                   System.currentTimeMillis() +
                                   ".properties");
        if (!Files.exists(f)) {
            Files.write(f,
                        new byte[0]);
        }
        //
        Files.write(f,
                    "# Elasticsearch Source\n".getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.TRUNCATE_EXISTING);
        //
        Files.write(f,
                    (agent +
                     ".sources" +
                     " = " +
                     source + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        String head = agent +
                      ".sources" +
                      "." +
                      source +
                      ".";
        //
        Files.write(f,
                    (head +
                     "type" +
                     " = " +
                     ElasticsearchSource.class.getName() + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     CLUSTER_NAME +
                     " = " +
                     DEFAULT_CLUSTER_NAME + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     HOST_NAME +
                     " = " +
                     DEFAULT_HOST_NAME + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     PORT +
                     " = " +
                     DEFAULT_PORT + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     BATCH_SIZE +
                     " = " +
                     DEFAULT_BATCH_SIZE + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     DURATION +
                     " = " +
                     DEFAULT_DURATION + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     BUILDER_TYPE +
                     " = " +
                     DEFAULT_BUILDER_TYPE + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     NODES_FILTERS +
                     " = " +
                     DEFAULT_NODES_FILTERS + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     NODES_METRICS +
                     " = " +
                     DEFAULT_NODES_METRICS + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     INDICES_FILTERS +
                     " = " +
                     DEFAULT_INDICES_FILTERS + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     INDICES_METRICS +
                     " = " +
                     DEFAULT_INDICES_METRICS + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     INDICES_INDEXNAME +
                     " = " +
                     DEFAULT_INDICES_INDEXNAME + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     BUFFER_SIZE +
                     " = " +
                     DEFAULT_BUFFER_SIZE + "\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
        //
        Files.write(f,
                    (head +
                     "channels" +
                     " = " +
                     channel + "\n\n").getBytes(DEFAULT_CHARACTER),
                    StandardOpenOption.APPEND);
    }
    
}
