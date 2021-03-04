package pyg.daheng.base.util.invoke.util;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:47
 */
public class DXKafkaConfig {
    private static int POLL_TIMEOUT_DEFAULT = 3000; // 单位：毫秒，缺省1秒
    public final static String CONFIG_PREFIX = "kafka."; // 传入的配置信息前缀
    private Constants.KafkaClientType clientType;

    /**
     * 对应bootstrap.servers，指定Kafka brokers，格式为：host1:port1,host2:port2,...
     */
    private String servers;
    /**
     * 对应group.id，指定客户端连接组标识，消费者模式下有效
     */
    private String groupId;
    /**
     * 对应enable.auto.commit，消费者模式有效，获取消息后，是否自动提交offset，缺省为false，需要手动调用consumer.commitSync();提交offset
     */
    private boolean enableAutoCommit = false;

    /**
     * consumer向zookeeper提交offset的频率，单位是秒
     */
    private String autoCommitInterval;
    /**
     * https://blog.csdn.net/lishuangzhe7047/article/details/74530417 用于重置偏移量，详见上述链接
     */
    private String autoOffsetReset;
    /**
     * poll消息时的缺省超时时间
     */
    private int polltimeout;
    /**
     * 设置每次从服务器poll消息是最多可以批量获取的消息条数
     */
    private int maxPollRecords;

    /**
     * 0、1 和 all,生产者模式有效,default=1 acks=0的情况下，生产者不等待broker的响应，只管发送数据，所以存在丢失消息的可能。
     * acks=1是消息写入首领分区日志文件就返回响应给生产者。
     * acks=all的情况下，还必须要求ISR列表里跟Leader保持同步的那些Follower都要把消息同步过去，才能认为这条消息是写入成功了。
     */
    private String acks;

    /**
     * 重试次数
     */
    private int retries;
    /**
     * producer组将会汇总任何在请求与发送之间到达的消息记录一个单独批量的请求。通常来说，这只有在记录产生速度大于发送速度的时候才能发生。
     * 然而，在某些条件下，客户端将希望降低请求的数量，甚至降低到中等负载一下。这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，
     * producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理。这可以认为是TCP种Nagle的算法类似。这项设置
     * 设定了批量处理的更高的延迟边界：一旦我们获得某个partition的batch.size，他将会立即发送而不顾这项设置，然而如果我们获得消息字
     * 节数比这项设置要小的多，我们需要“linger”特定的时间以获取更多的消息。 这个设置默认为0，即没有延迟。设定linger.ms=5，例如，将
     * 会减少请求数目，但是同时会增加5ms的延迟。
     */
    private int linger;
    /**
     * producer将试图批处理消息记录，以减少请求次数。default=16384
     */
    private int batchSize;
    /**
     * producer可以用来缓存数据的内存大小。default=33554432
     */
    private int bufferMemory;

    private Map<String, String> attributes;

    private boolean configIntegrity = false;

    public DXKafkaConfig() {
        this.attributes = new HashMap<>();
        this.polltimeout = POLL_TIMEOUT_DEFAULT;
        this.clientType = Constants.KafkaClientType.consumer;
        /*
         * Kafka客户端可以设置以下参数对Kafka客户端的行为进行参数配置配置参数如下： heartbeat.interval.ms = 3000 int
         * 指定消费者协调器的间隔时间，消loadParameters费者模式下有效 max.poll.records = 500 int
         * 设置每次从服务器poll消息是最多可以批量获取的消息条数 max.poll.interval.ms = 30000 int
         * 设置poll最多等待毫秒数从服务器批量获取消息指定最大数量或字节数 receive.buffer.bytes = 65536 int
         * 设置TCP的SO_RCVBUF接收数据缓冲区大小，-1使用操作系统配置大小 send.buffer.bytes = 131072 int
         * 设置TCP的SO_SNDBUF发送缓冲区大小，-1使用操作系统配置大小
         */
    }

    public boolean loadParameters(Properties prop) {
        if (prop == null || prop.isEmpty()){
            return this.configIntegrity;
        }
        for (Map.Entry<Object, Object> attr : prop.entrySet()) {
            if(String.valueOf(attr.getKey()).indexOf(CONFIG_PREFIX)==-1) {continue;}
            this.putParameter(String.valueOf(attr.getKey()).replace(CONFIG_PREFIX,""), String.valueOf(attr.getValue()));
        }
        return this.checkIntegrity();
    }

    public boolean putParameter(String k, String v) {
        if (k == null || "".equals(k.trim())){
            return false;}
        if (v == null || "".equals(v.trim())){
            return false;}
        String k0 = k.trim().toLowerCase();
        String v0 = v.trim();
        if (ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG.equals(k0)){
            setServers(v0);}
        else if (ConsumerConfig.GROUP_ID_CONFIG.equals(k0)){
            setGroupId(v0);}
        else if (ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG.equals(k0)) {
            setPolltimeout(Integer.parseInt(v0));
        } else if (ProducerConfig.ACKS_CONFIG.equals(k0)) {
            setAcks(v0);
        }  else if (ProducerConfig.RETRIES_CONFIG.equals(k0)) {
            setRetries(Integer.parseInt(v0));
        }  else if (ProducerConfig.BATCH_SIZE_CONFIG.equals(k0)) {
            setBatchSize(Integer.parseInt(v0));
        }  else if (ProducerConfig.LINGER_MS_CONFIG.equals(k0)) {
            setLinger(Integer.parseInt(v0));
        }  else if (ProducerConfig.BUFFER_MEMORY_CONFIG.equals(k0)) {
            setBufferMemory(Integer.parseInt(v0));
        } else {
            this.attributes.put(k0, v0);
        }
        return true;
    }

    public Constants.KafkaClientType getClientType() {
        return clientType;
    }

    public void setClienttype(Constants.KafkaClientType clientType) {
        this.clientType = clientType;
    }

    private boolean checkIntegrity() {
        if (this.servers == null || "".equals(this.servers)){
            return false;}
        if (this.clientType.equals(Constants.KafkaClientType.consumer)) {
            // TODO
            if (this.groupId == null || "".equals(this.groupId)){
                return false;}
        } else if (this.clientType.equals(Constants.KafkaClientType.producer)) {
            // TODO
        }
        this.configIntegrity = true;
        return this.configIntegrity;
    }

    public Properties pickupKafkaConfig() {
        if (!this.checkIntegrity()){
            return null;}
        Properties prop = new Properties();
        // 需要优先读取attributes，防止重复设置同一个参数
        for (Map.Entry<String, String> attr : this.attributes.entrySet()) {
            prop.put(attr.getKey(), attr.getValue());
        }
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getServers());
        if (this.clientType.equals(Constants.KafkaClientType.consumer)) {
            prop.put(ConsumerConfig.GROUP_ID_CONFIG, getGroupId());
            prop.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, getPolltimeout());
            if (this.enableAutoCommit) {
                prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
                prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, getAutoCommitInterval());
            } else {
                prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
            }

            if (getAutoOffsetReset() != null) {
                prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, getAutoOffsetReset());
            } else {
                prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, cn.csg.lib.kafka.Constants.AutoOffsetReset.earliest);
            }
            if (getMaxPollRecords() > 0) {
                prop.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, getMaxPollRecords());
            }
        } else if (this.clientType.equals(Constants.KafkaClientType.producer)) {
            prop.put(ProducerConfig.ACKS_CONFIG, getAcks());
            prop.put(ProducerConfig.RETRIES_CONFIG, getRetries());
            prop.put(ProducerConfig.BATCH_SIZE_CONFIG, getBatchSize());
            prop.put(ProducerConfig.LINGER_MS_CONFIG, getLinger());
            prop.put(ProducerConfig.BUFFER_MEMORY_CONFIG, getBufferMemory());
        }
        return prop;
    }

    public boolean isConfigIntegrity() {
        return this.configIntegrity;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public int getPolltimeout() {
        return polltimeout;
    }

    public void setPolltimeout(int polltimeout) {
        this.polltimeout = polltimeout;
    }

    public int getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(int maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }
    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public String getAutoCommitInterval() {
        return autoCommitInterval;
    }

    public void setAutoCommitInterval(String autoCommitInterval) {
        this.autoCommitInterval = autoCommitInterval;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getLinger() {
        return linger;
    }

    public void setLinger(int linger) {
        this.linger = linger;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
    public int getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(int bufferMemory) {
        this.bufferMemory = bufferMemory;
    }
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("DXKafkaConfig: ");
        sb.append("bootstrap.servers=").append(this.servers);
        if (this.clientType.equals(Constants.KafkaClientType.consumer)) {
            sb.append("; group.id=").append(this.groupId);
            sb.append("; enable.auto.commit=").append(this.enableAutoCommit);
            sb.append("; max.poll.interval.ms=").append(this.polltimeout);
        } else if (this.clientType.equals(Constants.KafkaClientType.producer)) {
            sb.append("; acks=").append(this.acks);
            sb.append("; batchSize=").append(this.batchSize);
            sb.append("; bufferMemory=").append(this.bufferMemory);
        }
        sb.append("; other properties=" + this.attributes);
        return sb.toString();
    }
}
