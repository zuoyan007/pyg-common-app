package pyg.daheng.kh.db.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Description:
 * @ClassName: SelectBusinessHallR
 * @Author: ZhanSSH
 * @Date: 2021/1/26 11:50
 */
@Data
@ApiModel(value = "查询营业厅信息返回对象")
public class SelectBusinessHallR {

    @ApiModelProperty(name = "jlbs",value = "营业厅唯一标识",required = true)
    private String jlbs;

    @ApiModelProperty(name = "yytbmnw",value = "营业厅编码",required = true)
    private String yytbmnw;

    @ApiModelProperty(name = "yytmcnw",value = "营业厅名称",required = true)
    private String yytmcnw;

    @ApiModelProperty(name = "yytbmzzzd",value = "营业厅标识",required = true)
    private String yytbmzzzd;

    @ApiModelProperty(name = "yytmczzzd",value = "营业厅名称",required = true)
    private String yytmczzzd;

    @ApiModelProperty(name = "gddwbm",value = "供电单位编码",required = true)
    private String gddwbm;

    @ApiModelProperty(name = "dqbm",value = "地区编码",required = true)
    private String dqbm;

    @ApiModelProperty(name = "cjsj",value = "时间",required = true)
    private String cjsj;

    @ApiModelProperty(name = "czsj",value = "操作时间",required = true)
    private String czsj;

    @ApiModelProperty(name = "ylzda",value = "？？？",required = true)
    private String ylzda;

    @ApiModelProperty(name = "ylzdb",value = "？？？",required = true)
    private String ylzdb;

    @ApiModelProperty(name = "ylzdc",value = "？？？",required = true)
    private String ylzdc;

    @ApiModelProperty(name = "bz",value = "备注",required = true)
    private String bz;
}
