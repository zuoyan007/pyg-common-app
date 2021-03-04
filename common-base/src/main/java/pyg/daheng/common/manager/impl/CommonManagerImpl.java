package pyg.daheng.common.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.manager.CommonManager;
import pyg.daheng.common.model.result.CheckVerifyCodeR;
import pyg.daheng.common.model.result.SendMsgR;
import pyg.daheng.common.model.result.UserInfoR;
import pyg.daheng.common.model.vo.CheckVerifyCodeVo;
import pyg.daheng.common.model.vo.GetUserInfoVo;
import pyg.daheng.common.model.vo.SendMsgVo;

/**
 * @Description:
 * @ClassName: CommonManagerImpl
 * @Author: ZhanSSH
 * @Date: 2021/1/19 17:23
 */
@Service
@Slf4j
public class CommonManagerImpl extends BaseManager implements CommonManager {

    @Override
    public R<UserInfoR> getUserInfo(GetUserInfoVo vo) {

        return null;
    }

    @Override
    public R<CheckVerifyCodeR> checkVerifyCode(CheckVerifyCodeVo vo) {
        return null;
    }

    @Override
    public R<SendMsgR> sendMsg(SendMsgVo vo, String ip) {
        return null;
    }

}
