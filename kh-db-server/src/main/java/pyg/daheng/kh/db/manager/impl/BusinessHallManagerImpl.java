package pyg.daheng.kh.db.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.constants.RCodeEnums;
import pyg.daheng.common.constants.ResultCode;
import pyg.daheng.kh.db.dto.BusinessHallDto;
import pyg.daheng.kh.db.entity.BusinessHallInfo;
import pyg.daheng.kh.db.manager.BusinessHallManager;
import pyg.daheng.kh.db.mapper.BusinessHallMapper;
import pyg.daheng.kh.db.model.result.SelectBusinessHallR;

import java.util.List;

/**
 * @Description:
 * @ClassName: BusinessHallManagerImpl
 * @Author: ZhanSSH
 * @Date: 2021/1/26 11:28
 */
@Service
@Slf4j
public class BusinessHallManagerImpl implements BusinessHallManager{

    @Autowired
    private BusinessHallMapper businessHallMapper;

    @Override
    public R<SelectBusinessHallR> selectBusinessHall(BusinessHallDto dto) {
        List<BusinessHallInfo> list = businessHallMapper.selectList(new QueryWrapper<BusinessHallInfo>()
                .lambda().eq(BusinessHallInfo::getYytbmnw, dto.getYytbmnw()).orderByDesc(BusinessHallInfo::getCzsj));
        if(list !=null && list.size()>0){
            SelectBusinessHallR r = new SelectBusinessHallR();
            BeanUtils.copyProperties(list.get(0),r);
            return R.success(r);
        }
        return R.codeMessage(RCodeEnums.NO_DATA.getCode(),RCodeEnums.NO_DATA.getDesc());

    }
}
