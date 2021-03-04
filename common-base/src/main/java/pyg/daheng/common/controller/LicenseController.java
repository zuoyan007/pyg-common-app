package pyg.daheng.common.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pyg.daheng.common.annotation.NoSupperVip;
import pyg.daheng.common.constants.MsAppResponse;
import pyg.daheng.common.manager.LicenseManager;
import pyg.daheng.common.model.vo.license.LicenseVo;

/**
 * @author ZhanSSH
 * @date 2021/2/7 12:10
 */
@Slf4j
@RestController
@RequestMapping("/license")
@Api(description = "证照相关API")
public class LicenseController {

    @Autowired
    private LicenseManager licenseManager;

    /**
     * 电子证照查询
     * @param vo
     * @return
     */
    @RequestMapping(value = "/getLicense",method = RequestMethod.POST)
    @NoSupperVip
    public MsAppResponse getLicense(@Validated @RequestBody LicenseVo vo){

        return licenseManager.getLicense(vo);

    }
}
