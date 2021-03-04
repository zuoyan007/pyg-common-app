package pyg.daheng.common.manager;

import pyg.daheng.common.constants.R;
import pyg.daheng.common.model.result.CheckVerifyCodeR;
import pyg.daheng.common.model.result.SendMsgR;
import pyg.daheng.common.model.result.UserInfoR;
import pyg.daheng.common.model.vo.CheckVerifyCodeVo;
import pyg.daheng.common.model.vo.GetUserInfoVo;
import pyg.daheng.common.model.vo.SendMsgVo;


/**
 * @Description:
 * @ClassName: CommonManager
 * @Author: ZhanSSH
 * @Date: 2021/1/19 17:22
 */
public interface CommonManager {

    /**
     * 获取用户信息
     * @param vo 入参
     * @return 出参
     */
    R<UserInfoR> getUserInfo(GetUserInfoVo vo);

    /**
     * 校验 验证码
     * @param vo 入参
     * @return 出参
     */
    R<CheckVerifyCodeR> checkVerifyCode(CheckVerifyCodeVo vo);

    /**
     * 发送短信验证码
     * @param vo 入参
     * @param ip ip
     * @return 出参
     */
    R<SendMsgR> sendMsg(SendMsgVo vo, String ip);
}
