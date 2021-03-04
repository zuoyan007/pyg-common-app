package pyg.daheng.common.model.result.license;

import lombok.Data;

/**
 * @Description:
 * @ClassName: LicenseRequest
 * @Author: ZhanSSH
 * @Date: 2020/12/4 10:35
 */
@Data
public class LicenseRequest {

    /**
     * 证照类型代码
     */
    private String typeCode;

    /**
     * 持证主体类型代码
     */
    private String ownerTypeCode;

    /**
     * 持证主体编号
     */
    private String ownerCode;

}
