package pyg.daheng.kh.db.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pyg.daheng.common.constants.CommonConstants;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.constants.RCodeEnums;
import pyg.daheng.common.model.result.UserInfoR;
import pyg.daheng.common.model.vo.GetUserInfoVo;
import pyg.daheng.common.utils.DateUtils;
import pyg.daheng.kh.db.dto.UserInfoDto;
import pyg.daheng.kh.db.entity.UserInfo;
import pyg.daheng.kh.db.manager.UserManager;
import pyg.daheng.kh.db.mapper.UserMapper;
import pyg.daheng.kh.db.model.result.CreateUserInfoR;
import pyg.daheng.kh.db.model.result.UpdateUserInfoR;
import pyg.daheng.kh.db.model.vo.SelectCountUserVo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @ClassName: UserManagerImpl
 * @Author: ZhanSSH
 * @Date: 2021/1/19 11:07
 */
@Service
@Slf4j
public class UserManagerImpl implements UserManager{

    @Autowired
    private UserMapper userMapper;

    @Override
    public R<UserInfoR> getUserInfo(GetUserInfoVo vo) {
        List<UserInfo> userInfoList = userMapper.selectList(new QueryWrapper<UserInfo>().lambda()
                 .eq(UserInfo::getUserId,vo.getUserId())
                 .eq(UserInfo::getIsDeleted,CommonConstants.UserInfoConstants.UN_DELETED_STATUS.getStr())
                 .orderByDesc(UserInfo::getUpdateTime));
        if(userInfoList.size() > 0){
            UserInfo info = userInfoList.get(0);
            UserInfoR r = new UserInfoR();
            BeanUtils.copyProperties(info,r);
            return R.success(r);
        }
        return R.codeMessage(RCodeEnums.NO_DATA.getCode(),RCodeEnums.NO_DATA.getDesc());
    }

    @Override
    public R<CreateUserInfoR> createUserInfo(UserInfoDto dto) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(dto,info);
        info.setCreateTime(DateUtils.getTimestamp(new Date()));
        info.setUpdateTime(DateUtils.getTimestamp(new Date()));
        info.setIsDeleted(CommonConstants.UserInfoConstants.UN_DELETED_STATUS.getStr());
        info.setIsVip(CommonConstants.UserInfoConstants.UN_VIP_STATUS.getStr());
        info.setUserId((UUID.randomUUID().toString()).replaceAll("-",""));
        int row = userMapper.insert(info);
        if(row > 0){
            //插入成功
            return R.success(CreateUserInfoR.builder().operateResult("success").build());
        }
        return R.codeMessage(RCodeEnums.OPERATE_DB_ERROR.getCode(),RCodeEnums.OPERATE_DB_ERROR.getDesc());
    }

    @Override
    public R<UpdateUserInfoR> updateUserInfo(UserInfoDto dto) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(dto,info);
        info.setUpdateTime(DateUtils.getTimestamp(new Date()));
        int row = userMapper.update(info,new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getUserId,dto.getUserId()));
        if(row > 0){
            //更新成功
            return R.success(UpdateUserInfoR.builder().operateResult("success").build());
        }
        return R.codeMessage(RCodeEnums.OPERATE_DB_ERROR.getCode(),RCodeEnums.OPERATE_DB_ERROR.getDesc());
    }

    @Override
    public R<Integer> selectCountUser(SelectCountUserVo vo) {
        Integer count = userMapper.selectCount(new QueryWrapper<UserInfo>().lambda()
                .eq(UserInfo::getUserId, vo.getUserId())
                .eq(UserInfo::getIsDeleted,CommonConstants.UserInfoConstants.UN_DELETED_STATUS.getStr()));
        return R.success(count);
    }

    @Override
    public R<UpdateUserInfoR> updateUserInfoAfterBuyVip(UserInfoDto dto) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(dto,info);
        info.setUpdateTime(DateUtils.getTimestamp(new Date()));
        info.setIsVip(CommonConstants.UserInfoConstants.VIP_STATUS.getStr());
        int row = userMapper.update(info,new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getUserId,dto.getUserId()));
        if(row > 0){
            //更新成功
            return R.success(UpdateUserInfoR.builder().operateResult("success").build());
        }
        return R.codeMessage(RCodeEnums.OPERATE_DB_ERROR.getCode(),RCodeEnums.OPERATE_DB_ERROR.getDesc());
    }

    @Override
    public R<UpdateUserInfoR> updateUserInfoAfterDeleted(UserInfoDto dto) {
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(dto,info);
        info.setUpdateTime(DateUtils.getTimestamp(new Date()));
        info.setIsDeleted(CommonConstants.UserInfoConstants.DELETED_STATUS.getStr());
        int row = userMapper.update(info,new QueryWrapper<UserInfo>().lambda().eq(UserInfo::getUserId,dto.getUserId()));
        if(row > 0){
            //更新成功
            return R.success(UpdateUserInfoR.builder().operateResult("success").build());
        }
        return R.codeMessage(RCodeEnums.OPERATE_DB_ERROR.getCode(),RCodeEnums.OPERATE_DB_ERROR.getDesc());
    }

}
