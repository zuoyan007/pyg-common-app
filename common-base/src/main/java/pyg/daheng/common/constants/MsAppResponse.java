package pyg.daheng.common.constants;

/**
 * @author ZhanSSH
 * @date 2021/2/5 11:56
 */
public class MsAppResponse {

    private String sta;
    private String message;
    private Object data;

    public MsAppResponse(String sta, String message, Object data) {
        this.sta = sta;
        this.message = message;
        this.data = data;
    }

    public MsAppResponse(String sta, String message) {
        this.sta = sta;
        this.message = message;
    }

    public MsAppResponse(String sta) {
        this.sta = sta;
    }

    public MsAppResponse() {

    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
