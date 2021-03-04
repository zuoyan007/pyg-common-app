package pyg.daheng.common.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.manager.LoginManager;
import pyg.daheng.common.model.result.login.LoginR;
import pyg.daheng.common.model.vo.login.LoginVo;

/**
 *
 * @author ZhanSSH
 * @date 2021/1/18 9:35
 */
@Service
@Slf4j
public class LoginManagerImpl extends BaseManager implements LoginManager {

    @Override
    public R<LoginR> login(LoginVo vo) {

        return R.success(LoginR.builder()
        .mobile("13800138000")
        .result("success")
        .userName("张三")
        .realName("张扬")
        .build());

    }
}
