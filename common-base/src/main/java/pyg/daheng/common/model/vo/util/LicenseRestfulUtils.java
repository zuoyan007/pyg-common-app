package pyg.daheng.common.model.vo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import pyg.daheng.common.config.AppConfig;
import pyg.daheng.common.model.result.license.ErrorResult;
import pyg.daheng.common.model.vo.license.LicenseVo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author ZhanSSH
 * @date 2021/2/7 12:29
 */
@Slf4j
public class LicenseRestfulUtils {
    static final String signKeyUrl = AppConfig.getProperty("sign.key.url","http://10.96.3.88:8181/sysapi/refreshappsecret");
    static final String tongTecurl = AppConfig.getProperty("tong.tec.url","http://10.96.3.88:8181/httpproxy");
    static final String SECRET_REDIS_KEY = AppConfig.getProperty("secret.redis.key","SECRET_REDIS_KEY");
    static final String appkey = AppConfig.getProperty("license.appkey","31dc18f44dab3876c4de1093ca545298");
    static final String rid = AppConfig.getProperty("license.rid","91460000713806039A@1232");
    static final String sid = AppConfig.getProperty("license.sid","s_2746000000000_14371");
    static final String bear = "Bearer 7bee4ad2-1d1f-36e0-b9ab-625a7e495de2";
    /** nginx转发地址 */
   /* static final String signKeyUrl = AppConfig.getProperty("sign.key.url","http://10.96.3.37:8089/HNSECRET");
    static final String tongTecurl = AppConfig.getProperty("tong.tec.url","http://10.96.3.37:8089/HNDZZJZ");
*/
    /*static final String SECRET_REDIS_KEY = "SECRET_REDIS_KEY";
    static final String appkey = "31dc18f44dab3876c4de1093ca545298 ";
    static final String rid = "91460000713806039A@1232";
    static final String sid = "s_2746000000000_14371";
    static final String bear = "Bearer 7bee4ad2-1d1f-36e0-b9ab-625a7e495de2";*/

    /*public static void main(String[] args) throws Exception {

     *//*String rtime = "" + System.currentTimeMillis();
        System.out.println("时间戳："+rtime);
        //第一步 获取sign
        String sign = TongTechEncode.HmacSHA256(sid, rid, appkey,rtime);
        System.out.println("对加解密后的秘钥第一次签名后的sign：" + sign);*//*
        //第二步 刷新秘钥
       *//* String secret=TongTechEncode.getSercret(sid, rid, rtime, sign,signKeyUrl);
        ParserConfig.getGlobalInstance().setAsmEnable(false); DataMain
                dataMain=(DataMain) JSON.parseObject(secret, DataMain.class);
        System.out.println("新密码："+dataMain.getData().getSecret());
        Thread.sleep(61000);
        *//*
        //第三步 解密
        //String newSecret =  TongTechEncode.AESDncode(appkey, dataMain.getData().getSecret());
        *//*String secret = "Stusnn7Uh/W+xgqt0YmoAvrEIw3CvyVzFCBmpVwrQRdnFyAuNdtmQWP9FzZ0P0rU";
        String newSecret =  TongTechEncode.AESDncode(appkey, secret);
        System.out.println("------newSecret---"+newSecret);*//*
        String newSecret = "dd562f2bcb52d9215ae0ad5d0836e3c1";

        // 第四步 根据新秘钥获取新sign
        String rtime2 = "" + System.currentTimeMillis();
        System.out.println(rtime2);
        String sign2 = TongTechEncode.HmacSHA256(sid, rid, newSecret, rtime2);
        System.out.println("对加解密后的秘钥第二次签名后的sign2：" + sign2);
       *//* String jsonStr = accessTongtec(rid, sid, sign2, rtime2);
        System.out.println("接口返回数据：" + jsonStr);*//*
        //String result = getLicense(vo);
    }*/
    public static String refreshSecret() throws Exception{
        String secret = (String)RedisUtil.get(SECRET_REDIS_KEY);
        log.info("redis old 秘钥{}" ,secret);
        String rtime = "" + System.currentTimeMillis();
        //第一步 获取sign
        String sign = TongTechEncode.HmacSHA256(sid, rid, appkey, rtime);
        log.info("对加解密后的秘钥第一次签名后的sign：" + sign);
        //第二步 刷新秘钥
        secret = TongTechEncode.getSercret(sid, rid, rtime, sign, signKeyUrl);
        ParserConfig.getGlobalInstance().setAsmEnable(false);
        DataMain dataMain = (DataMain)JSON.parseObject(secret, DataMain.class);
        log.info("新密码：" + dataMain.getData().getSecret());
        //Thread.sleep(61000);
        //第三步 解密
        String newSecret = TongTechEncode.AESDncode(appkey, dataMain.getData().getSecret());
        log.info("------redis save newSecret--->{}" , newSecret);
        //把秘钥放redis保存一个月
        RedisUtil.set(SECRET_REDIS_KEY,newSecret,60*60*24*30);
        return newSecret;
    }

    public static String getSign(String rid, String sid) throws Exception{
        String secret = (String)RedisUtil.get(SECRET_REDIS_KEY);
        log.info("redis 中秘钥:{}",secret);
        if(StringUtils.isBlank(secret)){
            String rtime = "" + System.currentTimeMillis();
            //第一步 获取sign
            String sign = TongTechEncode.HmacSHA256(sid, rid, appkey,rtime);
            //第二步 刷新秘钥
            secret=TongTechEncode.getSercret(sid, rid, rtime, sign,signKeyUrl);
            ParserConfig.getGlobalInstance().setAsmEnable(false);
            DataMain dataMain=(DataMain) JSON.parseObject(secret, DataMain.class);
            //Thread.sleep(61000);
            //第三步 解密
            String newSecret =  TongTechEncode.AESDncode(appkey, secret);
            secret = TongTechEncode.AESDncode(appkey, secret);
            log.info("------redis save newSecret---" + secret);
            //把秘钥放redis保存一个月
            RedisUtil.set(SECRET_REDIS_KEY, secret, 60 * 60 * 24 * 30);
        }
        log.info("本地秘钥:{}",secret);
        // 第四步 根据新秘钥获取新sign
        String rtime2 = "" + System.currentTimeMillis();
        String sign2 = TongTechEncode.HmacSHA256(sid, rid, secret, rtime2);
        log.info("本地秘钥 对加解密后的秘钥第二次签名后的sign2：" + sign2);
        return sign2;
    }

    public static String getLicense(LicenseVo vo) throws Exception{
        Map<String, String> paramsMap = convertToMap(vo,false);
        String secret = (String)RedisUtil.get(SECRET_REDIS_KEY);
        log.info("redis 中秘钥{}",secret);
        if(StringUtils.isBlank(secret)){
            secret = refreshSecret();
        }
        // 第四步 根据新秘钥获取新sign
        String rtime2 = "" + System.currentTimeMillis();
        String sign2 = TongTechEncode.HmacSHA256(sid, rid, secret, rtime2);
        log.info("本地秘钥 对加解密后的秘钥第二次签名后的sign2：" + sign2);
        String result = accessTongtec(rid, sid, sign2, rtime2, paramsMap);
        JSONObject jsonObject = JSON.parseObject(result);
        log.info("LicenseRestfulUtils.getLicense result{}",jsonObject.toString());
        if(jsonObject.containsKey("errCode")){
            ErrorResult errorR = JSON.parseObject(result, ErrorResult.class);
            if(errorR !=null && ("E-109".equals(errorR.getErrCode())||"E-110".equals(errorR.getErrCode()))){
                //刷新秘钥
                LicenseRestfulUtils.refreshSecret();
                log.info("秘钥认证失败 刷新秘钥");
                //return getLicense(vo);
            }
        }
        return result;
    }
    // 访问ESB服务
    public static String accessTongtec(String rid, String sid, String sign2, String rtime, Map<String,String>
            paramsMap) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = tongTecurl;
        if (paramsMap != null) {
            url = url + "?";
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                url = url + entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        log.info("开始 - invoke start url "+url);
        HttpGet httpPost = new HttpGet(url);
        httpPost.setHeader(new BasicHeader("Accept", "application/json;charset=utf-8"));

        String charSet = "UTF-8";
        // 封装请求头
        httpPost.setHeader("Authorization", "Bearer 7bee4ad2-1d1f-36e0-b9ab-625a7e495de2");
        httpPost.setHeader("hnjhpt_rid", rid);
        httpPost.setHeader("hnjhpt_sid", sid);
        httpPost.setHeader("hnjhpt_rtime", rtime);
        httpPost.setHeader("hnjhpt_sign", sign2);

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            log.info("invoke end url："+url+",state"+state+",result"+JSON.toJSONString(response));
            String resp = "";
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                resp = EntityUtils.toString(responseEntity);
                log.info("接口请求结果：result{}",resp);
                return resp;
            }
        } catch (Exception e) {
            log.info("请求错误:{}",e.getMessage());
        }
        return "";
    }
    /**
     * 请求响应格式均为json的Get方法
     *
     * @throws IOException
     */
    public static String getMethodWithJsonHeader(String url, Map<String, String> paramsMap, Map<String, String> header)
            throws IOException {
        CloseableHttpClient client = null;
        String httpId = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            log.debug("http invoke log start:id-"+httpId+",method-get,url-" + url + ",params-" + JSON.toJSONString(paramsMap)
                    + ",header-" + JSON.toJSONString(header));
            if (paramsMap != null) {
                url = url + "?";
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    url = url + entry.getKey() + "=" + entry.getValue() + "&";
                }
            }

            client = HttpClients.createDefault();
            if (!validateUrl(url)) {
                return null;
            }
            HttpGet httpPost = new HttpGet(url);
            // 封装请求头
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
            httpPost.setHeader("Accept", "application/json,text/javascript,*/*;q=0.01");

            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse response = client.execute(httpPost);
            String resp = getResponseEntityString(response);
            log.info("http invoke log end:id-"+httpId+",method-get,url-" + url + ",header"+ header + ",response-" + resp);
            return resp;
        } catch (Exception e) {
            log.error("Exception with id"+httpId, e);
            return null;
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }
    public static String getResponseEntityString(HttpResponse response) {
        try {
            if (response.getStatusLine().getStatusCode() == org.springframework.http.HttpStatus.OK.value()) {
                String resp = "";
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    resp = EntityUtils.toString(resEntity, Consts.UTF_8);
                    return resp;
                } else {
                    log.debug("resEntity is null.......");
                }
            } else {
                log.error("response status is " + response.getStatusLine().getStatusCode());
                String resp = "";
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    resp = EntityUtils.toString(resEntity, Consts.UTF_8);
                    return resp;
                } else {
                    log.debug("resEntity is null.......");
                }
            }
            return null;
        } catch (Exception e) {
            log.error("Exception", e);
            return null;
        }
    }

    /**
     * 校验url是否有相同的参数
     * @param url
     * @return
     */
    public static boolean validateUrl(String url) {
        int index = url.indexOf("?");
        String param = url.substring(index+1);

        String[] params = param.split("&");

        List<String> keys= new ArrayList<>();

        for (String item:params) {
            String[] kv = item.split("=");
            if (keys.contains(kv[0])) {
                return false;
            } else {
                keys.add(kv[0]);
            }
        }
        return true;
    }
    public static Map<String,String> getHeader(String sign,String rtime){
        Map<String,String> header = new HashMap<>(4);
        // 封装请求头
        header.put("hnjhpt_rid", rid);
        header.put("hnjhpt_sid", sid);
        header.put("hnjhpt_rtime", rtime);
        header.put("hnjhpt_sign", sign);
        header.put("Authorization",bear);
        return header;
    }

    public static Map<String, String> convertToMap(Object obj, boolean convertNull) {
        if(obj == null){
            return null;
        }
        Map<String, String> map = new HashMap<>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                Object o = field.get(obj);
                if (Objects.nonNull(o)){
                    if (o instanceof Collection) {
                        if (((Collection)o).size()==0){
                            continue;
                        }
                    } else if (o instanceof Map) {
                        if (((Map) o).size() == 0) {
                            continue;
                        }
                    } else if (o instanceof Enum) {
                        o = ((Enum) o).name();
                    }
                    map.put(field.getName(), o.toString());
                }
                else if(convertNull) {
                    map.put(field.getName(), null);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

}
