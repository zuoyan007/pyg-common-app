package pyg.daheng.kh.db.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Description:
 * @ClassName: UpdateUserInfoVo
 * @Author: ZhanSSH
 * @Date: 2021/1/19 17:48
 */
@Data
public class UpdateUserInfoVo {

    @ApiModelProperty(name = "name",value = "真实姓名")
    private String name;

    @ApiModelProperty(name = "nickName",value = "昵称")
    private String nickName;

    @ApiModelProperty(name = "phone",value = "手机号")
    private String phone;

    @ApiModelProperty(name = "email",value = "邮箱")
    private String email;

    @NotEmpty(message = "userId 不能为空")
    @ApiModelProperty(name = "userId",value = "用户标识",required = true)
    private String userId;

    @ApiModelProperty(name = "weChatId",value = "微信号")
    private String weChatId;

    @ApiModelProperty(name = "address",value = "详细地址")
    private String address;
}
