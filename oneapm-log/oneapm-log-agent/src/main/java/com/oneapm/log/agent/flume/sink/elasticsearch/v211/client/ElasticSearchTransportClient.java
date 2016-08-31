/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.oneapm.log.agent.flume.sink.elasticsearch.v211.client;

import static com.oneapm.log.agent.flume.sink.elasticsearch.v211.ElasticSearchSinkConstants.DEFAULT_PORT;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.oneapm.log.agent.flume.sink.elasticsearch.v211.ElasticSearchEventSerializer;
import com.oneapm.log.agent.flume.sink.elasticsearch.v211.ElasticSearchIndexRequestBuilderFactory;
import com.oneapm.log.agent.flume.sink.elasticsearch.v211.IndexNameBuilder;

public class ElasticSearchTransportClient implements ElasticSearchClient {
    
    public static final Logger                      logger   = LoggerFactory.getLogger(ElasticSearchTransportClient.class);
    
    private InetSocketTransportAddress[]            serverAddresses;
    private ElasticSearchEventSerializer            serializer;
    private ElasticSearchIndexRequestBuilderFactory indexRequestBuilderFactory;
    private BulkRequestBuilder                      bulkRequestBuilder;
    
    private Client                                  client;
    
    private Integer                                 retry    = 0;
    
    private ThreadLocal<BulkContext>                retryCtx = new ThreadLocal<ElasticSearchTransportClient.BulkContext>();
    
    @VisibleForTesting
    InetSocketTransportAddress[] getServerAddresses() {
        return serverAddresses;
    }
    
    @VisibleForTesting
    void setBulkRequestBuilder(BulkRequestBuilder bulkRequestBuilder) {
        this.bulkRequestBuilder = bulkRequestBuilder;
    }
    
    /**
     * Transport client for external cluster
     * 
     * @param hostNames
     * @param clusterName
     * @param retry
     * @param serializer
     */
    public ElasticSearchTransportClient(String[] hostNames,
                                        String clusterName,
                                        Integer retry,
                                        ElasticSearchEventSerializer serializer) {
        configureHostnames(hostNames);
        this.retry = retry;
        this.serializer = serializer;
        openClient(clusterName);
    }
    
    /**
     * Creates a new instance of ElasticSearchTransportClient.
     * 
     * @param hostNames
     * @param clusterName
     * @param retry
     * @param indexBuilder
     */
    public ElasticSearchTransportClient(String[] hostNames,
                                        String clusterName,
                                        Integer retry,
                                        ElasticSearchIndexRequestBuilderFactory indexBuilder) {
        configureHostnames(hostNames);
        this.retry = retry;
        this.indexRequestBuilderFactory = indexBuilder;
        openClient(clusterName);
    }
    
    /**
     * Local transport client only for testing
     * 
     * @param indexBuilderFactory
     */
    public ElasticSearchTransportClient(ElasticSearchIndexRequestBuilderFactory indexBuilderFactory) {
        this.indexRequestBuilderFactory = indexBuilderFactory;
        openLocalDiscoveryClient();
    }
    
    /**
     * Local transport client only for testing
     *
     * @param serializer
     */
    public ElasticSearchTransportClient(ElasticSearchEventSerializer serializer) {
        this.serializer = serializer;
        openLocalDiscoveryClient();
    }
    
    /**
     * Used for testing
     *
     * @param client
     *            ElasticSearch Client
     * @param serializer
     *            Event Serializer
     */
    public ElasticSearchTransportClient(Client client,
                                        ElasticSearchEventSerializer serializer) {
        this.client = client;
        this.serializer = serializer;
    }
    
    /**
     * Used for testing
     *
     * @param client
     *            ElasticSearch Client
     * @param serializer
     *            Event Serializer
     */
    public ElasticSearchTransportClient(Client client,
                                        ElasticSearchIndexRequestBuilderFactory requestBuilderFactory) throws IOException {
        this.client = client;
        requestBuilderFactory.createIndexRequest(client,
                                                 null,
                                                 null,
                                                 null);
    }
    
    private void configureHostnames(String[] hostNames) {
        logger.warn(Arrays.toString(hostNames));
        serverAddresses = new InetSocketTransportAddress[hostNames.length];
        for (int i = 0; i < hostNames.length; i++) {
            String[] hostPort = hostNames[i].trim()
                                            .split(":");
            String host = hostPort[0].trim();
            int port = hostPort.length == 2
                                           ? Integer.parseInt(hostPort[1].trim())
                                           : DEFAULT_PORT;
            // for es-2.1.1
            try {
                serverAddresses[i] = new InetSocketTransportAddress(InetAddress.getByName(host),
                                                                    port);
            } catch (UnknownHostException e) {
                // FIXME: deal with this exception, not only print it
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void close() {
        if (client != null) {
            client.close();
        }
        client = null;
    }
    
    @Override
    public void addEvent(Event event,
                         IndexNameBuilder indexNameBuilder,
                         String indexType,
                         long ttlMs) throws Exception {
        if (bulkRequestBuilder == null) {
            bulkRequestBuilder = client.prepareBulk();
        }
        // get index type from index name builder to decide the real index type
        IndexRequestBuilder indexRequestBuilder = null;
        String type = indexNameBuilder.getIndexType(event);
        Event document = indexNameBuilder.getDocument(event);
        try {
            
            if (indexRequestBuilderFactory == null) {
                indexRequestBuilder = client.prepareIndex(indexNameBuilder.getIndexName(event),
                                                          StringUtils.isBlank(type)
                                                                                   ? indexType
                                                                                   : type)
                                            .setSource(serializer.getContentBuilder(document == null
                                                                                                    ? event
                                                                                                    : document)
                                                                 .bytes());
            } else {
                indexRequestBuilder = indexRequestBuilderFactory.createIndexRequest(client,
                                                                                    indexNameBuilder.getIndexPrefix(event),
                                                                                    StringUtils.isBlank(type)
                                                                                                             ? indexType
                                                                                                             : type,
                                                                                    document == null
                                                                                                    ? event
                                                                                                    : document);
            }
        } catch (Exception e) {
            // FIXME: this may lead to JMX counter not exactly because some document not be added to
            // the bulk-request
            logger.error("Add an index-request-builder failed, may be because of the document is not a json, {}",
                         e);
            return;
        }
        
        if (ttlMs > 0) {
            indexRequestBuilder.setTTL(ttlMs);
        }
        // set thread local bulk context if not exists
        if (retryCtx.get() == null) {
            retryCtx.set(new BulkContext());
        }
        // add builder to retry context
        retryCtx.get()
                .add(indexRequestBuilder);
        bulkRequestBuilder.add(indexRequestBuilder);
    }
    
    @Override
    public void execute() throws Exception {
        try {
            BulkResponse bulkResponse = bulkRequestBuilder.execute()
                                                          .actionGet();
            if (bulkResponse.hasFailures()) {
                // retry
                retryExecute(retry.intValue(),
                             bulkResponse);
            }
        } finally {
            bulkRequestBuilder = client.prepareBulk();
            // clear the retry context
            retryCtx.get()
                    .clear();
        }
    }
    
    /**
     * retryExecute: <br/>
     * 
     * @author hadoop
     * @param current
     *            rest of chance to retry, from retry to 0, throw exception when 0
     * @param response
     *            the last time of bulk response
     * @throws EventDeliveryException
     * @since JDK 1.7
     */
    private void retryExecute(int current,
                              BulkResponse response) throws EventDeliveryException {
        // no chance
        if (current <= 0) {
            throw new EventDeliveryException(response.buildFailureMessage());
        }
        // get failed request ids
        int failCount = 0;
        List<String> ids = new ArrayList<String>();
        for (BulkItemResponse item : response.getItems()) {
            if (item.isFailed()) {
                failCount++;
                ids.add(item.getId());
            }
        }
        // log retry process
        logger.warn("Retry {0} times for the remain {1} failed requests.",
                    current,
                    failCount);
        // get remain request
        Collection<IndexRequestBuilder> remains = retryCtx.get()
                                                          .remain(ids);
        // retry remain bulk request
        bulkRequestBuilder = client.prepareBulk();
        for (IndexRequestBuilder builder : remains) {
            bulkRequestBuilder.add(builder);
        }
        BulkResponse bulkResp = bulkRequestBuilder.execute()
                                                  .actionGet();
        // if retry also has failures, retry again
        if (bulkResp.hasFailures()) {
            retryExecute(current - 1,
                         bulkResp);
        }
    }
    
    /**
     * ClassName: BulkContext <br/>
     * Function: <br/>
     *
     * @author hadoop
     * @version ElasticSearchTransportClient
     * @since JDK 1.7
     */
    private class BulkContext {
        
        // request builders in one bulk
        private Map<String, IndexRequestBuilder> bulkBuilders = new HashMap<String, IndexRequestBuilder>();
        
        /**
         * clear: <br/>
         * clear for next bulk <br>
         * 
         * @author hadoop
         * @since JDK 1.7
         */
        public void clear() {
            bulkBuilders.clear();
        }
        
        /**
         * add: <br/>
         * 
         * @author hadoop
         * @param builder
         * @since JDK 1.7
         */
        public void add(IndexRequestBuilder builder) {
            add(builder.request()
                       .id(),
                builder);
        }
        
        /**
         * add: <br/>
         * 
         * @author hadoop
         * @param id
         * @param builder
         * @since JDK 1.7
         */
        public void add(String id,
                        IndexRequestBuilder builder) {
            bulkBuilders.put(id,
                             builder);
        }
        
        /**
         * remain: <br/>
         * 
         * @author hadoop
         * @param ids
         * @return
         * @since JDK 1.7
         */
        public Collection<IndexRequestBuilder> remain(List<String> ids) {
            // collect all ids to remove
            List<String> rm = new ArrayList<String>();
            for (String s : bulkBuilders.keySet()) {
                if (!ids.contains(s)) {
                    rm.add(s);
                }
            }
            // remove all
            for (String s : rm) {
                bulkBuilders.remove(s);
            }
            return bulkBuilders.values();
        }
        
        /**
         * remain: <br/>
         * 
         * @author hadoop
         * @param ids
         * @return
         * @since JDK 1.7
         */
        @SuppressWarnings("unused")
        public Collection<IndexRequestBuilder> remain(String[] ids) {
            return remain(Arrays.asList(ids));
        }
        
    }
    
    /**
     * Open client to elaticsearch cluster
     * 
     * @param clusterName
     */
    private void openClient(String clusterName) {
        logger.info("Using ElasticSearch hostnames: {} ",
                    Arrays.toString(serverAddresses));
        // for elasticsearch 2.1.1
        Settings settings = Settings.settingsBuilder()
                                    .put("cluster.name",
                                         clusterName)
                                    .build();
        // for elasticsearch 2.1.1
        TransportClient transportClient = TransportClient.builder()
                                                         .settings(settings)
                                                         .build();
        for (InetSocketTransportAddress host : serverAddresses) {
            transportClient.addTransportAddress(host);
        }
        if (client != null) {
            client.close();
        }
        client = transportClient;
    }
    
    /*
     * FOR TESTING ONLY... Opens a local discovery node for talking to an elasticsearch server
     * running in the same JVM
     */
    private void openLocalDiscoveryClient() {
        logger.info("Using ElasticSearch AutoDiscovery mode");
        Node node = NodeBuilder.nodeBuilder()
                               .client(true)
                               .local(true)
                               .node();
        if (client != null) {
            client.close();
        }
        client = node.client();
    }
    
    @Override
    public void configure(Context context) {
        // To change body of implemented methods use File | Settings | File Templates.
    }
}
