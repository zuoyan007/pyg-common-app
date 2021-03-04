package pyg.daheng.kafka.annotation;

import pyg.daheng.kafka.constants.KafkaOffsetReset;

import java.lang.annotation.*;

/**
 * @Description:
 * @ClassName: KafkaListener
 * @Author: ZhanSSH
 * @Date: 2021/1/20 18:06
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KafkaListener {
    /**
     * topic数组
     */
    String[] topics();

    /**
     * 消费者Id
     */
    String groupId();

    /**
     * 线程数，一般等于分区数,默认单线程
     */
    String concurrency() default "1";

    /**
     * 新加的组从什么地方开始消费，默认从最后地方消费
     */
    KafkaOffsetReset offsetReset() default KafkaOffsetReset.LATEST;

    /**
     * 是否自动提交，默认手动提交
     */
    boolean isAutomatic() default false;

    /**
     * 是否批量消费，默认为单条消费
     */
    boolean isBatch() default false;

    /**
     * isBatch为true时才生效，为一次批量消费多少条记录，默认500条
     */
    int maxPollRecords() default 500;
}
