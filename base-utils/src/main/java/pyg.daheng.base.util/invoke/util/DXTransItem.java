package pyg.daheng.base.util.invoke.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:27
 */
public class DXTransItem implements Serializable, IQueueItem {
    private static final long serialVersionUID = 1L;
    private String dxtransId;
    private String dxspanId;
    private String mscaller;
    private String mscalled;
    private String name;
    private long tm;
    private long duration;
    private String url;
    private String protocol;
    private String respcode;
    private String respmsg;
    private String userId;
    private AtomicInteger currCallXH = new AtomicInteger();
    private AtomicInteger sqlRId;
    private String channel;
    private String ip;
    private List<Anno> anno;

    public String getTopic() {
        return "topic-dxmonitor";
    }

    public DXTransItem() {
        this.currCallXH.set(1);
        this.sqlRId = new AtomicInteger();
        this.sqlRId.set(1);
    }

    public Integer getNextSqlRId() {
        return this.sqlRId.getAndIncrement();
    }

    public Integer GetNextCallXH() {
        return this.currCallXH.getAndIncrement();
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDxtransId() {
        return this.dxtransId;
    }

    public void setDxtransId(String dxtransId) {
        this.dxtransId = dxtransId;
    }

    public String getDxspanId() {
        return this.dxspanId;
    }

    public void setDxspanId(String dxspanId) {
        this.dxspanId = dxspanId;
    }

    public String getMscaller() {
        return this.mscaller;
    }

    public void setMscaller(String mscaller) {
        this.mscaller = mscaller;
    }

    public String getMscalled() {
        return this.mscalled;
    }

    public void setMscalled(String mscalled) {
        this.mscalled = mscalled;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTm() {
        return this.tm;
    }

    public void setTm(long tm) {
        this.tm = tm;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRespcode() {
        return this.respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }

    public String getRespmsg() {
        return this.respmsg;
    }

    public void setRespmsg(String respmsg) {
        this.respmsg = respmsg;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Anno> getAnno() {
        if (this.anno == null) {
            this.anno = new ArrayList();
        }

        return this.anno;
    }

    public void setAnno(List<Anno> anno) {
        this.anno = anno;
    }

    public void addanno(String oper, String msg) {
        Anno anno = new Anno();
        anno.setTm(System.currentTimeMillis());
        anno.setMsg(msg);
        anno.setOper(oper);
        this.getAnno().add(anno);
    }

    public String toString() {
        return "DXTransItem [dxtransid=" + this.dxtransId + ", dxspanid=" + this.dxspanId + ", mscaller=" + this.mscaller + ", mscalled=" + this.mscalled + ", name=" + this.name + ", tm=" + this.tm + ", duration=" + this.duration + ", url=" + this.url + ", protocol=" + this.protocol + ", respcode=" + this.respcode + ", respmsg=" + this.respmsg + ", userId=" + this.userId + ", channel=" + this.channel + ", ip=" + this.ip + ", anno=" + this.anno + "]";
    }

    public String getSimpleName() {
        return this.getClass().getSimpleName();
    }
}
