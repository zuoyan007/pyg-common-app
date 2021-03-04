package pyg.daheng.kafka.model;

import lombok.Data;

/**
 * @Description:
 * @ClassName: NewTopic
 * @Author: ZhanSSH
 * @Date: 2021/1/20 16:32
 */
@Data
public class NewTopic {

    String topic;

    String numPartitions;

}
