package pyg.daheng.common.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.manager.UtilManager;
import pyg.daheng.common.model.result.util.File2base64R;
import pyg.daheng.common.model.result.util.UploadFileR;
import pyg.daheng.common.model.vo.util.File2base64Vo;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @ClassName: UtilManagerImpl
 * @Author: ZhanSSH
 * @Date: 2021/1/25 14:22
 */
@Slf4j
@Service
public class UtilManagerImpl implements UtilManager {

    @Override
    public R<File2base64R> file2base64(File2base64Vo vo) {

        return R.success();

    }

    @Override
    public R<List<UploadFileR>> uploadFile(MultipartFile[] files, HttpServletRequest request) {

        List<UploadFileR> list = new ArrayList<>();
        //获取文件名称
        for(int i = 0; i < files.length; i++){
            UploadFileR result = new UploadFileR();
            String fileName = files[i].getOriginalFilename().substring(files[i].getOriginalFilename().lastIndexOf("."));
            fileName = UUID.randomUUID().toString().replaceAll("-","")+fileName;
            File file = new File("D:/temp/" + fileName);
            result.setIndex(""+i);
            result.setFilePath("D:/temp/" + fileName);
            try{
                //上传
                files[i].transferTo(file);
            }catch(IOException e){
                result.setUploadResult("fail !");
            }
            result.setUploadResult("success !");
            list.add(result);
        }
        return R.success(list);
    }
}
