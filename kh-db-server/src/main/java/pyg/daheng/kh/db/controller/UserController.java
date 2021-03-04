package pyg.daheng.kh.db.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.model.result.UserInfoR;
import pyg.daheng.common.model.vo.GetUserInfoVo;
import pyg.daheng.kh.db.dto.UserInfoDto;
import pyg.daheng.kh.db.manager.UserManager;
import pyg.daheng.kh.db.model.result.CreateUserInfoR;
import pyg.daheng.kh.db.model.result.UpdateUserInfoR;
import pyg.daheng.kh.db.model.vo.CreateUserInfoVo;
import pyg.daheng.kh.db.model.vo.SelectCountUserVo;
import pyg.daheng.kh.db.model.vo.UpdateUserInfoVo;

/**
 * @Description:
 * @ClassName: UserController
 * @Author: ZhanSSH
 * @Date: 2021/1/19 11:03
 */
@Slf4j
@RestController
@RequestMapping("/center")
@Api("用户信息相关API")
public class UserController {

    @Autowired
    private UserManager userManager;

    @ApiOperation(value = "查询是否有用户信息",notes = "查询是否有用户信息")
    @RequestMapping(value = "/selectCountUser",method = RequestMethod.POST)
    public R<Integer> selectCountUser(@RequestBody @Validated SelectCountUserVo vo){

        return userManager.selectCountUser(vo);
    }

    @ApiOperation(value = "获取用户信息",notes = "根据用户标识获取用户信息")
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public R<UserInfoR> getUserInfo(@RequestBody @Validated GetUserInfoVo vo){

        return userManager.getUserInfo(vo);
    }

    @ApiOperation(value = "注册新用户",notes = "注册新用户")
    @RequestMapping(value = "/createUserInfo",method = RequestMethod.POST)
    public R<CreateUserInfoR> createUserInfo(@RequestBody @Validated CreateUserInfoVo vo){
        UserInfoDto dto = new UserInfoDto();
        BeanUtils.copyProperties(vo,dto);
        return userManager.createUserInfo(dto);
    }

    @ApiOperation(value = "编辑用户信息",notes = "编辑用户信息")
    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    public R<UpdateUserInfoR> updateUserInfo(@RequestBody @Validated UpdateUserInfoVo vo){
        UserInfoDto dto = new UserInfoDto();
        BeanUtils.copyProperties(vo,dto);
        return userManager.updateUserInfo(dto);
    }

    @ApiOperation(value = "购买VIP更新信息",notes = "购买VIP更新信息")
    @RequestMapping(value = "/updateUserInfoAfterBuyVip",method = RequestMethod.POST)
    public R<UpdateUserInfoR> updateUserInfoAfterBuyVip(@RequestBody @Validated UpdateUserInfoVo vo){
        UserInfoDto dto = new UserInfoDto();
        BeanUtils.copyProperties(vo,dto);
        return userManager.updateUserInfoAfterBuyVip(dto);
    }

    @ApiOperation(value = "用户删除账号更新信息",notes = "用户删除账号更新信息")
    @RequestMapping(value = "/updateUserInfoAfterDeleted",method = RequestMethod.POST)
    public R<UpdateUserInfoR> updateUserInfoAfterDeleted(@RequestBody @Validated UpdateUserInfoVo vo){
        UserInfoDto dto = new UserInfoDto();
        BeanUtils.copyProperties(vo,dto);
        return userManager.updateUserInfoAfterDeleted(dto);
    }

}
