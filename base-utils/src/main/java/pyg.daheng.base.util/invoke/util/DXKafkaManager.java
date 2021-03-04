package pyg.daheng.base.util.invoke.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:44
 */
public class DXKafkaManager {
    // private static final Log logger = LogFactory.getLog(DXKafkaManager.class);
    private static final Logger logger = LoggerFactory.getLogger(DXKafkaManager.class);
    private static DXKafkaManager instance;
    private boolean bInitialized = false;

    private DXKafkaManager() {
    }

    public static DXKafkaManager getInstance() {
        if (instance == null) {
            synchronized (DXKafkaManager.class) {
                if (instance == null) {
                    instance = new DXKafkaManager();
                    logger.info("KafkaManager Singleton Instance created.");
                }
            }
        }
        return instance;
    }

    private Map<String, DXKafkaConsumer<?>> consumers;
    private Map<String, DXKafkaProducer<?>> producers;

    public boolean init() {
        if (this.bInitialized){
            return true;
        }
        this.bInitialized = true;
        return this.bInitialized;
    }

    public DXKafkaBytesConsumer buildKafkaBytesConsumer(Properties prop) {
        DXKafkaBytesConsumer consumer = new DXKafkaBytesConsumer(this);
        consumer.loadParameters(prop);
        consumer.init();
        return consumer;
    }

   /* public DXKafkaStringConsumer buildKafkaStringConsumer(Properties prop) {
        DXKafkaStringConsumer consumer = new DXKafkaStringConsumer(this);
        return (DXKafkaStringConsumer) this.initConsumer(consumer, prop);
    }

    public DXKafkaBytesProducer buildKafkaBytesProducer(Properties prop) {
        DXKafkaBytesProducer producer = new DXKafkaBytesProducer(this);
        return (DXKafkaBytesProducer) this.initProducer(producer, prop);
    }*/

    public DXKafkaStringProducer buildKafkaStringProducer(Properties prop) {
        DXKafkaStringProducer producer = new DXKafkaStringProducer(this);
        return (DXKafkaStringProducer) this.initProducer(producer, prop);

    }

    private DXKafkaProducer<?> initProducer(DXKafkaProducer<?> producer, Properties prop) {
        boolean ret;
        ret = producer.loadParameters(prop);
        if (!ret)
            return null;
        ret = producer.init();
        if (!ret)
            return null;
        return producer;
    }

    private DXKafkaConsumer<?> initConsumer(DXKafkaConsumer<?> consumer, Properties prop) {
        boolean ret;
        ret = consumer.loadParameters(prop);
        if (!ret)
            return null;
        ret = consumer.init();
        if (!ret)
            return null;
        return consumer;
    }

    String monitor(DXKafkaConsumer<?> consumer) {
        synchronized (this) {
            if (this.consumers == null)
                this.consumers = new HashMap<>();
        }
        String id = null;
        synchronized (this.consumers) {
            while (true) {
                id = this.createId();
                if (this.consumers.containsKey(id)) {
                    sleep(10);
                } else {
                    this.consumers.put(id, consumer);
                    if (logger.isDebugEnabled()){
                        logger.debug("Monitor Consumer:" + consumer);}
                    break;
                }
            }
        }
        return id;
    }

    void unmonitor(DXKafkaConsumer<?> consumer) {
        if (this.consumers != null && this.consumers.containsKey(consumer.getConsumerId()))
        {this.consumers.remove(consumer.getConsumerId());}
    }

    String monitor(DXKafkaProducer<?> producer) {
        synchronized (this) {
            if (this.producers == null){
                this.producers = new HashMap<>();}
        }
        String id = null;
        synchronized (this.producers) {
            while (true) {
                id = this.createId();
                if (this.producers.containsKey(id)) {
                    sleep(10);
                } else {
                    this.producers.put(id, producer);
                    if (logger.isDebugEnabled()){
                        logger.debug("Monitor Producer:" + producer);}
                    break;
                }
            }
        }
        return id;
    }

    void unmonitor(DXKafkaProducer<?> producer) {
        if (this.producers != null && this.producers.containsKey(producer.getProducerId()))
        {this.producers.remove(producer.getProducerId());}
    }

    private String createId() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // just wake up early
            Thread.currentThread().interrupt();
        }
    }

    public void release() {
        if (this.consumers != null && !this.consumers.isEmpty()) {
            for (DXKafkaConsumer<?> consumer : this.consumers.values()) {
                consumer.close();
            }
        }
        if (this.producers != null && !this.producers.isEmpty()) {
            for (DXKafkaProducer<?> producer : this.producers.values()) {
                producer.close();
            }
        }
    }
}
