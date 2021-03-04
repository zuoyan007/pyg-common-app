package pyg.daheng.kafka.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description:
 * @ClassName: CreateTopicVo
 * @Author: ZhanSSH
 * @Date: 2021/1/20 15:17
 */
@Data
@ApiModel("创建topic入参接收对象")
public class CreateTopicVo {
    private String name;
}
