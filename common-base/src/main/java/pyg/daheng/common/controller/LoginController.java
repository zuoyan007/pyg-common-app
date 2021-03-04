package pyg.daheng.common.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pyg.daheng.common.annotation.LimitAnnotation;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.manager.LoginManager;
import pyg.daheng.common.model.result.login.LoginR;
import pyg.daheng.common.model.vo.login.LoginVo;

/**
 *
 * @author ZhanSSH
 * @date 2021/1/18 9:29
 */
@Slf4j
@RestController
@RequestMapping("/center")
@Api(description = "登录相关API")
@LimitAnnotation(key = "LoginController 登录#")
public class LoginController {

    @Autowired
    private LoginManager loginManager;

    @ApiOperation(value = "登录",notes = "登录接口")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public R<LoginR> login(@RequestBody @Validated LoginVo vo){

        return loginManager.login(vo);
    }
}
