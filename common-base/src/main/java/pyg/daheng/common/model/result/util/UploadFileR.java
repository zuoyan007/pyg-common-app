package pyg.daheng.common.model.result.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @ClassName: UploadFileR
 * @Author: ZhanSSH
 * @Date: 2021/1/26 15:48
 */
@Data
@ApiModel(description = "批量上传文件返回接收对象")
public class UploadFileR {

    @ApiModelProperty(name = "index",value = "文件索引",required = true)
    private String index;

    @ApiModelProperty(name = "operateResult",value = "上传结果",required = true)
    private String uploadResult;

    @ApiModelProperty(name = "filePath",value = "上传文件存储位置",required = true)
    private String filePath;
}
