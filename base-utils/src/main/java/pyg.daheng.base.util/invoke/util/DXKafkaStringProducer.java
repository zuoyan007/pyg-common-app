package pyg.daheng.base.util.invoke.util;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:54
 */
public class DXKafkaStringProducer extends DXKafkaProducer<String> {

    public DXKafkaStringProducer(DXKafkaManager mgr) {
        super(mgr);
    }

    @Override
    public boolean init() {
        if (this.bInitialized){
            return true;
        }
        this.config.putParameter(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        this.config.putParameter(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getCanonicalName());
        this.bInitialized = true;
        return this.bInitialized;
    }

}

