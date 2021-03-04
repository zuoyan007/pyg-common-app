package pyg.daheng.kh.db.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Description:
 * @ClassName: SelectCountUserVo
 * @Author: ZhanSSH
 * @Date: 2021/1/20 10:14
 */
@Data
@ApiModel(description = "查询是否有用户信息接收对象")
public class SelectCountUserVo {

    @NotEmpty(message = "userId 不能为空")
    @ApiModelProperty(name = "userId",value = "用户标识",required = true)
    private String userId;

}
