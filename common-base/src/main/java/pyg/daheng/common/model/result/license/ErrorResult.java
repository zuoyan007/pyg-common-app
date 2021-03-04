package pyg.daheng.common.model.result.license;

import lombok.Data;

/**
 * @Description:
 * @ClassName: ErrorResult
 * @Author: ZhanSSH
 * @Date: 2020/12/25 12:29
 */
@Data
public class ErrorResult {
    private String errCode;
    private String errMsg;
}
