package pyg.daheng.base.util.invoke.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:23
 */
public class MsinvokeHeaderFactory {
    private static final Logger log = LoggerFactory.getLogger(MsinvokeHeaderFactory.class);

    public MsinvokeHeaderFactory() {
    }

    public static HttpHeaders getHeader(List<String> cookie) {
        HttpHeaders header = getHeader();
        header.put("Cookie", cookie);
        return header;
    }

    public static HttpHeaders getHeader() {
        String dxspanid = getDxspanid();
        String dxtransid = getDxtransid();
        String dxtoken = getDxtoken();
        String userId = getUserId();
        return getHeadr(dxtransid, dxspanid, dxtoken, userId);
    }

    public static HttpHeaders getHeader(Map<String, String> headerParams) {
        HttpHeaders header = getHeader();
        MapSetHeadr(header, headerParams);
        return header;
    }

    public static HttpHeaders getHeadr(String dxtransid, String dxspanid, String dxtoken, String userId) {
        String mscaller = getMscaller();
        String channel = getChannel();
        String baseLoginInfo = getBaseLoginInfo();
        return getHeadr(dxtransid, dxspanid, mscaller, dxtoken, System.currentTimeMillis(), channel, userId, baseLoginInfo);
    }

    public static HttpHeaders getHeadr(String dxtransid, String dxspanid, String mscaller, String dxtoken, Long dxtokentm, String channel, String dxuserid, String baseLoginInfo) {
        Map<String, String> headerParams = new HashMap();
        headerParams.put("dxtransid", dxtransid);
        headerParams.put("dxspanid", dxspanid);
        headerParams.put("mscaller", mscaller);
        headerParams.put("dxtoken", dxtoken);
        headerParams.put("dxtokentm", dxtokentm.toString());
        headerParams.put("channel", channel);
        headerParams.put("uid", dxuserid);
        headerParams.put("baseLoginInfo", baseLoginInfo);
        headerParams.put("accept", "*/*");
        return getBaseHeadr(headerParams);
    }

    public static HttpHeaders getBaseHeadr(Map<String, String> headerParams) {
        return geHeaders(new HttpHeaders(), headerParams);
    }

    public static HttpHeaders geHeaders(HttpHeaders headers, Map<String, String> headerParams) {
        MapSetHeadr(headers, headerParams);
        return headers;
    }

    public static void MapSetHeadr(HttpHeaders headers, Map<String, String> params) {
        if (params != null && !params.isEmpty() && headers != null) {
            try {
                Map<String, String> reMap = removeMaptoBasePar(params);
                Iterator var3 = params.entrySet().iterator();

                while(var3.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry)var3.next();
                    headers.add((String)entry.getKey(), (String)entry.getValue());
                }

                changerHeadr(headers, reMap);
            } catch (Exception var5) {
                log.error("#MapSetHeadr#headers" + JSON.toJSONString(headers) + "  params" + JSON.toJSONString(params) + " error:" + var5.getMessage());
            }
        }

    }

    private static void changerHeadr(HttpHeaders headers, Map<String, String> params) {
        try {
            Iterator var2 = params.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var2.next();
                headers.remove(entry.getKey());
                headers.add((String)entry.getKey(), (String)entry.getValue());
            }
        } catch (Exception var4) {
            log.error("#changerHeadr#headers" + JSON.toJSONString(headers) + "  params" + JSON.toJSONString(params) + " error:" + var4.getMessage());
        }

    }

    private static Map<String, String> removeMaptoBasePar(Map<String, String> params) {
        Map<String, String> reMap = new HashMap();
        moveMap(params, "dxspanid", reMap);
        moveMap(params, "dxtransid", reMap);
        moveMap(params, "dxtoken", reMap);
        moveMap(params, "dxtokentm", reMap);
        moveMap(params, "mscaller", reMap);
        moveMap(params, "channel", reMap);
        moveMap(params, "uid", reMap);
        return reMap;
    }

    private static void moveMap(Map<String, String> params, String key, Map<String, String> reMap) {
        String remove = null;
        remove = (String)params.remove(key);
        if (StringUtils.isNotEmpty(remove)) {
            reMap.put(key, remove);
        }

    }

    private static String getDxspanid() {
        try {
            String dxSpanId = DXMSMonitor.GetNextSpanID();
            return dxSpanId == null ? "" : dxSpanId;
        } catch (Exception var1) {
            return "";
        }
    }

    private static String getDxtransid() {
        try {
            DXTransItem transItem = (DXTransItem)DXThreadLocal.getDxTransItemTlInstance().get();
            if (transItem != null) {
                String dxtransid = transItem.getDxtransId();
                return dxtransid == null ? "" : dxtransid;
            }
        } catch (Exception var2) {
            ;
        }

        return "";
    }

    private static String getDxtoken() {
        try {
            FilterHeader filterHeader = (FilterHeader)MsinvokeThreadLocal.getFilterHeader().get();
            String dxtoken = filterHeader.getDxtoken();
            if (dxtoken == null) {
                return "true".equals(MsinvokeAppConfig.getProperty("msinvoke.test")) ? "test" : null;
            } else {
                return dxtoken;
            }
        } catch (Exception var2) {
            return "";
        }
    }

    public static String getMscaller() {
        try {
            String mscaller = MsinvokeAppConfig.getProperty("spring.application.name");
            return org.springframework.util.StringUtils.isEmpty(mscaller) ? "" : mscaller;
        } catch (Exception var1) {
            return "";
        }
    }

    public static String getChannel() {
        try {
            String channel = ((FilterHeader)MsinvokeThreadLocal.getFilterHeader().get()).getChannel();
            return org.springframework.util.StringUtils.isEmpty(channel) ? null : channel;
        } catch (Exception var1) {
            return "";
        }
    }

    private static String getUserId() {
        try {
            String userId = ((FilterHeader)MsinvokeThreadLocal.getFilterHeader().get()).getUserId();
            if (org.springframework.util.StringUtils.isEmpty(userId)) {
                return "true".equals(MsinvokeAppConfig.getProperty("msinvoke.test")) ? "test" : null;
            } else {
                return userId;
            }
        } catch (Exception var1) {
            return "";
        }
    }

    private static String getBaseLoginInfo() {
        try {
            String baseLoginInfo = ((FilterHeader)MsinvokeThreadLocal.getFilterHeader().get()).getBaseLoginInfo();
            return org.springframework.util.StringUtils.isEmpty(baseLoginInfo) ? null : baseLoginInfo;
        } catch (Exception var1) {
            return "";
        }
    }
}
