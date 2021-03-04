package pyg.daheng.common.model.vo.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 *
 * @author ZhanSSH
 * @date 2021/1/18 9:30
 */
@Data
@ApiModel(description = "登录接口入参接收对象")
public class LoginVo {

    @NotEmpty(message = "accessToken 不能为空")
    @ApiModelProperty(name = "accessToken",value = "登录凭据",required = true)
    private String accessToken;

}
