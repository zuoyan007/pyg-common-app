package pyg.daheng.common.model.vo.util;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * @Description:
 * @ClassName: File2base64Vo
 * @Author: ZhanSSH
 * @Date: 2021/1/25 14:27
 */
@Data
@ApiModel(description = "文件流转成base64字符串入参接收对象")
public class File2base64Vo {

    @NotEmpty(message = "pdf1 不能为空")
    private String pdf1;

    @NotNull(message = "pdf2 不能为空")
    private File pdf2;

}
