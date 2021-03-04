package pyg.daheng.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import pyg.daheng.common.model.BaseVo;

import javax.validation.constraints.NotEmpty;

/**
 * @Description:
 * @ClassName: GetUserInfoVo
 * @Author: ZhanSSH
 * @Date: 2021/1/15 16:48
 */
@Data
@ApiModel(description = "获取用户信息接收对象")
public class GetUserInfoVo extends BaseVo {

    @NotEmpty(message = "token 不能为空")
    @ApiModelProperty(name = "token",value = "登录凭据",required = true)
    private String token;

    @NotEmpty(message = "userId 不能为空")
    @ApiModelProperty(name = "userId",value = "用户标识",required = true)
    private String userId;
}
