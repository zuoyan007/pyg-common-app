package pyg.daheng.common.constants.enums;

import lombok.Getter;

/**
 * @author ZhanSSH
 * @date 2021/2/25 17:24
 */
public enum SensitiveType {
    /** 手机号 */
    MOBILE("mobile"),
    /** 手机号 */
    MOBILE_PHONE("mobilePhone"),
    /** 电子邮箱 */
    EMAIL("email"),
    /** 用户号 */
    ELE_CUST_NUMBER("eleCustNumber"),
    /** 计量点 */
    METERING_POINT_NUMBER("meteringPointNumber"),
    /** 结算户号 */
    SETTLE_ACCT_NUMBER("settleAcctNumber"),
    /** 支付宝账户*/
    ALI_USER_NAME("aliUserName"),
    /** 用户姓名*/
    USER_NAME("userName"),
    /** 真实姓名*/
    REAL_NAME("realName"),
    /** 身份证号*/
    CERT_NUMBER("certNumber"),
    /** 资产编号*/
    DEVICEIDENTIF("deviceIdentif"),
    /** 银行账号 */
    BANKCARD("bankCard"),
    /** 结算户姓名 */
    SETTLE_ACCT_NAME("settleAcctName"),
    /** 我的工单-联系人 */
    CONTACTPERSON("contactPerson"),
    /** 我的工单-联系手机 */
    CONTACTPHONE("contactPhone"),
    /** 我的工单-停电报障-报障地址 */
    FAULTPOWFAILADDR("faultPowFailAddr"),
    /** 用电地址 */
    ELEADDRESS("eleAddress"),
    /** 计量点地址 */
    METERING_POINT_ADDRESS("meteringPointAddress"),
    /** 结算户地址 */
    SETTLE_ACCT_ADDRESS("settleAcctAddress"),
    /** 我的工单-用户名称 */
    ELECUSTNAME("eleCustName"),
    /** 我的工单-欠费复电-申请人电话 */
    APPLICANTPHONE("applicantPhone"),
    /** 不脱敏 */
    DISABLE("SensitiveDisable"),
    /**none */
    NONE(""),

    ;
    @Getter
    private String value;

    SensitiveType(String value){
        this.value = value;
    }
}
