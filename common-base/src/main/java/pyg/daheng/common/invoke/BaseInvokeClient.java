/*
package pyg.daheng.common.invoke;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pyg.daheng.common.utils.JsonUtils;

*/
/**
 * @author ZhanSSH
 * @date 2021/2/5 15:19
 *//*

@Component
@Slf4j
public class BaseInvokeClient {

    @Autowired
    private MsinvokeClient client;

    @Value("${syzw.proxy.service.name:ucs-bms-idc-interfaceAgent}")
    private String proxyServiceName;
    @Value("${syzw.proxy.path:/proxy/execute}")
    private String proxyPath;
    @Value("${syzw.proxy.enable:false}")
    private String enable;
    private void callBefore(String url, Object params) {
        log.info("微应用 - 发起请求 - url : {}", url);
        JSONObject paramJson = JsonUtils.toJsonObject(params);
        if (params.toString().length() > InvokeConstants.LOG_THRESHOLD) {
            log.info("微应用 - 请求入参 - params : {}", paramJson.toJSONString().substring(0, InvokeConstants.LOG_SIZE));
        } else {
            log.info("微应用 - 请求入参 - params : {}", paramJson);
        }
    }

    public <T> T postFormData(String serviceName, String path, Object params, Class<T> responseType) {
        boolean isProxy = BooleanUtils.toBoolean(enable);
        if (isProxy){
            T t = invokerFormData(serviceName, path, params, responseType);
            return t;
        }else {
            this.callBefore(serviceName + path, params);
            T t = client.postFormData(serviceName, path, params, responseType);
            this.callAfter(t);
            return t;
        }
    }

    public String postFormData(String serviceName, String path, Object params) {
        this.callBefore(serviceName + path, params);
        String s = client.postFormData(serviceName, path, params);
        this.callAfter(s);
        return s;
    }

    public <T> T postJson(String serviceName, String path, Object params, Class<T> responseType) {
        boolean isProxy = BooleanUtils.toBoolean(enable);
        if (isProxy){
            T t = invokerPostJson(serviceName, path, params, responseType);
            return t;
        }else {
            this.callBefore(serviceName + path, params);
            T t = client.postJson(serviceName, path, params, responseType);
            this.callAfter(t);
            return t;
        }
    }

    public <T> ResponseEntity<T> postFormData(String url, HttpHeaders headers, Object params, Class<T> responseType) {
        this.callBefore(url, params);
        ResponseEntity<T> tResponseEntity = client.postFormData(url, headers, params, responseType);
        this.callAfter(tResponseEntity.getBody());
        return tResponseEntity;
    }

    private <T> void callAfter(T t) {
        if (t == null){
            log.warn("after t is null");
            return;
        }
        JSONObject resultData = JsonUtils.toJsonObject(t);
        if (t.toString().length() > InvokeConstants.LOG_THRESHOLD) {
            log.info("微应用 - 请求结束,返回值 - body : {}", resultData.toJSONString().substring(0, InvokeConstants.LOG_SIZE));
        } else {
            log.info("微应用 - 请求结束,返回值 - body : {}", resultData);
        }
    }

    private <T> T invokerPostJson(String serviceName, String path, Object params, Class<T> responseType) {
        this.callBefore(serviceName + path, params);
        ProxyVo proxyVo = new ProxyVo();
        proxyVo.setContextType("json");
        proxyVo.setMsname(serviceName);
        proxyVo.setPath(path);
        proxyVo.setData(JSON.toJSONString(params));
        T t = client.postJson(proxyServiceName, proxyPath, proxyVo, responseType);
        this.callAfter(t);
        return t;
    }

    private <T> T invokerFormData(String serviceName, String path, Object params, Class<T> responseType) {
        this.callBefore(serviceName + path, params);
        ProxyVo proxyVo = new ProxyVo();
        proxyVo.setContextType("form-data");
        proxyVo.setMsname(serviceName);
        proxyVo.setPath(path);
        proxyVo.setData(JSON.toJSONString(params));
        T t = client.postJson(proxyServiceName, proxyPath, proxyVo, responseType);
        this.callAfter(t);
        return t;
    }
}
*/
