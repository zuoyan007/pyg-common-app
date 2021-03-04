package pyg.daheng.kafka.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @ClassName: SendSmsVo
 * @Author: ZhanSSH
 * @Date: 2021/1/22 9:41
 */
@Data
@ApiModel(value = "发送kafka消息入参接收对象")
public class SendSmsVo {

    @ApiModelProperty(name = "topicName",value = "订阅名称",required = true)
    @NotEmpty(message = "topicName 不能为空")
    private String topicName;

    @ApiModelProperty(name = "data",value = "发送的数据",required = true)
    @NotNull(message = "data 不能为空")
    private Object data;

}
