package pyg.daheng.common.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Description:
 * @ClassName: UserInfoR
 * @Author: ZhanSSH
 * @Date: 2021/1/15 16:54
 */
@Data
@ApiModel(value = "用户信息返回对象")
public class UserInfoR {

    @ApiModelProperty(name = "nickName",value = "昵称",required = true)
    private String nickName;

    @ApiModelProperty(name = "name",value = "真实名称",required = true)
    private String name;

    @ApiModelProperty(name = "phone",value = "手机号码",required = true)
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
