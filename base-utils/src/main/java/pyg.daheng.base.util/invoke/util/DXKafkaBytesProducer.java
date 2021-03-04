package pyg.daheng.base.util.invoke.util;

import cn.csg.lib.kafka.DXKafkaManager;
import cn.csg.lib.kafka.DXKafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:22
 */
public class DXKafkaBytesProducer extends DXKafkaProducer<byte[]> {

    public DXKafkaBytesProducer(DXKafkaManager mgr) {
        super(mgr);
    }

    @Override
    public boolean init() {
        if (this.bInitialized){
            return true;
        }
        this.config.putParameter(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        this.config.putParameter(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                ByteArraySerializer.class.getCanonicalName());
        this.bInitialized = true;
        return this.bInitialized;
    }

}
