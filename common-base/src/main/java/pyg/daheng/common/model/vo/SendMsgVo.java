package pyg.daheng.common.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @ClassName: SendMsgVO
 * @Author: ZhanSSH
 * @Date: 2021/1/18 17:52
 */
@Data
public class SendMsgVo {
    /**
     * 手机
     */
    @NotBlank(message = "手机号 phoneNumber 不能为空")
    private String phoneNumber;
    /**
     * 短信验证码类型
     */
    private String vcType;

    /**
     * 短信类型
     * smsMsgType
     * 当smsMsgType== 1 时 为短信验证码，vcType必填
     * 当smsMsgType== 2 时 为普通通知短信，msgContent必填
     */
    @NotNull(message = "短信类型 smsMsgType 不能为空")
    private Integer smsMsgType;

    /**短信类容*/
    //private String msgContent;

    /**
     * 信息类型
     * 1短信
     * 2邮件
     */
    @NotNull(message = "信息类型 infoType 不能为空")
    private Integer infoType;

    public Integer getSmsMsgType() {
        return smsMsgType == null ? 1 : smsMsgType;
    }

    public Integer getInfoType() {
        return infoType == null ? 1 : infoType;
    }
}
