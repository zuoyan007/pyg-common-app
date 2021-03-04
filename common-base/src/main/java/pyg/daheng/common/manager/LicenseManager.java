package pyg.daheng.common.manager;

import pyg.daheng.common.constants.MsAppResponse;
import pyg.daheng.common.model.vo.license.LicenseVo; /**
 * @author ZhanSSH
 * @date 2021/2/7 12:10
 */
public interface LicenseManager {

    MsAppResponse getLicense(LicenseVo vo);
}
