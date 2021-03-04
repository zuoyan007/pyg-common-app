package pyg.daheng.kafka.constants;

/**
 * @Description:
 * @ClassName: KafkaOffsetReset
 * @Author: ZhanSSH
 * @Date: 2021/1/20 18:09
 */
public enum KafkaOffsetReset {
    /*
        earliest
        当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        latest
        当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        none
        topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常*/
    EARLIEST("earliest"),LATEST("latest"),NONE("none");

    private final String offsetReset;

    KafkaOffsetReset(String offsetReset) {
        this.offsetReset = offsetReset;
    }

    public String getOffsetReset() {
        return offsetReset;
    }
}
