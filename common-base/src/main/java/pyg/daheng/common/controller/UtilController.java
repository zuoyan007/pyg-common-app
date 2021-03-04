package pyg.daheng.common.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pyg.daheng.common.constants.MsAppResponse;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.constants.RCodeEnums;
import pyg.daheng.common.manager.UtilManager;
import pyg.daheng.common.model.result.util.File2base64R;
import pyg.daheng.common.model.result.util.UploadFileR;
import pyg.daheng.common.model.vo.util.File2base64Vo;
import pyg.daheng.common.utils.exception.MsAppException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Description:
 * @ClassName: UtilController
 * @Author: ZhanSSH
 * @Date: 2021/1/25 14:20
 */
@Slf4j
@RestController
@Controller
@RequestMapping("/util")
@Api(description = "工具类API")
public class UtilController {

    @Autowired
    private UtilManager utilManager;

    @RequestMapping(value = "/index")
    public String index(){

        return "uploadBath";
    }

    @ApiOperation(value = "文件流转成base64字符串",notes = "文件流转成base64字符串")
    @RequestMapping(value = "/file2base64",method = RequestMethod.POST)
    public R<File2base64R> file2base64(@RequestBody @Validated File2base64Vo vo){

        return utilManager.file2base64(vo);
    }

    @ApiOperation(value = "上传文件",notes = "上传文件")
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    public R<List<UploadFileR>> uploadFile(MultipartFile[] file, HttpServletRequest request){

        return utilManager.uploadFile(file,request);
    }
    @PostMapping("/queryEleUserByEleCustNumber")
    public MsAppResponse queryEleUserByEleCustNumber(
            @Size(max = 20, message = "用电户号字符长度不能超过20个字符")
            @NotBlank(message = "用电户号不能为空")
            String eleCustNumber) {
        log.info("用户号==>{}",eleCustNumber);
        MsAppException.checkCondicition(true,"01000086","暂无相关户号信息");
        return new MsAppResponse(RCodeEnums.SUCCESS.getCode(), RCodeEnums.SUCCESS.getDesc(), "");
    }
}
