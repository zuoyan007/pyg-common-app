package pyg.daheng.kafka.manager.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import pyg.daheng.common.constants.R;
import pyg.daheng.kafka.manager.KafkaManager;
import pyg.daheng.kafka.model.vo.CreateTopicVo;
import pyg.daheng.kafka.model.vo.SendSmsVo;

/**
 * @Description:
 * @ClassName: KafkaManagerImpl
 * @Author: ZhanSSH
 * @Date: 2021/1/20 14:41
 */
@Slf4j
@Service
public class KafkaManagerImpl implements KafkaManager {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public R<String> createTopic(CreateTopicVo vo) {

        return null;
    }

    @Override
    public R<String> sendSms(SendSmsVo vo) {
        ListenableFuture future = kafkaTemplate.send(vo.getTopicName(), JSON.toJSONString(vo.getData()));
        return R.success(JSON.toJSONString(future));
    }
}
