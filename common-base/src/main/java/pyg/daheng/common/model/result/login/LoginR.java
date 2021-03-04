package pyg.daheng.common.model.result.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author ZhanSSH
 * @date 2021/1/18 9:32
 */
@Data
@ApiModel(description = "登录接口返回对象")
@Builder
public class LoginR implements Serializable{

    @ApiModelProperty(name = "result",value = "登录验证结果",required = true)
    private String result;

    private String userName;

    private String mobile;

    private String realName;

}
