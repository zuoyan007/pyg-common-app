package pyg.daheng.common.chain;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import pyg.daheng.common.constants.MsAppResponse;
import pyg.daheng.common.constants.ResultCode;
import pyg.daheng.common.constants.enums.SensitiveType;
import pyg.daheng.common.utils.SensitiveUtil;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author ZhanSSH
 * @date 2021/3/3 15:13
 */
@Slf4j
public class MsAppReturnHandler extends AbstractReturnHandler {

    public MsAppReturnHandler(AbstractReturnHandler nextHandler){super(nextHandler);}

    @Override
    protected void handler(Object result) {
        if(BooleanUtils.toBoolean(enableDesensitize) && result instanceof MsAppResponse){
            MsAppResponse response = (MsAppResponse) result;
            try{
                Object data = response.getData();
                if(data == null){
                    return;
                }
                //判断是否全部脱敏
                if(SensitiveType.DISABLE.getValue().equals(response.getMessage())){
                    //清空message
                    response.setMessage(null);
                }else {
                    if(data instanceof JSON){
                        //json脱敏
                        response.setData(SensitiveUtil.sensitiveObject(data.toString()));
                    }else {
                        //对象脱敏
                        response.setData(SensitiveUtil.sensitiveObject(Optional.of(data)));
                    }
                }
            }catch(Exception e){
                log.error("data desensitize fail:", e);
                response.setData(new HashMap<>(1));
                response.setMessage(DESENSITIZE_FAIL_MSG);
                response.setSta(ResultCode.CODE_ERROR);
            }
        }
    }
}
