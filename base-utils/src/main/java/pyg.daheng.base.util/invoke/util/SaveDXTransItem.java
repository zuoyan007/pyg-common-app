package pyg.daheng.base.util.invoke.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:56
 */
public class SaveDXTransItem {
    private static final Logger log = LoggerFactory.getLogger(SaveDXTransItem.class);
    private static String DXTransItemNewestPath = MonitorConfig.getDXTransItemNewestPath();
    private static String DXTransItemBackupPath = MonitorConfig.getDXTransItemBackupPath();
    private static long maximum = MonitorConfig.getFileMaximum();
    private static DXQueueManager qmanager = DXQueueManager.getInstance();
    public static final Integer HTTP_TIMEOUT = 20000;
    public static final String LOG_COLLECT_SERVER_URL = "log.collect.server.url";
    public static final String LOG_COLLECT_LISTTRACE_PATH = "/collect/list/trace";

    public SaveDXTransItem() {
    }

    public static void saveDXTransItem(DXTransItem transItem) {
        try {
            if (transItem != null) {
                qmanager.add(transItem);
                log.info("log queue size:{}", qmanager.size(new DXTransItem()));
            }
        } catch (Exception var2) {
            log.error("### SaveDXTransItem saveDXTransItem() ###", var2);
        }

    }

    public static void saveDXTransItemToFile(String transItem) {
        File newestFile = null;
        File backupFile = null;
        BufferedWriter writer = null;
        newestFile = new File(DXTransItemNewestPath);
        backupFile = new File(DXTransItemBackupPath);

        try {
            if (newestFile.exists() && backupFile.exists()) {
                if (newestFile.length() > maximum) {
                    backupFile.delete();
                    newestFile.renameTo(backupFile);
                    newestFile.createNewFile();
                }
            } else if (newestFile.exists() && !backupFile.exists()) {
                if (newestFile.length() > maximum) {
                    newestFile.renameTo(backupFile);
                    newestFile.createNewFile();
                }
            } else if (!newestFile.exists() || !backupFile.exists()) {
                if (!newestFile.getParentFile().exists()) {
                    newestFile.getParentFile().mkdir();
                }

                newestFile.createNewFile();
            }

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newestFile, true), "UTF-8"));
            writer.write(transItem);
            writer.flush();
            writer.close();
        } catch (IOException var13) {
            log.error("### saveDXTransItemToFile saveDXTransItem() ###", var13);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException var12) {
                    log.error("###################context:", var12);
                }
            }

        }

    }

    public static String saveDXTransItemToRemote(List<DXTransItem> dxTransItemList) {
        String result = "";
        Map<String, Object> trsListMap = new HashMap();
        trsListMap.put("list", dxTransItemList);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_TIMEOUT).setSocketTimeout(HTTP_TIMEOUT).build();

        try {
            String url = getLogCollectUrl();
            String reqData;
            if (url == null) {
                log.error("发送链路追踪日志 POST 请求URL未配置，key是log.collect.server.url");
                reqData = result;
                return reqData;
            }

            reqData = JSON.toJSONString(trsListMap);
            HttpPost httppost = new HttpPost(url);
            httppost.setConfig(requestConfig);
            HttpEntity re = new StringEntity(reqData, Consts.UTF_8);
            httppost.setHeader("Content-Type", "application/json; charset=utf-8");
            httppost.setEntity(re);
            HttpResponse response = httpClient.execute(httppost);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception var19) {
            log.error("发送链路追踪日志 POST 请求出现异常！" + var19);
        } finally {
            try {
                httpClient.close();
            } catch (IOException var18) {
                log.error(var18.getMessage(), var18);
            }

        }

        return result;
    }

    private static String getLogCollectUrl() {
        String url = (String)MonitorConfig.configs.get("log.collect.server.url");
        return url != null && !url.equals("") ? url + "/collect/list/trace" : null;
    }
}
