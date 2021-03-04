package pyg.daheng.base.util.invoke.util;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:06
 */
public class MsinvokeDataConstants {
    public static final String DXTRANSID = "dxtransid";
    public static final String DXSPANID = "dxspanid";
    public static final String MSCALLER = "mscaller";
    public static final String DXTOKEN = "dxtoken";
    public static final String DXTOKENTM = "dxtokentm";
    public static final String DXUSERID = "uid";
    public static final String CHANNEL = "channel";
    public static final String GET_MSCALLER_KEY = "spring.application.name";
    public static final String GET_MSINVOKE_CONNECTION_REQUEST_TIMEOUT_KEY = "msinvoke.connection.request.timeout";
    public static final String GET_MSINVOKE_CONNECTION_TIMEOUT_KEY = "msinvoke.connection.timeout";
    public static final String GET_MSINVOKE_READ_TIMEOUT_KEY = "msinvoke.read.timeout";
    public static final String REQUEST_RETRY_NUM = "msinvoke.request.retry.num";
    public static final String MAX_MAX_PER_ROUTE = "msinvoke.max.per.route";
    public static final String MAX_TOTAL = "msinvoke.max.total";
    public static final String IS_PRINT_INVOKE_LOG = "msinvoke.log.isprint";
    public static final String TRANSACTION_UNIQUE_ID = "transaction-uniqueid";
    public static final String INVOKE_APPLICATIONNAME = "invokeApplicationName";
    public static final String GET_DXQDFLAG_KEY = "msinvoke.dxqd.flag";
    public static final String GET_CUSTOM_REQUEST_HREADER_KEY = "msinvoke.requst.header.custom.valid";
    public static final String GET_REQUEST_HEADER_VALID_KEY = "msinvoke.request.header.valid";
    public static final String GET_REQUEST_PROTOCOL_HEADER_KEY = "msinvoke.request.protocol.header";
    public static final String GET_PROTOCOL_HEADER = MsinvokeAppConfig.getProperty("msinvoke.request.protocol.header", "http://");
    public static final String GET_MSINVOKE_NON_FILTER_KEY = "msinvoke.non.filter";
    public static final String GET_MSINVOKE_TEST_KEY = "msinvoke.test";
    public static final String MSINVOKE_TEST = MsinvokeAppConfig.getProperty("msinvoke.test", "false");
    public static final String PROTOCOL_HTTP = "http://";
    public static final String PROTOCOL_HTTPS = "https://";
    public static final String IDC = "idc";
    public static final String DMZ = "dmz";
    public static final String STATUS = "status";
    public static final String STATUS00 = "00";
    public static final String STATUS99 = "99";
    public static final String DATA = "data";
    public static final int NNW_VALUE = 2;
    public static final String POST = "POST";
    public static final String GET = "GET";

    private MsinvokeDataConstants() {
    }
}
