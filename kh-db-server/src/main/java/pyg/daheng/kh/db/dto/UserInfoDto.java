package pyg.daheng.kh.db.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Description:
 * @ClassName: UserInfoDto
 * @Author: ZhanSSH
 * @Date: 2021/1/19 15:19
 */
@Data
public class UserInfoDto {

    /**真实姓名*/
    private String name;
    /**昵称*/
    private String nickName;
    /**手机号*/
    private String phone;
    /**邮箱*/
    private String email;
    /**微信号*/
    private String weChatId;
    /**用户标识*/
    private String userId;
    /**地址*/
    private String address;
    /**创建时间*/
    private Timestamp createTime;
    /**更新时间*/
    private Timestamp updateTime;
    /**是否删除标识 0否，1是*/
    private String isDeleted;
    /**是否会有标识 0否，1是*/
    private String isVip;
    /**身份证号码*/
    private String idNumber;

}
