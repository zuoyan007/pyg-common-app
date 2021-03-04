package pyg.daheng.common.manager;

import pyg.daheng.common.constants.R;
import pyg.daheng.common.model.result.login.LoginR;
import pyg.daheng.common.model.vo.login.LoginVo;


/**
 *
 * @author ZhanSSH
 * @date 2021/1/18 9:35
 */
public interface LoginManager {

    /**
     * 登录
     * @param vo 入参
     * @return 出参
     */
    R<LoginR> login(LoginVo vo);
}
