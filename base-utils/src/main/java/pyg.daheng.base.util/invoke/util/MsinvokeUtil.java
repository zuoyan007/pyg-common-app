package pyg.daheng.base.util.invoke.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:24
 */
public class MsinvokeUtil {
    private static final Logger log = LoggerFactory.getLogger(MsinvokeUtil.class);

    public MsinvokeUtil() {
    }

    public static void checkBaseHeader(HttpHeaders headers) throws Exception {
        if ("true".equals(MsinvokeAppConfig.getProperty("msinvoke.request.header.valid", "false"))) {
            checkHeader(headers, "dxspanid", "spanId为空!");
            checkHeader(headers, "dxtransid", "交易标识为空!");
            checkHeader(headers, "dxtoken", "TOKEN为空!");
            checkHeader(headers, "dxtokentm", "调用发起时间为空!");
            checkHeader(headers, "mscaller", "调用服务名为空!");
        }

        String customKey = MsinvokeAppConfig.getProperty("msinvoke.requst.header.custom.valid");
        if (!StringUtils.isEmpty(customKey)) {
            String[] keyArray = customKey.split(",");
            String[] var3 = keyArray;
            int var4 = keyArray.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String key = var3[var5];
                checkHeader(headers, key, key + "为空");
            }
        }

    }

    public static void checkBaseHeader(HttpEntity<?> requestEntity) throws Exception {
        HttpHeaders headers = requestEntity.getHeaders();
        checkBaseHeader(headers);
    }

    public static void checkHeader(HttpHeaders headers, String headerKey, String exceptionMsg) throws Exception {
        List<String> keyList = headers.get(headerKey);
        if (keyList == null || keyList.isEmpty() || org.apache.commons.lang.StringUtils.isEmpty((String)keyList.get(0))) {
            throw new Exception(exceptionMsg);
        }
    }

    public static boolean checkProtocolHeader(String serviceName) {
        String serName = serviceName.toLowerCase();
        return serName.contains("http://") || serName.contains("https://");
    }

    public static int checkGateway(String serviceName) {
        String mscaller = MsinvokeAppConfig.getProperty("spring.application.name");
        String mscallerFlag = idcOrDmz(mscaller);
        if (mscallerFlag == null) {
            log.info("#checkGateway# 当前微服务名称无法获取服务区域--默认按同网关处理 ");
            return 1;
        } else {
            String serviceFlag = idcOrDmz(serviceName);
            if (serviceFlag == null) {
                log.info("#checkGateway# 被调用微服务名称无法获取服务区域--默认按同网关处理 ");
                return 3;
            } else {
                return mscallerFlag.equals(serviceFlag) ? 1 : 2;
            }
        }
    }

    public static String idcOrDmz(String serviceName) {
        if (serviceName == null) {
            return null;
        } else {
            int dmzFlag = serviceName.toLowerCase().indexOf("dmz");
            int idcFlag = serviceName.toLowerCase().indexOf("idc");
            if (dmzFlag > 0) {
                return "dmz";
            } else {
                return idcFlag > 0 ? "idc" : null;
            }
        }
    }

    public static String urlGetServiceName(String url) {
        String regex = "^((http://)|(https://))?.*/";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String url2 = matcher.group(0);
            return url2.replaceAll("^((http://)|(https://))", "").split("/")[0];
        } else {
            return null;
        }
    }

    public static boolean isIpAddress(String url) {
        String ip = urlGetServiceName(url);
        if (ip == null) {
            return false;
        } else {
            String regex = "^(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})(\\.(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})){3}$";
            String regex2 = "^(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})(\\.(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})){3}:([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";
            return ip.matches(regex) ? true : ip.matches(regex2);
        }
    }

    public static List<String> getCookie(HttpHeaders headers) {
        List<String> list = headers.get("Cookie");
        return list;
    }

    public static String getCookieValue(HttpHeaders headers, String key) {
        List<String> cookies = getCookie(headers);
        Iterator var3 = cookies.iterator();

        String k;
        String val;
        do {
            if (!var3.hasNext()) {
                return null;
            }

            String v = (String)var3.next();
            int i = v.lastIndexOf("=") - 1;
            k = v.substring(0, i - 1);
            val = v.substring(i);
        } while(!k.equals(key));

        return val;
    }

    public static HttpHeaders setCookie(HttpHeaders headers, List<String> cookie) {
        if (headers == null) {
            headers = new HttpHeaders();
        }

        headers.put("Cookie", cookie);
        return headers;
    }

    public static void main(String[] args) {
    }
}
