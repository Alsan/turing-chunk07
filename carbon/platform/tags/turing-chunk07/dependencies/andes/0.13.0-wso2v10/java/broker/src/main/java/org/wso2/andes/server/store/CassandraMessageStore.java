/*
 *  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.andes.server.store;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.ByteBufferSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.AMQException;
import org.wso2.andes.AMQStoreException;
import org.wso2.andes.framing.AMQShortString;
import org.wso2.andes.framing.FieldTable;
import org.wso2.andes.kernel.AndesMessagePart;
import org.wso2.andes.kernel.Subscrption;
import org.wso2.andes.kernel.distrupter.SimplyfiedDataSender;
import org.wso2.andes.messageStore.CassandraBasedMessageStoreImpl;
import org.wso2.andes.server.ClusterResourceHolder;
import org.wso2.andes.server.cassandra.CassandraMessageContentCache;
import org.wso2.andes.server.cassandra.CassandraQueueMessage;
import org.wso2.andes.server.cassandra.CassandraTopicPublisherManager;
import org.wso2.andes.server.cassandra.ClusteringEnabledSubscriptionManager;
import org.wso2.andes.server.cassandra.DefaultClusteringEnabledSubscriptionManager;
import org.wso2.andes.server.cassandra.ExpiredCassandraMessageRemover;
import org.wso2.andes.server.cassandra.OnceInOrderEnabledSubscriptionManager;
import org.wso2.andes.server.cluster.ClusterManagementInformationMBean;
import org.wso2.andes.server.cluster.ClusterManager;
import org.wso2.andes.server.cluster.GlobalQueueManager;
import org.wso2.andes.server.cluster.coordination.MessageIdGenerator;
import org.wso2.andes.server.cluster.coordination.SubscriptionCoordinationManager;
import org.wso2.andes.server.cluster.coordination.SubscriptionCoordinationManagerImpl;
import org.wso2.andes.server.cluster.coordination.TimeStampBasedMessageIdGenerator;
import org.wso2.andes.server.cluster.coordination.TopicSubscriptionCoordinationManager;
import org.wso2.andes.server.configuration.ClusterConfiguration;
import org.wso2.andes.server.exchange.Exchange;
import org.wso2.andes.server.information.management.QueueManagementInformationMBean;
import org.wso2.andes.server.logging.LogSubject;
import org.wso2.andes.server.message.AMQMessage;
import org.wso2.andes.server.message.MessageMetaData;
import org.wso2.andes.server.protocol.AMQProtocolSession;
import org.wso2.andes.server.queue.AMQQueue;
import org.wso2.andes.server.queue.IncomingMessage;
import org.wso2.andes.server.queue.QueueEntry;
import org.wso2.andes.server.queue.SimpleQueueEntryList;
import org.wso2.andes.server.store.util.CassandraDataAccessException;
import org.wso2.andes.server.store.util.CassandraDataAccessHelper;
import org.wso2.andes.server.util.AndesConstants;
import org.wso2.andes.server.util.AndesUtils;
import org.wso2.andes.server.virtualhost.VirtualHostConfigSynchronizer;
import org.wso2.andes.tools.utils.DisruptorBasedExecutor;

import com.google.common.base.Splitter;

/**
 * Class <code>CassandraMessageStore</code> is the Message Store implemented for
 * cassandra Working with andes as an alternative to Derby Message Store
 */
public class CassandraMessageStore implements org.wso2.andes.server.store.MessageStore {

    private boolean configured = false;
    private boolean isCassandraConnectionLive = false;
    private boolean isInMemoryMode = false;

    private static Log log = LogFactory.getLog(CassandraMessageStore.class);

    private Cluster cluster;
    private final String USERNAME_KEY = "username";
    private final String PASSWORD_KEY = "password";
    private final String CONNECTION_STRING = "connectionString";
    private final String REPLICATION_FACTOR = "advanced.replicationFactor";
    private final String READ_CONSISTENCY_LEVEL = "advanced.readConsistencyLevel";
    private final String WRITE_CONSISTENCY_LEVEL = "advanced.writeConsistencyLevel";
    private final String STRATERGY_CLASS = "advanced.strategyClass";
    private final String CLUSTER_KEY = "cluster";
    private final String ID_GENENRATOR = "idGenerator";

    private Keyspace keyspace;
    public final static String KEYSPACE = "QpidKeySpace";
    private final static String LONG_TYPE = "LongType";
    private final static String UTF8_TYPE = "UTF8Type";
    private final static String INTEGER_TYPE = "IntegerType";

    private static StringSerializer stringSerializer = StringSerializer.get();
    private static LongSerializer longSerializer = LongSerializer.get();
    private static BytesArraySerializer bytesArraySerializer = BytesArraySerializer.get();
    private static IntegerSerializer integerSerializer = IntegerSerializer.get();
    private static ByteBufferSerializer byteBufferSerializer = ByteBufferSerializer.get();

    // private final static String QUEUE_COLUMN_FAMILY = "Queue";
    // private final static String MESSAGE_CONTENT_ID_COLUMN_FAMILY =
    // "MessageContentIDs";
    // private final static String SQ_COLUMN_FAMILY = "SubscriptionQueues";
    // private final static String TOPIC_EXCHANGE_MESSAGE_IDS =
    // "TopicExchangeMessageIds";

    // column family to keep track of created and removed destination queues and
    // their details (<queueName,owner|exclusive>)
    private final static String QUEUE_DETAILS_COLUMN_FAMILY = "QueueDetails";
    private final static String QUEUE_DETAILS_ROW = "QUEUE_DETAILS";

    // column family to keep track of queue entries for transactions
    private final static String QUEUE_ENTRY_COLUMN_FAMILY = "QueueEntries";
    private final static String QUEUE_ENTRY_ROW = "QueueEntriesRow";

    // column family to keep track of loaded exchanges
    private final static String EXCHANGE_COLUMN_FAMILY = "ExchangeColumnFamily";
    private final static String EXCHANGE_ROW = "ExchangesRow";

    // column family to keep track of created and removed durable bindings with
    // their queues <exchange,queue name,routing key>
    private final static String BINDING_COLUMN_FAMILY = "Binding";

    // column family to add and remove message content with their
    // <messageID,offset> values
    public final static String MESSAGE_CONTENT_COLUMN_FAMILY = "MessageContent";

    // column family to keep track of destination queue-messageID mapping
    private final static String MESSAGE_QUEUE_MAPPING_COLUMN_FAMILY = "MessageQueueMappingColumnFamily";

    // column family to keep track of node queues belonging to a global queue
    // (<global,node>)
    private final static String GLOBAL_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY = "QpidQueues";

    // column family to keep track of node queues belonging to a destination
    // queue (<destination,node>)
    private final static String DESTINATION_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY = "DestinationToNodeQueueMappingCF";

    // column family to keep messages for node queues (<nodequeue,messageID>)
    public final static String NODE_QUEUES_COLUMN_FAMILY = "NodeQueues";

    // column family to keep messages for global queues
    // (<global-queue,messageID>)
    public final static String GLOBAL_QUEUES_COLUMN_FAMILY = "GlobalQueue";

    // column family to keep track of global queues created under
    // GLOBAL_QUEUE_LIST_ROW
    public final static String GLOBAL_QUEUE_LIST_COLUMN_FAMILY = "GlobalQueueList";
    public final static String GLOBAL_QUEUE_LIST_ROW = "GlobalQueueListRow";

    // column family to keep meta data for messages under QMD_ROW_NAME with
    // messageIDs
    public final static String QMD_COLUMN_FAMILY = "MetaData";
    public final static String QMD_ROW_NAME = "qpidMetaData";

    // column family to keep track of message IDs for topics
    // <nodeQueueName,MessageID>
    private final static String PUB_SUB_MESSAGE_IDS_COLUMN_FAMILY = "pubSubMessages";

    // column family to keep track of subscribers for topics <topic
    // name,nodeQueueNameForSubscriberQueue>
    private final static String TOPIC_SUBSCRIBERS_COLUMN_FAMILY = "topicSubscribers";

    // column family to keep track of subscribers registered for topics <topic
    // name,destination Queue Name>
    private final static String TOPIC_SUBSCRIBER_QUEUES_COLUMN_FAMILY = "topicSubscriberQueues";

    // column family to keep track of topics created and deleted under
    // TOPICS_ROW
    private final static String TOPICS_COLUMN_FAMILY = "topics";
    private final static String TOPICS_ROW = "TOPICS";

    // column family to keep track of messages that are acknowledged under
    // ACKED_MESSAGE_IDS_ROW
    private final static String ACKED_MESSAGE_IDS_COLUMN_FAMILY = "acknowledgedMessageIds";
    private final static String ACKED_MESSAGE_IDS_ROW = "acknowledgedMessageIdsRow";

    // column family to keep track of nodes and their syncing info under
    // NODE_DETAIL_ROW
    private final static String NODE_DETAIL_COLUMN_FAMILY = "CusterNodeDetails";
    private final static String NODE_DETAIL_ROW = "NodeDetailsRow";

    // column family to keep track of message properties (count) under
    // MESSAGE_COUNTERS_RAW_NAME
    public final static String MESSAGE_COUNTERS_COLUMN_FAMILY = "MessageCountDetails";
    public final static String MESSAGE_COUNTERS_RAW_NAME = "QueueMessageCountRow";

    //
    private final static String SUBSCRIPTION_COUNTERS_COLUMN_FAMILY = "DestinationSubscriptionsCountRow";

    // column family to store of any property of incoming messages
    // <property,messageID,value>
    private final static String MESSAGE_PROPERTIES_COLUMN_FAMILY = "MessageProperties";
    private final static String MESSAGE_EXPIRATION_PROPERTY_RAW_NAME = "MessageExpirationPropertyRow";

    final static Splitter pipeSplitter = Splitter.on('|');

    private final AtomicLong _messageId = new AtomicLong(0);

    // message ID Generator for message store
    private MessageIdGenerator messageIdGenerator = null;

    // ID s of messages whose content and message properties should be removed
    // from Cassandra will be piled up here
    private SortedMap<Long, Long> contentDeletionTasks = new ConcurrentSkipListMap<Long, Long>();
    // task to remove content of messages from MESSAGE_CONTENT_COLUMN_FAMILY
    private ContentRemoverAndMessageQueueMappingRemoverTask messageContentRemovalTask = null;

    // IDs of messages which should be removed from
    // MESSAGE_QUEUE_MAPPING_COLUMN_FAMILY will be piled up here
    private SortedMap<Long, MessageQueueMapping> messageQueueMappingDeletionTasks = new ConcurrentSkipListMap<Long, MessageQueueMapping>();

    // IDs of messages which should be removed from
    // PUB_SUB_MESSAGE_IDS_COLUMN_FAMILY will be piled up here
    private ConcurrentHashMap<Long, Long> pubSubMessageContentDeletionTasks;
    // task to remove messages from QMD_COLUMN_FAMILY
    private PubSubMessageContentRemoverTask pubSubMessageContentRemoverTask = null;

    // in-memory map keeping subscriber queues (destination queues) for each
    // topic
    private ConcurrentHashMap<String, ArrayList<String>> topicSubscribersMap = new ConcurrentHashMap<String, ArrayList<String>>();
    // in-memory map keeping <topic,node queues> mapping
    private ConcurrentHashMap<String, ArrayList<String>> topicNodeQueuesMap = new ConcurrentHashMap<String, ArrayList<String>>();

    private CassandraMessageContentCache messageCacheForCassandra = null;

    // management beans registered for cassandra message store
    private ClusterManagementInformationMBean clusterManagementMBean;
    private QueueManagementInformationMBean queueManagementMBean;

    // IDs of messages which should be removed from in memory store will be
    // piled up here
    private Hashtable<Long, Long> removalPendingTopicMessageIds = new Hashtable<Long, Long>();

    // this keeps messages in memory
    private Hashtable<Long, IncomingMessage> incomingTopicMessagesHashtable = new Hashtable<Long, IncomingMessage>();

    // keep track of added messages to in memory store
    private HashSet<Long> alreadyAddedTopicMessages = new HashSet<Long>();

    // task running to remove messages from in memory store
    private InMemoryMessageRemoverTask inMemoryTopicMessageRemoverTask = null;

    // map keeping messages addressed to subscriber queues for topics
    private HashMap<String, LinkedBlockingQueue<Long>> topicSubscriberQueueMap = new HashMap<String, LinkedBlockingQueue<Long>>();

    // sent but not acked messages mapped with subscriber queue
    private HashMap<String, HashSet<Long>> sentButNotAckedTopicMessageMap = new HashMap<String, HashSet<Long>>();


    // this task will buffer message content to be written to Cassandra
    // private PublishMessageContentWriter publishMessageContentWriter;

    private HashSet<Long> alreadyAddedQueueMessages = new HashSet<Long>();
    private Hashtable<Long, IncomingMessage> incomingQueueMessageHashtable = new Hashtable<Long, IncomingMessage>();
    private LinkedBlockingQueue<Long> pendingMessageIdsQueue = new LinkedBlockingQueue<Long>();
    private LinkedBlockingQueue<Long> ignoredMessageIdsQueue = new LinkedBlockingQueue<Long>();
    private HashSet<Long> sentButNotAckedMids = new HashSet<Long>();

    // memory map keeping queues and their message count
    private Hashtable<String, Long> queueMessageCountMap = new Hashtable<String, Long>();

    private static DisruptorBasedExecutor disruptorBasedExecutor;

    /**
     * Set CassandraMessageStore at ClusterResourceHolder
     */
    public CassandraMessageStore() {
        ClusterResourceHolder.getInstance().setCassandraMessageStore(this);
    }

    public static DisruptorBasedExecutor getDisruptorBasedExecutor() {
        return disruptorBasedExecutor;
    }

    /**
     * Create a cassandra key space for andes usage
     * 
     * @return Key Space
     * @throws CassandraDataAccessException
     */
    private Keyspace createKeySpace(int replicationFactor, String strategyClass) throws CassandraDataAccessException {

        this.keyspace = CassandraDataAccessHelper.createKeySpace(cluster, KEYSPACE, replicationFactor, strategyClass);

        // CassandraDataAccessHelper.createColumnFamily(QUEUE_COLUMN_FAMILY,
        // KEYSPACE, this.cluster, LONG_TYPE);
        // CassandraDataAccessHelper.createColumnFamily(TOPIC_EXCHANGE_MESSAGE_IDS,
        // KEYSPACE, this.cluster, LONG_TYPE);
        // CassandraDataAccessHelper.createColumnFamily(MESSAGE_CONTENT_ID_COLUMN_FAMILY,
        // KEYSPACE, this.cluster, LONG_TYPE);
        // CassandraDataAccessHelper.createColumnFamily(SQ_COLUMN_FAMILY,
        // KEYSPACE, this.cluster, UTF8_TYPE);
        CassandraDataAccessHelper.createColumnFamily(BINDING_COLUMN_FAMILY, KEYSPACE, this.cluster, UTF8_TYPE);
        CassandraDataAccessHelper.createColumnFamily(MESSAGE_CONTENT_COLUMN_FAMILY, KEYSPACE, this.cluster,
                INTEGER_TYPE);
        CassandraDataAccessHelper.createColumnFamily(GLOBAL_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY, KEYSPACE, this.cluster,
                UTF8_TYPE);
        CassandraDataAccessHelper.createColumnFamily(QMD_COLUMN_FAMILY, KEYSPACE, this.cluster, LONG_TYPE);
        CassandraDataAccessHelper.createColumnFamily(QUEUE_DETAILS_COLUMN_FAMILY, KEYSPACE, this.cluster, UTF8_TYPE);
        CassandraDataAccessHelper.createColumnFamily(QUEUE_ENTRY_COLUMN_FAMILY, KEYSPACE, this.cluster, UTF8_TYPE);
        CassandraDataAccessHelper.createColumnFamily(EXCHANGE_COLUMN_FAMILY, KEYSPACE, this.cluster, UTF8_TYPE);
        CassandraDataAccessHelper.createColumnFamily(NODE_QUEUES_COLUMN_FAMILY, KEYSPACE, this.cluster, LONG_TYPE);
        CassandraDataAccessHelper.createColumnFamily(MESSAGE_QUEUE_MAPPING_COLUMN_FAMILY, KEYSPACE, this.cluster,
                LONG_TYPE);
        CassandraDataAccessHelper.createColumnFamily(MESSAGE_PROPERTIES_COLUMN_FAMILY, KEYSPACE, this.cluster,
                LONG_TYPE);
        CassandraDataAccessHelper.createColumnFamily(GLOBAL_QUEUES_COLUMN_FAMILY, KEYSPACE, this.cluster, LONG_TYPE);
        CassandraDataAccessHelper
                .createColumnFamily(GLOBAL_QUEUE_LIST_COLUMN_FAMILY, KEYSPACE, this.cluster, UTF8_TYPE);
        CassandraDataAccessHelper.createColumnFamily(PUB_SUB_MESSAGE_IDS_COLUMN_FAMILY, KEYSPACE, this.cluster,
                LONG_TYPE);
        CassandraDataAccessHelper
                .createColumnFamily(TOPIC_SUBSCRIBERS_COLUMN_FAMILY, KEYSPACE, this.cluster, UTF8_TYPE);
        CassandraDataAccessHelper.createColumnFamily(TOPIC_SUBSCRIBER_QUEUES_COLUMN_FAMILY, KEYSPACE, this.cluster,
                UTF8_TYPE);
        CassandraDataAccessHelper.createColumnFamily(TOPICS_COLUMN_FAMILY, KEYSPACE, this.cluster, UTF8_TYPE);
        CassandraDataAccessHelper
                .createColumnFamily(ACKED_MESSAGE_IDS_COLUMN_FAMILY, KEYSPACE, this.cluster, LONG_TYPE);
        CassandraDataAccessHelper.createColumnFamily(NODE_DETAIL_COLUMN_FAMILY, KEYSPACE, this.cluster, UTF8_TYPE);
        CassandraDataAccessHelper.createCounterColumnFamily(MESSAGE_COUNTERS_COLUMN_FAMILY, KEYSPACE, this.cluster);
        CassandraDataAccessHelper
                .createCounterColumnFamily(SUBSCRIPTION_COUNTERS_COLUMN_FAMILY, KEYSPACE, this.cluster);
        CassandraDataAccessHelper.createColumnFamily(DESTINATION_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY, KEYSPACE,
                this.cluster, UTF8_TYPE);

        return keyspace;
    }

    /**
     * message ID of current message.
     * 
     * @return
     */
    public AtomicLong currentMessageId() {
        return _messageId;
    }

    @Override
    /**
     * Initialise the mesage store
     */
    public void configureMessageStore(String name, MessageStoreRecoveryHandler recoveryHandler, Configuration config,
            LogSubject logSubject) throws Exception {
        if (!configured) {
            performCommonConfiguration(config);
            ClusterResourceHolder resourceHolder = ClusterResourceHolder.getInstance();

            CassandraTopicPublisherManager cassandraTopicPublisherManager = resourceHolder
                    .getCassandraTopicPublisherManager();
            if (cassandraTopicPublisherManager == null) {
                cassandraTopicPublisherManager = new CassandraTopicPublisherManager();
                resourceHolder.setCassandraTopicPublisherManager(cassandraTopicPublisherManager);
            }
            cassandraTopicPublisherManager.init();
            cassandraTopicPublisherManager.start();

        }

        recoverMessages(recoveryHandler);
    }

    /**
     * Perform configurations using the configurations at cluster
     * 
     * @param configuration
     *            configuration object
     * @throws Exception
     */
    private void performCommonConfiguration(Configuration configuration) throws Exception {

        if (ClusterResourceHolder.getInstance().getClusterConfiguration().isInMemoryMode()) {
            log.info("Configuring Message Store in -- IN-MEMORY MODE --");
        } else {
            log.info("Configuring Message Store in -- PERSISTENT MODE --");
        }

        // create cassandra cluster and key space
        String userName = (String) configuration.getProperty(USERNAME_KEY);
        String password = (String) configuration.getProperty(PASSWORD_KEY);
        Object connections = configuration.getProperty(CONNECTION_STRING);
        int replicationFactor = configuration.getInt(REPLICATION_FACTOR, 1);
        String strategyClass = configuration.getString(STRATERGY_CLASS);
        String readConsistancyLevel = configuration.getString(READ_CONSISTENCY_LEVEL);
        String writeConsistancyLevel = configuration.getString(WRITE_CONSISTENCY_LEVEL);
        String connectionString = "";

        if (connections instanceof ArrayList) {
            ArrayList<String> cons = (ArrayList<String>) connections;

            for (String c : cons) {
                connectionString += c + ",";
            }
            connectionString = connectionString.substring(0, connectionString.length() - 1);
        } else if (connectionString instanceof String) {
            connectionString = (String) connections;
            if (connectionString.indexOf(":") > 0) {
                String host = connectionString.substring(0, connectionString.indexOf(":"));
                int port = AndesUtils.getInstance().getCassandraPort();
                connectionString = host + ":" + port;
            }
        }
        String clusterName = (String) configuration.getProperty(CLUSTER_KEY);
        String idGeneratorImpl = (String) configuration.getProperty(ID_GENENRATOR);

        cluster = CassandraDataAccessHelper.createCluster(userName, password, clusterName, connectionString);
        checkCassandraConnection();
        keyspace = createKeySpace(replicationFactor, strategyClass);
        
        //We must call this after setting up the keyspace
        disruptorBasedExecutor = new DisruptorBasedExecutor(getMessageStore(), getDataSender());

        // configure message ID generator
        if (idGeneratorImpl != null && !"".equals(idGeneratorImpl)) {
            try {
                Class clz = Class.forName(idGeneratorImpl);

                Object o = clz.newInstance();
                messageIdGenerator = (MessageIdGenerator) o;
            } catch (Exception e) {
                log.error("Error while loading Message id generator implementation : " + idGeneratorImpl
                        + " adding TimeStamp based implementation as the default", e);
                messageIdGenerator = new TimeStampBasedMessageIdGenerator();
            }
        } else {
            messageIdGenerator = new TimeStampBasedMessageIdGenerator();
        }

        // start message content remover task
        messageContentRemovalTask = new ContentRemoverAndMessageQueueMappingRemoverTask(ClusterResourceHolder
                .getInstance().getClusterConfiguration().getContentRemovalTaskInterval());
        messageContentRemovalTask.setRunning(true);
        Thread t = new Thread(messageContentRemovalTask);
        t.setName(messageContentRemovalTask.getClass().getSimpleName() + "-Thread");
        t.start();

        // start topic message content deletion task
        pubSubMessageContentDeletionTasks = new ConcurrentHashMap<Long, Long>();
        ClusterConfiguration clusterConfiguration = ClusterResourceHolder.getInstance().getClusterConfiguration();
        pubSubMessageContentRemoverTask = new PubSubMessageContentRemoverTask(
                clusterConfiguration.getPubSubMessageRemovalTaskInterval());
        pubSubMessageContentRemoverTask.setRunning(true);
        Thread th = new Thread(pubSubMessageContentRemoverTask);
        th.start();

        messageCacheForCassandra = new CassandraMessageContentCache();

        ConfigurableConsistencyLevel configurableConsistencyLevel = new ConfigurableConsistencyLevel();
        if (readConsistancyLevel == null || readConsistancyLevel.isEmpty()) {
            configurableConsistencyLevel.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
        } else {
            configurableConsistencyLevel
                    .setDefaultReadConsistencyLevel(HConsistencyLevel.valueOf(readConsistancyLevel));
        }
        if (writeConsistancyLevel == null || writeConsistancyLevel.isEmpty()) {
            configurableConsistencyLevel.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
        } else {
            configurableConsistencyLevel.setDefaultWriteConsistencyLevel(HConsistencyLevel
                    .valueOf(writeConsistancyLevel));
        }

        keyspace.setConsistencyLevelPolicy(configurableConsistencyLevel);

        // configure the cluster
        if (ClusterResourceHolder.getInstance().getSubscriptionCoordinationManager() == null) {

            SubscriptionCoordinationManager subscriptionCoordinationManager = new SubscriptionCoordinationManagerImpl();
            subscriptionCoordinationManager.init();
            ClusterResourceHolder.getInstance().setSubscriptionCoordinationManager(subscriptionCoordinationManager);
        }

        if (ClusterResourceHolder.getInstance().getTopicSubscriptionCoordinationManager() == null) {

            TopicSubscriptionCoordinationManager topicSubscriptionCoordinationManager = new TopicSubscriptionCoordinationManager();
            topicSubscriptionCoordinationManager.init();
            ClusterResourceHolder.getInstance().setTopicSubscriptionCoordinationManager(
                    topicSubscriptionCoordinationManager);
        }
        ClusterManager clusterManager = null;

        if (clusterConfiguration.isClusteringEnabled()) {
            clusterManager = new ClusterManager(ClusterResourceHolder.getInstance().getCassandraMessageStore(),
                    clusterConfiguration.getZookeeperConnection());
        } else {
            clusterManager = new ClusterManager(ClusterResourceHolder.getInstance().getCassandraMessageStore());
        }

        ClusterResourceHolder.getInstance().setClusterManager(clusterManager);
        clusterManager.init();
        if (!ClusterResourceHolder.getInstance().getClusterConfiguration().isClusteringEnabled()) {
            clusterManager.startAllGlobalQueueWorkers();
        }

        // perform in memory configurations
        isInMemoryMode = clusterConfiguration.isInMemoryMode();
        if (isInMemoryMode) {
            inMemoryTopicMessageRemoverTask = new InMemoryMessageRemoverTask(ClusterResourceHolder.getInstance()
                    .getClusterConfiguration().getContentRemovalTaskInterval());
            inMemoryTopicMessageRemoverTask.setRunning(true);
            Thread inMemoryMessageRemover = new Thread(inMemoryTopicMessageRemoverTask);
            inMemoryMessageRemover.setName(inMemoryTopicMessageRemoverTask.getClass().getSimpleName() + "-Thread");
            inMemoryMessageRemover.start();
        }

        clusterManagementMBean = new ClusterManagementInformationMBean(clusterManager);
        clusterManagementMBean.register();

        queueManagementMBean = new QueueManagementInformationMBean();
        queueManagementMBean.register();
        ClusteringEnabledSubscriptionManager subscriptionManager = null;

        if (ClusterResourceHolder.getInstance().getClusterConfiguration().isOnceInOrderSupportEnabled()) {
            // TODO : Disruptor - what is once in order support means ?  drop this from docs and configs
            subscriptionManager = new OnceInOrderEnabledSubscriptionManager();
        } else {
            subscriptionManager = new DefaultClusteringEnabledSubscriptionManager();
        }
        ClusterResourceHolder.getInstance().setSubscriptionManager(subscriptionManager);
        subscriptionManager.init();
        configured = true;
    }

    /**
     * Add a message (meta-data) to cassandra message store
     * 
     * @param message
     *            IncomingMessage
     */
    public void addMessage(IncomingMessage message) {
        long messageId = message.getMessageNumber();

        if (isInMemoryMode && message.getExchange().toString().equalsIgnoreCase("amq.topic")) {
            try {
                if (alreadyAddedTopicMessages.contains(messageId)) {
                    return;
                }
                addIncomingTopicMessagesToMemory(messageId, message);
                addCompletedTopicMessageIds(message.getBinding(), messageId);
                alreadyAddedTopicMessages.add(messageId);
            } catch (Exception e) {
                throw new RuntimeException("Error while adding messages to queues  ", e);
            }
        } else if (isInMemoryMode && message.getExchange().toString().equalsIgnoreCase("amq.direct")) {
            try {
                if (alreadyAddedQueueMessages.contains(messageId)) {
                    return;
                }
                addIncomingQueueMessagesToMemory(messageId, message);
                incrementQueueCount(message.getRoutingKey(), 1);
                addCompletedQueueMessageIds(messageId);
                alreadyAddedQueueMessages.add(messageId);
            } catch (Exception e) {
                throw new RuntimeException("Error while adding messages to queues  ", e);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * add an incoming message to in memory store
     * 
     * @param messageId
     *            ID of the message
     * @param incomingMessage
     *            IncomingMessage incoming message to add
     */
    private void addIncomingTopicMessagesToMemory(long messageId, IncomingMessage incomingMessage) {
        incomingTopicMessagesHashtable.put(messageId, incomingMessage);
    }

    private void addIncomingQueueMessagesToMemory(long messageId, IncomingMessage incomingMessage) {
        incomingQueueMessageHashtable.put(messageId, incomingMessage);
    }

    /**
     * Clear message from in-memory message store
     * 
     * @param messageId
     *            ID of the message
     */
    private void removePendingTopicMessageId(long messageId) {
        removalPendingTopicMessageIds.put(messageId, System.currentTimeMillis());
    }

    /**
     * Retrieve message from in-memory message store
     * 
     * @param messageId
     *            id of message
     * @return IncomingMessage object
     */
    private IncomingMessage getTopicIncomingMessageFromMemory(long messageId) {
        return incomingTopicMessagesHashtable.get(messageId);
    }

    private IncomingMessage getQueueIncomingMessageFromMemory(long messageId) {
        return incomingQueueMessageHashtable.get(messageId);
    }

    public void removeIncomingQueueMessage(long messageId) {
        incomingQueueMessageHashtable.remove(messageId);
    }

    public Hashtable<Long, IncomingMessage> getIncomingQueueMessageHashtable() {
        return incomingQueueMessageHashtable;
    }

    public boolean isInMemoryMode() {
        return isInMemoryMode;
    }

    public void setInMemoryMode(boolean inMemoryMode) {
        isInMemoryMode = inMemoryMode;
    }

    public Hashtable<Long, IncomingMessage> getIncomingTopicMessageHashtable() {
        return incomingTopicMessagesHashtable;
    }

    /**
     * Add message ID to all subscriber queues registered under this topic
     * 
     * @param topic
     *            name of topic
     * @param messageId
     *            ID of the message
     */
    public void addCompletedTopicMessageIds(String topic, long messageId) {
        try {
            List<String> registeredSubscribers = getRegisteredSubscribersForTopic(topic);
            if (registeredSubscribers != null) {
                for (String subscriber : registeredSubscribers) {

                    try {
                        addCompletedMessageToTopicSubscriberQueue(subscriber, messageId);
                    } catch (InterruptedException e) {
                        log.error("Error adding message id " + messageId + "To subscriber " + subscriber
                                + " using in memory mode");
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while adding Message Id to Subscriber queue", e);
        }

    }

    public void addCompletedQueueMessageIds(long messageId) {
        try {
            pendingMessageIdsQueue.put(messageId);
        } catch (InterruptedException e) {
            log.error("Error adding message id " + messageId + "To pemdining messages queue using in-memory mode");
        }
    }

    /**
     * add incoming topic message ID to subscription queue (bound for a topic)
     * 
     * @param subscriptionQueueName
     *            name of subscription queue
     * @param messageID
     *            ID of message
     * @throws InterruptedException
     */
    private void addCompletedMessageToTopicSubscriberQueue(String subscriptionQueueName, long messageID)
            throws InterruptedException {
        if (null != topicSubscriberQueueMap.get(subscriptionQueueName)) {
            topicSubscriberQueueMap.get(subscriptionQueueName).put(messageID);
        } else {
            LinkedBlockingQueue<Long> subscriberQueue = new LinkedBlockingQueue<Long>();
            subscriberQueue.put(messageID);
            topicSubscriberQueueMap.put(subscriptionQueueName.trim(), subscriberQueue);
        }
    }

    /**
     * get next pending topic message in a given destination queue to be
     * delivered.Topic delivery thread will access this method.
     * 
     * @return AMQMessage next pending message to deliver
     * @throws InterruptedException
     */
    public List<AMQMessage> getNextTopicMessageToDeliver() throws InterruptedException {
        List<AMQMessage> amqMessages = new ArrayList<AMQMessage>();
        // when in In-memory mode we read the messages directly from the
        // incomingTopicMessagesHashtable
        Hashtable<Long, IncomingMessage> messages = getIncomingTopicMessageHashtable();
        Enumeration<IncomingMessage> enu = messages.elements();
        while (enu.hasMoreElements()) {
            IncomingMessage incomingMessage = enu.nextElement();
            amqMessages.add(new AMQMessage(incomingMessage.getStoredMessage()));
        }
        return amqMessages;
    }

    /**
     * get next message (ID) to be delivered via topic subscription for the
     * given subscription
     * 
     * @param subscriptionQueueName
     * @return pending message ID
     * @throws InterruptedException
     */
    private Long getPendingTopicMessageId(String subscriptionQueueName) throws InterruptedException {
        long pendingMessageID = -1;
        LinkedBlockingQueue<Long> pendingMessageIds = topicSubscriberQueueMap.get(subscriptionQueueName);
        HashSet<Long> sentButNotAckedMids = sentButNotAckedTopicMessageMap.get(subscriptionQueueName);
        if (null == sentButNotAckedMids) {
            sentButNotAckedMids = new HashSet<Long>();
            sentButNotAckedTopicMessageMap.put(subscriptionQueueName, sentButNotAckedMids);
        }
        if (null != pendingMessageIds) {
            pendingMessageID = pendingMessageIds.take();
            sentButNotAckedMids.add(pendingMessageID);
        } else {
            LinkedBlockingQueue<Long> subscriberQueue = new LinkedBlockingQueue<Long>();
            topicSubscriberQueueMap.put(subscriptionQueueName.trim(), subscriberQueue);
        }
        return pendingMessageID;
    }

    public List<QueueEntry> getNextQueueMessagesToDeliver(AMQQueue queue, int messageCount) throws InterruptedException {
        List<QueueEntry> amqMessageList = new ArrayList<QueueEntry>();
        SimpleQueueEntryList list = new SimpleQueueEntryList(queue);
        AMQMessage message = null;
        for (int i = 0; i < messageCount; i++) {
            long nextMessageId = getNextQueueMessageId();
            message = null;
            if (nextMessageId != -1) {
                IncomingMessage incomingMessage = getQueueIncomingMessageFromMemory(nextMessageId);
                message = new AMQMessage(incomingMessage.getStoredMessage());
                amqMessageList.add(list.add(message));
            } else {
                break;
            }
        }
        return amqMessageList;
    }

    public List<QueueEntry> getNextIgnoredQueueMessagesToDeliver(AMQQueue queue, int messageCount)
            throws InterruptedException {
        List<QueueEntry> amqMessageList = new ArrayList<QueueEntry>();
        SimpleQueueEntryList list = new SimpleQueueEntryList(queue);
        AMQMessage message = null;
        for (int i = 0; i < messageCount; i++) {
            long nextMessageId = getNextIgnoredQueueMessageId();
            message = null;
            if (nextMessageId != -1) {
                IncomingMessage incomingMessage = getQueueIncomingMessageFromMemory(nextMessageId);
                if (incomingMessage != null) {
                    message = new AMQMessage(incomingMessage.getStoredMessage());
                    amqMessageList.add(list.add(message));
                }
            } else {
                break;
            }
        }
        return amqMessageList;
    }

    private Long getNextQueueMessageId() throws InterruptedException {
        long pendingMessageID = -1;
        if (null != pendingMessageIdsQueue && null != sentButNotAckedMids) {
            Object pendingMessageId = pendingMessageIdsQueue.poll(1000, TimeUnit.MILLISECONDS);
            if (pendingMessageId != null) {
                pendingMessageID = (Long) pendingMessageId;
                sentButNotAckedMids.add(pendingMessageID);
            }
        } else {
            pendingMessageIdsQueue = new LinkedBlockingQueue<Long>();
            sentButNotAckedMids = new HashSet<Long>();
        }
        return pendingMessageID;
    }

    private Long getNextIgnoredQueueMessageId() throws InterruptedException {
        long pendingMessageID = -1;
        Object pendingMessageId = ignoredMessageIdsQueue.poll(1000, TimeUnit.MILLISECONDS);
        if (pendingMessageId != null) {
            pendingMessageID = (Long) pendingMessageId;
        }
        return pendingMessageID;
    }

    public boolean setNextIgnoredQueueMessageId(long messageID) throws InterruptedException {
        return ignoredMessageIdsQueue.add(messageID);
    }

    /**
     * Get a given Number of Messages from node queue using the given offset
     *
     * @param queue
     *            Queue name
     * @param messageCount
     *            how many messages to receive
     * @param lastMessageId
     *            last processed message id. we will try to get messages from
     *            lasProcessedMessageId+1 .. lasProcessedMessageId+1 + count
     * @return List of messages
     * @throws AMQStoreException
     *             in case of an Data Access Error
     */
    public List<QueueEntry> getMessagesFromNodeQueue(String nodeQueue, AMQQueue queue, int messageCount,
            long lastMessageId) throws AMQStoreException {

        List<QueueEntry> messages = null;
        SimpleQueueEntryList list = new SimpleQueueEntryList(queue);
        messages = new ArrayList<QueueEntry>();

        if (!isCassandraConnectionLive) {
            log.error("Cassandra Message Store is Inaccessible. Cannot Receive Messages from User Queues");
            return messages;
        }
        try {
            ColumnSlice<Long, byte[]> messagesColumnSlice = CassandraDataAccessHelper.getMessagesFromQueue(nodeQueue,
                    NODE_QUEUES_COLUMN_FAMILY, keyspace, lastMessageId, messageCount);
            // if list is empty return
            if (messagesColumnSlice == null || messagesColumnSlice.getColumns().size() == 0) {
                return messages;
            }

            // long startMessageIdInQueryRange = lastMessageId;
            // long endMessageIdInQueryRange = Long.MAX_VALUE;
            // if(!messagesColumnSlice.getColumns().isEmpty()) {
            // Object lastMessageColumn =
            // messagesColumnSlice.getColumns().get(messagesColumnSlice.getColumns().size()
            // - 1);
            // if (lastMessageColumn instanceof HColumn) {
            // endMessageIdInQueryRange = ((HColumn<Long,byte[]>)
            // lastMessageColumn).getName();
            // }
            // }
            // get message expiration properties
            // ColumnSlice<Long, String> messagePropertiesColumnSlice =
            // CassandraDataAccessHelper.getStringTypeValuesForGivenRowWithColumnsFiltered
            // (MESSAGE_EXPIRATION_PROPERTY_RAW_NAME,MESSAGE_PROPERTIES_COLUMN_FAMILY,keyspace,startMessageIdInQueryRange,endMessageIdInQueryRange);

            // combining metadata with message properties create QueueEntries
            for (Object column : messagesColumnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    long messageId = ((HColumn<Long, byte[]>) column).getName();
                    byte[] value = ((HColumn<Long, byte[]>) column).getValue();
                    byte[] dataAsBytes = value;
                    ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                    buf.position(1);
                    buf = buf.slice();
                    MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                    StorableMessageMetaData metaData = type.getFactory().createMetaData(buf);
                    // create message with meta data. This has access to message
                    // content
                    StoredCassandraMessage message = new StoredCassandraMessage(messageId, metaData);
                    message.setExchange("amq.direct");
                    AMQMessage amqMessage = new AMQMessage(message);
                    // amqMessage.setExpiration(Long.parseLong(messagePropertiesColumnSlice.getColumnByName(messageId).getValue()));
                    messages.add(list.add(amqMessage));
                }
            }
        } catch (NumberFormatException e) {
            throw new AMQStoreException("Error while accessing user queue" + nodeQueue, e);
        } catch (Exception e) {
            throw new AMQStoreException("Error while accessing user queue" + nodeQueue, e);
        }

        return messages;
    }

    /**
     * Get given number of messages from User Queue. If number of messages in
     * the queue (qn) is less than the requested Number of messages(rn) (qn <=
     * rn) this will return all the messages in the given user queue
     * 
     * @param nodeQueue
     *            User Queue name
     * @param messageCount
     *            max message count
     * @param lastReadMessageId
     *            id of the last processed message
     * @return List of Messages
     */
    public List<CassandraQueueMessage> getMessagesFromNodeQueue(String nodeQueue, int messageCount,
            long lastReadMessageId) {

        List<CassandraQueueMessage> messages = new ArrayList<CassandraQueueMessage>();

        if (!isCassandraConnectionLive) {
            log.error("Cassandra Message Store is Inaccessible. Cannot Receive Messages from User Queues");
            return messages;
        }
        try {
            ColumnSlice<Long, byte[]> messagesColumnSlice = CassandraDataAccessHelper.getMessagesFromQueue(
                    nodeQueue.trim(), NODE_QUEUES_COLUMN_FAMILY, keyspace, lastReadMessageId, messageCount);

            // if list is empty return
            if (messagesColumnSlice == null || messagesColumnSlice.getColumns().size() == 0) {
                return messages;
            }

            // long startMessageIdInQueryRange = lastReadMessageId;
            // long endMessageIdInQueryRange = Long.MAX_VALUE;
            // if(!messagesColumnSlice.getColumns().isEmpty()) {
            // Object lastMessageColumn =
            // messagesColumnSlice.getColumns().get(messagesColumnSlice.getColumns().size()
            // - 1);
            // if (lastMessageColumn instanceof HColumn) {
            // endMessageIdInQueryRange = ((HColumn<Long,byte[]>)
            // lastMessageColumn).getName();
            // }
            // }
            // get message expiration properties
            // ColumnSlice<Long, String> messagePropertiesColumnSlice =
            // CassandraDataAccessHelper.getStringTypeValuesForGivenRowWithColumnsFiltered
            // (MESSAGE_EXPIRATION_PROPERTY_RAW_NAME,MESSAGE_PROPERTIES_COLUMN_FAMILY,keyspace,startMessageIdInQueryRange,endMessageIdInQueryRange);

            for (Object column : messagesColumnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    long messageId = ((HColumn<Long, byte[]>) column).getName();
                    byte[] value = ((HColumn<Long, byte[]>) column).getValue();

                    byte[] dataAsBytes = value;
                    ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                    buf.position(1);
                    buf = buf.slice();
                    MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                    StorableMessageMetaData metaData = type.getFactory().createMetaData(buf);
                    // create message with meta data. This has access to message
                    // content
                    StoredCassandraMessage message = new StoredCassandraMessage(messageId, metaData);
                    message.setExchange("amq.direct");
                    AMQMessage amqMessage = new AMQMessage(message);
                    // long messageExpiration =
                    // (Long.parseLong(messagePropertiesColumnSlice.getColumnByName(messageId).getValue()));
                    // amqMessage.setExpiration(messageExpiration);
                    String queueName = amqMessage.getMessageMetaData().getMessagePublishInfo().getRoutingKey()
                            .toString();
                    CassandraQueueMessage cqm = new CassandraQueueMessage(messageId, queueName, dataAsBytes, amqMessage);
                    messages.add(cqm);
                }
            }
        } catch (NumberFormatException e) {
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }
        return messages;
    }

    /**
     * get message properties for a given property with message ID range
     * 
     * @param propertyRowName
     *            name of cassandra row fro property
     * @param startMessageIdInQueryRange
     *            starting message ID
     * @param endMessageIdInQueryRange
     *            end message ID
     * @return
     */
    public ColumnSlice<Long, String> getMessagePropertiesForMessagesInRange(String propertyRowName,
            long startMessageIdInQueryRange, long endMessageIdInQueryRange) {
        ColumnSlice<Long, String> messagePropertiesColumnSlice = null;
        try {
            messagePropertiesColumnSlice = CassandraDataAccessHelper
                    ._getStringTypeValuesForGivenRowWithColumnsFiltered(propertyRowName,
                            MESSAGE_PROPERTIES_COLUMN_FAMILY, keyspace, startMessageIdInQueryRange,
                            endMessageIdInQueryRange);

        } catch (CassandraDataAccessException e) {
            log.error("Error in getting message properties from Cassandra message store", e);
        }
        return messagePropertiesColumnSlice;
    }

    /**
     * get number of messages in a node queue addressed to a given destination
     * queue
     * 
     * @param nodeQueueName
     *            node queue name
     * @param destinationQueue
     *            destination queue name
     * @return message count
     */
    public int getMessageCountOfNodeQueueForDestinationQueue(String nodeQueueName, String destinationQueue) {
        int numberOfMessages = 0;
        if (!isCassandraConnectionLive) {
            log.error("Error in Getting Messages from Node Queue: " + nodeQueueName
                    + ". Message Store is Inaccessible.");
            return numberOfMessages;
        }
        try {
            List<Long> messageIDList = getMessageIDsAddressedToQueue(destinationQueue);
            int numberOfMessagesCounted = 0;
            while (messageIDList.size() > 0) {
                long lastProcessedMessageId = messageIDList.get(messageIDList.size() - 1);
                if (messageIDList.size() >= 1000) {
                    // skip processing last message ID. We will catch it in next
                    // round
                    messageIDList.remove(messageIDList.size() - 1);
                }
                long startingId = messageIDList.get(0);
                long lastId = messageIDList.get(messageIDList.size() - 1);
                HashSet<Long> messageIdsOfNodeQueue = getMessageIdsFromNodeQueue(nodeQueueName, startingId, lastId);
                for (long messageID : messageIDList) {
                    if (messageIdsOfNodeQueue.contains(messageID)) {
                        numberOfMessagesCounted += 1;
                    }
                }
                // ask for the next 1000
                messageIDList = getMessageIDsAddressedToQueue(destinationQueue, lastProcessedMessageId, 1000);
                // as message select is inclusive it will return last processed
                // message ID for ever.
                if (messageIDList.size() == 1) {
                    messageIDList.remove(0);
                }
            }
            return numberOfMessagesCounted;

        } catch (NumberFormatException e) {
            log.error("Number format error in getting messages from global queue : " + nodeQueueName, e);
        } catch (Exception e) {
            log.error("Error in getting messages from global queue: " + nodeQueueName, e);
        }
        return numberOfMessages;
    }

    /**
     * get number of subscribers on given node for a given destination queue
     * 
     * @param ZKId
     *            zooKeeper ID of node
     * @param destinationQueueName
     *            destination queue name
     * @return number of subscribers
     */
    public int getNumberOfSubscribersOnNodeForDestinationQueue(int ZKId, String destinationQueueName) {
        return CassandraDataAccessHelper.safeLongToInt(getSubscriptionCountForQueue(destinationQueueName,
                AndesConstants.NODE_QUEUE_NAME_PREFIX + ZKId));
    }

    /**
     * Get message count of global queue
     * 
     * @param globalQueueName
     *            name of global queue
     * @return message count
     */
    public int getMessageCountOfGlobalQueue(String globalQueueName) {
        int messageCount = 0;
        if (!isCassandraConnectionLive) {
            log.error("Error in getting messages from global queue: " + globalQueueName
                    + ". Message Store is Inaccessible.");
            return messageCount;
        }
        try {
            ColumnSlice<Long, byte[]> columnSlice = CassandraDataAccessHelper.getMessagesFromQueue(
                    globalQueueName.trim(), GLOBAL_QUEUES_COLUMN_FAMILY, keyspace, 0L,
                    CassandraDataAccessHelper.safeLongToInt(400));
            int currentMsgCount = columnSlice.getColumns().size();
            messageCount += currentMsgCount;
            if (currentMsgCount == 0) {
                return messageCount;
            }
            long lastMessageId = 0;
            Object lastColumn = columnSlice.getColumns().get(columnSlice.getColumns().size() - 1);
            if (lastColumn instanceof HColumn) {
                lastMessageId = ((HColumn<Long, byte[]>) lastColumn).getName();
            }
            while (currentMsgCount > 0) {
                ColumnSlice<Long, byte[]> nextColumnSlice = CassandraDataAccessHelper.getMessagesFromQueue(
                        globalQueueName.trim(), GLOBAL_QUEUES_COLUMN_FAMILY, keyspace, lastMessageId,
                        CassandraDataAccessHelper.safeLongToInt(400));
                currentMsgCount = nextColumnSlice.getColumns().size();
                messageCount += currentMsgCount;
                if (currentMsgCount == 0) {
                    break;
                }
                lastColumn = (nextColumnSlice.getColumns().get(nextColumnSlice.getColumns().size() - 1));
                if (lastColumn instanceof HColumn) {
                    lastMessageId = ((HColumn<Long, byte[]>) lastColumn).getName();
                }
            }

        } catch (NumberFormatException e) {
            log.error("Number format error in getting messages from global queue : " + globalQueueName, e);
        } catch (Exception e) {
            log.error("Error in getting messages from global queue: " + globalQueueName, e);
        }
        return messageCount;
    }

    /**
     * get a list of message IDs (Long) addressed to a given destination queue
     * (routing key) starting from lastProcessedMsgId inclusive
     * 
     * @param destinationQueueName
     *            destination queue name
     * @param lastProcessedMsgId
     *            last process message ID
     * @param count
     *            message count to receive
     * @return ArrayList<Long> messageIDs
     */
    public ArrayList<Long> getMessageIDsAddressedToQueue(String destinationQueueName, long lastProcessedMsgId, int count) {
        if (!isCassandraConnectionLive) {
            log.error("Error in getting messageIDs from message queue mapping:" + destinationQueueName
                    + "Message Store is Inaccessible.");
            return null;
        }
        try {
            ArrayList<Long> messageIDList = new ArrayList<Long>();
            ColumnSlice<Long, String> columnSlice = CassandraDataAccessHelper.getLongTypeColumnsInARowWithOffset(
                    destinationQueueName, MESSAGE_QUEUE_MAPPING_COLUMN_FAMILY, keyspace, count, lastProcessedMsgId);
            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    long messageId = ((HColumn<Long, String>) column).getName();
                    messageIDList.add(messageId);
                }
            }
            return messageIDList;
        } catch (Exception e) {
            log.error("Error in getting messageIDs from message queue mapping: " + destinationQueueName, e);
            return null;
        }
    }

    /**
     * get first chunk of 1000 message IDs whose destination queue name given is
     * 
     * @param destinationQueueName
     *            name of destination queue
     * @return message ID list
     */
    public ArrayList<Long> getMessageIDsAddressedToQueue(String destinationQueueName) {
        if (!isCassandraConnectionLive) {
            log.error("Error in getting messageIDs from message queue mapping:" + destinationQueueName
                    + "Message Store is Inaccessible.");
            return null;
        }
        try {
            ArrayList<Long> messageIDList = new ArrayList<Long>();
            ColumnSlice<Long, String> columnSlice = CassandraDataAccessHelper.getLongTypeColumnsInARow(
                    destinationQueueName, MESSAGE_QUEUE_MAPPING_COLUMN_FAMILY, keyspace, 1000);
            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    long messageId = ((HColumn<Long, String>) column).getName();
                    messageIDList.add(messageId);
                }
            }
            return messageIDList;
        } catch (Exception e) {
            log.error("Error in getting messageIDs from message queue mapping: " + destinationQueueName, e);
            return null;
        }
    }

    /**
     * remove all the messages from messageID-destination queue mapping
     * 
     * @param destinationQueueName
     *            name of destination queue
     */
    public void deleteAllMessageIDsAddressedToQueue(String destinationQueueName) {
        if (!isCassandraConnectionLive) {
            log.error("Error in getting messageIDs from message queue mapping:" + destinationQueueName
                    + "Message Store is Inaccessible.");
        }
        try {
            CassandraDataAccessHelper.deleteWholeRowFromColumnFamily(MESSAGE_QUEUE_MAPPING_COLUMN_FAMILY, keyspace,
                    destinationQueueName);
        } catch (Exception e) {
            log.error("Error in getting deleting  messageIDs from message queue mapping:" + destinationQueueName, e);
        }
    }

    /**
     * remove message properties for the given message ID list
     * 
     * @param messageIDList
     *            message ID list
     */
    public void deleteMessagePropertiesForMessageList(List<Long> messageIDList) {
        if (!isCassandraConnectionLive) {
            log.error("Error in removing message properties for message IDs. Message Store is Inaccessible.");
        }
        String property = MESSAGE_EXPIRATION_PROPERTY_RAW_NAME;
        try {
            CassandraDataAccessHelper.deleteLongColumnListFromColumnFamily(MESSAGE_PROPERTIES_COLUMN_FAMILY, keyspace,
                    property, messageIDList);
        } catch (Exception e) {
            log.error("Error in removing message properties for message IDs", e);
        }
    }

    /**
     * Get List of messages from a given Global queue
     * 
     * @param globalQueueName
     *            Global queue Name
     * @param messageCount
     *            Number of messages that should be fetched.
     * @return List of Messages.
     */
    public Queue<CassandraQueueMessage> getMessagesFromGlobalQueue(String globalQueueName, int messageCount)
            throws AMQStoreException {
        Queue<CassandraQueueMessage> messages = new LinkedList<CassandraQueueMessage>();

        if (!isCassandraConnectionLive) {
            log.error("Error in getting messages from global queue: " + globalQueueName
                    + ". Message Store is Inaccessible.");
            return messages;
        }

        try {
            ColumnSlice<Long, byte[]> columnSlice = CassandraDataAccessHelper.getMessagesFromQueue(
                    globalQueueName.trim(), GLOBAL_QUEUES_COLUMN_FAMILY, keyspace, messageCount);

            // long startMessageIdInQueryRange = 0L;
            // long endMessageIdInQueryRange = Long.MAX_VALUE;
            // if(!columnSlice.getColumns().isEmpty()) {
            // Object lastMessageColumn =
            // columnSlice.getColumns().get(columnSlice.getColumns().size() -
            // 1);
            // if (lastMessageColumn instanceof HColumn) {
            // endMessageIdInQueryRange = ((HColumn<Long,byte[]>)
            // lastMessageColumn).getName();
            // }
            // }
            // get message expiration properties
            // ColumnSlice<Long, String> messagePropertiesColumnSlice =
            // CassandraDataAccessHelper.getStringTypeValuesForGivenRowWithColumnsFiltered
            // (MESSAGE_EXPIRATION_PROPERTY_RAW_NAME,MESSAGE_PROPERTIES_COLUMN_FAMILY,keyspace,startMessageIdInQueryRange,endMessageIdInQueryRange);
            for (Object column : columnSlice.getColumns()) {

                if (column instanceof HColumn) {
                    long messageId = ((HColumn<Long, byte[]>) column).getName();
                    byte[] value = ((HColumn<Long, byte[]>) column).getValue();
                    byte[] dataAsBytes = value;
                    ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                    buf.position(1);
                    buf = buf.slice();
                    MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                    StorableMessageMetaData metaData = type.getFactory().createMetaData(buf);
                    // create message with meta data. This has access to message
                    // content
                    StoredCassandraMessage message = new StoredCassandraMessage(messageId, metaData);
                    message.setExchange("amq.direct");
                    AMQMessage amqMessage = new AMQMessage(message);
                    // long messageExpiration =
                    // (Long.parseLong(messagePropertiesColumnSlice.getColumnByName(messageId).getValue()));
                    // amqMessage.setExpiration(messageExpiration);
                    String routingKey = ((MessageMetaData) metaData).getMessagePublishInfo().getRoutingKey().toString();
                    CassandraQueueMessage msg = new CassandraQueueMessage(messageId, routingKey, value, amqMessage);
                    messages.add(msg);
                }
            }
        } catch (NumberFormatException e) {
            throw new AMQStoreException("Number format error in getting messages from global queue : "
                    + globalQueueName, e);
        } catch (Exception e) {
            throw new AMQStoreException("Error in getting messages from global queue: " + globalQueueName, e);
        }

        return messages;
    }

    public List<CassandraQueueMessage> getMessagesFromGlobalQueue(String globalQueueName, long lastProcessedMessageId,
            int messageCount) throws AMQStoreException {
        List<CassandraQueueMessage> messages = new ArrayList<CassandraQueueMessage>();

        if (!isCassandraConnectionLive) {
            log.error("Error in getting messages from global queue: " + globalQueueName
                    + ". Message Store is Inaccessible.");
            return messages;
        }

        try {
            ColumnSlice<Long, byte[]> columnSlice = CassandraDataAccessHelper
                    .getMessagesFromQueue(globalQueueName.trim(), GLOBAL_QUEUES_COLUMN_FAMILY, keyspace,
                            lastProcessedMessageId, messageCount);

            // long startMessageIdInQueryRange = lastProcessedMessageId;
            // long endMessageIdInQueryRange = Long.MAX_VALUE;

            if (columnSlice.getColumns().isEmpty()) {
                return messages;
            }

            // if(!columnSlice.getColumns().isEmpty()) {
            // Object lastMessageColumn =
            // columnSlice.getColumns().get(columnSlice.getColumns().size() -
            // 1);
            // if (lastMessageColumn instanceof HColumn) {
            // endMessageIdInQueryRange = ((HColumn<Long,byte[]>)
            // lastMessageColumn).getName();
            // }
            // }
            // get message expiration properties
            // ColumnSlice<Long, String> messagePropertiesColumnSlice =
            // CassandraDataAccessHelper.getStringTypeValuesForGivenRowWithColumnsFiltered
            // (MESSAGE_EXPIRATION_PROPERTY_RAW_NAME,
            // MESSAGE_PROPERTIES_COLUMN_FAMILY, keyspace,
            // startMessageIdInQueryRange, endMessageIdInQueryRange);

            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    long messageId = ((HColumn<Long, byte[]>) column).getName();
                    byte[] value = ((HColumn<Long, byte[]>) column).getValue();
                    byte[] dataAsBytes = value;
                    ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                    buf.position(1);
                    buf = buf.slice();
                    MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                    StorableMessageMetaData metaData = type.getFactory().createMetaData(buf);
                    // create message with meta data. This has access to message
                    // content
                    StoredCassandraMessage message = new StoredCassandraMessage(messageId, metaData);
                    message.setExchange("amq.direct");
                    AMQMessage amqMessage = new AMQMessage(message);
                    // long messageExpiration =
                    // (Long.parseLong(messagePropertiesColumnSlice.getColumnByName(messageId).getValue()));
                    // amqMessage.setExpiration(messageExpiration);
                    String routingKey = ((MessageMetaData) metaData).getMessagePublishInfo().getRoutingKey().toString();
                    CassandraQueueMessage msg = new CassandraQueueMessage(messageId, routingKey, value, amqMessage);
                    messages.add(msg);
                }
            }
        } catch (NumberFormatException e) {
            throw new AMQStoreException("Number format error in getting messages from global queue : "
                    + globalQueueName, e);
        } catch (Exception e) {
            throw new AMQStoreException("Error in getting messages from global queue: " + globalQueueName, e);
        }

        return messages;
    }

    /**
     * get message Ids from global queue for given ID range
     * 
     * @param globalQueueName
     *            global queue name
     * @param startingId
     *            starting message ID
     * @param lastId
     *            last message Id
     * @return message Ids
     * @throws AMQStoreException
     */
    public HashSet<Long> getMessageIdsFromGlobalQueue(String globalQueueName, long startingId, long lastId)
            throws AMQStoreException {
        HashSet<Long> messageIds = new HashSet<Long>();

        if (!isCassandraConnectionLive) {
            log.error("Error in getting message IDs from global queue: " + globalQueueName
                    + ". Message Store is Inaccessible.");
            return messageIds;
        }

        try {
            ColumnSlice<Long, byte[]> columnSlice = CassandraDataAccessHelper.getMessagesFromQueue(
                    globalQueueName.trim(), GLOBAL_QUEUES_COLUMN_FAMILY, keyspace, startingId,
                    CassandraDataAccessHelper.safeLongToInt(lastId - startingId));

            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    long messageId = ((HColumn<Long, byte[]>) column).getName();
                    messageIds.add(messageId);
                }
            }
        } catch (NumberFormatException e) {
            throw new AMQStoreException("Number format error in getting messages from global queue : "
                    + globalQueueName, e);
        } catch (Exception e) {
            throw new AMQStoreException("Error in getting messages from global queue: " + globalQueueName, e);
        }

        return messageIds;

    }

    /**
     * get message Ids from Node queue for given Id range
     * 
     * @param nodeQueueName
     *            name of node queue
     * @param startingId
     *            starting message ID
     * @param lastId
     *            last message Id
     * @return message Ids
     * @throws AMQStoreException
     */
    public HashSet<Long> getMessageIdsFromNodeQueue(String nodeQueueName, long startingId, long lastId)
            throws AMQStoreException {
        HashSet<Long> messageIds = new HashSet<Long>();
        if (!isCassandraConnectionLive) {
            log.error("Error in getting message IDs from global queue: " + nodeQueueName
                    + ". Message Store is Inaccessible.");
            return messageIds;
        }

        try {
            ColumnSlice<Long, byte[]> columnSlice = CassandraDataAccessHelper.getMessagesFromQueue(
                    nodeQueueName.trim(), NODE_QUEUES_COLUMN_FAMILY, keyspace, startingId,
                    CassandraDataAccessHelper.safeLongToInt(lastId - startingId));

            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    long messageId = ((HColumn<Long, byte[]>) column).getName();
                    messageIds.add(messageId);
                }
            }
        } catch (NumberFormatException e) {
            throw new AMQStoreException("Number format error in getting messages from global queue : " + nodeQueueName,
                    e);
        } catch (Exception e) {
            throw new AMQStoreException("Error in getting messages from global queue: " + nodeQueueName, e);
        }

        return messageIds;
    }

    /**
     * Get List of messages from a given Global queue. Used for in order message
     * flushing
     * 
     * @param destinationQueue
     *            AMQ Queue
     * @param session
     *            AMQ Protocol Session
     * @param messageCount
     *            maximum message count to be fetched
     * @return List of messages
     * @throws AMQStoreException
     */
    public List<QueueEntry> getQueueFilteredMessagesFromGlobalQueue(AMQQueue destinationQueue,
            AMQProtocolSession session, int messageCount) throws AMQStoreException {

        List<QueueEntry> messages = new ArrayList<QueueEntry>();
        SimpleQueueEntryList list = new SimpleQueueEntryList(destinationQueue);

        if (!isCassandraConnectionLive) {
            log.error("Error while getting messages from queue : " + destinationQueue
                    + ". Message Store is Inaccessible.");
            return messages;
        }

        try {
            String destinationQueueName = destinationQueue.getName();
            String globalQueueName = AndesUtils.getGlobalQueueNameForDestinationQueue(destinationQueueName);
            List<Long> messageIDList = getMessageIDsAddressedToQueue(destinationQueueName);
            while (messageIDList.size() > 0) {
                long lastProcessedMessageId = messageIDList.get(messageIDList.size() - 1);
                if (messageIDList.size() >= 1000) {
                    // skip processing last message ID. We will catch it in next
                    // round
                    messageIDList.remove(messageIDList.size() - 1);
                }

                long startingId = messageIDList.get(0);
                long lastId = messageIDList.get(messageIDList.size() - 1);

                ColumnSlice<Long, byte[]> columnSlice = CassandraDataAccessHelper.getMessagesFromQueue(
                        globalQueueName.trim(), GLOBAL_QUEUES_COLUMN_FAMILY, keyspace, startingId,
                        CassandraDataAccessHelper.safeLongToInt(lastId - startingId));

                long startMessageIdInQueryRange = 0L;
                long endMessageIdInQueryRange = Long.MAX_VALUE;

                if (!columnSlice.getColumns().isEmpty()) {
                    Object lastMessageColumn = columnSlice.getColumns().get(columnSlice.getColumns().size() - 1);
                    if (lastMessageColumn instanceof HColumn) {
                        endMessageIdInQueryRange = ((HColumn<Long, byte[]>) lastMessageColumn).getName();
                    }
                }
                // get message expiration properties
                // ColumnSlice<Long, String> messagePropertiesColumnSlice =
                // CassandraDataAccessHelper.getStringTypeValuesForGivenRowWithColumnsFiltered
                // (MESSAGE_EXPIRATION_PROPERTY_RAW_NAME,MESSAGE_PROPERTIES_COLUMN_FAMILY,keyspace,startMessageIdInQueryRange,endMessageIdInQueryRange);

                for (Object column : columnSlice.getColumns()) {
                    if (column instanceof HColumn) {
                        long messageId = ((HColumn<Long, byte[]>) column).getName();
                        byte[] value = ((HColumn<Long, byte[]>) column).getValue();
                        byte[] dataAsBytes = value;
                        ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                        buf.position(1);
                        buf = buf.slice();
                        MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                        StorableMessageMetaData metaData = type.getFactory().createMetaData(buf);
                        StoredCassandraMessage message = new StoredCassandraMessage(messageId, metaData);
                        message.setExchange("amq.direct");
                        AMQMessage amqMessage = new AMQMessage(message);
                        // long messageExpiration =
                        // (Long.parseLong(messagePropertiesColumnSlice.getColumnByName(messageId).getValue()));
                        // amqMessage.setExpiration(messageExpiration);
                        amqMessage.setClientIdentifier(session);
                        messages.add(list.add(amqMessage));

                        if (messages.size() >= messageCount) {
                            return messages;
                        }
                    }
                }

                // ask for the next 1000
                messageIDList = getMessageIDsAddressedToQueue(destinationQueueName, lastProcessedMessageId, 1000);
                // as message select is inclusive it will return last processed
                // message ID for ever.
                if (messageIDList.size() == 1) {
                    messageIDList.remove(0);
                }
            }
        } catch (Exception e) {
            throw new AMQStoreException("Error while getting messages from queue : " + destinationQueue, e);
        }

        return messages;
    }

    /**
     * Remove a message from node Queue
     * 
     * @param nodeQueueName
     *            node queue name
     * @param messageId
     *            message id
     */
    public void removeMessageFromNodeQueue(String nodeQueueName, long messageId) throws AMQStoreException {
        if (!isCassandraConnectionLive) {
            log.error("Error while removing message from User queue. Message Store is Inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.deleteLongColumnFromRaw(NODE_QUEUES_COLUMN_FAMILY, nodeQueueName, messageId,
                    keyspace);
        } catch (CassandraDataAccessException e) {
            throw new AMQStoreException("Error while removing message from User queue", e);
        }
    }

    /**
     * Remove List of Message From Cassandra Message Store. Use this to Delete
     * set of messages in CassandraMessageStore In one DB Call
     * 
     * @param nodeQueueName
     *            Node Queue name
     * @param msgList
     *            Message List to be removed
     * @throws AMQStoreException
     *             If Error occurs while removing data.
     */
    public void removeMessageBatchFromNodeQueue(String nodeQueueName, List<CassandraQueueMessage> msgList)
            throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error while removing messages from User queue. Message Store is Inaccessible.");
            return;
        }
        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
        try {
            for (CassandraQueueMessage msg : msgList) {
                CassandraDataAccessHelper.deleteLongColumnFromRaw(NODE_QUEUES_COLUMN_FAMILY, nodeQueueName,
                        msg.getMessageId(), mutator, false);
            }
        } catch (CassandraDataAccessException e) {
            throw new AMQStoreException("Error while removing messages from User queue", e);
        } finally {
            mutator.execute();
        }

    }

    /**
     * remove a message batch from a node queue giving a node queue name and a
     * list of message IDs
     * 
     * @param msgIdList
     *            message IDs to be removed from node queue
     * @param nodeQueueName
     *            node queue name from which message should be removed
     * @throws AMQStoreException
     */
    public void removeMessageBatchFromNodeQueue(List<Long> msgIdList, String nodeQueueName) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error while removing messages from User queue. Message Store is Inaccessible.");
            return;
        }
        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
        try {
            for (Long msgId : msgIdList) {
                CassandraDataAccessHelper.deleteLongColumnFromRaw(NODE_QUEUES_COLUMN_FAMILY, nodeQueueName, msgId,
                        mutator, false);
            }
        } catch (CassandraDataAccessException e) {
            throw new AMQStoreException("Error while removing messages from User queue", e);
        } finally {
            mutator.execute();
        }

    }

    /**
     * Remove one message from Global queue (used by in-order message flushing)
     * 
     * @param globalQueueName
     *            name of global queue
     * @param messageId
     *            message ID
     */
    public void removeMessageFromGlobalQueue(String globalQueueName, long messageId) {
        if (!isCassandraConnectionLive) {
            log.error("Error while removing messages from global queue " + globalQueueName
                    + ". Message Store is Inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.deleteLongColumnFromRaw(GLOBAL_QUEUES_COLUMN_FAMILY, globalQueueName, messageId,
                    keyspace);
        } catch (CassandraDataAccessException e) {
            log.error("Error while removing messages from global queue " + globalQueueName, e);
        }
    }

    /**
     * Remove a message batch from global queue
     * 
     * @param globalQueueName
     *            name of global queue
     * @param messageId
     *            ID of message to remove
     * @param mutator
     *            mutator used
     */
    public void removeMessageFromGlobalQueue(String globalQueueName, long messageId, Mutator<String> mutator) {
        if (!isCassandraConnectionLive) {
            log.error("Error while removing messages from global queue " + globalQueueName
                    + ". Message Store is Inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.deleteLongColumnFromRaw(GLOBAL_QUEUES_COLUMN_FAMILY, globalQueueName, messageId,
                    mutator, false);
        } catch (CassandraDataAccessException e) {
            log.error("Error while removing messages from global queue " + globalQueueName, e);
        }
    }

    /**
     * Transfer message batch from global queue to node queue in one call
     * 
     * @param list
     *            message list to move
     * @param globalQueueName
     *            name of global queue
     */
    public void transferMessageBatchFromGlobalQueueToNodeQueue(List<CassandraQueueMessage> list, String globalQueueName) {

        if (!isCassandraConnectionLive) {
            log.error("Error while transferring messages from Global Queue to User Queues. Message Store is Inaccessible.");
            return;
        }
        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
        try {
            for (CassandraQueueMessage msg : list) {
                addMessageToNodeQueue(msg.getNodeQueue(), msg.getMessageId(), msg.getMessage(), mutator);
                removeMessageFromGlobalQueue(globalQueueName, msg.getMessageId(), mutator);
            }
        } catch (CassandraDataAccessException e) {
            e.printStackTrace();
            log.error("Error while transferring messages from Global Queue to User Queues");
        } finally {
            mutator.execute();
        }
    }

    /**
     * Remove a message batch from global queue
     * 
     * @param list
     *            list of messages
     * @param globalQUeueName
     *            name of global queue
     */
    public void removeMessageBatchFromGlobalQueue(List<CassandraQueueMessage> list, String globalQUeueName) {

        if (!isCassandraConnectionLive) {
            log.error("Error while removing messages from global queue " + globalQUeueName + ". "
                    + "Message Store is Inaccessible.");
            return;
        }
        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
        try {
            for (CassandraQueueMessage msg : list) {
                removeMessageFromGlobalQueue(globalQUeueName, msg.getMessageId(), mutator);
            }
        } finally {
            mutator.execute();
        }
    }

    /**
     * Remove message batch from global queue
     * 
     * @param list
     *            message ID list
     * @param globalQueueName
     */
    public void removeMessageBatchFromGlobalQueueByMessageIds(List<Long> list, String globalQueueName) {

        if (!isCassandraConnectionLive) {
            log.error("Error while removing messages from global queue " + globalQueueName + ". "
                    + "Message Store is Inaccessible.");
            return;
        }
        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
        try {
            for (long msgId : list) {
                removeMessageFromGlobalQueue(globalQueueName, msgId, mutator);
            }
        } finally {
            mutator.execute();
        }
    }

    /**
     * recover bindings
     * 
     * @param recoveryHandler
     *            recovery handler
     * @throws AMQException
     */
    public void recover(ConfigurationRecoveryHandler recoveryHandler) throws AMQException {

        boolean readyOrTimeOut = false;
        boolean error = false;

        int initTimeOut = 10;
        int count = 0;
        int maxTries = 10;

        while (!readyOrTimeOut) {
            try {
                ConfigurationRecoveryHandler.QueueRecoveryHandler qrh = recoveryHandler.begin(this);
                loadQueues(qrh);

                ConfigurationRecoveryHandler.ExchangeRecoveryHandler erh = qrh.completeQueueRecovery();
                List<String> exchanges = loadExchanges(erh);
                ConfigurationRecoveryHandler.BindingRecoveryHandler brh = erh.completeExchangeRecovery();
                recoverBindings(brh, exchanges);
                brh.completeBindingRecovery();
            } catch (Exception e) {
                error = true;
                log.error("Error recovering persistent state: " + e.getMessage(), e);
            } finally {
                if (!error) {
                    readyOrTimeOut = true;
                    continue;
                } else {
                    long waitTime = initTimeOut * 1000 * (long) Math.pow(2, count);
                    log.warn("Waiting for Cluster data to be synced Please ,start the other nodes soon, wait time: "
                            + waitTime + "ms");
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {

                    }
                    if (count > maxTries) {
                        readyOrTimeOut = true;
                        throw new AMQStoreException("Max Backoff attempts expired for data recovery");
                    }
                    count++;
                }
            }

        }

    }

    /**
     * add a message counter for Amq queue
     * 
     * @param destinationQueueName
     *            name of destination queue
     * @throws Exception
     */
    public void addMessageCounterForQueue(String destinationQueueName) throws Exception {
        if (isInMemoryMode) {
            if (!queueMessageCountMap.containsKey(destinationQueueName)) {
                queueMessageCountMap.put(destinationQueueName, 0L);
            }
            return;
        }
        if (!isCassandraConnectionLive) {
            log.error("Error in adding message counters");
            return;
        }
        try {
            if (!getDestinationQueueNames().contains(destinationQueueName))
                CassandraDataAccessHelper.insertCounterColumn(MESSAGE_COUNTERS_COLUMN_FAMILY,
                        MESSAGE_COUNTERS_RAW_NAME, destinationQueueName, keyspace);
        } catch (Exception e) {
            log.error("Error in accessing message counters", e);
            throw e;
        }
    }

    /**
     * remove message counter for the destination queue
     * 
     * @param destinationQueueName
     *            name of the queue
     */
    public void removeMessageCounterForQueue(String destinationQueueName) {
        if (isInMemoryMode) {
            queueMessageCountMap.remove(destinationQueueName);
        }
        if (!isCassandraConnectionLive) {
            log.error("Error removing the counter. Message Store is Inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.removeCounterColumn(MESSAGE_COUNTERS_COLUMN_FAMILY, MESSAGE_COUNTERS_RAW_NAME,
                    destinationQueueName, keyspace);
        } catch (CassandraDataAccessException e) {
            if (e.getMessage().contains("Unable to remove counter column as cassandra connection is down")) {
                log.error("Error in accessing message counters as cassandra connection is down");
            } else {
                log.error("Error in accessing message counters", e);
            }
        }
    }

    /**
     * increment message counter for queue by given value
     * 
     * @param destinationQueueName
     *            name of destination queue
     * @param incrementBy
     *            increment by
     */
    public void incrementQueueCount(String destinationQueueName, long incrementBy) {
        if (isInMemoryMode) {
            queueMessageCountMap.put(destinationQueueName, queueMessageCountMap.get(destinationQueueName) + 1);
            return;
        }
        if (!isCassandraConnectionLive) {
            log.error("Error while incrementing message counters. Message Store is Inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.incrementCounter(destinationQueueName, MESSAGE_COUNTERS_COLUMN_FAMILY,
                    MESSAGE_COUNTERS_RAW_NAME, keyspace, incrementBy);
        } catch (CassandraDataAccessException e) {
            log.error("Error in accessing message counters", e);
        }
    }

    /**
     * decrement message count by a given value
     * 
     * @param destinationQueueName
     *            name of queue
     * @param decrementBy
     *            decrement by
     */
    public void decrementQueueCount(String destinationQueueName, long decrementBy) {
        if (isInMemoryMode) {
            queueMessageCountMap.put(destinationQueueName, queueMessageCountMap.get(destinationQueueName) - 1);
            return;
        }
        if (!isCassandraConnectionLive) {
            log.error("Error while decrementing message counters. Message Store is Inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.decrementCounter(destinationQueueName, MESSAGE_COUNTERS_COLUMN_FAMILY,
                    MESSAGE_COUNTERS_RAW_NAME, keyspace, decrementBy);
        } catch (CassandraDataAccessException e) {
            log.error("Error in accessing message counters", e);
        }
    }

    /**
     * get message count for destination queue using message counters
     * 
     * @param destinationQueueName
     *            name of the queue
     * @return
     */
    public long getCassandraMessageCountForQueue(String destinationQueueName) {
        long msgCount = 0;
        if (isInMemoryMode) {
            long messageCount = queueMessageCountMap.get(destinationQueueName);
            return messageCount;
        }
        if (!isCassandraConnectionLive) {
            log.error("Error while getting message count for queue. Message Store is Inaccessible.");
        }
        try {
            msgCount = CassandraDataAccessHelper.getCountValue(keyspace, MESSAGE_COUNTERS_COLUMN_FAMILY,
                    destinationQueueName, MESSAGE_COUNTERS_RAW_NAME);
        } catch (CassandraDataAccessException e) {
            log.error("Error in accessing message counters", e);
        }
        return msgCount;
    }

    /**
     * Get message count for the given destination queue using column families
     * this method is called on demand from the user
     *
     * @param destinationQueueName - name of the queue
     * @return msgCount
     */
    public long getMessageCountForDestinationQueue(String destinationQueueName){
        long msgCount = 0;
        if (!isCassandraConnectionLive) {
            log.error("Error while getting message count for queue. Message Store is Inaccessible.");
        }
        try{
            /* This block gets the message count of destinationQueueName in the Node Queues*/
            List<String> nodeQueues = getNodeQueuesForDestinationQueue(destinationQueueName);
             for(String nodeQueue: nodeQueues){
                 ColumnSlice<Long, byte[]> messageColumnSlice = CassandraDataAccessHelper.getMessagesFromQueue
                         (nodeQueue.trim(),NODE_QUEUES_COLUMN_FAMILY,keyspace,Integer.MAX_VALUE);
                 for (Object column : messageColumnSlice.getColumns()) {
                     if (column instanceof HColumn) {
                         byte[] value = ((HColumn<Long, byte[]>) column).getValue();
                         byte[] dataAsBytes = value;
                         ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                         buf.position(1);
                         buf = buf.slice();
                         MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                         StorableMessageMetaData metaData = type.getFactory().createMetaData(buf);

                         // create message meta data from dataAsBytes[] and get the routing key of message
                         // then we compare it with destinationQueueName and incrementing msgCount
                         String queueName  = ((MessageMetaData)metaData).getMessagePublishInfo().getRoutingKey().toString();
                         if(queueName.equals(destinationQueueName)){
                             msgCount ++;
                         }

                     }
                 }

             }

            /* This block gets the message count of destinationQueueName in the Global Queue*/
            String globalQueueName = AndesUtils.getGlobalQueueNameForDestinationQueue(destinationQueueName);
            ColumnSlice<Long, byte[]> columnSlice = CassandraDataAccessHelper.getMessagesFromQueue(
                         globalQueueName.trim(), GLOBAL_QUEUES_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);
                 for (Object column : columnSlice.getColumns()) {
                     if (column instanceof HColumn) {
                         byte[] value = ((HColumn<Long, byte[]>) column).getValue();
                         byte[] dataAsBytes = value;
                         ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                         buf.position(1);
                         buf = buf.slice();
                         MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                         StorableMessageMetaData metaData = type.getFactory().createMetaData(buf);
                         // create message meta data from dataAsBytes[] and get the routing key of message
                         // then we compare it with destinationQueueName and incrementing msgCount
                         String queueName  = ((MessageMetaData)metaData).getMessagePublishInfo().getRoutingKey().toString();
                         if(queueName.equals(destinationQueueName)){
                             msgCount ++;
                         }

                     }
                 }


        } catch (NumberFormatException e) {
            log.error("Number format error in getting message count from queue : " + destinationQueueName, e);
        } catch (Exception e) {
            log.error("Error in getting message count from queue: " + destinationQueueName, e);
        }

        return msgCount;
    }

    /**
     * add a subscription counter for destination queue
     * 
     * @param destinationQueueName
     *            name of destination queue
     * @param nodeQueueName
     *            name of the node queue
     * @throws Exception
     */
    public void addSubscriptionCounterForQueue(String destinationQueueName, String nodeQueueName) {
        if (!isCassandraConnectionLive) {
            log.error("Error in adding message counters");
            return;
        }
        try {
            CassandraDataAccessHelper.insertCounterColumn(SUBSCRIPTION_COUNTERS_COLUMN_FAMILY, destinationQueueName,
                    nodeQueueName, keyspace);
        } catch (Exception e) {
            log.error("Error in accessing subscription counters", e);

        }
    }

    /**
     * remove subscription counter for the destination queue
     * 
     * @param destinationQueueName
     *            name of the queue
     * @param nodeQueueName
     *            Name of the node queue
     */
    public void removeSubscriptionCounterForQueue(String destinationQueueName, String nodeQueueName) {
        if (!isCassandraConnectionLive) {
            log.error("Error removing the counter. Message Store is Inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.removeCounterColumn(SUBSCRIPTION_COUNTERS_COLUMN_FAMILY, destinationQueueName,
                    nodeQueueName, keyspace);
        } catch (CassandraDataAccessException e) {
            if (e.getMessage().contains("Unable to remove counter column as cassandra connection is down")) {
                log.error("Error in accessing message counters as cassandra connection is down");
            } else {
                log.error("Error in accessing subscription counters", e);
            }
        }
    }

    /**
     * increment subscription counter for queue by given value
     * 
     * @param destinationQueueName
     *            name of destination queue
     * @param incrementBy
     *            increment by
     */
    public void incrementSubscriptionCount(String destinationQueueName, String nodeQueueName, long incrementBy) {
        if (!isCassandraConnectionLive) {
            log.error("Error while incrementing message counters. Message Store is Inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.incrementCounter(nodeQueueName, SUBSCRIPTION_COUNTERS_COLUMN_FAMILY,
                    destinationQueueName, keyspace, incrementBy);
        } catch (CassandraDataAccessException e) {
            log.error("Error in accessing subscription counters", e);
        }
    }

    /**
     * decrement subscription count by a given value
     * 
     * @param destinationQueueName
     *            name of queue
     * @param decrementBy
     *            decrement by
     */
    public void decrementSubscriptionCount(String destinationQueueName, String nodeQueueName, long decrementBy) {
        if (!isCassandraConnectionLive) {
            log.error("Error while decrementing message counters. Message Store is Inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.decrementCounter(nodeQueueName, SUBSCRIPTION_COUNTERS_COLUMN_FAMILY,
                    destinationQueueName, keyspace, decrementBy);
        } catch (CassandraDataAccessException e) {
            if (e.getMessage().contains("Unable to remove active subscribers as cassandra connection is down")) {
                log.error("Error in accessing subscription counters as cassandra connection is down");
            } else {
                log.error("Error in accessing subscription counters", e);
            }

        }
    }

    /**
     * get subscription count for destination queue
     * 
     * @param destinationQueueName
     *            name of the queue
     * @return
     */
    public long getSubscriptionCountForQueue(String destinationQueueName, String nodeQueue) {
        long msgCount = 0;
        if (!isCassandraConnectionLive) {
            log.error("Error while getting message count for queue. Message Store is Inaccessible.");
        }
        try {
            msgCount = CassandraDataAccessHelper.getCountValue(keyspace, SUBSCRIPTION_COUNTERS_COLUMN_FAMILY,
                    nodeQueue, destinationQueueName);
        } catch (CassandraDataAccessException e) {
            log.error("Error in accessing subscription counters", e);
        }
        return msgCount;
    }

    /**
     * check how many node queues are under the global queue
     * 
     * @param globalQueueName
     * @return
     * @throws AMQStoreException
     */
    private int getNodeQueueCountForGlobalQueue(String globalQueueName) throws AMQStoreException {
        int queueCount = 0;
        if (!isCassandraConnectionLive) {
            log.error("Error in getting user queue count for " + globalQueueName + ". "
                    + "Message Store is Inaccessible.");
            return queueCount;
        }
        try {
            ColumnSlice<String, String> columnSlice = CassandraDataAccessHelper.getStringTypeColumnsInARow(
                    globalQueueName, GLOBAL_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);
            queueCount = columnSlice.getColumns().size();
        } catch (Exception e) {
            throw new AMQStoreException("Error in getting user queue count", e);
        }
        return queueCount;
    }

    /**
     * Add a Message to node Queue. This can be used to add a message batch also
     * using mutator
     * 
     * @param nodeQueue
     *            User Queue Name
     * @param messageId
     *            message id
     * @param message
     *            message content.
     * @param mutator
     *            mutator used
     */
    public void addMessageToNodeQueue(String nodeQueue, long messageId, byte[] message, Mutator<String> mutator)
            throws CassandraDataAccessException {

        if (!isCassandraConnectionLive) {
            log.error("Error in adding message :" + messageId + " to user queue :" + nodeQueue
                    + ". Message Store is Inaccessible");
            return;
        }
        try {

            CassandraDataAccessHelper.addMessageToQueue(NODE_QUEUES_COLUMN_FAMILY, nodeQueue, messageId, message,
                    mutator, false);
        } catch (Exception e) {
            throw new CassandraDataAccessException("Error in adding message :" + messageId + " to user queue :"
                    + nodeQueue, e);
        }
    }

    /**
     * Add a message batch to node queue
     *
     * @param messages
     *            list of messages to add
     * @throws CassandraDataAccessException
     */
    public void addMessageBatchToNodeQueues(CassandraQueueMessage[] messages) throws CassandraDataAccessException {

        if (!isCassandraConnectionLive) {
            log.error("Error in adding message batch to Queues. Message Store is Inaccessible.");
            return;
        }
        try {
            Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
            try {
                for (CassandraQueueMessage message : messages) {
                    addMessageToNodeQueue(message.getNodeQueue(), message.getMessageId(), message.getMessage(), mutator);
                }
            } finally {
                mutator.execute();
            }

        } catch (CassandraDataAccessException e) {
            throw new CassandraDataAccessException("Error in adding message batch to Queues ", e);
        }
    }

    /**
     * convert CassandraQueueMessage list to a QueueEntry
     * 
     * @param queue
     *            destination queue name
     * @param session
     *            AMQProtocolSession
     * @param queueMessages
     *            list of CassandraQueueMessages
     * @return list of QueueEntry messages
     * @throws AMQStoreException
     */
    public List<QueueEntry> getPreparedBrowserMessages(AMQQueue queue, AMQProtocolSession session,
            List<CassandraQueueMessage> queueMessages) throws AMQStoreException {
        List<QueueEntry> messages = new ArrayList<QueueEntry>();
        SimpleQueueEntryList list = new SimpleQueueEntryList(queue);
        if (!isCassandraConnectionLive) {
            log.error("Error while getting messages from queue : " + queue + "Message Store is Inaccessible.");
            return messages;
        }
        try {

            for (CassandraQueueMessage message : queueMessages) {
                long messageId = message.getMessageId();
                byte[] value = message.getMessage();
                byte[] dataAsBytes = value;
                ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                buf.position(1);
                buf = buf.slice();
                MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                StorableMessageMetaData metaData = type.getFactory().createMetaData(buf);
                StoredCassandraMessage storedMessage = new StoredCassandraMessage(messageId, metaData);
                storedMessage.setExchange("amq.direct");
                AMQMessage amqMessage = new AMQMessage(storedMessage);
                amqMessage.setClientIdentifier(session);
                messages.add(list.add(amqMessage));
            }
        } catch (Exception e) {
            throw new AMQStoreException("Error while getting messages from queue : " + queue, e);
        }

        return messages;
    }

    // /**
    // * Add message meta-data to global queue. We actually schedule it to be
    // added at the moment.
    // * @param globalQueueName global queue name
    // * @param routingKey roting key of the message
    // * @param messageId ID of the message
    // * @param message message meta data
    // * @param isNewMessage if this is a message coming from client or some
    // procedure inside broker
    // * @param isDestinationQueueBoundToTopicExchange if this is a message
    // bount to topic exchange
    // * @throws Exception
    // */
    public void addMessageToGlobalQueue(String globalQueueName, String routingKey, long messageId, byte[] message,
            boolean isNewMessage, long messageExpiration, boolean isDestinationQueueBoundToTopicExchange)
            throws Exception {
         if (log.isDebugEnabled()) {
         log.debug("Adding Message with id " + messageId + " to Queue " + globalQueueName);
         }
         throw new UnsupportedOperationException();
//         publishMessageWriter.addMessage(globalQueueName, routingKey,
//         messageId, message, isNewMessage, messageExpiration,
//         isDestinationQueueBoundToTopicExchange);
    }

    /**
     * add message content to cassandra message store with offset
     *
     * @param messageId
     *            id of incoming message
     * @param offset
     *            offset at data chunk
     * @param src
     *            content as a ByteBuffer
     * @throws AMQStoreException
     */
    public void addMessageContent(String messageId, final int offset, ByteBuffer src) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error in adding message content. Message Store is Inaccessible.");
            return;
        }
        try {
            final String rowKey = AndesConstants.MESSAGE_CONTENT_CASSANDRA_ROW_NAME_PREFIX + messageId;
            src = src.slice();
            final byte[] chunkData = new byte[src.limit()];

            src.duplicate().get(chunkData);

            long start = System.currentTimeMillis();
            Mutator<String> messageMutator = HFactory.createMutator(keyspace, stringSerializer);
            CassandraDataAccessHelper.addIntegerByteArrayContentToRaw(MESSAGE_CONTENT_COLUMN_FAMILY, rowKey, offset,
                    chunkData, messageMutator, false);
            messageMutator.execute();
            if (log.isDebugEnabled()) {
                log.debug("Content Write for " + rowKey + " took " + (System.currentTimeMillis() - start) + "ms");
            }
            // above inner class is instead of following
            // publishMessageContentWriter.addMessage(rowKey.trim(), offset,
            // chunkData);
        } catch (Exception e) {
            throw new AMQStoreException("Error in adding message content", e);
        }
    }

    /**
     * get message content for message ID
     * 
     * @param messageId
     *            ID of the message
     * @param offsetValue
     *            buffer offset value
     * @param dst
     *            ByteBuffer
     * @return written buffer size
     */
    public int getContent(String messageId, int offsetValue, ByteBuffer dst) {

        int written = 0;
        int chunkSize = 65534;
        byte[] content = null;
        // read from cache.
        // written =
        // messageCacheForCassandra.getContent(messageId,offsetValue,dst);
        // If entry is not there written value won't change
        if (!isCassandraConnectionLive) {
            log.error("Error in reading content. Message Store is Inaccessible.");
            return written;
        }
        if (written == 0) {
            // load from DB and add entry to the cache
            try {

                String rowKey = "mid" + messageId;
                if (offsetValue == 0) {

                    ColumnQuery columnQuery = HFactory.createColumnQuery(keyspace, stringSerializer, integerSerializer,
                            byteBufferSerializer);
                    columnQuery.setColumnFamily(MESSAGE_CONTENT_COLUMN_FAMILY);
                    columnQuery.setKey(rowKey.trim());
                    columnQuery.setName(offsetValue);

                    QueryResult<HColumn<Integer, ByteBuffer>> result = columnQuery.execute();
                    HColumn<Integer, ByteBuffer> column = result.get();
                    if (column != null) {
                        int offset = column.getName();
                        content = bytesArraySerializer.fromByteBuffer(column.getValue());

                        final int size = (int) content.length;
                        int posInArray = offset + written - offset;
                        int count = size - posInArray;
                        if (count > dst.remaining()) {
                            count = dst.remaining();
                        }
                        dst.put(content, 0, count);
                        written = count;
                    } else {
                        throw new RuntimeException("Unexpected Error , content already deleted for the message "+ messageId);
                    }
                } else {
                    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                    int k = offsetValue / chunkSize;
                    SliceQuery query = HFactory.createSliceQuery(keyspace, stringSerializer, integerSerializer,
                            byteBufferSerializer);
                    query.setColumnFamily(MESSAGE_CONTENT_COLUMN_FAMILY);
                    query.setKey(rowKey.trim());
                    query.setRange(k * chunkSize, (k + 1) * chunkSize + 1, false, 10);

                    QueryResult<ColumnSlice<Integer, ByteBuffer>> result = query.execute();
                    ColumnSlice<Integer, ByteBuffer> columnSlice = result.get();
                    boolean added = false;
                    for (HColumn<Integer, ByteBuffer> column : columnSlice.getColumns()) {
                        added = true;
                        byteOutputStream.write(bytesArraySerializer.fromByteBuffer(column.getValue()));
                    }
                    content = byteOutputStream.toByteArray();
                    final int size = (int) content.length;
                    int posInArray = offsetValue - (k * chunkSize);
                    int count = size - posInArray;
                    if (count > dst.remaining()) {
                        count = dst.remaining();
                    }

                    dst.put(content, posInArray, count);

                    written += count;
                }

                // add a new entry to the cache. If cache is full eldest entry
                // will be removed.
                /*
                 * byte[] cacheValue = new byte[content.length];
                 * System.arraycopy(content, 0, cacheValue, 0, content.length);
                 * messageCacheForCassandra
                 * .addEntryToCache(messageId,offsetValue, cacheValue);
                 */

            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error in reading content", e);
            }
        }
        return written;
    }

    /**
     * Removes already delivered topic messages from hashtable to avoid
     * repetitive looping
     * 
     * @param messageIdsToBeRemoved
     *            - delivered messages list
     */
    public void removeDeliveredTopicMessageIdsFromIncomingMessagesTable(List<Long> messageIdsToBeRemoved) {
        for (Long mid : messageIdsToBeRemoved) {
            if (incomingTopicMessagesHashtable.containsKey(mid)) {
                incomingTopicMessagesHashtable.remove(mid);
            }
        }
    }



    /**
     * write meta data for the message ID given
     * 
     * @param messageId
     *            message ID
     * @param metaData
     *            metaData
     */
    public void storeMetaData(long messageId, StorableMessageMetaData metaData) {

        if (!isCassandraConnectionLive) {
            log.error("Error in storing meta data. Message Store is Inaccessible.");
            return;
        }
        try {
            final int bodySize = 1 + metaData.getStorableSize();
            byte[] underlying = new byte[bodySize];
            underlying[0] = (byte) metaData.getType().ordinal();
            java.nio.ByteBuffer buf = java.nio.ByteBuffer.wrap(underlying);
            buf.position(1);
            buf = buf.slice();
            metaData.writeToBuffer(0, buf);

            Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

            mutator.addInsertion(QMD_ROW_NAME, QMD_COLUMN_FAMILY,
                    HFactory.createColumn(messageId, underlying, longSerializer, bytesArraySerializer));
            mutator.execute();

        } catch (Exception e) {
            log.error("Error in storing meta data", e);
        }
    }

    /**
     * get meta data for the message
     * 
     * @param messageId
     *            Id if the message
     * @return StorableMessageMetaData object
     */
    private StorableMessageMetaData getMetaData(long messageId) {

        StorableMessageMetaData metaData = null;
        if (!isCassandraConnectionLive) {
            log.error("Error in getting meta data of provided message id. Message Store is Inaccessible.");
            return metaData;
        }
        try {
            HColumn<Long, byte[]> column = CassandraDataAccessHelper.getLongByteArrayColumnInARow(QMD_ROW_NAME,
                    QMD_COLUMN_FAMILY, messageId, keyspace);
            if (null != column) {
                byte[] dataAsBytes = column.getValue();
                ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                buf.position(1);
                buf = buf.slice();
                MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                metaData = type.getFactory().createMetaData(buf);
            }
        } catch (Exception e) {
            log.error("Error in getting meta data of provided message id", e);
        }
        return metaData;
    }

    /**
     * remove meta data for give message
     * 
     * @param messageId
     *            message ID
     * @throws AMQStoreException
     */
    private void removeMetaData(long messageId) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error in removing metadata. Message Store is Inaccessible.");
            return;
        }
        try {
            Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
            CassandraDataAccessHelper
                    .deleteLongColumnFromRaw(QMD_COLUMN_FAMILY, QMD_ROW_NAME, messageId, mutator, true);
        } catch (Exception e) {
            throw new AMQStoreException("Error in removing metadata", e);
        }
    }

    /**
     * Acknowledged messages are added to this column family with the current
     * system time as the acknowledged time. Content of such messages are not
     * reoved yet
     * 
     * @param messageId
     *            id of message
     */
    public void addAckedMessage(long messageId) {

        if (!isCassandraConnectionLive) {
            log.error("Error in storing meta data. Message Store is Inaccessible.");
            return;
        }
        try {
            pubSubMessageContentDeletionTasks.put(messageId, messageId);
            Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
            long ackTime = System.currentTimeMillis();

            mutator.addInsertion(ACKED_MESSAGE_IDS_ROW, ACKED_MESSAGE_IDS_COLUMN_FAMILY,
                    HFactory.createColumn(messageId, ackTime, longSerializer, longSerializer));
            mutator.execute();
        } catch (Exception e) {
            log.error("Error in storing meta data", e);
        }
    }

    /**
     * When message contents are ready to remove , removing the reference to
     * that from the acknowledged message column family
     * 
     * @param messageId
     *            id of message
     * @throws AMQStoreException
     */
    private void removeAckedMessage(long messageId) throws AMQStoreException {
        if (!isCassandraConnectionLive) {
            log.error("Error in storing meta data. Message Store is Inaccessible.");
            return;
        }
        try {
            Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
            CassandraDataAccessHelper.deleteLongColumnFromRaw(ACKED_MESSAGE_IDS_COLUMN_FAMILY, ACKED_MESSAGE_IDS_ROW,
                    messageId, mutator, true);
        } catch (Exception e) {
            throw new AMQStoreException("Error in storing meta data", e);
        }
    }

    /**
     * Checking whether the topic message is ready to remove and remove the
     * message if conditions satisfied
     * 
     * @param messageId
     *            id of message
     * @return if message is removed successfully
     */
    public boolean isReadyAndRemovedMessageContent(long messageId) {

        long currentSystemTime = System.currentTimeMillis();
        try {
            ColumnQuery<String, Long, Long> columnQuery = HFactory.createColumnQuery(keyspace, stringSerializer,
                    longSerializer, longSerializer);
            columnQuery.setKey(ACKED_MESSAGE_IDS_ROW);
            columnQuery.setColumnFamily(ACKED_MESSAGE_IDS_COLUMN_FAMILY);
            columnQuery.setName(messageId);
            QueryResult<HColumn<Long, Long>> result = null;
            if (isCassandraConnectionLive) {
                result = columnQuery.execute();
            } else {
                log.warn("Cassandra Connection is not alive, Message Store is Inaccessible.");
            }

            if (result != null) {
                HColumn<Long, Long> column = result.get();
                // Checking whether the message is ready to remove

                if (column != null && column.getValue() != null) {
                    ClusterConfiguration clusterConfiguration = ClusterResourceHolder.getInstance()
                            .getClusterConfiguration();
                    if ((currentSystemTime - column.getValue()) >= clusterConfiguration
                            .getContentRemovalTimeDifference()) {
                        List<Long> midList = new ArrayList<Long>();
                        midList.add(messageId);
                        removeMetaData(messageId);
                        removeAckedMessage(messageId);
                        deleteMessagePropertiesForMessageList(midList);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error("Error while removing Message data", e);
            return false;
        }
    }

    /**
     * bind a AMQQueue to an Exchange using a routingKey
     * 
     * @param exchange
     *            exchange
     * @param amqQueue
     *            amQ queue
     * @param routingKey
     *            routing key
     * @throws CassandraDataAccessException
     */
    public void addBinding(Exchange exchange, AMQQueue amqQueue, String routingKey) throws CassandraDataAccessException {
        if (keyspace == null) {
            return;
        }
        if (!isCassandraConnectionLive) {
            log.error("Cannot add bindings. Message Store is Inaccessible.");
            return;
        }
        String columnName = routingKey;
        String columnValue = amqQueue.getName();
        CassandraDataAccessHelper.addMappingToRaw(BINDING_COLUMN_FAMILY, exchange.getName(), columnName, columnValue,
                keyspace);

    }

    /**
     * bind a AMQQueue to an Exchange using a routingKey
     * 
     * @param exchangeName
     *            name of exchange
     * @param amqQueueName
     *            name of amq queue
     * @param routingKey
     *            routing key
     * @throws CassandraDataAccessException
     */
    public void addBinding(String exchangeName, String amqQueueName, String routingKey)
            throws CassandraDataAccessException {
        if (keyspace == null) {
            return;
        }
        if (!isCassandraConnectionLive) {
            log.error("Cannot add bindings. Message Store is Inaccessible.");
            return;
        }
        String columnName = routingKey;
        String columnValue = amqQueueName;
        CassandraDataAccessHelper.addMappingToRaw(BINDING_COLUMN_FAMILY, exchangeName, columnName, columnValue,
                keyspace);
    }

    /**
     * remove a binding of a amq queue from an exchange
     * 
     * @param exchange
     *            Exchange
     * @param amqQueue
     *            AMQQueue
     * @param routingKey
     *            routing key
     * @throws CassandraDataAccessException
     */
    public void removeBinding(Exchange exchange, AMQQueue amqQueue, String routingKey)
            throws CassandraDataAccessException {

        if (keyspace == null) {
            return;
        }
        if (!isCassandraConnectionLive) {
            log.error("Cannot add bindings. Message Store is Inaccessible.");
            return;
        }
        CassandraDataAccessHelper.deleteStringColumnFromRaw(BINDING_COLUMN_FAMILY, exchange.getName(), routingKey,
                keyspace);

    }

    /**
     * When a new message arrived for a topic, in implementations before
     * (0.13.0-wso2v5) it searched for the registered subscribers for that topic
     * once it got the list of registered subscribers for that topic it adds the
     * received message for all of those subscription queues
     * 
     * But from 0,13.0-wso2v5 it does not do any search for subscriber queues ,
     * It get the TopicNodeQueue names which has subscriptions for that topic
     * and add the message id to those queues. This is done to resolve the issue
     * https://wso2.org/jira/browse/MB-89
     * 
     * 
     * @param topic
     *            - Topic
     * @param messageId
     *            - Id of the new message
     */
    public void addMessageIdRoutedViaTopic(String topic, long messageId) {
        try {
            List<String> nodeQueuesForTopic = getRegisteredTopicNodeQueuesForTopic(topic);
            if (nodeQueuesForTopic != null) {
                for (String nodeQueue : nodeQueuesForTopic) {

                    try {
                        addMessageIdToSubscriberQueue(nodeQueue, messageId);
                    } catch (AMQStoreException e) {
                        log.error("Error adding message id " + messageId + "To subscriber " + nodeQueue);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while adding Message Id to Subscriber queue", e);
        }
    }

    /**
     * Getting messages from the provided queue
     * <p/>
     * This method retrives message from the queue. It search for the message
     * ids from the provided id to above
     * 
     * @param nodeQueue
     *            - node Queue
     * @param lastDeliveredMid
     *            - Id of the last delivered message
     * @return List of messages to be delivered
     */
    public List<AMQMessage> getSubscriberMessages(String nodeQueue, long lastDeliveredMid) {
        List<AMQMessage> messages = null;
        try {
            List<Long> messageIds = getPendingMessageIds(nodeQueue, lastDeliveredMid);
            if (messageIds.size() > 0) {
                long startMessageIdInQueryRange = lastDeliveredMid;
                long endMessageIdInQueryRange = Long.MAX_VALUE;
                endMessageIdInQueryRange = messageIds.get(messageIds.size() - 1);
                // get message expiration properties
                // ColumnSlice<Long, String> messagePropertiesColumnSlice =
                // CassandraDataAccessHelper.getStringTypeValuesForGivenRowWithColumnsFiltered
                // (MESSAGE_EXPIRATION_PROPERTY_RAW_NAME,MESSAGE_PROPERTIES_COLUMN_FAMILY,keyspace,startMessageIdInQueryRange,endMessageIdInQueryRange);
                messages = new ArrayList<AMQMessage>();
                for (long messageId : messageIds) {
                    StorableMessageMetaData messageMetaData = getMetaData(messageId);
                    if (messageMetaData != null) {
                        // we create stored cassandra message here, which can
                        // get message content
                        StoredCassandraMessage storedCassandraMessage = new StoredCassandraMessage(messageId,
                                messageMetaData, true);
                        AMQMessage message = new AMQMessage(storedCassandraMessage, null);
                        // if(null !=
                        // messagePropertiesColumnSlice.getColumnByName(messageId))
                        // {
                        // message.setExpiration(Long.parseLong(messagePropertiesColumnSlice.getColumnByName(messageId).getValue()));
                        // }else{
                        // log.warn("Unable to set the message expiration property for the message with id "
                        // + messageId);
                        // }
                        messages.add(message);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error while getting topic messages from cassandra storage", e);
        }
        return messages;
    }

    /**
     * Registers topic Add an entry to the Topics column family to indicate that
     * there is a subscriber for this topic
     * 
     * @param topic
     *            - Topic name
     */
    private void registerTopic(String topic) {

        if (!isCassandraConnectionLive) {
            log.error("Error in registering queue for the topic. Message Store is Inaccessible.");
            return;
        }
        try {
            if (topic != null && (topicSubscribersMap.get(topic) == null)) {
                topicSubscribersMap.put(topic, new ArrayList<String>());
            }
            if (topic != null && (topicNodeQueuesMap.get(topic) == null)) {
                topicNodeQueuesMap.put(topic, new ArrayList<String>());
            }
            CassandraDataAccessHelper.addMappingToRaw(TOPICS_COLUMN_FAMILY, TOPICS_ROW, topic, topic, keyspace);
            log.info("Created Topic : " + topic);
        } catch (Exception e) {
            log.error("Error in registering queue for the topic", e);
        }
    }

    /**
     * Get all the topics where subscribers exists
     * 
     * @return topic names list
     * @throws Exception
     */
    public List<String> getTopics() throws Exception {
        List<String> topicList = null;
        if (!isCassandraConnectionLive) {
            log.error("Error in getting the topic list. Message Store is Inaccessible.");
            return topicList;
        }
        try {

            topicList = CassandraDataAccessHelper.getColumnNameList(TOPICS_COLUMN_FAMILY, TOPICS_ROW, keyspace);

        } catch (Exception e) {
            log.error("Error in getting the topic list", e);
            throw e;
        }

        return topicList;
    }

    /**
     * Get a list of node queues mapped for a global queue
     * 
     * @param globalQueueName
     *            name of global queue
     * @return node queues list
     * @throws Exception
     */
    public List<String> getNodeQueuesForGlobalQueue(String globalQueueName) throws Exception {
        if (keyspace == null) {
            return new ArrayList<String>();
        }
        if (!isCassandraConnectionLive) {
            log.error("Error in getting user queues for qpid queue :" + globalQueueName
                    + ". Message Store is Inaccessible.");
            return new ArrayList<String>();
        }
        try {
            List<String> userQueues = CassandraDataAccessHelper.getColumnNameList(
                    GLOBAL_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY, globalQueueName, keyspace);
            return userQueues;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in getting user queues for qpid queue :" + globalQueueName, e);
            throw e;
        }
    }

    /**
     * Get the list of node queues which has subscriptions for the specified
     * destination queue name
     * 
     * @param destinationQueueName
     *            - name of the destination
     * @return node queue list
     * @throws Exception
     * */
    public List<String> getNodeQueuesForDestinationQueue(String destinationQueueName) throws Exception {
        if (keyspace == null) {
            return new ArrayList<String>();
        }
        if (!isCassandraConnectionLive) {
            log.error("Error in getting node queues for destination queue :" + destinationQueueName
                    + ". Message Store is Inaccessible.");
            return new ArrayList<String>();
        }
        try {
            List<String> nodeQueues = CassandraDataAccessHelper.getColumnNameList(
                    DESTINATION_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY, destinationQueueName, keyspace);
            return nodeQueues;
        } catch (CassandraDataAccessException ce) {
            throw new CassandraDataAccessException("Error in getting node queues for destination queue "
                    + destinationQueueName + " as cassandra connection is down");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in getting node queues for destination queue :" + destinationQueueName, e);
            throw e;
        }
    }

    /**
     * get a list of destination queues available in the broker
     * 
     * @return list of queues
     * @throws Exception
     */
    public List<String> getDestinationQueueNames() throws Exception {

        List<String> destinationQueueNamesList = new ArrayList<String>();
        if (isInMemoryMode) {
            destinationQueueNamesList.addAll(queueMessageCountMap.keySet());
            return destinationQueueNamesList;
        }
        if (keyspace == null) {
            return new ArrayList<String>();
        }
        if (!isCassandraConnectionLive) {
            log.error("Error in getting global queues. Message Store is Inaccessible.");
        }
        try {
            destinationQueueNamesList = CassandraDataAccessHelper.getColumnNameListForCounterColumnFamily(
                    MESSAGE_COUNTERS_COLUMN_FAMILY, MESSAGE_COUNTERS_RAW_NAME, keyspace);
            return destinationQueueNamesList;
        } catch (Exception e) {
            log.error("Error in getting global queues", e);
            throw e;
        }
    }

    /**
     * Remove the topic from the topics column family when there are no
     * subscribers for that topic
     * 
     * @param topic
     *            name of topic
     */
    private void unRegisterTopic(String topic) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error in un registering topic. Cassandra Message Store is Inaccessible.");
        }
        try {
            CassandraDataAccessHelper.deleteStringColumnFromRaw(TOPICS_COLUMN_FAMILY, TOPICS_ROW, topic, keyspace);
            log.info("Removing Topic : " + topic);
        } catch (Exception e) {
            throw new AMQStoreException("Error in un registering topic", e);
        }
    }

    /**
     * Registers subscriber for topic adding the destination queue name as a
     * subscriber for the provided topic together with node queue name hashed by
     * destination queue name
     * 
     * @param topic
     *            - Topic to be subscribed
     * @param nodeQueueName
     *            - Name of the TopicDeliveryWorker queue (node queue)
     * @param subscriptionQueue
     *            - Name of the amq queue (destination queue)
     */
    public void registerSubscriberForTopic(String topic, String nodeQueueName, String subscriptionQueue) {
        if (keyspace == null) {
            return;
        }
        if (!isCassandraConnectionLive) {
            log.error("Error in registering queue for the topic. Message store is inaccessible.");
            return;
        }
        try {
            registerTopic(topic);
            CassandraDataAccessHelper.addMappingToRaw(TOPIC_SUBSCRIBERS_COLUMN_FAMILY, topic, nodeQueueName,
                    nodeQueueName, keyspace);
            CassandraDataAccessHelper.addMappingToRaw(TOPIC_SUBSCRIBER_QUEUES_COLUMN_FAMILY, topic, subscriptionQueue,
                    subscriptionQueue, keyspace);
            log.info("Registered Subscription " + subscriptionQueue + " for Topic " + topic);
        } catch (Exception e) {
            log.error("Error in registering queue for the topic", e);
        }
    }

    /**
     * Retrieving the names of the subscriptions (Destination Queue Names) which
     * are subscribed for the provided topic
     * 
     * @param topic
     *            - Name of the topic
     * @return List of names
     */
    public List<String> getRegisteredSubscribersForTopic(String topic) throws Exception {
        try {
            List<String> destinationQueueList = topicSubscribersMap.get(topic);
            return destinationQueueList;
        } catch (Exception e) {
            log.error("Error in getting registered subscribers for the topic", e);
            throw e;
        }
    }

    /**
     * Retrieving the names of the node queues of subscriptions (Queue Names)
     * which are subscribed for the provided topic
     * 
     * @param topic
     *            - Name of the topic
     * @return List of node queue names registered for topic
     */
    public List<String> getRegisteredTopicNodeQueuesForTopic(String topic) throws Exception {
        try {

            HashSet<String> topicList = new HashSet<String>();
            Enumeration<String> topics = topicNodeQueuesMap.keys();
            while (topics.hasMoreElements()) {
                String subscribedTopic = topics.nextElement();
                if (isMatching(subscribedTopic, topic)) {
                    for (String nodeQueue : topicNodeQueuesMap.get(subscribedTopic)) {
                        topicList.add(nodeQueue);
                    }
                }
            }
            List<String> queueList = new ArrayList<String>(topicList);
            return queueList;
        } catch (Exception e) {
            log.error("Error in getting registered subscribers for the topic", e);
            throw e;
        }

    }

    public boolean isMatching(String binding, String topic) {
        boolean isMatching = false;
        if (binding.equals(topic)) {
            isMatching = true;
        } else if (binding.indexOf(".#") > 1) {
            String p = binding.substring(0, binding.indexOf(".#"));
            Pattern pattern = Pattern.compile(p + ".*");
            Matcher matcher = pattern.matcher(topic);
            isMatching = matcher.matches();
        } else if (binding.indexOf(".*") > 1) {
            String p = binding.substring(0, binding.indexOf(".*"));
            Pattern pattern = Pattern.compile("^" + p + "[.][^.]+$");
            Matcher matcher = pattern.matcher(topic);
            isMatching = matcher.matches();
        }
        return isMatching;
    }

    /**
     * Removing the subscription entry from the subscribers list for the topic
     * 
     * @param topic
     *            - Name of the topic
     * @param destinationQueueName
     *            - Queue name to be removed
     */
    public void unRegisterQueueFromTopic(String topic, String destinationQueueName) {

        try {
            if (log.isDebugEnabled()) {
                log.debug(" removing queue = " + destinationQueueName + " from topic =" + topic);
            }
            if (!isCassandraConnectionLive) {
                log.error("Error in un registering queue from the topic. Message store in inaccessible.");
                return;
            }
            // we update message store, no need to update in memory map as it
            // will be reloaded
            CassandraDataAccessHelper.deleteStringColumnFromRaw(TOPIC_SUBSCRIBER_QUEUES_COLUMN_FAMILY, topic,
                    destinationQueueName, keyspace);

            // no need to log removing subscription from direct exchange
            // (internal change)
            if (!topic.startsWith("tmp_")) {
                log.info("Removing Subscription " + destinationQueueName + " from Topic " + topic);
            }

            if ((getRegisteredSubscribersForTopic(topic) != null)
                    && (getRegisteredSubscribersForTopic(topic).size() == 0)) {
                unRegisterTopic(topic);
                topicSubscribersMap.remove(topic);
            }
            if ((getRegisteredTopicNodeQueuesForTopic(topic) != null)
                    && (getRegisteredTopicNodeQueuesForTopic(topic).size() == 0)) {
                topicNodeQueuesMap.remove(topic);
            }
        } catch (CassandraDataAccessException ce) {
            log.error("Error in un registering queue from the topic as Cassandra storage is down");

        } catch (Exception e) {
            log.error("Error in un registering queue from the topic", e);
        }
    }

    /**
     * Adding message id to the node queue for topic messages
     * 
     * @param nodeQueueName
     *            - Name of the node queue
     * @param messageId
     *            - Message ID
     */
    private void addMessageIdToSubscriberQueue(String nodeQueueName, long messageId) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error in adding message Id to subscriber queue. Message store is Inaccessible.");
            return;
        }
        try {
            long columnName = messageId;
            long columnValue = messageId;
            CassandraDataAccessHelper.addLongContentToRow(PUB_SUB_MESSAGE_IDS_COLUMN_FAMILY, nodeQueueName, columnName,
                    columnValue, keyspace);

        } catch (Exception e) {
            throw new AMQStoreException("Error in adding message Id to subscriber queue", e);
        }
    }

    /**
     * Add any message property to Cassandra Storage
     * 
     * @param propertyName
     *            name of property (defined constant at andes constants class)
     * @param messageId
     *            message Id
     * @param value
     *            value of the property
     */
    public void addMessagePropertyToCassandra(String propertyName, long messageId, String value) {
        try {
            if (propertyName.equals(AndesConstants.MESSAGE_EXPIRATION_PROPERTY)) {
                Mutator<String> propertyMutator = HFactory.createMutator(keyspace, stringSerializer);
                CassandraDataAccessHelper.addStringContentToRow(MESSAGE_PROPERTIES_COLUMN_FAMILY,
                        MESSAGE_EXPIRATION_PROPERTY_RAW_NAME, messageId, value, propertyMutator, true);
            }
        } catch (CassandraDataAccessException e) {
            log.error("Error while writing message properties to Cassandra", e);
        }
    }

    /**
     * Search and return message ids of the provided queue beginning from the
     * provided message id to above 1000 messages for topic messages
     * 
     * @param nodeQueueName
     *            - Name of the node queue
     * @param lastDeliveredMid
     *            - Last delivered message Id
     * @return list of message IDs
     */
    private List<Long> getPendingMessageIds(String nodeQueueName, long lastDeliveredMid) {
        List<Long> messageIDList = new ArrayList<Long>();
        if (!isCassandraConnectionLive) {
            log.error("Error in retriving message ids of the queue:" + nodeQueueName
                    + ". Message store is inaccessible.");
            return messageIDList;
        }
        try {
            SliceQuery<String, Long, Long> sliceQuery = HFactory.createSliceQuery(keyspace, stringSerializer,
                    longSerializer, longSerializer);
            sliceQuery.setKey(nodeQueueName);
            sliceQuery.setColumnFamily(PUB_SUB_MESSAGE_IDS_COLUMN_FAMILY);
            sliceQuery.setRange(lastDeliveredMid, Long.MAX_VALUE, false, 1000);

            QueryResult<ColumnSlice<Long, Long>> result = sliceQuery.execute();
            ColumnSlice<Long, Long> columnSlice = result.get();
            for (HColumn<Long, Long> column : columnSlice.getColumns()) {
                messageIDList.add(column.getValue());
            }

        } catch (Exception e) {
            log.error("Error in retriving message ids of the queue", e);
        }

        return messageIDList;
    }

    /**
     * Remove delivered messages from the provided queue for topic messages
     * 
     * @param messageIdsToBeRemoved
     *            - List of delivered message ids to be removed
     * @param nodeQueueName
     *            - name of the topic node queue
     */
    public void removeDeliveredTopicMessageIds(List<Long> messageIdsToBeRemoved, String nodeQueueName)
            throws AMQStoreException {

        if (isInMemoryMode) {
            HashSet<Long> unackedMessageIDsSet = sentButNotAckedTopicMessageMap.get(nodeQueueName);
            if (unackedMessageIDsSet != null) {
                for (Long mid : messageIdsToBeRemoved) {
                    unackedMessageIDsSet.remove(mid);
                    removePendingTopicMessageId(mid);
                }
            }

        } else {
            if (!isCassandraConnectionLive) {
                log.error("Error in removing message ids from subscriber queue. Message Store is inaccessible");
                return;
            }
            try {
                Mutator<String> messageIdMutator = HFactory.createMutator(keyspace, stringSerializer);
                for (Long mid : messageIdsToBeRemoved) {
                    CassandraDataAccessHelper.deleteLongColumnFromRaw(PUB_SUB_MESSAGE_IDS_COLUMN_FAMILY, nodeQueueName,
                            mid, messageIdMutator, false);
                    if (log.isDebugEnabled()) {
                        log.debug(" removing mid = " + mid + " from =" + nodeQueueName);
                    }
                }
                messageIdMutator.execute();
            } catch (Exception e) {
                throw new AMQStoreException("Error in removing message ids from subscriber queue", e);
            }
        }
    }

    /**
     * sync bindings. Called when a subscription has changed in the cluster
     * 
     * @param vhcs
     *            virtualHostConfigSynchronizer
     */
    public void synchBindings(VirtualHostConfigSynchronizer vhcs) {
        try {

            if (!isCassandraConnectionLive) {
                log.error("Error in synchronizing bindings. Message store is unreachable.");
                return;
            }

            Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

            RangeSlicesQuery<String, String, String> rangeSliceQuery = HFactory.createRangeSlicesQuery(keyspace,
                    stringSerializer, stringSerializer, stringSerializer);
            rangeSliceQuery.setKeys("", "");
            rangeSliceQuery.setColumnFamily(BINDING_COLUMN_FAMILY);
            rangeSliceQuery.setRange("", "", false, 100);

            QueryResult<OrderedRows<String, String, String>> result = rangeSliceQuery.execute();
            OrderedRows<String, String, String> orderedRows = result.get();
            List<Row<String, String, String>> rowArrayList = orderedRows.getList();
            for (Row<String, String, String> row : rowArrayList) {
                String exchange = row.getKey();
                ColumnSlice<String, String> columnSlice = row.getColumnSlice();
                for (Object column : columnSlice.getColumns()) {
                    if (column instanceof HColumn) {
                        String columnName = ((HColumn<String, String>) column).getName();
                        String value = ((HColumn<String, String>) column).getValue();
                        vhcs.binding(exchange, value, columnName, null);
                    }
                }
            }
        } catch (NumberFormatException e) {
            log.error("Error in synchronizing bindings", e);
        }

    }

    /**
     * recover the bindings
     * 
     * @param brh
     *            BindingRecoveryHandler
     * @param exchanges
     *            exchanges
     * @throws Exception
     */
    public void recoverBindings(ConfigurationRecoveryHandler.BindingRecoveryHandler brh, List<String> exchanges)
            throws Exception {

        if (!isCassandraConnectionLive) {
            log.error("Error occurred when recovering bindings. Message store is inaccessible.");
        }
        try {

            RangeSlicesQuery<String, String, String> rangeSliceQuery = HFactory.createRangeSlicesQuery(keyspace,
                    stringSerializer, stringSerializer, stringSerializer);
            rangeSliceQuery.setKeys("", "");
            rangeSliceQuery.setColumnFamily(BINDING_COLUMN_FAMILY);
            rangeSliceQuery.setRange("", "", false, 100);

            QueryResult<OrderedRows<String, String, String>> result = rangeSliceQuery.execute();
            OrderedRows<String, String, String> orderedRows = result.get();
            List<Row<String, String, String>> rowArrayList = orderedRows.getList();
            for (Row<String, String, String> row : rowArrayList) {
                String exchange = row.getKey();
                ColumnSlice<String, String> columnSlice = row.getColumnSlice();
                for (Object column : columnSlice.getColumns()) {
                    if (column instanceof HColumn) {
                        String columnName = ((HColumn<String, String>) column).getName();
                        String value = ((HColumn<String, String>) column).getValue();
                        brh.binding(exchange, value, columnName, null);

                    }
                }
            }
        } catch (NumberFormatException e) {
            log.error("Number formatting error occurred when recovering bindings", e);
        }

    }

    /**
     * get binding names for the given routing key
     * 
     * @param routingKey
     *            routing key
     * @return list of bindings
     */
    private List<String> getBindings(String routingKey) {

        if (!isCassandraConnectionLive) {
            log.error("Error in getting bindings. Message store is inaccessible.");
            return new ArrayList<String>();
        }
        List<String> bindings = new ArrayList<String>();
        try {

            Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

            // Retrieving multiple rows with Range Slice Query
            RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace,
                    stringSerializer, stringSerializer, stringSerializer);
            rangeSlicesQuery.setKeys("DirectExchange", "DirectExchange");
            rangeSlicesQuery.setColumnFamily(BINDING_COLUMN_FAMILY);
            rangeSlicesQuery.setRange(routingKey, "", false, 10);

            QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
            OrderedRows<String, String, String> columnSlice = result.get();
            List<Row<String, String, String>> rows = columnSlice.getList();

            for (Object column : columnSlice.getList().get(0).getColumnSlice().getColumns()) {
                if (column instanceof HColumn) {
                    String columnName = ((HColumn<String, String>) column).getName();
                    String value = ((HColumn<String, String>) column).getValue();
                    String stringValue = new String(value);
                    bindings.add(stringValue);

                }
            }
        } catch (Exception e) {
            log.error("Error in getting bindings", e);
        }
        return bindings;
    }

    /**
     * recover messages from the store when message store initializes
     * 
     * @param recoveryHandler
     *            recovery handler
     */
    private void recoverMessages(MessageStoreRecoveryHandler recoveryHandler) {

        StorableMessageMetaData metaData = null;
        long maxId = 0;
        if (!isCassandraConnectionLive) {
            log.error("Error in recovering bindings. Message store is inaccessible.");
            return;
        }
        try {
            LongSerializer ls = LongSerializer.get();
            BytesArraySerializer bs = BytesArraySerializer.get();

            SliceQuery sliceQuery = HFactory.createSliceQuery(keyspace, stringSerializer, ls, bs);
            sliceQuery.setColumnFamily(QMD_COLUMN_FAMILY);
            sliceQuery.setKey(QMD_ROW_NAME);
            sliceQuery.setRange(Long.parseLong("0"), Long.MAX_VALUE, false, 10000);

            QueryResult<ColumnSlice<Long, byte[]>> result = sliceQuery.execute();

            ColumnSlice<Long, byte[]> columnSlice = result.get();

            List<HColumn<Long, byte[]>> columnList = columnSlice.getColumns();

            for (HColumn<Long, byte[]> column : columnList) {

                long key = column.getName();
                if (key > maxId) {
                    maxId = key;
                }
                byte[] dataAsBytes = column.getValue();

                ByteBuffer buf = ByteBuffer.wrap(dataAsBytes);
                buf.position(1);
                buf = buf.slice();
                MessageMetaDataType type = MessageMetaDataType.values()[dataAsBytes[0]];
                metaData = type.getFactory().createMetaData(buf);
            }
            _messageId.set(maxId);
        } catch (Exception e) {
            log.error("Error in recovering bindings", e);
        }
    }

    /**
     * Synschronize queues
     * 
     * @param vhcs
     *            virtual host config sinchronizer. Called when a subscription
     *            has changed in cluster
     * @throws Exception
     */
    public void synchQueues(VirtualHostConfigSynchronizer vhcs) throws Exception {

        if (!isCassandraConnectionLive) {
            log.error("Error in queue synchronization. Message store is inaccessble.");
        }
        try {
            // Retrieving multiple rows with Range Slice Query
            ColumnSlice<String, String> columnSlice = CassandraDataAccessHelper.getStringTypeColumnsInARow(
                    QUEUE_DETAILS_ROW, QUEUE_DETAILS_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);
            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    String columnName = ((HColumn<String, String>) column).getName();
                    String value = ((HColumn<String, String>) column).getValue();
                    Iterable<String> results = pipeSplitter.split(value);
                    Iterator<String> it = results.iterator();
                    String owner = it.next();
                    boolean isExclusive = Boolean.parseBoolean(it.next());
                    vhcs.queue(columnName, owner, isExclusive, null);
                }
            }
        } catch (Exception e) {
            throw new AMQStoreException("Error in queue synchronization", e);
        }
    }

    /**
     * at recovery load queues which were there when shutting down
     * 
     * @param qrh
     *            Queue Recovery Handler
     * @throws Exception
     */
    public void loadQueues(ConfigurationRecoveryHandler.QueueRecoveryHandler qrh) throws Exception {

        if (!isCassandraConnectionLive) {
            log.error("Error in loading queues. Message store is inaccessible.");
            return;
        }
        try {

            // Retriving multiple rows with Range Slice Query
            ColumnSlice<String, String> columnSlice = CassandraDataAccessHelper.getStringTypeColumnsInARow(
                    QUEUE_DETAILS_ROW, QUEUE_DETAILS_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);
            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    String columnName = ((HColumn<String, String>) column).getName();
                    String value = ((HColumn<String, String>) column).getValue();
                    String[] valuesFields = value.split("\\|");
                    String owner = valuesFields[1];
                    boolean isExclusive = Boolean.parseBoolean(valuesFields[2]);
                    qrh.queue(columnName, owner, isExclusive, null);
                    if (isInMemoryMode) {
                        queueMessageCountMap.put(columnName, 0L);
                    }
                }
            }

        } catch (Exception e) {
            throw new AMQStoreException("Error in loading queues", e);
        }

    }

    /**
     * get destination queues active in cluster
     * 
     * @return destination queues list
     * @throws AMQStoreException
     */
    public List<String> getDestinationQueues() throws AMQStoreException {

        List<String> destinationQueues = new ArrayList<String>();
        if (!isCassandraConnectionLive) {
            log.error("Error in loading queues. Message store is inaccessible.");
            return destinationQueues;
        }
        try {

            // Retriving multiple rows with Range Slice Query
            ColumnSlice<String, String> columnSlice = CassandraDataAccessHelper.getStringTypeColumnsInARow(
                    QUEUE_DETAILS_ROW, QUEUE_DETAILS_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);
            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    String columnName = ((HColumn<String, String>) column).getName();
                    destinationQueues.add(columnName);
                }
            }
            return destinationQueues;
        } catch (Exception e) {
            throw new AMQStoreException("Error in loading queues", e);
        }
    }

    /**
     * Add Global Queue to node Queue Mapping. We will add the global queue as
     * well. When a subscription happens this method should be called
     * 
     * @param globalQueueName
     *            global queue name
     * @param nodeQueueName
     *            node queue name
     */
    public void addNodeQueueToGlobalQueue(String globalQueueName, String nodeQueueName) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error in adding user queue to global queue. Message store is inaccessible.");
            return;
        }
        try {
            Mutator<String> qqMutator = HFactory.createMutator(keyspace, stringSerializer);
            CassandraDataAccessHelper.addMappingToRaw(GLOBAL_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY, globalQueueName,
                    nodeQueueName, nodeQueueName, qqMutator, false);
            // add the global queue as well
            CassandraDataAccessHelper.addMappingToRaw(GLOBAL_QUEUE_LIST_COLUMN_FAMILY, GLOBAL_QUEUE_LIST_ROW,
                    globalQueueName, globalQueueName, qqMutator, true);
        } catch (Exception e) {
            throw new AMQStoreException("Error in adding user queue to global queue", e);
        }
    }

    /**
     * Add Destination Queue to node Queue Mapping. When a subscription happens
     * this method should be called
     * 
     * @param destinationQueueName
     *            global queue name
     * @param nodeQueueName
     *            node queue name
     */
    public void addNodeQueueToDestinationQueue(String destinationQueueName, String nodeQueueName)
            throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error in adding node queue to destination queue. Message store is inaccessible.");
            return;
        }
        try {
            Mutator<String> qqMutator = HFactory.createMutator(keyspace, stringSerializer);
            CassandraDataAccessHelper.addMappingToRaw(DESTINATION_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY,
                    destinationQueueName, nodeQueueName, nodeQueueName, qqMutator, true);
        } catch (Exception e) {
            throw new AMQStoreException("Error in adding node queue to destination queue mapping", e);
        }
    }

    /**
     * remove a node queue from a destination queue mapping
     * 
     * @param destinationQueue
     *            destination queue name
     */
    public void removeNodeQueueFromDestinationQueue(String destinationQueue, String nodeQueueName) {

        if (!isCassandraConnectionLive) {
            log.error("Error in removing node queue from global queue. Message store is inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.deleteStringColumnFromRaw(DESTINATION_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY,
                    destinationQueue.trim(), nodeQueueName, keyspace);
        } catch (Exception e) {
            log.error("Error in removing node queue from global queue", e);
        }
    }

    /**
     * remove messages from a given node queue and copy them to relevant global
     * queues
     * 
     * @param nodeQueueName
     *            name of node queue to remove messages from
     */
    public void removeMessagesFromNodeQueueAndCopyToGlobalQueues(String nodeQueueName) {
        if (!isCassandraConnectionLive) {
            log.error("Error when moving messages from node queue to global queues. Message store is inaccessible.");
            return;
        }
        try {

            CassandraMessageStore messageStore = ClusterResourceHolder.getInstance().getCassandraMessageStore();

            // remove this node queue from all destination queues
            List<String> destinationQueueList = this.getDestinationQueues();
            for (String destinationQueue : destinationQueueList) {
                removeNodeQueueFromDestinationQueue(destinationQueue, nodeQueueName);
            }

            long lastProcessedMessageID = 0;
            List<CassandraQueueMessage> messages = messageStore.getMessagesFromNodeQueue(nodeQueueName, 40,
                    lastProcessedMessageID);
            while (messages.size() != 0) {
                for (CassandraQueueMessage msg : messages) {
                    lastProcessedMessageID = msg.getMessageId();
                    messageStore.removeMessageFromNodeQueue(nodeQueueName, msg.getMessageId());

                    try {
                        // when adding back to global queue we mark it as an
                        // message that was already came in (as un-acked)
                        // we do not evaluate if message addressed queue is
                        // bound to topics as it is not used. Just pass false
                        // for that.
                        // for message properties just pass default values as
                        // they will not be written to Cassandra again.
                        // we should add it to relevant globalQueue also
                        String destinationQueueName = msg.getDestinationQueueName();
                        String globalQueueName = AndesUtils.getGlobalQueueNameForDestinationQueue(destinationQueueName);
                        messageStore.addMessageToGlobalQueue(globalQueueName, msg.getNodeQueue(), msg.getMessageId(),
                                msg.getMessage(), false, 0, false);
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
                messages = messageStore.getMessagesFromNodeQueue(nodeQueueName, 40, lastProcessedMessageID);
            }
        } catch (Exception e) {
            log.error("Error in moving messages from node queue to global queue", e);
        }
    }

    /**
     * remove a node queue from a global queue mapping
     * 
     * @param globalQueueName
     *            global queue name
     */
    public void removeNodeQueueFromGlobalQueue(String globalQueueName) {

        if (!isCassandraConnectionLive) {
            log.error("Error in removing node queue from global queue. Message store is inaccessible.");
            return;
        }
        try {
            ClusterManager clusterManager = ClusterResourceHolder.getInstance().getClusterManager();
            String userQueueName = globalQueueName + "_" + clusterManager.getNodeId();
            CassandraDataAccessHelper.deleteStringColumnFromRaw(GLOBAL_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY,
                    globalQueueName.trim(), userQueueName, keyspace);
        } catch (Exception e) {
            log.error("Error in removing node queue from global queue", e);
        }
    }

    /**
     * sync destination queues (AMQ queues) bound with given topic with database
     * 
     * @param topic
     *            name of topic
     * @throws Exception
     */
    public void syncTopicSubscriptionsWithDatabase(String topic) throws Exception {

        if (!isCassandraConnectionLive) {
            log.error("Error Synchronizing subscribers for topic. Message store is inaccessible.");
            return;
        }
        if (topic != null) {
            ArrayList<String> subscriberQueues = new ArrayList<String>();
            List<String> subscribers = CassandraDataAccessHelper.getColumnNameList(
                    TOPIC_SUBSCRIBER_QUEUES_COLUMN_FAMILY, topic, keyspace);
            for (String subscriber : subscribers) {
                subscriberQueues.add(subscriber);
            }
            topicSubscribersMap.remove(topic);
            topicSubscribersMap.put(topic, subscriberQueues);
        }
        if (log.isDebugEnabled()) {
            log.debug("Synchronizing subscribers for topic" + topic);
        }
    }

    /**
     * Update node queues hashed for each subscriber queue for topics with data
     * at TOPIC_SUBSCRIBERS_COLUMN_FAMILY column family in cassandra
     * 
     * @param topic
     *            name of topic
     * @throws Exception
     */
    public void syncTopicNodeQueuesWithDatabase(String topic) throws Exception {

        if (!isCassandraConnectionLive) {
            log.error("Error Synchronizing subscribers for topic. Message store is inaccessible.");
            return;
        }
        if (topic != null) {
            ArrayList<String> topicNodeQueuesList = new ArrayList<String>();
            List<String> topicNodeQueues = CassandraDataAccessHelper.getColumnNameList(TOPIC_SUBSCRIBERS_COLUMN_FAMILY,
                    topic, keyspace);
            for (String topicNodeQueue : topicNodeQueues) {
                topicNodeQueuesList.add(topicNodeQueue);
            }
            topicNodeQueuesMap.remove(topic);
            topicNodeQueuesMap.put(topic, topicNodeQueuesList);
        }
        if (log.isDebugEnabled()) {
            log.debug("Synchronizing subscribers for topic" + topic);
        }
    }

    @Override
    /**
     * close and stop tasks running under cassandra message store
     */
    public void close() throws Exception {
        if (!ClusterResourceHolder.getInstance().getClusterManager().isClusteringEnabled()) {
            ClusterResourceHolder.getInstance().getClusterManager().shutDownMyNode();
        }
        if (ClusterResourceHolder.getInstance().getClusterManager().isClusteringEnabled()) {
            deleteNodeData("" + ClusterResourceHolder.getInstance().getClusterManager().getNodeId());
        }
        if (messageContentRemovalTask != null && messageContentRemovalTask.isRunning()) {
            messageContentRemovalTask.setRunning(false);
        }

        if (pubSubMessageContentRemoverTask != null && pubSubMessageContentRemoverTask.isRunning()) {
            pubSubMessageContentRemoverTask.setRunning(false);
        }
        log.info("Stopping all current queue message publishers");
        ClusteringEnabledSubscriptionManager csm = ClusterResourceHolder.getInstance().getSubscriptionManager();
        if (csm != null) {
            csm.stopAllMessageFlushers();
        }

        log.info("Stopping all current topic message publishers");
        CassandraTopicPublisherManager stpm = ClusterResourceHolder.getInstance().getCassandraTopicPublisherManager();
        if (stpm != null && stpm.isActive()) {
            stpm.stop();
        }

        log.info("Stopping all global queue workers locally");
        ClusterManager cm = ClusterResourceHolder.getInstance().getClusterManager();
        if (cm != null) {
            GlobalQueueManager gqm = cm.getGlobalQueueManager();
            if (gqm != null) {
                gqm.stopAllQueueWorkersLocally();
            }
        }
    }

    @Override
    public <T extends StorableMessageMetaData> StoredMessage<T> addMessage(T metaData) {
        long mid = messageIdGenerator.getNextId();
        if (log.isDebugEnabled()) {
            log.debug("MessageID generated:" + mid);
        }
        return new StoredCassandraMessage(mid, metaData);
    }

    @Override
    public boolean isPersistent() {
        return false;
    }

    @Override
    public void configureConfigStore(String name, ConfigurationRecoveryHandler recoveryHandler,

    Configuration config, LogSubject logSubject) throws Exception {
        if (!configured) {
            performCommonConfiguration(config);
            recover(recoveryHandler);

            ClusterResourceHolder resourceHolder = ClusterResourceHolder.getInstance();
            CassandraTopicPublisherManager cassandraTopicPublisherManager = resourceHolder
                    .getCassandraTopicPublisherManager();
            if (cassandraTopicPublisherManager == null) {
                cassandraTopicPublisherManager = new CassandraTopicPublisherManager();
                resourceHolder.setCassandraTopicPublisherManager(cassandraTopicPublisherManager);
            }
            cassandraTopicPublisherManager.init();
            cassandraTopicPublisherManager.start();
        }

    }

    @Override
    /**
     * Create a new exchange adding it to the store
     */
    public void createExchange(Exchange exchange) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error in creating exchange " + exchange.getName() + ". Message store is inaccessible.");
            return;
        }
        try {
            String name = exchange.getName();
            String type = exchange.getTypeShortString().asString();
            Short autoDelete = exchange.isAutoDelete() ? (short) 1 : (short) 0;
            String value = name + "|" + type + "|" + autoDelete;
            CassandraDataAccessHelper.addMappingToRaw(EXCHANGE_COLUMN_FAMILY, EXCHANGE_ROW, name, value, keyspace);
        } catch (Exception e) {
            throw new AMQStoreException("Error in creating exchange " + exchange.getName(), e);
        }
    }

    /**
     * Load exchanges at a recovery from the permanent cassandra storage
     * 
     * @param erh
     *            Exchange Recovery Handler
     * @return list of exchanges
     * @throws Exception
     */
    public List<String> loadExchanges(ConfigurationRecoveryHandler.ExchangeRecoveryHandler erh) throws Exception {

        List<String> exchangeNames = new ArrayList<String>();
        if (!isCassandraConnectionLive) {
            log.error("Error in loading exchanges. Message store is inaccessible.");
            return exchangeNames;
        }
        try {
            ColumnSlice<String, String> columnSlice = CassandraDataAccessHelper.getStringTypeColumnsInARow(
                    EXCHANGE_ROW, EXCHANGE_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);
            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    String columnName = ((HColumn<String, String>) column).getName();
                    String value = ((HColumn<String, String>) column).getValue();
                    String[] valuesFields = value.split("|");
                    String type = valuesFields[1];
                    short autoDelete = Short.parseShort(valuesFields[2]);
                    exchangeNames.add(columnName);
                    erh.exchange(columnName, type, autoDelete != 0);

                }
            }
        } catch (Exception e) {
            throw new AMQStoreException("Error in loading exchanges", e);
        }

        return exchangeNames;
    }

    /**
     * Sync exchanges of cluster with cassandra storage. Called when
     * subscriptions in cluster has changed.
     * 
     * @param vhcs
     * @return
     * @throws Exception
     */
    public List<String> synchExchanges(VirtualHostConfigSynchronizer vhcs) throws Exception {

        List<String> exchangeNames = new ArrayList<String>();
        if (!isCassandraConnectionLive) {
            log.error("Error in synchronizing exchanges. Message store is inaccessible.");
            return exchangeNames;
        }
        try {
            // Retriving multiple rows with Range Slice Query
            ColumnSlice<String, String> columnSlice = CassandraDataAccessHelper.getStringTypeColumnsInARow(
                    EXCHANGE_ROW, EXCHANGE_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);
            for (Object column : columnSlice.getColumns()) {
                if (column instanceof HColumn) {
                    String columnName = ((HColumn<String, String>) column).getName();
                    String value = ((HColumn<String, String>) column).getValue();
                    String[] valuesFields = value.split("|");
                    String type = valuesFields[1];
                    short autoDelete = Short.parseShort(valuesFields[2]);
                    exchangeNames.add(columnName);
                    vhcs.exchange(columnName, type, autoDelete != 0);

                }
            }
        } catch (Exception e) {
            throw new AMQStoreException("Error in synchronizing exchanges", e);
        }

        return exchangeNames;
    }

    @Override
    public void removeExchange(Exchange exchange) throws AMQStoreException {
        throw new UnsupportedOperationException("removeExchange function is unsupported");
    }

    @Override
    /**
     * bind a queue to an exchange in durable subscriptions
     */
    public void bindQueue(Exchange exchange, AMQShortString routingKey, AMQQueue queue, FieldTable args)
            throws AMQStoreException {

        try {
            addBinding(exchange, queue, routingKey.asString());
        } catch (CassandraDataAccessException e) {
            throw new AMQStoreException("Error adding Binding details to cassandra store", e);
        }

    }

    @Override
    public void unbindQueue(Exchange exchange, AMQShortString routingKey, AMQQueue queue, FieldTable args)
            throws AMQStoreException {
        try {
            removeBinding(exchange, queue, routingKey.asString());
        } catch (CassandraDataAccessException e) {
            throw new AMQStoreException("Error removing binding details from cassandra store", e);
        }
    }

    @Override
    public void createQueue(AMQQueue queue, FieldTable arguments) throws AMQStoreException {
        createQueue(queue);
    }

    public void createQueue(AMQQueue queue) {

        if (!isCassandraConnectionLive) {
            log.error("Error While creating queue" + queue.getName() + "Message store is inaccessible.");
            return;
        }
        try {
            String owner = queue.getOwner() == null ? null : queue.getOwner().toString();
            String value = queue.getNameShortString().toString() + "|" + owner + "|"
                    + (queue.isExclusive() ? "true" : "false");
            CassandraDataAccessHelper.addMappingToRaw(QUEUE_DETAILS_COLUMN_FAMILY, QUEUE_DETAILS_ROW, queue
                    .getNameShortString().toString(), value, keyspace);
        } catch (Exception e) {
            throw new RuntimeException("Error While creating queue" + queue.getName(), e);
        }
    }

    /**
     * Add Node details to cassandra
     * 
     * @param nodeId
     *            node id
     * @param data
     *            node data
     */
    public void addNodeDetails(String nodeId, String data) {
        if (!isCassandraConnectionLive) {
            log.error("Error writing Node details to cassandra database. Message store is inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.addMappingToRaw(NODE_DETAIL_COLUMN_FAMILY, NODE_DETAIL_ROW, nodeId, data,
                    keyspace);
        } catch (CassandraDataAccessException e) {
            throw new RuntimeException("Error writing Node details to cassandra database", e);
        }
    }

    /**
     * Get Node data for a given node
     * 
     * @param nodeId
     *            node id assigned by the cluster manager
     * @return Node data
     */
    public String getNodeData(String nodeId) {
        if (!isCassandraConnectionLive) {
            log.error("Error accessing Node details to cassandra database. Message store is inaccessible.");
            return null;
        }
        try {

            ColumnSlice<String, String> values = CassandraDataAccessHelper.getStringTypeColumnsInARow(NODE_DETAIL_ROW,
                    NODE_DETAIL_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);

            Object column = values.getColumnByName(nodeId);

            String columnName = ((HColumn<String, String>) column).getName();
            String value = ((HColumn<String, String>) column).getValue();
            return value;

        } catch (CassandraDataAccessException e) {
            throw new RuntimeException("Error accessing Node details to cassandra database");
        }
    }

    /**
     * Returns list of all Node ids stored as Cluster nodes in the cassandra
     * database
     * 
     * @return node id list
     */
    public List<String> getStoredNodeIDList() {

        if (!isCassandraConnectionLive) {
            log.error("Error accessing Node details to cassandra database. Message store is inaccessible.");
            return new ArrayList<String>();
        }
        try {
            ColumnSlice<String, String> values = CassandraDataAccessHelper.getStringTypeColumnsInARow(NODE_DETAIL_ROW,
                    NODE_DETAIL_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);

            List<HColumn<String, String>> columns = values.getColumns();
            List<String> nodes = new ArrayList<String>();
            for (HColumn<String, String> column : columns) {
                nodes.add(column.getName());
            }

            return nodes;

        } catch (CassandraDataAccessException e) {
            throw new RuntimeException("Error accessing Node details to cassandra database");
        }
    }

    /**
     * Get node details (id, bindIPAddress) of nodes in cluster
     * 
     * @return list of node detail
     */
    public HashMap<String, String> getStoredNodeDetails() {

        HashMap<String, String> nodeDetails = new HashMap<String, String>();
        if (!isCassandraConnectionLive) {
            log.error("Error accessing Node details to cassandra database. Message store is inaccessible.");
            return nodeDetails;
        }
        try {
            ColumnSlice<String, String> values = CassandraDataAccessHelper.getStringTypeColumnsInARow(NODE_DETAIL_ROW,
                    NODE_DETAIL_COLUMN_FAMILY, keyspace, Integer.MAX_VALUE);

            List<HColumn<String, String>> columns = values.getColumns();
            for (HColumn<String, String> column : columns) {
                nodeDetails.put(column.getName(), column.getValue());
            }

            return nodeDetails;

        } catch (CassandraDataAccessException e) {
            throw new RuntimeException("Error accessing Node details from cassandra database");
        }
    }

    /**
     * Remove node data from cassandra. Called when this instance is closed or
     * via node existence listener
     * 
     * @param nodeId
     */
    public void deleteNodeData(String nodeId) {

        if (!isCassandraConnectionLive) {
            log.error("Error accessing Node details to cassandra database. Message store is inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.deleteStringColumnFromRaw(NODE_DETAIL_COLUMN_FAMILY, NODE_DETAIL_ROW, nodeId,
                    keyspace);
        } catch (CassandraDataAccessException e) {
            throw new RuntimeException("Error accessing Node details to cassandra database");
        }
    }

    /**
     * Create a Global Queue in Cassandra MessageStore
     * 
     * @param globalQueueName
     *            name of global queue
     */
    public void createGlobalQueue(String globalQueueName) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error while adding Global Queue to Cassandra message store. Message store is inaccessible.");
            return;
        }
        try {
            CassandraDataAccessHelper.addMappingToRaw(GLOBAL_QUEUE_LIST_COLUMN_FAMILY, GLOBAL_QUEUE_LIST_ROW,
                    globalQueueName, globalQueueName, keyspace);
            log.info("Created Queue : " + globalQueueName);
        } catch (CassandraDataAccessException e) {
            throw new AMQStoreException("Error while adding Global Queue to Cassandra message store", e);
        }

    }

    @Override
    /**
     * Remove destination queue detail from cassandra
     */
    public void removeQueue(AMQQueue queue) throws AMQStoreException {

        // avoiding cassandra alive check, as error should be shown in UI.
        try {
            String queueName = queue.getNameShortString().toString();
            CassandraDataAccessHelper.deleteStringColumnFromRaw(QUEUE_DETAILS_COLUMN_FAMILY, QUEUE_DETAILS_ROW,
                    queueName, keyspace);
        } catch (CassandraDataAccessException e) {
            throw new AMQStoreException("Error while deleting queue : " + queue, e);
        }

    }

    /**
     * Removes a global queue from Cassandra Message Store This will remove the
     * Global queue and associated Node queues from the Stores
     * 
     * 
     * @param globalQueueName
     *            Global QueueName
     * @throws AMQStoreException
     *             If Error occurs while deleting the queues
     */
    public void removeGlobalQueueEntryWithAssociatedNodeQueues(String globalQueueName) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error while removing Global Queue" + globalQueueName + ". Message store is inaccessible.");
            return;
        }
        try {

            List<String> nodeQueues = getNodeQueuesForGlobalQueue(globalQueueName);

            for (String userQ : nodeQueues) {
                CassandraDataAccessHelper.deleteStringColumnFromRaw(GLOBAL_QUEUE_TO_NODE_QUEUE_COLUMN_FAMILY,
                        globalQueueName, userQ, keyspace);
            }

            CassandraDataAccessHelper.deleteStringColumnFromRaw(GLOBAL_QUEUE_LIST_COLUMN_FAMILY, GLOBAL_QUEUE_LIST_ROW,
                    globalQueueName, keyspace);

        } catch (Exception e) {
            throw new AMQStoreException("Error while removing Global Queue  : " + globalQueueName, e);
        }
    }

    /**
     * This will check if cassandra connection is live in an exponential
     * back-off way
     */
    public void checkCassandraConnection() {
        Thread cassandraConnectionCheckerThread = new Thread(new Runnable() {
            public void run() {
                int retriedCount = 0;
                while (true) {
                    try {
                        if (cluster.describeClusterName() != null) {
                            boolean previousState = isCassandraConnectionLive;
                            isCassandraConnectionLive = true;
                            retriedCount = 0;
                            if (previousState == false) {
                                // start back all tasks accessing cassandra
                                log.info("Cassandra Message Store is alive....");

                                log.info("Starting all current queue message publishers");
                                ClusteringEnabledSubscriptionManager csm = ClusterResourceHolder.getInstance()
                                        .getSubscriptionManager();
                                if (csm != null) {
                                    csm.startAllMessageFlushers();
                                }
                                log.info("Starting all current topic message publishers");
                                CassandraTopicPublisherManager stpm = ClusterResourceHolder.getInstance()
                                        .getCassandraTopicPublisherManager();
                                if (stpm != null && !stpm.isActive()) {
                                    stpm.start();
                                }

                                log.info("Starting all available Global Queue Workers");
                                ClusterManager cm = ClusterResourceHolder.getInstance().getClusterManager();
                                if (cm != null) {
                                    GlobalQueueManager gqm = cm.getGlobalQueueManager();
                                    if (gqm != null) {
                                        gqm.startAllQueueWorkersLocally();
                                    }
                                }

//                                log.info("Starting all message metadata writers");
//                                if (messageMetaDataWriter != null) {
//                                    messageMetaDataWriter.start();
//                                }

                                // log.info("Starting all message content writers");
                                // if(publishMessageWriter != null) {
                                // publishMessageWriter.start();
                                // }

                                log.info("Starting message content deletion");
                                if (messageContentRemovalTask != null && !messageContentRemovalTask.isRunning()) {
                                    messageContentRemovalTask.setRunning(true);
                                }

                                log.info("Starting pub-sub message removal task");
                                if (pubSubMessageContentRemoverTask != null
                                        && !pubSubMessageContentRemoverTask.isRunning()) {
                                    pubSubMessageContentRemoverTask.setRunning(true);
                                }
                            }
                            Thread.sleep(10000);
                        }
                    } catch (HectorException e) {

                        try {

                            if (e.getMessage()
                                    .contains("All host pools marked down. Retry burden pushed out to client")) {

                                isCassandraConnectionLive = false;
                                // print the error log several times
                                if (retriedCount < 5) {
                                    log.error(e);
                                }
                                retriedCount += 1;
                                if (retriedCount == 4) {
                                    // stop all tasks accessing Cassandra
                                    log.error("Cassandra Message Store is Inaccessible....");

                                    log.info("Stopping all current queue message publishers");
                                    ClusteringEnabledSubscriptionManager csm = ClusterResourceHolder.getInstance()
                                            .getSubscriptionManager();
                                    if (csm != null) {
                                        csm.stopAllMessageFlushers();
                                    }

                                    log.info("Stopping all current topic message publishers");
                                    CassandraTopicPublisherManager stpm = ClusterResourceHolder.getInstance()
                                            .getCassandraTopicPublisherManager();
                                    if (stpm != null && stpm.isActive()) {
                                        stpm.stop();
                                    }

                                    log.info("Stopping all global queue workers locally");
                                    ClusterManager cm = ClusterResourceHolder.getInstance().getClusterManager();
                                    if (cm != null) {
                                        GlobalQueueManager gqm = cm.getGlobalQueueManager();
                                        if (gqm != null) {
                                            gqm.stopAllQueueWorkersLocally();
                                        }
                                    }

//                                    log.info("Stopping all message metadata writers");
//                                    if (messageMetaDataWriter != null) {
//                                        messageMetaDataWriter.stop();
//                                    }

                                    // log.info("Stopping all message content writers");
                                    // if(publishMessageWriter != null) {
                                    // publishMessageWriter.stop();
                                    // }

                                    log.info("Stopping message content deletion");
                                    if (messageContentRemovalTask != null && messageContentRemovalTask.isRunning()) {
                                        messageContentRemovalTask.setRunning(false);
                                    }

                                    log.info("Stopping pub-sub message removal task");
                                    if (pubSubMessageContentRemoverTask != null
                                            && pubSubMessageContentRemoverTask.isRunning()) {
                                        pubSubMessageContentRemoverTask.setRunning(false);
                                    }

                                    log.info("Stopping expired message removal task");
                                    ExpiredCassandraMessageRemover.getInstance().stopTask();
                                }
                                log.info("Waiting for Cassandra connection configured to become live...");

                                if (retriedCount <= 10) {
                                    Thread.sleep(6000);
                                } else {
                                    if (retriedCount == 120) {
                                        retriedCount = 10;
                                    }
                                    Thread.sleep(500 * retriedCount);
                                }

                            }
                        } catch (InterruptedException ex) {
                            // silently ignore
                        } catch (Exception ex) {
                            log.error("Error while checking if Cassandra Connection is alive.", ex);
                        }
                    } catch (InterruptedException e) {
                        // silently ignore
                    } catch (Exception e) {
                        log.error("Error while checking if Cassandra Connection is alive.", e);
                    }
                }
            }
        });
        cassandraConnectionCheckerThread.start();
    }

    @Override
    /**
     * Update queue detail in Cassandra. This is only for durable queues
     */
    public void updateQueue(AMQQueue queue) throws AMQStoreException {

        if (!isCassandraConnectionLive) {
            log.error("Error in updating the queue. Message store is inaccessible.");
            return;
        }
        try {
            String owner = queue.getOwner() == null ? null : queue.getOwner().toString();
            String value = queue.getNameShortString().toString() + "|" + owner + "|"
                    + (queue.isExclusive() ? "true" : "false");
            CassandraDataAccessHelper.addMappingToRaw(QUEUE_DETAILS_COLUMN_FAMILY, QUEUE_DETAILS_ROW, queue
                    .getNameShortString().toString(), value, keyspace);
        } catch (CassandraDataAccessException e) {
            throw new AMQStoreException("Error in updating the queue", e);
        }
    }

    @Override
    public void configureTransactionLog(String name, TransactionLogRecoveryHandler recoveryHandler,
            Configuration storeConfiguration, LogSubject logSubject) throws Exception {
    }

    @Override
    public Transaction newTransaction() {
        return new CassandraTransaction();
    }

    public boolean isConfigured() {
        return configured;
    }

    // stored cassandra message class
    // TODO : Disruptor - this should be removed from  here and create a seperate class
    public class StoredCassandraMessage implements StoredMessage {

        private final long _messageId;
        private StorableMessageMetaData metaData;
        private String channelID;
        private String exchange;
        private ByteBuffer _content;

        /**
         * Create a stored cassandra message combining metadata and message ID
         *
         * @param messageId
         * @param metaData
         */
        private StoredCassandraMessage(long messageId, StorableMessageMetaData metaData) {
            this._messageId = messageId;
            this.metaData = metaData;
        }

        private StoredCassandraMessage(long messageId, StorableMessageMetaData metaData, boolean isTopics) {
            this._messageId = messageId;
            this.metaData = metaData;
            if (isTopics) {
                this.exchange = "amq.topic";
            }
        }

        @Override
        public StorableMessageMetaData getMetaData() {
            if (metaData == null) {
                metaData = CassandraMessageStore.this.getMetaData(_messageId);
            }
            return metaData;
        }

        @Override
        public long getMessageNumber() {
            return _messageId;
        }

        @Override
        /**
         * write content to the message store
         */
        public void addContent(int offsetInMessage, ByteBuffer src) {
            if (isInMemoryMode && exchange.equalsIgnoreCase("amq.topic")) {
                src = src.duplicate();
                this._content = ByteBuffer.allocate(metaData.getContentSize());
                ByteBuffer dst = _content.duplicate();
                dst.position(offsetInMessage);
                dst.put(src);
            } else {
                addContentInPersistentMode(offsetInMessage, src);
            }
        }

        /**
         * write the message content to cassandra (we submit this task to
         * AndesExecutor pool)
         *
         * @param offsetInMessage
         *            Int message content offset
         * @param src
         *            ByteBuffer message content
         */
        private void addContentInPersistentMode(final int offsetInMessage, ByteBuffer src) {
            AndesMessagePart part = new AndesMessagePart();
            src = src.slice();
            final byte[] chunkData = new byte[src.limit()];

            src.duplicate().get(chunkData);

            part.setData(chunkData);
            part.setMessageID(_messageId);
            part.setOffSet(offsetInMessage);
            part.setDataLength(chunkData.length);

            disruptorBasedExecutor.messagePartReceived(part);

            // AndesExecuter.getInstance(ClusterResourceHolder.getInstance().getClusterConfiguration().
            // getAndesInternalParallelThreadPoolSize()).submit(new Runnable() {
            // public void run() {
            // try {
            // executer.messagePartReceived(queue, part);
            // CassandraMessageStore.this.addMessageContent(_messageId + "",
            // offsetInMessage, src);
            // } catch (Throwable e) {
            // log.error("Error processing completed messages", e);
            //
            // /**
            // * TODO close the session, have to find a way to get access to
            // protocol session.
            // * if (_session instanceof AMQProtocolEngine) {
            // ((AMQProtocolEngine) _session).closeProtocolSession();
            // }
            // */
            // }
            // }
            // }, channelID);
        }

        @Override
        /**
         * get content for offset in a message
         */
        public int getContent(int offsetInMessage, ByteBuffer dst) {
            int c;
            if (isInMemoryMode && exchange.equalsIgnoreCase("amq.topic")) {
                ByteBuffer src = _content.duplicate();
                src.position(offsetInMessage);
                src = src.slice();
                if (dst.remaining() < src.limit()) {
                    src.limit(dst.remaining());
                }
                dst.put(src);
                c = src.limit();
            } else {
                c = CassandraMessageStore.this.getContent(_messageId + "", offsetInMessage, dst);
            }
            return c;
        }

        @Override
        public TransactionLog.StoreFuture flushToStore() {
//            try {
//                // storeMetaData(_messageId, metaData);
//                messageMetaDataWriter.addMetaDataMessage(_messageId, metaData);
//            } catch (InterruptedException e) {
//                log.error("Error in adding meta data to meta data writer thread ", e);
//            }
            //return IMMEDIATE_FUTURE;
            throw new UnsupportedOperationException();
        }

        public String getChannelID() {
            return channelID;
        }

        public void setChannelID(String channelID) {
            this.channelID = channelID;
        }

        @Override
        public void remove() {
            // Todo:when this is called we have to remove content from the
            // storage?? we have to do buffering here. but both queue and topic
            // deletions come here
            // remove content from store
            /*
             * try { List<String> messageIDsToRemove = new ArrayList<String>();
             * messageIDsToRemove.add(Long.toString(this._messageId));
             * if(!messageIDsToRemove.isEmpty()) {
             * CassandraDataAccessHelper.deleteIntegerRowListFromColumnFamily
             * (MESSAGE_CONTENT_COLUMN_FAMILY, messageIDsToRemove, keyspace); }
             * } catch (CassandraDataAccessException e) {
             * log.error("Error removing message content for message:" +
             * this._messageId, e); }
             */

            // if(ClusterResourceHolder.getInstance().getClusterConfiguration().isOnceInOrderSupportEnabled()){
            // return;
            // }
            // ColumnQuery<String, String, String> columnQuery =
            // HFactory.createColumnQuery(keyspace, stringSerializer,
            // stringSerializer ,
            // stringSerializer);
            // columnQuery.setColumnFamily(MESSAGE_QUEUE_MAPPING_COLUMN_FAMILY).
            // setKey(MESSAGE_QUEUE_MAPPING_ROW).setName("" + _messageId);
            // QueryResult<HColumn<String, String>> result =
            // columnQuery.execute();
            //
            // HColumn<String, String> rc = result.get();
            // if (rc != null) {
            // String qname = result.get().getValue();
            // try {
            // CassandraMessageStore.this.removeMessageFromNodeQueue(qname,_messageId);
            // } catch (AMQStoreException e) {
            // log.error("Error remove message",e);
            // }
            // contentDeletionTasks.add(_messageId);
            // } else {
            // throw new
            // RuntimeException("Can't remove message : message does not exist");
            // }

        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }
    }

    // inner class handling Cassandra Transactions
    private class CassandraTransaction implements Transaction {

        public void enqueueMessage(final TransactionLogResource queue, final Long messageId) throws AMQStoreException {

//            try {
//                AndesExecuter.getInstance(
//                        ClusterResourceHolder.getInstance().getClusterConfiguration()
//                                .getAndesInternalParallelThreadPoolSize()).submit(new Runnable() {
//                    public void run() {
//
//                        try {
//                            Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
//                            String name = queue.getResourceName();
//                            LongSerializer ls = LongSerializer.get();
//                            mutator.addInsertion(QUEUE_ENTRY_ROW, QUEUE_ENTRY_COLUMN_FAMILY,
//                                    HFactory.createColumn(name, messageId, stringSerializer, ls));
//                            mutator.execute();
//                        } catch (Throwable e) {
//                            log.error("Error adding Queue Entry ", e);
//                        }
//
//                    }
//                }, null);
//            } catch (Throwable e) {
//
//                log.error("Error adding Queue Entry ", e);
//                throw new AMQStoreException("Error adding Queue Entry " + queue.getResourceName(), e);
//            }
        }

        /**
         * dequeue message from queue entries for transactions
         *
         * @param queue
         *            The queue to place the message on.
         * @param messageId
         *            The message to dequeue.
         * @throws AMQStoreException
         */
        public void dequeueMessage(final TransactionLogResource queue, Long messageId) throws AMQStoreException {
//            try {
//                AndesExecuter.getInstance(
//                        ClusterResourceHolder.getInstance().getClusterConfiguration()
//                                .getAndesInternalParallelThreadPoolSize()).submit(new Runnable() {
//                    public void run() {
//                        String name = queue.getResourceName();
//                        try {
//                            CassandraDataAccessHelper.deleteStringColumnFromRaw(QUEUE_ENTRY_COLUMN_FAMILY,
//                                    QUEUE_DETAILS_ROW, name, keyspace);
//                        } catch (Throwable e) {
//                            log.error("Error deleting Queue Entry", e);
//                        }
//                    }
//                }, null);
//            } catch (Throwable e) {
//                log.error("Error deleting Queue Entry", e);
//                throw new AMQStoreException("Error deleting Queue Entry :" + queue.getResourceName(), e);
//            }

        }

        public void commitTran() throws AMQStoreException {

        }

        public StoreFuture commitTranAsync() throws AMQStoreException {
            return new StoreFuture() {
                public boolean isComplete() {
                    return true;
                }

                public void waitForCompletion() {

                }
            };
        }

        public void abortTran() throws AMQStoreException {

        }
    }

    /**
     * This task removes message-content,message-queue mapping and message
     * properties from cassandra space executing in a separate thread
     */
    private class ContentRemoverAndMessageQueueMappingRemoverTask implements Runnable {
        private int waitInterval = 5000;
        private long timeOutPerMessage = 60000; // 10s
        private boolean running = true;

        public ContentRemoverAndMessageQueueMappingRemoverTask(int waitInterval) {
            this.waitInterval = waitInterval;
        }

        public void run() {

            while (running) {
                try {

                    if (!contentDeletionTasks.isEmpty()) {
                        long currentTime = System.currentTimeMillis();

                        SortedMap<Long, Long> timedOutContentList = contentDeletionTasks.headMap(currentTime
                                - timeOutPerMessage);

                        List<String> rows2Remove = new ArrayList<String>();
                        List<Long> messageIDsToRemove = new ArrayList<Long>();
                        for (Long key : timedOutContentList.keySet()) {
                            rows2Remove.add(new StringBuffer(AndesConstants.MESSAGE_CONTENT_CASSANDRA_ROW_NAME_PREFIX)
                                    .append(timedOutContentList.get(key)).toString());
                            messageIDsToRemove.add(key);
                        }
                        // remove content
                        if (!rows2Remove.isEmpty()) {
                            CassandraDataAccessHelper.deleteIntegerRowListFromColumnFamily(
                                    MESSAGE_CONTENT_COLUMN_FAMILY, rows2Remove, keyspace);
                        }
                        // remove message properites
                        if (!messageIDsToRemove.isEmpty()) {
                            deleteMessagePropertiesForMessageList(messageIDsToRemove);
                        }

                        for (Long key : timedOutContentList.keySet()) {
                            contentDeletionTasks.remove(key);
                        }
                    }

                    if (!messageQueueMappingDeletionTasks.isEmpty()) {
                        long currentTime = System.currentTimeMillis();
                        SortedMap<Long, MessageQueueMapping> timedOutQueueMappingList = messageQueueMappingDeletionTasks
                                .headMap(currentTime - timeOutPerMessage);
                        Map<Long, String> messageQueueMappings = new HashMap<Long, String>();
                        for (MessageQueueMapping mapping : timedOutQueueMappingList.values()) {
                            messageQueueMappings.put(mapping.getMessageID(), mapping.getDestinationQueueName());
                        }
                        if (!messageQueueMappings.isEmpty()) {
                            CassandraDataAccessHelper.deleteLongColumnSpecifiedInRowAsBatch(
                                    MESSAGE_QUEUE_MAPPING_COLUMN_FAMILY, keyspace, messageQueueMappings);
                        }
                        for (Long key : timedOutQueueMappingList.keySet()) {
                            messageQueueMappingDeletionTasks.remove(key);
                        }
                    }
                    try {
                        Thread.sleep(waitInterval);
                    } catch (InterruptedException e) {
                        log.error("Error while Executing content removal Task", e);
                    }
                } catch (Throwable e) {
                    log.error("Error while Executing content removal Task", e);
                }
            }
        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }

    /**
     * this task will remove message content for in-memory implementation
     */
    private class InMemoryMessageRemoverTask implements Runnable {
        private int waitInterval = 5000;
        private long timeOutPerMessage = 5000; // 10s
        private boolean running = true;

        public InMemoryMessageRemoverTask(int waitInterval) {
            this.waitInterval = waitInterval;
        }

        public void run() {

            while (running) {
                try {

                    if (!removalPendingTopicMessageIds.isEmpty()) {
                        long currentTime = System.currentTimeMillis();
                        List<Long> readyToRemove = new ArrayList<Long>();

                        Enumeration<Long> messageIds = removalPendingTopicMessageIds.keys();
                        while (messageIds.hasMoreElements()) {
                            long mid = messageIds.nextElement();
                            if ((currentTime - removalPendingTopicMessageIds.get(mid)) > timeOutPerMessage) {
                                readyToRemove.add(mid);
                            }
                        }

                        for (Long mid : readyToRemove) {
                            removalPendingTopicMessageIds.remove(mid);
                            incomingTopicMessagesHashtable.remove(mid);
                            alreadyAddedTopicMessages.remove(mid);
                        }

                    }
                    try {
                        Thread.sleep(waitInterval);
                    } catch (InterruptedException e) {
                        log.error("Error while Executing content removal Task", e);
                    }
                } catch (Throwable e) {
                    log.error("Error while Executing content removal Task", e);
                }
            }
        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }

    /**
     * <code>PubSubMessageContentRemoverTask</code> This task is used to remove
     * message content from database when the message published and acknowledged
     * from client. It checks the acknowledged message was delivered before a
     * time difference of CONTENT_REMOVAL_TIME_DEFFERENCE and it condition
     * satisfies, it removes messages from data store
     */
    private class PubSubMessageContentRemoverTask implements Runnable {

        private int waitInterval = 5000;

        private boolean running = true;

        public PubSubMessageContentRemoverTask(int waitInterval) {
            this.waitInterval = waitInterval;
        }

        public void run() {
            while (running) {
                try {
                    while (!pubSubMessageContentDeletionTasks.isEmpty()) {
                        Set<Long> messageIds = pubSubMessageContentDeletionTasks.keySet();
                        for (long messageID : messageIds) {
                            // If ready to remove , remove it from content table
                            if (CassandraMessageStore.this.isReadyAndRemovedMessageContent(messageID)) {
                                pubSubMessageContentDeletionTasks.remove(messageID);
                            }
                        }
                    }
                    try {
                        Thread.sleep(waitInterval);
                    } catch (InterruptedException e) {
                        log.error(e);
                    }

                } catch (Throwable e) {
                    log.error("Erring in removing pub sub message content details ", e);
                }
            }
        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }

    /**
     * Schedule content of a message to be removed from cassandra space along
     * with message properties
     * 
     * @param messageId
     */
    public void addContentDeletionTask(long messageId) {
        contentDeletionTasks.put(System.currentTimeMillis(), messageId);
    }

    /**
     * Schedule messageID-routing key mapping kept at cassandra space to be
     * removed
     */
    public void addMessageQueueMappingDeletionTask(String destinationQueueName, long messageID) {
        messageQueueMappingDeletionTasks.put(System.currentTimeMillis(), new MessageQueueMapping(destinationQueueName,
                messageID));
    }

    /**
     * Class of entity to track message-routing key mapping
     */
    private class MessageQueueMapping {

        private long messageID;
        private String destinationQueueName;

        public MessageQueueMapping(String destinationQueueName, long messageID) {
            this.destinationQueueName = destinationQueueName;
            this.messageID = messageID;
        }

        public long getMessageID() {
            return messageID;
        }

        public String getDestinationQueueName() {
            return destinationQueueName;
        }
    }

    private org.wso2.andes.kernel.MessageStore store;
    public org.wso2.andes.kernel.MessageStore getMessageStore() {
        synchronized (this) {
            if(store == null){
                store = new CassandraBasedMessageStoreImpl(keyspace); 
            }
        }
        return store;
    }

    private Subscrption sender;

    public Subscrption getDataSender() {
        synchronized (this) {
            if(sender == null){
                sender = new SimplyfiedDataSender();
            }
        }
        return sender;
    }
}
