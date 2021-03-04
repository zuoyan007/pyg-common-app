package pyg.daheng.kh.db.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @ClassName: UpdateUserInfoR
 * @Author: ZhanSSH
 * @Date: 2021/1/19 17:49
 */
@Data
@ApiModel(value = "编辑用户信息返回对象")
@Builder
public class UpdateUserInfoR {

    @ApiModelProperty(name = "operateResult",value = "操作结果",required = true)
    private String operateResult;

}
