package pyg.daheng.common.chain;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.constants.ResultCode;
import pyg.daheng.common.constants.enums.SensitiveType;
import pyg.daheng.common.utils.SensitiveUtil;

import java.util.HashMap;
import java.util.Optional;

/**
 * @author ZhanSSH
 * @date 2021/3/3 15:46
 */
@Slf4j
public class ResultReturnHandler extends AbstractReturnHandler {

    public ResultReturnHandler(AbstractReturnHandler nextHandler){super(nextHandler);}

    @Override
    protected void handler(Object result) {
        if(BooleanUtils.toBoolean(enableDesensitize) && result instanceof R){
            R r = (R) result;
            try{
                Object data = r.getData();
                if(data == null){
                    return;
                }
                //判断是否全部脱敏
                if(SensitiveType.DISABLE.getValue().equals(r.getMessage())){
                    //清空message
                    r.setMessage(null);
                }else {
                    if(data instanceof JSON){
                        //json脱敏
                        r.setData(SensitiveUtil.sensitiveObject(data.toString()));
                    }else {
                        //对象脱敏
                        r.setData(SensitiveUtil.sensitiveObject(Optional.of(data)));
                    }
                }
            }catch(Exception e){
                log.error("data desensitize fail:", e);
                r.setData(new HashMap<>(1));
                r.setMessage(DESENSITIZE_FAIL_MSG);
                r.setSta(ResultCode.CODE_ERROR);
            }
        }
    }
}
