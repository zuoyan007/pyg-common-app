package pyg.daheng.common.constants;

import lombok.Data;

/**
 * @Description:
 * @ClassName: SessionKeys
 * @Author: ZhanSSH
 * @Date: 2021/1/15 17:27
 */
@Data
public class SessionKeys {
    private SessionKeys(){}

    /**登录*/
    public static String LOGIN_USER = "login-user";

    /**会员标识*/
    public static String SUPPER_VIP = "supper-vip";
}
