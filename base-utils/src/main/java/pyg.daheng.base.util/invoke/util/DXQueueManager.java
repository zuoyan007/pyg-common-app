package pyg.daheng.base.util.invoke.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:43
 */
public class DXQueueManager {
    private static final HashMap<String, BlockingQueue<IQueueItem>> queueMap = new HashMap();
    private static final Logger logger = LoggerFactory.getLogger(DXQueueManager.class);
    private static final int DEFAULT_SIZE = 10000;
    private static volatile DXQueueManager instance;

    public static DXQueueManager getInstance() {
        if (instance == null) {
            Class var0 = DXQueueManager.class;
            synchronized(DXQueueManager.class) {
                if (instance == null) {
                    instance = new DXQueueManager();
                    logger.info("QueueManager Singleton Instance created.");
                }
            }
        }

        return instance;
    }

    public HashMap<String, BlockingQueue<IQueueItem>> getQueueMap() {
        return queueMap;
    }

    private DXQueueManager() {
    }

    public void add(IQueueItem queueItem) {
        BlockingQueue<IQueueItem> queue = this.createQueue(queueItem);
        queue.add(queueItem);
    }

    public BlockingQueue<IQueueItem> createQueue(IQueueItem queueItem) {
        String classPath = this.getClassPath(queueItem);
        if (classPath == null) {
            return null;
        } else if (queueMap.get(classPath) == null) {
            BlockingQueue<IQueueItem> queue = new LinkedBlockingDeque(10000);
            queueMap.put(classPath, queue);
            return queue;
        } else {
            return (BlockingQueue)queueMap.get(classPath);
        }
    }

    private String getClassPath(IQueueItem queueItem) {
        return queueItem == null ? null : queueItem.getClass().getCanonicalName();
    }

    public BlockingQueue<IQueueItem> getQueue(IQueueItem queueItem) {
        String classPath = this.getClassPath(queueItem);
        if (classPath == null) {
            return null;
        } else {
            return queueMap.get(classPath) == null ? null : (BlockingQueue)queueMap.get(classPath);
        }
    }

    public int size(IQueueItem queueItem) {
        BlockingQueue<IQueueItem> queue = this.getQueue(queueItem);
        return queue == null ? 0 : queue.size();
    }

    public IQueueItem take(IQueueItem queueItem) {
        IQueueItem output = null;

        try {
            if (this.size(queueItem) > 0) {
                BlockingQueue<IQueueItem> queue = this.getQueue(queueItem);
                output = (IQueueItem)queue.take();
            }
        } catch (InterruptedException var4) {
            Thread.currentThread().interrupt();
            logger.error(var4.getMessage(), var4);
        }

        return output;
    }
}
