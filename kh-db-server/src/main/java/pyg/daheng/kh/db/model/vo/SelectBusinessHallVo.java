package pyg.daheng.kh.db.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description:
 * @ClassName: SelectBusinessHall
 * @Author: ZhanSSH
 * @Date: 2021/1/26 11:31
 */
@Data
@ApiModel(description = "查询营业厅信息接收对象")
public class SelectBusinessHallVo{

    private String jlbs;

    private String yytbmnw;

    private String yytmcnw;

    private String yytbmzzzd;

    private String yytmczzzd;

    private String gddwbm;

    private String dqbm;

    private String cjsj;

    private String czsj;

    private String ylzda;

    private String ylzdb;

    private String ylzdc;

    private String bz;
}
