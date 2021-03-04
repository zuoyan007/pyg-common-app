package pyg.daheng.common.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pyg.daheng.common.annotation.AuthAnonymousLogin;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.constants.ResultCode;
import pyg.daheng.common.manager.CommonManager;
import pyg.daheng.common.manager.impl.BaseManager;
import pyg.daheng.common.model.result.CheckVerifyCodeR;
import pyg.daheng.common.model.result.SendMsgR;
import pyg.daheng.common.model.result.UserInfoR;
import pyg.daheng.common.model.vo.CheckVerifyCodeVo;
import pyg.daheng.common.model.vo.GetUserInfoVo;
import pyg.daheng.common.model.vo.SendMsgVo;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ZhanSSH
 * @date 2021/1/14 11:20
 */
@Slf4j
@RestController
@RequestMapping("/base")
@Api(description = "基础通用API")
@AuthAnonymousLogin
public class BaseController extends BaseManager {

    @Autowired
    private CommonManager commonManager;

    @ApiOperation(value = "获取用户信息",notes = "根据用户标识获取用户信息")
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public R<UserInfoR> getUserInfo(@RequestBody @Validated GetUserInfoVo vo){

        return commonManager.getUserInfo(vo);
    }

    @ApiOperation(value = "发送短信验证码",notes = "发送短信验证码")
    @RequestMapping(value = "/sendMsg",method = RequestMethod.POST)
    public R<SendMsgR> sendMsg(@RequestBody @Validated SendMsgVo vo,HttpServletRequest request){
        String ip = getIpAddr(request);
        log.info("#######################当前登录用户 getIpAddr  ip " + ip);
        if (checkVctype(vo.getVcType())) {
            return R.paramsError("参数异常");
        }
        return commonManager.sendMsg(vo,ip);
    }

    @ApiOperation(value = "校验验证码",notes = "校验验证码")
    @RequestMapping(value = "/checkVerifyCode",method = RequestMethod.POST)
    public R<CheckVerifyCodeR> checkVerifyCode(@RequestBody @Validated CheckVerifyCodeVo vo){

        return commonManager.checkVerifyCode(vo);
    }

    /**
     * 获取IP地址
     * @param request 入参
     * @return 出参
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            if (log.isDebugEnabled()) {
                log.debug("#getIpAddr# X-Real-IP");
            }
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            if (log.isDebugEnabled()) {
                log.debug("#getIpAddr# X-Forwarded-For");
            }
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("#getIpAddr# getRemoteAddr");
            }
            return request.getRemoteAddr();
        }
    }

    /**校验是否符合类型取值范围**/
    public boolean checkVctype(String vcType) {
        if (!vcType.equals(ResultCode.VC_TYPE_ONE) && !vcType.equals(ResultCode.VC_TYPE_TWO)
                && !vcType.equals(ResultCode.VC_TYPE_THREE) && !vcType.equals(ResultCode.VC_TYPE_FOUR)
                && !vcType.equals(ResultCode.VC_TYPE_FIVE) && !vcType.equals(ResultCode.VC_TYPE_SIX)
                && !vcType.equals(ResultCode.VC_TYPE_SERVIE) && !vcType.equals(ResultCode.VC_TYPE_ENITH)) {
            return true;
        }
        return false;
    }

}
