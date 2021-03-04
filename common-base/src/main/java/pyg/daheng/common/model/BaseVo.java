package pyg.daheng.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Description:
 * @ClassName: BaseVo
 * @Author: ZhanSSH
 * @Date: 2021/1/15 16:47
 */
@Data
@ApiModel(value = "基础入参对象")
public class BaseVo {

    @ApiModelProperty(name = "areaCode",value = "地区编码",required = true)
    @NotEmpty(message = "areaCode 不能为空")
    private String areaCode;
}
