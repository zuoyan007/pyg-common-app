package pyg.daheng.kh.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description:
 * @ClassName: BusinessHallInfo
 * @Author: ZhanSSH
 * @Date: 2021/1/26 11:33
 */
@Data
@TableName(value = "BUSINESS_HALL_NUMBER_INFO")
public class BusinessHallInfo {

    private static final long serialVersionUID = 180410407296897961L;

    @TableId
    @TableField(value = "JLBS")
    private String jlbs;

    @TableField(value = "YYTBMNW")
    private String yytbmnw;

    @TableField(value = "YYTMCNW")
    private String yytmcnw;

    @TableField(value = "YYTBMZZZD")
    private String yytbmzzzd;

    @TableField(value = "YYTMCZZZD")
    private String yytmczzzd;

    @TableField(value = "GDDWBM")
    private String gddwbm;

    @TableField(value = "DQBM")
    private String dqbm;

    @TableField(value = "CJSJ")
    private String cjsj;

    @TableField(value = "CZSJ")
    private String czsj;

    @TableField(value = "YLZDA")
    private String ylzda;

    @TableField(value = "YLZDB")
    private String ylzdb;

    @TableField(value = "YLZDC")
    private String ylzdc;

    @TableField(value = "BZ")
    private String bz;

}
