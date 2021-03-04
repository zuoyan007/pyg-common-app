package pyg.daheng.common.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:
 * @ClassName: CheckVerifyCodeVo
 * @Author: ZhanSSH
 * @Date: 2021/1/18 17:37
 */
@Data
@ApiModel(description = "校验验证码入参接收对象")
public class CheckVerifyCodeVo {
    /**
     * 必填且必须符合手机号规则
     */
    @NotBlank(message = "手机号 phoneNumber 不能为空")
    @ApiModelProperty(name = "phoneNumber",value = "手机号",required = true)
    private String phoneNumber;

    /**
     * 验证码类型
     * vcType ==1 登录
     * vcType ==2 注册
     * vcType == 3 找回密码
     */
    @NotBlank(message = "验证码类型 vcType 不能为空")
    @ApiModelProperty(name = "vcType",value = "验证码类型 vcType ==1(登录); vcType ==2(注册); vcType == 3(找回密码)",required = true)
    private String vcType;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码 vcCode 不能为空")
    @ApiModelProperty(name = "vcCode",value = "验证码",required = true)
    private String vcCode;
}
