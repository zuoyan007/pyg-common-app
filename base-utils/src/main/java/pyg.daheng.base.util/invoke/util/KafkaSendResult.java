package pyg.daheng.base.util.invoke.util;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:51
 */
public class KafkaSendResult {
    private Boolean status = false;
    private String topic;
    private Object msg;
    private Exception err;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Exception getErr() {
        return err;
    }

    public void setErr(Exception err) {
        this.err = err;
    }
}
