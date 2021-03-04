package pyg.daheng.common.constants;

import lombok.Data;

/**
 * @Description:响应状态码
 * @ClassName: ResultCode
 * @Author: ZhanSSH
 * @Date: 2021/1/15 17:30
 */
@Data
public class ResultCode {

    private ResultCode(){}

    public static final String CODE_NORMAL = "00";
    public static final String CODE_ERROR_01 = "01";
    public static final String CODE_ERROR_02 = "02";
    public static final String CODE_ERROR_03 = "03";
    public static final String CODE_ERROR_04 = "04";
    public static final String CODE_ERROR_09 = "09";
    public static final String CODE_ERROR = "99";
    public static final String CODE_ERROR_100 = "100";

    /** 登录替换同一用户的旧的session时，旧的用户再次访问请求的响应代码 */
    public static final String DEL_OLD_SESSION = "0408";
    // 用户没有其他的登录方式
    public final static String ELE_CUST_NUMBER_NO_WAY = "1010";
    // 用户还有其他的登录方式
    public final static String ELE_CUST_NUMBER_YES_WAY = "0101";
    // 用户注销下线
    public final static String CANCEL_ACCOUNT_WAY = "0201";

    public static final String VC_TYPE_ONE="1";
    public static final String VC_TYPE_TWO="2";
    public static final String VC_TYPE_THREE="3";
    public static final String VC_TYPE_FOUR="4";
    public static final String VC_TYPE_FIVE="5";
    public static final String VC_TYPE_SIX="6";
    public static final String VC_TYPE_SERVIE="7";
    public static final String VC_TYPE_ENITH="8";

}
