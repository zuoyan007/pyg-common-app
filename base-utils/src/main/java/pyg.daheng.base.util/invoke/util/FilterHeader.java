package pyg.daheng.base.util.invoke.util;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:02
 */
public class FilterHeader {
    private String dxtoken;
    private String userId;
    private String channel;
    private String baseLoginInfo;

    public FilterHeader() {
    }

    public String getDxtoken() {
        return this.dxtoken;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getChannel() {
        return this.channel;
    }

    public String getBaseLoginInfo() {
        return this.baseLoginInfo;
    }

    public void setDxtoken(String dxtoken) {
        this.dxtoken = dxtoken;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setBaseLoginInfo(String baseLoginInfo) {
        this.baseLoginInfo = baseLoginInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof FilterHeader)) {
            return false;
        } else {
            FilterHeader other = (FilterHeader)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$dxtoken = this.getDxtoken();
                    Object other$dxtoken = other.getDxtoken();
                    if (this$dxtoken == null) {
                        if (other$dxtoken == null) {
                            break label59;
                        }
                    } else if (this$dxtoken.equals(other$dxtoken)) {
                        break label59;
                    }

                    return false;
                }

                Object this$userId = this.getUserId();
                Object other$userId = other.getUserId();
                if (this$userId == null) {
                    if (other$userId != null) {
                        return false;
                    }
                } else if (!this$userId.equals(other$userId)) {
                    return false;
                }

                Object this$channel = this.getChannel();
                Object other$channel = other.getChannel();
                if (this$channel == null) {
                    if (other$channel != null) {
                        return false;
                    }
                } else if (!this$channel.equals(other$channel)) {
                    return false;
                }

                Object this$baseLoginInfo = this.getBaseLoginInfo();
                Object other$baseLoginInfo = other.getBaseLoginInfo();
                if (this$baseLoginInfo == null) {
                    if (other$baseLoginInfo != null) {
                        return false;
                    }
                } else if (!this$baseLoginInfo.equals(other$baseLoginInfo)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof FilterHeader;
    }

    @Override
    public int hashCode() {
        //int PRIME = true;
        int result = 1;
        Object $dxtoken = this.getDxtoken();
        result = result * 59 + ($dxtoken == null ? 43 : $dxtoken.hashCode());
        Object $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        Object $channel = this.getChannel();
        result = result * 59 + ($channel == null ? 43 : $channel.hashCode());
        Object $baseLoginInfo = this.getBaseLoginInfo();
        result = result * 59 + ($baseLoginInfo == null ? 43 : $baseLoginInfo.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "FilterHeader(dxtoken=" + this.getDxtoken() + ", userId=" + this.getUserId() + ", channel=" + this.getChannel() + ", baseLoginInfo=" + this.getBaseLoginInfo() + ")";
    }
}
