package pyg.daheng.base.util.invoke.util;

import cn.csg.lib.common.util.ResourceUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:41
 */
public class QueueMonitor extends BaseThread{
    private static final Logger log = LoggerFactory.getLogger(QueueMonitor.class);
    private static DXQueueManager qManager = DXQueueManager.getInstance();
    private static long queueSize = MonitorConfig.getQueueSize();
    private static Properties kafkaProperties;
    private static DXKafkaManager manager;
    private static DXKafkaStringProducer producer;
    private StringBuilder buffer;

    public QueueMonitor() {
        this.init();
    }

    @Override
    public void init() {
        this.buffer = new StringBuilder();
    }

    @Override
    protected boolean dowork() {
        IQueueItem item = null;
        String itemToString = null;

        try {
            HashMap<String, BlockingQueue<IQueueItem>> queueMap = qManager.getQueueMap();
            Iterator var4 = queueMap.values().iterator();

            while(true) {
                BlockingQueue queue;
                do {
                    if (!var4.hasNext()) {
                        return false;
                    }

                    queue = (BlockingQueue)var4.next();
                } while(queue.size() <= 0);

                this.buffer.setLength(0);
                item = (IQueueItem)queue.element();

                for(long i = 0L; i < (long)queue.size() && i < queueSize; ++i) {
                    item = (IQueueItem)queue.take();
                    itemToString = JSON.toJSONString(item, new SerializerFeature[]{SerializerFeature.WriteMapNullValue});
                    if (item.getSimpleName().equals(DXTransItem.class.getSimpleName())) {
                        this.buffer.append(itemToString);
                        this.buffer.append("\n");
                    }

                    if (producer != null) {
                        producer.pushNotClose(item.getTopic(), itemToString);
                    }

                    log.debug(item.getSimpleName() + "::" + itemToString);
                }

                if (item.getSimpleName().equals(DXTransItem.class.getSimpleName())) {
                    SaveDXTransItem.saveDXTransItemToFile(this.buffer.toString());
                }
            }
        } catch (Exception var11) {
            log.error("### QueueMonitor run() ###", var11);
        } finally {
            if (producer != null) {
                producer.flush();
            }

        }

        return false;
    }

    static {
        kafkaProperties = ResourceUtil.map2Properties(MonitorConfig.configs);
        manager = DXKafkaManager.getInstance();
        producer = manager.buildKafkaStringProducer(kafkaProperties);
    }
}
