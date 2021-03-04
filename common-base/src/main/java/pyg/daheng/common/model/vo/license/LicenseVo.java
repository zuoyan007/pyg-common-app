package pyg.daheng.common.model.vo.license;

import lombok.Data;

/**
 * @Description:
 * @ClassName: LicenseVo
 * @Author: ZhanSSH
 * @Date: 2020/12/4 10:35
 */
@Data
public class LicenseVo {

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
