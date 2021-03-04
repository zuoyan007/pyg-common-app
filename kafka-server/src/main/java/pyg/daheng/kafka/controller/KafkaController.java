package pyg.daheng.kafka.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pyg.daheng.common.constants.R;
import pyg.daheng.kafka.manager.KafkaManager;
import pyg.daheng.kafka.model.vo.CreateTopicVo;
import pyg.daheng.kafka.model.vo.SendSmsVo;

/**
 * @Description:
 * @ClassName: KafkaController
 * @Author: ZhanSSH
 * @Date: 2021/1/20 14:40
 */
@RestController
@RequestMapping("/kafka")
@Slf4j
@Api(value = "操作kafka相关API")
public class KafkaController {

    @Autowired
    private KafkaManager kafkaManager;

    @RequestMapping("/createTopic")
    @ApiOperation(value = "新建topic",notes = "新建topic")
    public R<String> createTopic(@RequestBody @Validated CreateTopicVo vo){

        return kafkaManager.createTopic(vo);
    }
    @RequestMapping("/sendSms")
    @ApiOperation(value = "发送消息",notes = "发送消息")
    public R<String> sendSms(@RequestBody @Validated SendSmsVo vo){

        return kafkaManager.sendSms(vo);
    }

}
