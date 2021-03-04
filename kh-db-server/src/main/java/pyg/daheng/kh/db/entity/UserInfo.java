package pyg.daheng.kh.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description:
 * @ClassName: UserInfo
 * @Author: ZhanSSH
 * @Date: 2021/1/19 14:48
 */
@Data
@TableName(value = "USER_DETAIL_INFO")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 180410407296897961L;

    @TableField(value = "NAME")
    private String name;

    @TableField(value = "NICK_NAME")
    private String nickName;

    @TableField(value = "PHONE")
    private String phone;

    @TableField(value = "EMAIL")
    private String email;

    @TableField(value = "WECHAT_ID")
    private String weChatId;

    @TableField(value = "USER_ID")
    @TableId
    private String userId;

    @TableField(value = "ADDRESS")
    private String address;

    @TableField(value = "CREATE_TIME")
    private Timestamp createTime;

    @TableField(value = "UPDATE_TIME")
    private Timestamp updateTime;

    @TableField(value = "IS_DELETED")
    private String isDeleted;

    @TableField(value = "IS_VIP")
    private String isVip;

    @TableField(value = "ID_NUMBER")
    private String idNumber;

}
