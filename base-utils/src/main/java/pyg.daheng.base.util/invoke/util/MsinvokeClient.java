package pyg.daheng.base.util.invoke.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Stopwatch;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:21
 */
@Component
public class MsinvokeClient {
    private static final Logger log = LoggerFactory.getLogger(MsinvokeClient.class);
    @Autowired
    private RestTemplate msinvokeRestTemplate;
    @Autowired
    private RestTemplate ipTemplate;
    private static final Pattern patternIphone = Pattern.compile("( |:|=|\\\")+(?<val>[^a-zA-Z]?(13|14|15|17|18|19)[0-9]{9})+( |,|\\\")?");
    private static final Pattern patternIdCard = Pattern.compile("( |:|=|\\\")+(?<val>[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx])+( |,|\\\")?");
    private static final Pattern patternEmail = Pattern.compile("( |:|=|\\\")+(?<val>[^ :=\\\"a-zA-Z]\\w[-\\w{1,2}]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14})+( |,|\\\")?");
    private static final Pattern patternBankCard = Pattern.compile("( |:|=|\\\")+(?<val>(10|18|30|35|37|40|41|42|43|44|45|46|47|48|49|50|51|52|53|54|55|56|58|60|62|65|68|69|84|87|88|94|95|98|99)(\\d{14,17}))( |,|\\\")?");
    private static final Pattern patternCredentials = Pattern.compile("( |:|=|\\\")+?(credentials|newCredentials|acctId|dentials|username|password)+\\\"?(:|=|\\\")+(?<val>.+?)( |,|\\\")+");

    public MsinvokeClient() {
    }

    private static Map<String, String> objectToMap(Object object) {
        String jsonString = JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.WriteNonStringValueAsString});
        return (Map)JSON.parseObject(jsonString, Map.class);
    }

    private static HttpHeaders getHeader() {
        return MsinvokeHeaderFactory.getHeader();
    }

    private static void mapSetHeadr(HttpHeaders headers, Map<String, String> params) {
        MsinvokeHeaderFactory.MapSetHeadr(headers, params);
    }

    public <T> ResponseEntity<T> call(String serviceName, String path, HttpHeaders headers, MsinvokeMethod callMethod, Object params, Class<T> responseType) {
        StringBuffer url = new StringBuffer();
        if (MsinvokeUtil.checkProtocolHeader(serviceName)) {
            url.append(serviceName).append(path);
        } else {
            url.append(MsinvokeDataConstants.GET_PROTOCOL_HEADER).append(serviceName).append(path);
        }

        if (MsinvokeMethod.GET.equals(callMethod)) {
            return this.get(url.toString(), headers, params, responseType);
        } else if (MsinvokeMethod.POSTJSON.equals(callMethod)) {
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            return this.postJson(url.toString(), headers, params, responseType);
        } else if (MsinvokeMethod.POSTFORMURL.equals(callMethod)) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            return this.postFormUrl(url.toString(), headers, params, responseType);
        } else if (MsinvokeMethod.POSTFORMDATA.equals(callMethod)) {
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            return this.postFormData(url.toString(), headers, params, responseType);
        } else {
            return null;
        }
    }

    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Object params, Class<T> responseType) {
        ResponseEntity<T> response = null;
        String serviceName = MsinvokeUtil.urlGetServiceName(url);
        int checkGateway = MsinvokeUtil.checkGateway(serviceName);
        if (checkGateway == 2) {
            return this.nnwExecute(url, headers, params, "GET", responseType);
        } else {
            StringBuilder urlSb = new StringBuilder(url);
            HttpEntity<String> requestEntity = new HttpEntity((Object)null, headers);
            urlSb.append("?").append("1=1");
            if (params != null) {
                Map<String, String> oMap = objectToMap(params);
                Iterator var11 = oMap.entrySet().iterator();

                while(var11.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry)var11.next();
                    urlSb.append("&").append((String)entry.getKey()).append("={").append((String)entry.getKey()).append("}");
                }

                url = urlSb.toString();
                response = this.exchange(url, HttpMethod.GET, requestEntity, responseType, oMap);
            } else {
                response = this.exchange(url, HttpMethod.GET, requestEntity, responseType);
            }

            return response;
        }
    }

    public <T> ResponseEntity<T> postJson(String url, HttpHeaders headers, Object params, Class<T> responseType) {
        ResponseEntity<T> response = null;
        String serviceName = MsinvokeUtil.urlGetServiceName(url);
        int checkGateway = MsinvokeUtil.checkGateway(serviceName);
        if (checkGateway == 2) {
            return this.nnwExecute(url, headers, params, "POST", responseType);
        } else {
            String value = null;
            if (params != null) {
                value = JSON.toJSONString(params);
            }

            HttpEntity<String> requestEntity = new HttpEntity(value, headers);
            response = this.exchange(url, HttpMethod.POST, requestEntity, responseType);
            return response;
        }
    }

    public <T> ResponseEntity<T> postFormUrl(String url, HttpHeaders headers, Object params, Class<T> responseType) {
        ResponseEntity<T> response = null;
        String serviceName = MsinvokeUtil.urlGetServiceName(url);
        int checkGateway = MsinvokeUtil.checkGateway(serviceName);
        if (checkGateway == 2) {
            return this.nnwExecute(url, headers, params, "POST", responseType);
        } else {
            MultiValueMap<String, String> value = new LinkedMultiValueMap();
            if (params != null) {
                Map<String, String> oMap = objectToMap(params);
                Iterator var10 = oMap.entrySet().iterator();

                while(var10.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry)var10.next();
                    value.add(entry.getKey(), entry.getValue());
                }
            }

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(value, headers);
            response = this.exchange(url, HttpMethod.POST, requestEntity, responseType);
            return response;
        }
    }

    public <T> ResponseEntity<T> postFormData(String url, HttpHeaders headers, Object params, Class<T> responseType) {
        ResponseEntity<T> response = null;
        String serviceName = MsinvokeUtil.urlGetServiceName(url);
        int checkGateway = MsinvokeUtil.checkGateway(serviceName);
        if (checkGateway == 2) {
            return this.nnwExecute(url, headers, params, "POST", responseType);
        } else {
            MultiValueMap<String, String> value = new LinkedMultiValueMap();
            if (params != null) {
                Map<String, String> oMap = objectToMap(params);
                Iterator var10 = oMap.entrySet().iterator();

                while(var10.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry)var10.next();
                    value.add(entry.getKey(), entry.getValue());
                }
            }

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(value, headers);
            response = this.exchange(url, HttpMethod.POST, requestEntity, responseType);
            return response;
        }
    }

    public ResponseEntity<String> call(String serviceName, String path, HttpHeaders headers, MsinvokeMethod callMethod, Object params) {
        ResponseEntity<String> call = this.call(serviceName, path, headers, callMethod, params, String.class);
        return call;
    }

    public ResponseEntity<String> get(String serviceName, String path, HttpHeaders headers, Object params) {
        return this.call(serviceName, path, headers, MsinvokeMethod.GET, params);
    }

    public ResponseEntity<String> postJson(String serviceName, String path, HttpHeaders headers, Object params) {
        return this.call(serviceName, path, headers, MsinvokeMethod.POSTJSON, params);
    }

    public ResponseEntity<String> postFormUrl(String serviceName, String path, HttpHeaders headers, Object params) {
        return this.call(serviceName, path, headers, MsinvokeMethod.POSTFORMURL, params);
    }

    public ResponseEntity<String> postFormData(String serviceName, String path, HttpHeaders headers, Object params) {
        return this.call(serviceName, path, headers, MsinvokeMethod.POSTFORMDATA, params);
    }

    public ResponseEntity<String> get(String serviceName, String path, Map<String, String> heardMap, Object params) {
        HttpHeaders header = getHeader();
        mapSetHeadr(header, heardMap);
        return this.get(serviceName, path, header, params);
    }

    public ResponseEntity<String> postJson(String serviceName, String path, Map<String, String> heardMap, Object params) {
        HttpHeaders header = getHeader();
        mapSetHeadr(header, heardMap);
        return this.postJson(serviceName, path, header, params);
    }

    public ResponseEntity<String> postFormUrl(String serviceName, String path, Map<String, String> heardMap, Object params) {
        HttpHeaders header = getHeader();
        mapSetHeadr(header, heardMap);
        return this.postFormUrl(serviceName, path, header, params);
    }

    public ResponseEntity<String> postFormData(String serviceName, String path, Map<String, String> heardMap, Object params) {
        HttpHeaders header = getHeader();
        mapSetHeadr(header, heardMap);
        return this.postFormData(serviceName, path, header, params);
    }

    public String baseCall(String serviceName, String path, HttpHeaders headers, MsinvokeMethod callMethod, Object params) {
        ResponseEntity<String> call = this.call(serviceName, path, headers, callMethod, params);
        return call == null ? null : (String)call.getBody();
    }

    public String baseGet(String serviceName, String path, HttpHeaders headers, Object params) {
        ResponseEntity<String> call = this.get(serviceName, path, headers, params);
        return call == null ? null : (String)call.getBody();
    }

    public String basePostJson(String serviceName, String path, HttpHeaders headers, Object params) {
        ResponseEntity<String> call = this.postJson(serviceName, path, headers, params);
        return call == null ? null : (String)call.getBody();
    }

    public String basePostFormUrl(String serviceName, String path, HttpHeaders headers, Object params) {
        ResponseEntity<String> call = this.postFormUrl(serviceName, path, headers, params);
        return call == null ? null : (String)call.getBody();
    }

    public String basePostFormData(String serviceName, String path, HttpHeaders headers, Object params) {
        ResponseEntity<String> call = this.postFormData(serviceName, path, headers, params);
        return call == null ? null : (String)call.getBody();
    }

    public String baseCall(String serviceName, String path, Map<String, String> heardMap, MsinvokeMethod callMethod, Object params) {
        HttpHeaders header = getHeader();
        mapSetHeadr(header, heardMap);
        ResponseEntity<String> call = this.call(serviceName, path, header, callMethod, params);
        return call == null ? null : (String)call.getBody();
    }

    public String baseGet(String serviceName, String path, Map<String, String> heardMap, Object params) {
        HttpHeaders header = getHeader();
        mapSetHeadr(header, heardMap);
        ResponseEntity<String> call = this.get(serviceName, path, header, params);
        return call == null ? null : (String)call.getBody();
    }

    public String basePostJson(String serviceName, String path, Map<String, String> heardMap, Object params) {
        HttpHeaders header = getHeader();
        mapSetHeadr(header, heardMap);
        ResponseEntity<String> call = this.postJson(serviceName, path, header, params);
        return call == null ? null : (String)call.getBody();
    }

    public String basePostFormUrl(String serviceName, String path, Map<String, String> heardMap, Object params) {
        HttpHeaders header = getHeader();
        mapSetHeadr(header, heardMap);
        ResponseEntity<String> call = this.postFormUrl(serviceName, path, header, params);
        return call == null ? null : (String)call.getBody();
    }

    public String basePostFormData(String serviceName, String path, Map<String, String> heardMap, Object params) {
        HttpHeaders header = getHeader();
        mapSetHeadr(header, heardMap);
        ResponseEntity<String> call = this.postFormData(serviceName, path, header, params);
        return call == null ? null : (String)call.getBody();
    }

    public <T> T call(String serviceName, String path, MsinvokeMethod callMethod, Object params, Class<T> responseType) {
        String call = this.call(serviceName, path, callMethod, params);
        if (call == null) {
            return null;
        } else {
            T parseObject = JSON.parseObject(call, responseType);
            return parseObject;
        }
    }

    public <T> T get(String serviceName, String path, Object params, Class<T> responseType) {
        return this.call(serviceName, path, MsinvokeMethod.GET, params, responseType);
    }

    public <T> T postJson(String serviceName, String path, Object params, Class<T> responseType) {
        return this.call(serviceName, path, MsinvokeMethod.POSTJSON, params, responseType);
    }

    public <T> T postFormUrl(String serviceName, String path, Object params, Class<T> responseType) {
        return this.call(serviceName, path, MsinvokeMethod.POSTFORMURL, params, responseType);
    }

    public <T> T postFormData(String serviceName, String path, Object params, Class<T> responseType) {
        return this.call(serviceName, path, MsinvokeMethod.POSTFORMDATA, params, responseType);
    }

    public String call(String serviceName, String path, MsinvokeMethod callMethod, Object params) {
        HttpHeaders header = getHeader();
        ResponseEntity<String> call = this.call(serviceName, path, header, callMethod, params);
        return call == null ? null : (String)call.getBody();
    }

    public String postJson(String serviceName, String path, Object params) {
        HttpHeaders header = getHeader();
        ResponseEntity<String> call = this.postJson(serviceName, path, header, params);
        return call == null ? null : (String)call.getBody();
    }

    public String postFormUrl(String serviceName, String path, Object params) {
        HttpHeaders header = getHeader();
        ResponseEntity<String> call = this.postFormUrl(serviceName, path, header, params);
        return call == null ? null : (String)call.getBody();
    }

    public String postFormData(String serviceName, String path, Object params) {
        HttpHeaders header = getHeader();
        ResponseEntity<String> call = this.postFormData(serviceName, path, header, params);
        return call == null ? null : (String)call.getBody();
    }

    public String get(String serviceName, String path, Object params) {
        HttpHeaders header = getHeader();
        ResponseEntity<String> call = this.get(serviceName, path, header, params);
        return call == null ? null : (String)call.getBody();
    }

    public String get(String serviceName, String path) {
        return this.get(serviceName, path, (Object)null);
    }

    public String post(String serviceName, String path) {
        return this.postJson(serviceName, path, (Object)null);
    }

    public <T> ResponseEntity<T> nnwExecute(String url, HttpHeaders headers, Object params, String method, Class<T> responseType) {
        ResponseEntity response = null;

        try {
            MsinvokeUtil.checkBaseHeader(headers);
            String serviceName = MsinvokeUtil.urlGetServiceName(url);
            String nnwUrl = getNnwUrl();
            MediaType contentType = headers.getContentType();
            String type = contentType != null ? contentType.toString() : "";
            int start = url.indexOf(serviceName) + serviceName.length();
            String path = url.substring(start);
            Map<String, Object> reqMap = new HashMap();
            reqMap.put("method", method);
            reqMap.put("body", params);
            reqMap.put("serverName", serviceName);
            reqMap.put("path", path);
            Map<String, Object> headerMap = new HashMap();
            headerMap.putAll(headers);
            headerMap.put("Content-Type", type.replaceAll(";", "; "));
            reqMap.put("header", headerMap);
            String value = JSON.toJSONString(reqMap);
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> requestEntity = new HttpEntity(value, headers);
            ResponseEntity<String> nnwResponse = this.exchange(nnwUrl, HttpMethod.POST, requestEntity, String.class);
            int status = nnwResponse.getStatusCodeValue();
            if (status != HttpStatus.OK.value()) {
                log.error("#nnwExecute#url:内外网请求失败!" + nnwUrl + "reqMap:" + JSON.toJSONString(reqMap) + " response:" + JSON.toJSONString(nnwResponse));
                return null;
            } else {
                String body = (String)nnwResponse.getBody();
                Map<String, Object> rsMap = (Map)JSON.parseObject(body, Map.class);
                String mapStatus = (String)((String)(rsMap == null ? "" : rsMap.get("status")));
                if ("00".equals(mapStatus)) {
                    T t = (T)rsMap.get("data");
                    response = new ResponseEntity(t, HttpStatus.OK);
                    return response;
                } else {
                    log.error("#nnwExecute#请求状态异常! url:" + nnwUrl + " body:" + body);
                    return null;
                }
            }
        } catch (Exception var23) {
            log.error("#nnwExecute#url:" + url + " headers:" + JSON.toJSONString(headers) + " params:" + params + " error:" + var23.getMessage(), var23);
            return null;
        }
    }

    private static String getNnwUrl() {
        String nnwServiceName = MsinvokeAppConfig.getProperty("nnw-service-name", "cms-dmz-nwwinterface-server");
        String protocolHeader = MsinvokeDataConstants.GET_PROTOCOL_HEADER;
        String msCaller = MsinvokeAppConfig.getProperty("spring.application.name");
        String path = MsinvokeUtil.idcOrDmz(msCaller);
        String basePath = MsinvokeAppConfig.getProperty("msinvoke.nnw.base.path", "");
        return StringUtils.isEmpty(basePath)?protocolHeader + nnwServiceName + "/" + path:protocolHeader + nnwServiceName + basePath + "/" + path;
    }

    private <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType) {
        return this.exchange(url, method, requestEntity, responseType, (Map)null);
    }

    private <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ResponseEntity responseEntity = null;

        try {
            boolean ipAddress = MsinvokeUtil.isIpAddress(url);
            RestTemplate template = this.ipTemplate;
            if (!ipAddress) {
                log.debug("Interface Invoke Client: #exchange#内部调用!");
                MsinvokeUtil.checkBaseHeader(requestEntity);
                template = this.msinvokeRestTemplate;
            }

            if (MsinvokeBeanConfig.IS_PRINT_INVOKE_LOG) {
                String transactionUniqueId = UUID.randomUUID().toString().replace("-", "");
                HttpServletRequest httpServletRequest = getCurrentRequest();
                if (httpServletRequest != null) {
                    transactionUniqueId = httpServletRequest.getHeader("transaction-uniqueid");
                }

                if (Strings.isEmpty(transactionUniqueId)) {
                    transactionUniqueId = UUID.randomUUID().toString().replace("-", "");
                }

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(requestEntity.getHeaders());
                if (httpHeaders.containsKey("transaction-uniqueid")) {
                    httpHeaders.set("transaction-uniqueid", transactionUniqueId);
                } else {
                    httpHeaders.add("transaction-uniqueid", transactionUniqueId);
                }

                String currApplicationName = MsinvokeAppConfig.getProperty("spring.application.name");
                if (currApplicationName != null && currApplicationName.length() > 0) {
                    if (httpHeaders.containsKey("invokeApplicationName")) {
                        httpHeaders.set("invokeApplicationName", currApplicationName);
                    } else {
                        httpHeaders.add("invokeApplicationName", currApplicationName);
                    }
                }

                requestEntity = new HttpEntity(requestEntity.getBody(), httpHeaders);
            }

            if (uriVariables == null) {
                responseEntity = template.exchange(url, method, requestEntity, responseType, new Object[0]);
            } else {
                responseEntity = template.exchange(url, method, requestEntity, responseType, uriVariables);
            }

            ResponseEntity var19 = responseEntity;
            return var19;
        } catch (Exception var17) {
            log.error("Interface Invoke Client EstimatedTime:{}, StatusCode:{}, URL:{}, HttpMethod:{}, RequestEntity:{}, UriVariables:{}", new Object[]{stopwatch.toString(), responseEntity != null ? responseEntity.getStatusCodeValue() : "", url, method, desenstitzation(JSON.toJSONString(requestEntity)), desenstitzation(JSON.toJSONString(uriVariables)), var17});
        } finally {
            if (MsinvokeBeanConfig.IS_PRINT_INVOKE_LOG && !StringUtils.isEmpty(url) && !url.contains("upload") && !url.contains("download")) {
                log.info("Interface Invoke Client EstimatedTime:{}, StatusCode:{}, URL:{}, HttpMethod:{}, RequestEntity:{}, UriVariables:{}", new Object[]{stopwatch.toString(), responseEntity != null ? responseEntity.getStatusCodeValue() : "", url, method, desenstitzation(JSON.toJSONString(requestEntity)), desenstitzation(JSON.toJSONString(uriVariables))});
            }

        }

        return null;
    }

    private static HttpServletRequest getCurrentRequest() throws IllegalStateException {
        ServletRequestAttributes attrs = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getRequest();
    }

    private static String desenstitzation(String value) {
        try {
            value = matchAndReplace(value, patternIphone);
            value = matchAndReplace(value, patternIdCard);
            value = matchAndReplace(value, patternEmail);
            value = matchAndReplace(value, patternBankCard);
            value = matchAndReplace(value, patternCredentials);
        } catch (Exception var2) {
            ;
        }

        return value;
    }

    private static String matchAndReplace(String inputPhone, Pattern p) {
        String value;
        for(Matcher m = p.matcher(inputPhone); m.find(); inputPhone = inputPhone.replaceAll(value, replaceStringByLength(value))) {
            value = m.group("val");
        }

        return inputPhone;
    }

    private static String replaceStringByLength(String value) {
        int length = value.length();
        if (length == 2) {
            value = value.substring(0, 1) + "\\\\*";
        } else if (length == 3) {
            value = value.substring(0, 1) + "\\\\*" + value.substring(length - 1);
        } else if (length > 3 && length <= 5) {
            value = value.substring(0, 1) + "\\\\*\\\\*" + value.substring(length - 2);
        } else if (length > 5 && length <= 7) {
            value = value.substring(0, 2) + "\\\\*\\\\*\\\\*" + value.substring(length - 2);
        } else if (length > 7) {
            value = value.substring(0, 3) + "\\\\*\\\\*\\\\*\\\\*\\\\*" + value.substring(length - 3);
        }

        return value;
    }
}
