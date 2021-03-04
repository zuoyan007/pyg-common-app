package pyg.daheng.base.util.invoke.util;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:22
 */
public class DXKafkaBytesConsumer extends DXKafkaConsumer<byte[]> {

    public DXKafkaBytesConsumer(DXKafkaManager mgr) {
        super(mgr);
    }

    public boolean init() {
        if (this.bInitialized)
            return true;
        this.config.putParameter(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getCanonicalName());
        this.config.putParameter(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                ByteArrayDeserializer.class.getCanonicalName());
        this.bInitialized = true;
        return this.bInitialized;
    }
}

