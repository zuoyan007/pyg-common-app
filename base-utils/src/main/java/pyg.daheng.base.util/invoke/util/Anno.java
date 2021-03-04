package pyg.daheng.base.util.invoke.util;

import java.io.Serializable;

/**
 * @author ZhanSSH
 * @date 2021/2/5 15:37
 */
public class Anno implements Serializable {
    private static final long serialVersionUID = 1L;
    private Number tm;
    private String oper;
    private String msg;

    public Anno() {
    }

    public Number getTm() {
        return this.tm;
    }

    public void setTm(Number tm) {
        this.tm = tm;
    }

    public String getOper() {
        return this.oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
