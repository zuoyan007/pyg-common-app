package pyg.daheng.kh.db.manager;

import pyg.daheng.common.constants.R;
import pyg.daheng.common.model.result.UserInfoR;
import pyg.daheng.common.model.vo.GetUserInfoVo;
import pyg.daheng.kh.db.dto.UserInfoDto;
import pyg.daheng.kh.db.model.result.CreateUserInfoR;
import pyg.daheng.kh.db.model.result.UpdateUserInfoR;
import pyg.daheng.kh.db.model.vo.SelectCountUserVo;

/**
 * @Description:
 * @ClassName: UserManager
 * @Author: ZhanSSH
 * @Date: 2021/1/19 11:06
 */
public interface UserManager {

    /**
     * 查询用户
     * @param vo
     * @return
     */
    R<UserInfoR> getUserInfo(GetUserInfoVo vo);

    /**
     * 注册新用户
     * @param dto
     * @return
     */
    R<CreateUserInfoR> createUserInfo(UserInfoDto dto);

    /**
     * 编辑用户信息
     * @param dto
     * @return
     */
    R<UpdateUserInfoR> updateUserInfo(UserInfoDto dto);

    /**
     * 查询是否有用户信息
     * @param vo
     * @return
     */
    R<Integer> selectCountUser(SelectCountUserVo vo);

    /**
     * 购买VIP更新信息
     * @param dto
     * @return
     */
    R<UpdateUserInfoR> updateUserInfoAfterBuyVip(UserInfoDto dto);

    /**
     * 用户删除账号更新信息
     * @param dto
     * @return
     */
    R<UpdateUserInfoR> updateUserInfoAfterDeleted(UserInfoDto dto);
}
