package pyg.daheng.common.constants;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description:
 * @ClassName: CommonResponse
 * @Author: ZhanSSH
 * @Date: 2021/1/15 17:18
 */
@Data
@ApiModel(value = "通用返回")
public class CommonResponse {

    private String sta;
    private String message;
    private Object data;

    public CommonResponse(String sta) {
        this.sta = sta;
    }

    public CommonResponse(String sta, String message) {
        this.sta = sta;
        this.message = message;
    }

    public CommonResponse() {
    }

    public CommonResponse(String sta, String message, Object data) {
        this.sta = sta;
        this.message = message;
        this.data = data;
    }
}
