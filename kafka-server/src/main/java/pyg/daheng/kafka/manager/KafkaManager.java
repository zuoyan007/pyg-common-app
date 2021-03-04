package pyg.daheng.kafka.manager;

import pyg.daheng.common.constants.R;
import pyg.daheng.kafka.model.vo.CreateTopicVo;
import pyg.daheng.kafka.model.vo.SendSmsVo; /**
 * @Description:
 * @ClassName: KafkaManager
 * @Author: ZhanSSH
 * @Date: 2021/1/20 14:41
 */
public interface KafkaManager {

    /**
     * 创建topic
     * @param vo
     * @return
     */
    R<String> createTopic(CreateTopicVo vo);

    /**
     * 发送消息
     * @param vo
     * @return
     */
    R<String> sendSms(SendSmsVo vo);
}
