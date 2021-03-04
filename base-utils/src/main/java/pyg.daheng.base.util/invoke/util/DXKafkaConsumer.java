package pyg.daheng.base.util.invoke.util;

import cn.csg.lib.common.model.DXMessage;
import cn.csg.lib.kafka.IMessageRender;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.*;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:22
 */
public abstract class DXKafkaConsumer<T> implements Closeable {
    private static final String DELIMITER = ",";
    protected final Logger logger;
    protected DXKafkaManager manager;
    protected final String consumerId;
    protected DXKafkaConfig config;
    protected boolean bInitialized = false;
    protected boolean bSubscribe = false;
    protected KafkaConsumer<String, T> consumer;
    protected IMessageRender<T> render;
    private String[] topics;

    public DXKafkaConsumer(DXKafkaManager mgr) {
        this.config = new DXKafkaConfig();
        this.config.setClienttype(Constants.KafkaClientType.consumer);
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.manager = mgr;
        this.consumerId = this.manager.monitor(this);
        this.render = null;
    }

    public DXKafkaConfig getConfig() {
        return config;
    }

    public boolean loadParameters(Properties prop) {
        return this.config.loadParameters(prop);
    }

    public String getConsumerId() {
        return consumerId;
    }

    public IMessageRender<T> getRender() {
        return render;
    }

    public void setRender(IMessageRender<T> v_render) {
        this.render = v_render;
    }

    public abstract boolean init();

    /**默认批量获取数据
     * @param topics
     * @return
     */
    public boolean subscribe(String... topics) {
        // [latest, earliest, none]
        return this.subscribe(cn.csg.lib.kafka.Constants.AutoOffsetReset.earliest, false, topics);
    }

    /**
     * @param bOneRecordATime true表示单条获取数据 false表示批量数据获取
     * @param topics
     * @return
     */
    public boolean subscribe(boolean bOneRecordATime, String... topics) {
        return this.subscribe(cn.csg.lib.kafka.Constants.AutoOffsetReset.earliest, bOneRecordATime, topics);
    }

    /**底层方法，配置消费者
     * @param mode
     * @param bOneRecordATime true表示单条获取数据； false表示批量数据获取
     * @param incomingTopics
     * @return
     */
    public boolean subscribe(cn.csg.lib.kafka.Constants.AutoOffsetReset mode, boolean bOneRecordATime, String... incomingTopics) {
        if (!this.bInitialized)
            return false;
        if (incomingTopics == null)
            return false;
        if (this.bSubscribe) {
            int sameTopicCount = 0;
            String [] currentTopics = getTopics().split(DELIMITER);
            for (String existedTopic:currentTopics) {
                for (String inputTopic:incomingTopics) {
                    if (!inputTopic.equals(existedTopic)) {
                        continue;
                    }
                    else {
                        sameTopicCount ++;
                    }
                }
            }
            if (sameTopicCount <= currentTopics.length && sameTopicCount > 0) {
                return true; //没有新的主题，无需重新关注
            }
        }
        Properties prop = this.config.pickupKafkaConfig();
        if (prop == null)
            return false;
        if (mode != null)
            prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, mode.getValue());
        if(bOneRecordATime)
            prop.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        if (!this.bSubscribe) {
            this.consumer = new KafkaConsumer<>(prop);
        }
        this.consumer.subscribe(Arrays.asList(incomingTopics));
        this.topics = this.consumer.subscription().toArray(new String[0]);
        this.bSubscribe = true;
        return true;
    }

    @Override
    public void close() {
        if (this.manager != null)
            this.manager.unmonitor(this);
        if (this.consumer == null)
            return;
        this.consumer.close();
    }
    /**手动提交commit，表示数据消费完毕，isEnableAutoCommit 为false时使用
     *
     */
    public void commitSync() {
        if (!this.config.isEnableAutoCommit() && this.bSubscribe && topics.length > 0) {
            this.consumer.commitSync();
        }
    }
    /**TODO 未完善功能，大量调用时请使用批量读取
     * 获取单条数据，配合 max.poll.records=1和enable.auto.commit=false，
     * @return
     */
    public ConsumerRecord<String, T> listen(String... topics) {
        if (subscribe(topics)) {
            return poll();
        } else {
            return null;
        }
    }

    /**
     * 底层方法，获取单条数据，配合subscribe和 max.poll.records=1 使用，commitSync为强制使用，调用即消费
     *
     * @return
     */
    public ConsumerRecord<String, T> poll() {
        return this.poll(this.config.getPolltimeout());
    }

    /**
     * 获取单条数据，配合 max.poll.records=1 ,大量调用时请使用批量读取
     * @param v_timeout 单位：毫秒，获取数据的超时时间
     * @return
     */
    public ConsumerRecord<String, T> poll(long v_timeout) {
        if (!this.bSubscribe)
            return null;
        ConsumerRecord<String, T> output = null;
        ConsumerRecords<String, T> records = null;
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        boolean hasTimeOut = false;
        while (!hasTimeOut) {
            end = System.currentTimeMillis();
            if (end - start > v_timeout) {
                hasTimeOut = true;
            }
            records = this.consumer.poll(v_timeout);
            if (records == null || records.isEmpty()) {
                continue;
            }
            output = records.iterator().next();
            this.consumer.commitSync();
            if (output != null) {
                break;
            }
        }
        return output;
    }

    /**获取多条数据
     * @param v_least 获取消息条数
     * @param topics 接收的主题
     * @return
     */
    public List<ConsumerRecord<String, T>> listen(int v_least, String... topics) {
        if (subscribe(topics)) {
            return bulkpoll(v_least);
        } else {
            return null;
        }
    }

    public List<ConsumerRecord<String, T>> bulkpoll(int v_least) {
        return this.bulkpoll(v_least, this.config.getPolltimeout());
    }

    /**
     * 从Kafka服务器获取订阅的消息
     *
     * @param v_least   获取消息条数
     * @param v_timeout 单位：毫秒，获取数据的超时时间
     * @return 返回消息行集，在超时时间内，获得多余或等于请求数量消息，如果没有更多消息，超时时间后将返回已经获得获得行集
     */
    public List<ConsumerRecord<String, T>> bulkpoll(int v_least, long v_timeout) {
        if (!this.bSubscribe)
            return null;
        List<ConsumerRecord<String, T>> buffer = null;
        long start = System.currentTimeMillis();
        long remaining = v_timeout < 0 ? 0 : v_timeout;
        ConsumerRecords<String, T> records;
        while (remaining >= 0) {
            records = this.consumer.poll(remaining);
            if (records != null) {
                for (ConsumerRecord<String, T> record : records) {
                    if (buffer == null)
                        buffer = new ArrayList<>();
                    buffer.add(record);
                }
                if (buffer != null && !this.config.isEnableAutoCommit() && !buffer.isEmpty()) {
                    this.consumer.commitSync();
                }
            }
            if (buffer != null && buffer.size() >= v_least)
                break;
            remaining = remaining - (System.currentTimeMillis() - start);
        }
        return buffer;
    }

    public List<DXMessage<T>> bulkpollex(int v_least) {
        return this.bulkpollex(v_least, this.config.getPolltimeout());
    }

    /**
     * 从Kafka服务器获取订阅的消息
     *
     * @param v_least   获取消息条数
     * @param v_timeout 单位：毫秒，获取数据的超时时间
     * @return 返回消息行集，在超时时间内，获得多余或等于请求数量消息，如果没有更多消息，超时时间后将返回已经获得获得行集
     */
    public List<DXMessage<T>> bulkpollex(int v_least, long v_timeout) {
        if (!this.bSubscribe)
            return null;
        List<DXMessage<T>> buffer = null;
        long start = System.currentTimeMillis();
        long remaining = v_timeout < 0 ? 0 : v_timeout;
        ConsumerRecords<String, T> records;
        DXMessage<T> msg = null;
        while (remaining >= 0) {
            records = this.consumer.poll(remaining);
            if (records != null) {
                for (ConsumerRecord<String, T> record : records) {
                    if (buffer == null)
                        buffer = new ArrayList<>();
                    msg = new DXMessage<T>();
                    msg.setRvtime(new Date());
                    if (this.render != null)
                        msg.setMsgid(this.render.createMessageID(msg.getRvtime()));
                    msg.setTopic(record.topic());
                    msg.setKey(record.key());
                    msg.setValue(record.value());
                    buffer.add(msg);
                }
                if (buffer != null && !this.config.isEnableAutoCommit() && !buffer.isEmpty()) {
                    if (this.render != null)
                        this.render.WALcallback(buffer);
                    this.consumer.commitSync();
                }
            }
            if (buffer != null && buffer.size() >= v_least)
                break;
            remaining = remaining - (System.currentTimeMillis() - start);
        }
        return buffer;
    }

    public String getTopics() {
        StringBuffer sb = new StringBuffer();
        boolean hasCell = false;
        if (this.topics != null) {
            for (String s : topics) {
                sb.append(s).append(DELIMITER);
                hasCell = true;
            }
        }
        if (hasCell)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String ListTopicPartitionInfo(String topic) {
        if (this.consumer == null)
            return "NULL";
        StringBuffer sb = new StringBuffer(topic);
        sb.append(" Partions:");
        List<PartitionInfo> pis = this.consumer.partitionsFor(topic);
        List<TopicPartition> tps = new ArrayList<>();
        for (PartitionInfo pi : pis)
            tps.add(new TopicPartition(topic, pi.partition()));
        Map<TopicPartition, Long> beginOffsets = this.consumer.beginningOffsets(tps);
        Map<TopicPartition, Long> endOffsets = this.consumer.endOffsets(tps);
        for (Map.Entry<TopicPartition, Long> entry : beginOffsets.entrySet()) {
            sb.append(entry.getKey().partition()).append(" offset from ").append(entry.getValue()).append(" to ");
            sb.append(endOffsets.get(entry.getKey()));
            //			sb.append(",current next position:" + this.consumer.position(entry.getKey()));
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public String ListAssignmentPartition() {
        if (this.consumer == null)
            return "NULL";
        StringBuffer sb = new StringBuffer("My Partitions(");
        Set<TopicPartition> tps = this.consumer.assignment();
        sb.append(tps.size()).append("):");
        for (TopicPartition tp : tps) {
            sb.append("\r\n");
            sb.append(tp.topic()).append(",partition=").append(tp.partition());
            sb.append(",next position=").append(this.consumer.position(tp));
        }
        return sb.toString();
    }

    public void seekToBeginning(String topic) {
        if (this.consumer == null)
            return;
        List<PartitionInfo> pis = this.consumer.partitionsFor(topic);
        List<TopicPartition> tps = new ArrayList<>();
        for (PartitionInfo pi : pis)
            tps.add(new TopicPartition(topic, pi.partition()));
        this.consumer.seekToBeginning(tps);
    }
}

