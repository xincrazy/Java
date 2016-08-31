/**
 * Project Name:oneapm-log-agent
 * File Name:ElasticsearchSourceNodesStatsIterator.java
 * Package Name:com.oneapm.log.agent.flume.source.elasticsearch.v211
 * Date:
 * Copyright (c) 2016, All Rights Reserved.
 *
 */
package com.oneapm.log.agent.flume.source.elasticsearch.v211;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.conf.Configurable;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequestBuilder;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceEventBody;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.builder.ElasticsearchSourceStatsBuilder;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.util.ElasticsearchSourceConstants;
import com.oneapm.log.agent.flume.source.elasticsearch.v211.util.ElasticsearchSourceUtil;

/**
 * ClassName:ElasticsearchSourceNodesStatsIterator <br/>
 * Function: <br/>
 * Date: <br/>
 * 
 * @author hadoop
 * @version
 * @since JDK 1.7
 * @see
 */
public class ElasticsearchSourceEventBodyIterator extends Thread implements Iterator<ElasticsearchSourceEventBody>, AutoCloseable, Configurable {
    
    private static final Logger                                                               log          = LoggerFactory.getLogger(ElasticsearchSourceEventBodyIterator.class);
    
    @Getter
    @Setter
    private Optional<Context>                                                                 context      = Optional.empty();
    
    private ImmutableMap<String, String>                                                      backup;
    
    private long                                                                              period       = ElasticsearchSourceConstants.DEFAULT_DURATION;
    
    private int                                                                               bufferSize   = ElasticsearchSourceConstants.DEFAULT_BUFFER_SIZE;
    
    private Class<ElasticsearchSourceStatsBuilder<? extends ElasticsearchSourceEventBody>>    builderClass;
    
    private String                                                                            filters      = ElasticsearchSourceConstants.DEFAULT_NODES_FILTERS;
    
    private Optional<ElasticsearchSourceStatsBuilder<? extends ElasticsearchSourceEventBody>> statsBuilder = Optional.empty();
    
    private ScheduledExecutorService                                                          scheduler;
    
    private BlockingQueue<ElasticsearchSourceEventBody>                                       queue;
    
    private Optional<TransportClient>                                                         admin        = Optional.empty();
    
    private Runnable                                                                          nodesTask    = () -> {
                                                                                                               //
                                                                                                               if (scheduler.isShutdown()) {
                                                                                                                   return;
                                                                                                               }
                                                                                                               //
                                                                                                               Stream.of(admin)
                                                                                                                     .filter(option -> option.isPresent())
                                                                                                                     .map(element -> element.get())
                                                                                                                     .map(client -> {
                                                                                                                         NodesStatsRequestBuilder request = client.admin()
                                                                                                                                                                  .cluster()
                                                                                                                                                                  .prepareNodesStats();
                                                                                                                         return configNodesStatsRequestBuilder(request,
                                                                                                                                                               filters).get();
                                                                                                                     })
                                                                                                                     .flatMap(resp -> {
                                                                                                                         if (statsBuilder.isPresent()) {
                                                                                                                             return statsBuilder.get()
                                                                                                                                                .build(resp)
                                                                                                                                                .stream();
                                                                                                                         } else {
                                                                                                                             log.error("ElasticsearchSourceStatsBuilder has none instance.");
                                                                                                                             return Stream.empty();
                                                                                                                         }
                                                                                                                     })
                                                                                                                     .forEach(body -> {
                                                                                                                         log.debug(body.toJSON());
                                                                                                                         if (!queue.offer(body)) {
                                                                                                                             log.error("Offer body {0} to queue failed, queue size {1}.",
                                                                                                                                       body.toJSON(),
                                                                                                                                       queue.size());
                                                                                                                         }
                                                                                                                     });
                                                                                                           };
    
    private Runnable                                                                          indexTask    = () -> {
                                                                                                               // for
                                                                                                               // shutdown
                                                                                                               if (scheduler.isShutdown()) {
                                                                                                                   return;
                                                                                                               }
                                                                                                               //
                                                                                                               Stream.of(admin)
                                                                                                                     .filter(option -> option.isPresent())
                                                                                                                     .map(element -> element.get())
                                                                                                                     .map(client -> {
                                                                                                                         IndicesStatsRequestBuilder request = client.admin()
                                                                                                                                                                    .indices()
                                                                                                                                                                    .prepareStats(getContext().get()
                                                                                                                                                                                              .getString(ElasticsearchSourceConstants.INDICES_INDEXNAME,
                                                                                                                                                                                                         ElasticsearchSourceConstants.DEFAULT_INDICES_INDEXNAME)
                                                                                                                                                                                              .split(ElasticsearchSourceConstants.DEFAULT_SPLITS));
                                                                                                                         return configIndicesStatsRequestBuilder(request,
                                                                                                                                                                 getContext().get()
                                                                                                                                                                             .getString(ElasticsearchSourceConstants.INDICES_FILTERS,
                                                                                                                                                                                        ElasticsearchSourceConstants.DEFAULT_INDICES_FILTERS)).get();
                                                                                                                     })
                                                                                                                     .flatMap(resp -> {
                                                                                                                         if (statsBuilder.isPresent()) {
                                                                                                                             return statsBuilder.get()
                                                                                                                                                .build(resp)
                                                                                                                                                .stream();
                                                                                                                         } else {
                                                                                                                             log.error("ElasticsearchSourceStatsBuilder has none instance.");
                                                                                                                             return Stream.empty();
                                                                                                                         }
                                                                                                                     })
                                                                                                                     .forEach(body -> {
                                                                                                                         log.debug(body.toJSON());
                                                                                                                         if (!queue.offer(body)) {
                                                                                                                             log.error("Offer body {0} to queue failed, queue size {1}.",
                                                                                                                                       body.toJSON(),
                                                                                                                                       queue.size());
                                                                                                                         }
                                                                                                                     });
                                                                                                           };
    
    /**
     * build: <br/>
     * 
     * @author hadoop
     * @param context
     * @return
     * @since JDK 1.7
     */
    public static Optional<ElasticsearchSourceEventBodyIterator> build(Context context) {
        // new
        ElasticsearchSourceEventBodyIterator iterator = new ElasticsearchSourceEventBodyIterator();
        iterator.setContext(Optional.ofNullable(context));
        // backup
        iterator.backup = context.getParameters();
        // period
        iterator.period = (context.getLong(ElasticsearchSourceConstants.DURATION) != null && context.getLong(ElasticsearchSourceConstants.DURATION)
                                                                                                    .longValue() > 0)
                                                                                                                     ? context.getLong(ElasticsearchSourceConstants.DURATION)
                                                                                                                              .longValue()
                                                                                                                     : ElasticsearchSourceConstants.DEFAULT_DURATION;
        // filters
        iterator.filters = (context.getString(ElasticsearchSourceConstants.NODES_FILTERS) != null && !context.getString(ElasticsearchSourceConstants.NODES_FILTERS)
                                                                                                             .equals(""))
                                                                                                                         ? context.getString(ElasticsearchSourceConstants.NODES_FILTERS)
                                                                                                                         : ElasticsearchSourceConstants.DEFAULT_NODES_FILTERS;
        // load builder class
        try {
            iterator.builderClass = (Class<ElasticsearchSourceStatsBuilder<? extends ElasticsearchSourceEventBody>>) Class.forName(context.getString(ElasticsearchSourceConstants.BUILDER_TYPE,
                                                                                                                                                     ElasticsearchSourceConstants.DEFAULT_BUILDER_TYPE));
        } catch (ClassNotFoundException e1) {
            log.error("Fail to load user defined class by its name: {}",
                      e1);
            return Optional.empty();
        }
        // client
        iterator.admin = Optional.ofNullable(ElasticsearchSourceUtil.getTransClient(context));
        if (!iterator.admin.isPresent()) {
            log.error("Build transport client failed.");
            return Optional.empty();
        }
        // scheduler
        iterator.scheduler = Executors.newScheduledThreadPool(2);
        // builder
        try {
            iterator.statsBuilder = Optional.ofNullable(iterator.builderClass.newInstance()
                                                                             .config(context));
        } catch (Exception e) {
            log.error("Construct stats builder failed: {}",
                      e);
            return Optional.empty();
        }
        // queue
        iterator.bufferSize = (context.getInteger(ElasticsearchSourceConstants.BUFFER_SIZE) != null && context.getLong(ElasticsearchSourceConstants.BUFFER_SIZE)
                                                                                                              .intValue() > 0)
                                                                                                                              ? context.getInteger(ElasticsearchSourceConstants.BUFFER_SIZE)
                                                                                                                                       .intValue()
                                                                                                                              : ElasticsearchSourceConstants.DEFAULT_BUFFER_SIZE;
        iterator.queue = new LinkedBlockingQueue<ElasticsearchSourceEventBody>(iterator.bufferSize);
        return Optional.ofNullable(iterator);
    }
    
    /**
     * ClassName: Filters <br/>
     * Function: <br/>
     * date: <br/>
     *
     * @author hadoop
     * @version ElasticsearchSourceEventBodyIterator
     * @since JDK 1.8
     */
    private static enum Filters {
        // nodes
        BREAKER("breaker"),
        FS("fs"),
        HTTP("http"),
        INDICES("indices"),
        JVM("jvm"),
        OS("os"),
        PROCESS("process"),
        SCRIPT("script"),
        THREADPOOL("threadpool"),
        TRANSPORT("transport"),
        // indices
        COMPLETION("completion"),
        DOCS("docs"),
        FIELDDATA("fielddata"),
        FLUSH("flush"),
        GET("get"),
        INDEXING("indexing"),
        MERGE("merge"),
        PERCOLATE("percolate"),
        QUERYCACHE("querycache"),
        RECOVERY("recovery"),
        REFRESH("refresh"),
        REQUESTCACHE("requestcache"),
        SEARCH("search"),
        SEGMENTS("segments"),
        STORE("store"),
        SUGGEST("suggest"),
        TRANSLOG("translog"),
        WARMER("warmer");
        
        private String filterName;
        
        /**
         * Creates a new instance of Filters.
         * 
         * @param filterName
         */
        private Filters(String filterName) {
            this.filterName = filterName;
        }
        
        /**
         * ifPresent: <br/>
         * 
         * @author hadoop
         * @param filters
         * @return
         * @since JDK 1.8
         */
        public boolean ifPresent(String... filters) {
            return Arrays.asList(filters)
                         .parallelStream()
                         .anyMatch(filter -> filterName.equalsIgnoreCase(filter));
        }
    }
    
    /**
     * configNodesStatsRequestBuilder: <br/>
     * 
     * @author hadoop
     * @param builder
     * @param filters
     * @return
     * @since JDK 1.8
     */
    private NodesStatsRequestBuilder configNodesStatsRequestBuilder(NodesStatsRequestBuilder builder,
                                                                    String filters) {
        if (!(StringUtils.isBlank(filters) || builder == null)) {
            String[] split = filters.split(ElasticsearchSourceConstants.DEFAULT_SPLITS);
            builder.setBreaker(Filters.BREAKER.ifPresent(split))
                   .setFs(Filters.FS.ifPresent(split))
                   .setHttp(Filters.HTTP.ifPresent(split))
                   .setIndices(Filters.INDICES.ifPresent(split))
                   .setJvm(Filters.JVM.ifPresent(split))
                   .setOs(Filters.OS.ifPresent(split))
                   .setProcess(Filters.PROCESS.ifPresent(split))
                   .setScript(Filters.SCRIPT.ifPresent(split))
                   .setThreadPool(Filters.THREADPOOL.ifPresent(split))
                   .setTransport(Filters.TRANSPORT.ifPresent(split));
        }
        return builder;
    }
    
    /**
     * configIndicesStatsRequestBuilder: <br/>
     * 
     * @author hadoop
     * @param builder
     * @param filters
     * @return
     * @since JDK 1.8
     */
    private IndicesStatsRequestBuilder configIndicesStatsRequestBuilder(IndicesStatsRequestBuilder builder,
                                                                        String filters) {
        if (!(StringUtils.isBlank(filters) || builder == null)) {
            String[] split = filters.split(ElasticsearchSourceConstants.DEFAULT_SPLITS);
            builder.setCompletion(Filters.COMPLETION.ifPresent(split))
                   .setDocs(Filters.DOCS.ifPresent(split))
                   .setFieldData(Filters.FIELDDATA.ifPresent(split))
                   .setFlush(Filters.FLUSH.ifPresent(split))
                   .setGet(Filters.GET.ifPresent(split))
                   .setIndexing(Filters.INDEXING.ifPresent(split))
                   .setMerge(Filters.MERGE.ifPresent(split))
                   .setPercolate(Filters.PERCOLATE.ifPresent(split))
                   .setQueryCache(Filters.QUERYCACHE.ifPresent(split))
                   .setRecovery(Filters.RECOVERY.ifPresent(split))
                   .setRefresh(Filters.REFRESH.ifPresent(split))
                   .setRequestCache(Filters.REQUESTCACHE.ifPresent(split))
                   .setSearch(Filters.SEARCH.ifPresent(split))
                   .setSegments(Filters.SEGMENTS.ifPresent(split))
                   .setStore(Filters.STORE.ifPresent(split))
                   .setSuggest(Filters.SUGGEST.ifPresent(split))
                   .setTranslog(Filters.TRANSLOG.ifPresent(split))
                   .setWarmer(Filters.WARMER.ifPresent(split));
        }
        return builder;
    }
    
    /**
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        // start scheduler node stats
        scheduler.scheduleAtFixedRate(nodesTask,
                                      0L,
                                      period,
                                      TimeUnit.MILLISECONDS);
        // start scheduler index stats
        scheduler.scheduleAtFixedRate(indexTask,
                                      0L,
                                      period,
                                      TimeUnit.MILLISECONDS);
    }
    
    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        // close scheduler executor
        scheduler.shutdown();
        try {
            boolean terminated = scheduler.awaitTermination(60,
                                                            TimeUnit.SECONDS);
            if (!terminated) {
                log.error("Closing iterator and waitting scheduler executor terminate timeout.");
                return;
            }
        } catch (InterruptedException e) {
            log.error("Closing iterator and waitting scheduler executor terminate interrupted: {}",
                      e);
        }
        // close transport client
        admin.ifPresent(a -> a.close());
        // clear queue
        queue.clear();
        log.info("Iterator is terminated.");
    }
    
    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return queue.isEmpty()
                              ? false
                              : true;
    }
    
    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public ElasticsearchSourceEventBody next() {
        return queue.poll();
    }
    
    /**
     * getRemaining: <br/>
     * 
     * @author hadoop
     * @return
     * @since JDK 1.7
     */
    public int getRemaining() {
        return queue.size();
    }
    
    /**
     * compare: <br/>
     * 
     * @author hadoop
     * @param origin
     * @param now
     * @return true: need re-config; false: not-need;
     * @since JDK 1.7
     */
    private boolean compare(ImmutableMap<String, String> origin,
                            ImmutableMap<String, String> now) {
        //
        if (origin.keySet()
                  .size() == 0 &&
            now.keySet()
               .size() != 0) {
            return true;
        }
        //
        return origin.keySet()
                     .stream()
                     .anyMatch(k -> {
                         if (!now.containsKey(k)) {
                             return true;
                         }
                         if (origin.get(k) == null ||
                             !origin.get(k)
                                    .equalsIgnoreCase(now.get(k))) {
                             return true;
                         }
                         return false;
                     });
    }
    
    /**
     * @see org.apache.flume.conf.Configurable#configure(org.apache.flume.Context)
     */
    @Override
    public synchronized void configure(Context context) {
        this.context = Optional.ofNullable(context);
        // compare
        if (!compare(backup,
                     context.getParameters())) {
            log.warn("No something need to re-config.");
            return;
        }
        // period
        long newPeriod = (context.getLong(ElasticsearchSourceConstants.DURATION) != null && context.getLong(ElasticsearchSourceConstants.DURATION)
                                                                                                   .longValue() > 0)
                                                                                                                    ? context.getLong(ElasticsearchSourceConstants.DURATION)
                                                                                                                             .longValue()
                                                                                                                    : ElasticsearchSourceConstants.DEFAULT_DURATION;
        // filters
        String newFilters = (context.getString(ElasticsearchSourceConstants.NODES_FILTERS) != null && !context.getString(ElasticsearchSourceConstants.NODES_FILTERS)
                                                                                                              .equals(""))
                                                                                                                          ? context.getString(ElasticsearchSourceConstants.NODES_FILTERS)
                                                                                                                          : ElasticsearchSourceConstants.DEFAULT_NODES_FILTERS;
        // load builder class
        Class<ElasticsearchSourceStatsBuilder<? extends ElasticsearchSourceEventBody>> newBuilderClass = null;
        try {
            newBuilderClass = (Class<ElasticsearchSourceStatsBuilder<? extends ElasticsearchSourceEventBody>>) Class.forName(context.getString(ElasticsearchSourceConstants.BUILDER_TYPE,
                                                                                                                                               ElasticsearchSourceConstants.DEFAULT_BUILDER_TYPE));
        } catch (ClassNotFoundException e1) {
            log.error("Exit re-config, Fail to load user defined class by its name: {}",
                      e1);
            return;
        }
        // client
        Optional<TransportClient> newAdmin = Optional.ofNullable(ElasticsearchSourceUtil.getTransClient(context));
        if (!newAdmin.isPresent()) {
            log.error("Exit re-config, Build transport client failed.");
            return;
        }
        // scheduler
        ScheduledExecutorService newScheduler = Executors.newSingleThreadScheduledExecutor();
        // builder
        Optional<ElasticsearchSourceStatsBuilder<? extends ElasticsearchSourceEventBody>> newStatsBuilder = Optional.empty();
        try {
            newStatsBuilder = Optional.ofNullable(newBuilderClass.newInstance()
                                                                 .config(context));
        } catch (Exception e) {
            log.error("Exit re-config, Construct stats builder failed: {}",
                      e);
            return;
        }
        // queue
        int newBufferSize = (context.getInteger(ElasticsearchSourceConstants.BUFFER_SIZE) != null && context.getLong(ElasticsearchSourceConstants.BUFFER_SIZE)
                                                                                                            .intValue() > 0)
                                                                                                                            ? context.getInteger(ElasticsearchSourceConstants.BUFFER_SIZE)
                                                                                                                                     .intValue()
                                                                                                                            : ElasticsearchSourceConstants.DEFAULT_BUFFER_SIZE;
        BlockingQueue<ElasticsearchSourceEventBody> newQueue = new LinkedBlockingQueue<ElasticsearchSourceEventBody>(newBufferSize);
        // close
        close();
        // re-config
        backup = context.getParameters();
        period = newPeriod;
        filters = newFilters;
        builderClass = newBuilderClass;
        admin = newAdmin;
        scheduler = newScheduler;
        statsBuilder = newStatsBuilder;
        bufferSize = newBufferSize;
        queue = newQueue;
        // re-start
        run();
    }
    
}
