package pyg.daheng.common.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.model.result.CheckVerifyCodeR;
import pyg.daheng.common.model.result.SendMsgR;
import pyg.daheng.common.model.result.UserInfoR;
import pyg.daheng.common.model.vo.CheckVerifyCodeVo;
import pyg.daheng.common.model.vo.GetUserInfoVo;
import pyg.daheng.common.model.vo.SendMsgVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description:
 * @ClassName: BaseManager
 * @Author: ZhanSSH
 * @Date: 2021/1/15 16:44
 */
@Slf4j
@Component
public class BaseManager{
    @Autowired
    protected HttpSession session;
    @Autowired
    protected HttpServletRequest request;
}
