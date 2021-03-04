package pyg.daheng.common.manager.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pyg.daheng.common.constants.MsAppResponse;
import pyg.daheng.common.constants.ResultCode;
import pyg.daheng.common.manager.LicenseManager;
import pyg.daheng.common.model.vo.license.LicenseVo;
import pyg.daheng.common.model.vo.util.LicenseRestfulUtils;

/**
 * @author ZhanSSH
 * @date 2021/2/7 12:12
 */
@Service
public class LicenseManagerImpl implements LicenseManager {

    @Override
    public MsAppResponse getLicense(LicenseVo vo) {
        MsAppResponse response = new MsAppResponse();
        try {

            String result = LicenseRestfulUtils.getLicense(vo);

            /*JSONObject jsonObject = JSON.parseObject(result);
            if(jsonObject.containsKey("Fault")){
                FaultResult faultR = JSON.parseObject(result, FaultResult.class);
                response.setData(faultR.getFault().getDetail());
                response.setMessage(faultR.getFault().getReason());
                response.setSta(ResultCode.CODE_ERROR_02);
            }else if(jsonObject.containsKey("message")){
                response.setSta(ResultCode.CODE_ERROR_09);
                response.setMessage("大数据局返回"+jsonObject.get("message"));
            }else {
                SuccessResult successR = JSON.parseObject(jsonObject.get("data").toString(), SuccessResult.class);
                if(Integer.parseInt(successR.getData().getTotal()) > 0){
                    response.setSta(ResultCode.CODE_NORMAL);
                    response.setMessage(successR.getHead().getMessage());
                    response.setData(successR.getData().getDatas());
                }else {
                    response.setSta(ResultCode.CODE_ERROR_09);
                    response.setMessage("暂无数据");
                }
            }*/
            response.setSta(ResultCode.CODE_NORMAL);
            response.setData(result);
        } catch (Exception e) {
            response.setSta(ResultCode.CODE_ERROR);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
