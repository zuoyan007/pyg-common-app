package pyg.daheng.base.util.invoke.util;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:47
 */
public abstract class DXKafkaProducer<T> implements Closeable {
    protected final Logger logger;
    protected DXKafkaManager manager;
    protected final String producerId;
    protected DXKafkaConfig config;
    protected boolean bInitialized = false;
    protected KafkaProducer<String, T> producer;
    private Set<String> topics;
    public static Map<String, Integer> sendTimesMap = new ConcurrentHashMap<>();

    public abstract boolean init();

    public DXKafkaProducer(DXKafkaManager mgr) {
        this.config = new DXKafkaConfig();
        this.config.setClienttype(Constants.KafkaClientType.producer);
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.manager = mgr;
        this.producerId = this.manager.monitor(this);
    }

    public int sendTimes(String uuid) {
        if (sendTimesMap.get(uuid) != null) {
            int i = sendTimesMap.get(uuid) + 1;
            sendTimesMap.put(uuid, i);
            return i;
        } else {
            sendTimesMap.put(uuid, 1);
            return 1;
        }
    }

    @Override
    public void close() {
        if (this.manager != null)
            this.manager.unmonitor(this);
        if (this.producer == null)
            return;
        this.producer.close();
    }

    /**
     * 暴露flush接口，需要业务方自主控制调用时机，错误调用会导致数据顺序发送异常。
     */
    public void flush() {
        if (this.producer == null)
            return;
        this.producer.flush();
    }

    public boolean loadParameters(Properties prop) {
        return this.config.loadParameters(prop);
    }

    public String getProducerId() {
        return producerId;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }

    public boolean addTopic(String topic) {
        if (this.topics == null) {
            this.topics = new HashSet<String>();
        }
        return this.topics.add(topic);
    }

    /**
     * 高性能单条发送，需要用户自主调用flush和close方法，否则会导致消息消费失败
     *
     * @param topic
     * @param msg
     * @return
     */
    public int pushNotClose(String topic, T msg) {
        try {
            if (!this.bInitialized || topic == null) {
                return 0;
            }
            addTopic(topic);
            Properties prop = this.config.pickupKafkaConfig();
            if (prop == null) {
                return 0;
            }
            this.producer = new KafkaProducer<>(prop);
            ProducerRecord<String, T> record = new ProducerRecord<String, T>(topic, msg);
            this.producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * TODO 默认异步提交，返还信息会降低吞吐量，待开发
     *
     * @param topic
     * @param msg
     * @return 0 - 提交成功；1 - 提交成功并返回服务器确认成功；2 - 提交成功服务器超时未返回；其他cans
     */
    public int push(String topic, T msg) {
        try {
            if (!this.bInitialized || topic == null) {
                return 0;
            }
            addTopic(topic);
            Properties prop = this.config.pickupKafkaConfig();
            if (prop == null) {
                return 0;
            }
            this.producer = new KafkaProducer<>(prop);
            ProducerRecord<String, T> record = new ProducerRecord<String, T>(topic, msg);
            this.producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.producer.flush();
            this.producer.close();
        }
        return 0;
    }

    /**
     * 高性能单条发送，需要用户自主调用flush和close方法，否则会导致消息消费失败
     *
     * @param topic
     * @param key
     * @param msg
     * @return
     */
    public int pushNotClose(String topic, String key, T msg) {
        try {
            if (!this.bInitialized || topic == null) {
                return 0;
            }
            addTopic(topic);
            Properties prop = this.config.pickupKafkaConfig();
            this.producer = new KafkaProducer<>(prop);
            ProducerRecord<String, T> record = new ProducerRecord<String, T>(topic, key, msg);
            this.producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * TODO 默认异步提交，返还信息会降低吞吐量，待开发
     *
     * @param topic
     * @param key
     * @param msg
     * @return
     */
    public int push(String topic, String key, T msg) {
        try {
            if (!this.bInitialized || topic == null) {
                return 0;
            }
            addTopic(topic);
            Properties prop = this.config.pickupKafkaConfig();
            this.producer = new KafkaProducer<>(prop);
            ProducerRecord<String, T> record = new ProducerRecord<String, T>(topic, key, msg);
            this.producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.producer.flush();
            this.producer.close();
        }
        return 0;
    }

    public void bulkpush(String topic, List<T> list){
        try {
            this.bulkpushNotClose(topic,list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.producer.flush();
            this.producer.close();
        }
    }


    //批量发送消息不关闭
    public void bulkpushNotClose(String topic, List<T> list){
        if (!this.bInitialized || topic == null) {
            return;
        }
        addTopic(topic);
        Properties prop = this.config.pickupKafkaConfig();
        this.producer = new KafkaProducer<>(prop);
        for (T msg : list) {
            ProducerRecord<String, T> record = new ProducerRecord<String, T>(topic, msg);
            this.producer.send(record);
        }
    }

    //单条发送消息带回调,自动关闭生产者
    public void pushCallback(String topic, T msg, KafkaSendCallback sendCallback){
        try {
            this.pushCallbackNotClose(topic, msg, sendCallback);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }
    }

    //单条发送消息带回调，不关闭生产者
    public void pushCallbackNotClose(String topic, T msg, KafkaSendCallback sendCallback) {
        //        String uuid = UUID.randomUUID().toString();
        if (!this.bInitialized || topic == null) {
            return;
        }
        addTopic(topic);
        Properties prop = this.config.pickupKafkaConfig();
        this.producer = new KafkaProducer<>(prop);

        ProducerRecord<String, T> record = new ProducerRecord<String, T>(topic, msg);
        Future<RecordMetadata> future = this.producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception e) {
                try {
                    KafkaSendResult kafkaSendResult = new KafkaSendResult();
                    kafkaSendResult.setMsg(msg);
                    kafkaSendResult.setTopic(topic);
                    if (metadata != null && e == null) {//发送消息成功
                        kafkaSendResult.setStatus(true);
                    } else {
                        kafkaSendResult.setStatus(false);
                        if (e != null) kafkaSendResult.setErr(e);
                    }
                    sendCallback.getSendResult(kafkaSendResult);
                } catch (Exception e1) {
                    KafkaSendResult kafkaSendResult = new KafkaSendResult();
                    kafkaSendResult.setErr(e1);
                    sendCallback.getSendResult(kafkaSendResult);
                }
            }
        });
        System.out.println(future);

    }

    //批量发送消息带回调,自动关闭生产者
    public void bulkPushCallback(String topic, List<T> list, KafkaSendCallback sendCallback){
        try {
            this.bulkPushCallbackNotClose(topic, list, sendCallback);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.flush();
            producer.close();
        }
    }

    //批量发送消息带回调，不关闭生产者
    public void bulkPushCallbackNotClose(String topic, List<T> list, KafkaSendCallback sendCallback){
        //        String uuid = UUID.randomUUID().toString();
        if (!this.bInitialized || topic == null) {
            return;
        }
        addTopic(topic);
        Properties prop = this.config.pickupKafkaConfig();
        this.producer = new KafkaProducer<>(prop);
        for (T msg : list) {
            ProducerRecord<String, T> record = new ProducerRecord<String, T>(topic, msg);
            Future<RecordMetadata> future = this.producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    try {
                        KafkaSendResult kafkaSendResult = new KafkaSendResult();
                        kafkaSendResult.setMsg(msg);
                        kafkaSendResult.setTopic(topic);
                        if (metadata != null && e == null) {//发送消息成功
                            kafkaSendResult.setStatus(true);
                        } else {
                            kafkaSendResult.setStatus(false);
                            if (e != null) kafkaSendResult.setErr(e);
                        }
                        sendCallback.getSendResult(kafkaSendResult);
                    } catch (Exception e1) {
                        KafkaSendResult kafkaSendResult = new KafkaSendResult();
                        kafkaSendResult.setErr(e1);
                        sendCallback.getSendResult(kafkaSendResult);
                    }
                }
            });
        }
    }


    public int bulkpushex(String topic, List<DXPair<String, T>> list) {
        try {
            if (!this.bInitialized || topic == null) {
                return 0;
            }
            addTopic(topic);
            Properties prop = this.config.pickupKafkaConfig();
            this.producer = new KafkaProducer<>(prop);
            for (DXPair<String, T> msg : list) {
                ProducerRecord<String, T> record = new ProducerRecord<String, T>(topic, msg.getFirst(),
                        msg.getSecond());
                this.producer.send(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.producer.flush();
            this.producer.close();
        }
        return 0;
    }
}

