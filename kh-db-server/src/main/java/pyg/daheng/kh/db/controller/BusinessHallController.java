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
import pyg.daheng.kh.db.dto.BusinessHallDto;
import pyg.daheng.kh.db.manager.BusinessHallManager;
import pyg.daheng.kh.db.model.result.SelectBusinessHallR;
import pyg.daheng.kh.db.model.vo.SelectBusinessHallVo;

/**
 * @Description:
 * @ClassName: BusinessHallController
 * @Author: ZhanSSH
 * @Date: 2021/1/26 11:27
 */
@Slf4j
@RestController
@RequestMapping("/businessHall")
@Api("营业厅相关API")
public class BusinessHallController {

    @Autowired
    private BusinessHallManager businessHallManager;

    @ApiOperation(value = "查询营业厅信息",notes = "查询营业厅信息")
    @RequestMapping(value = "/selectBusinessHall",method = RequestMethod.POST)
    public R<SelectBusinessHallR> selectBusinessHall(@RequestBody @Validated SelectBusinessHallVo vo){
        BusinessHallDto dto = new BusinessHallDto();
        BeanUtils.copyProperties(vo,dto);
        return businessHallManager.selectBusinessHall(dto);
    }

}
