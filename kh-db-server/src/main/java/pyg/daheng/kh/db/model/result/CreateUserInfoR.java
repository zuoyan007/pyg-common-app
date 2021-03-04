package pyg.daheng.kh.db.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @ClassName: CreateUserInfoR
 * @Author: ZhanSSH
 * @Date: 2021/1/19 16:23
 */
@Data
@ApiModel(value = "注册新用户返回对象")
@Builder
public class CreateUserInfoR {

    @ApiModelProperty(name = "operateResult",value = "操作结果",required = true)
    private String operateResult;
}
