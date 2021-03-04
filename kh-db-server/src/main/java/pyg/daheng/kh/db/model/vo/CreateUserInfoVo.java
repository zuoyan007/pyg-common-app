package pyg.daheng.kh.db.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Description:
 * @ClassName: CreateUserInfoVo
 * @Author: ZhanSSH
 * @Date: 2021/1/19 16:22
 */
@Data
@ApiModel(description = "注册新用户接收对象")
public class CreateUserInfoVo implements Serializable{

    @NotEmpty(message = "name 不能为空")
    @ApiModelProperty(name = "name",value = "真实姓名",required = true)
    private String name;

    @NotEmpty(message = "nickName 不能为空")
    @ApiModelProperty(name = "nickName",value = "昵称",required = true)
    private String nickName;

    @NotEmpty(message = "phone 不能为空")
    @ApiModelProperty(name = "phone",value = "手机号",required = true)
    private String phone;

    @ApiModelProperty(name = "email",value = "邮箱")
    private String email;

    @ApiModelProperty(name = "weChatId",value = "微信号")
    private String weChatId;

    @ApiModelProperty(name = "userId",value = "用户标识")
    private String userId;

    @ApiModelProperty(name = "address",value = "详细地址")
    private String address;

}
