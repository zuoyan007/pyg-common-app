package pyg.daheng.common.manager;

import org.springframework.web.multipart.MultipartFile;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.model.result.util.File2base64R;
import pyg.daheng.common.model.result.util.UploadFileR;
import pyg.daheng.common.model.vo.util.File2base64Vo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description:
 * @ClassName: UtilManager
 * @Author: ZhanSSH
 * @Date: 2021/1/25 14:22
 */
public interface UtilManager {

    /**
     * 文件流转成base64字符串
     * @param vo
     * @return
     */
    R<File2base64R> file2base64(File2base64Vo vo);

    /**
     * 批量上传文件
     * @param file
     * @param request
     * @return
     */
    R<List<UploadFileR>> uploadFile(MultipartFile[] file, HttpServletRequest request);
}
