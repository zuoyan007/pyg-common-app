package pyg.daheng.base.util.invoke.util;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:07
 */
public class MsinvokeBeanConfig {
    private static final Logger log = LoggerFactory.getLogger(MsinvokeBeanConfig.class);
    private static int MAX_TOTAL = Integer.parseInt(MsinvokeAppConfig.getProperty("msinvoke.max.total", "2000"));
    private static int MAX_MAX_PER_ROUTE = Integer.parseInt(MsinvokeAppConfig.getProperty("msinvoke.max.per.route", "200"));
    private static int REQUEST_RETRY_NUM = Integer.parseInt(MsinvokeAppConfig.getProperty("msinvoke.request.retry.num", "3"));
    private static int CONNECT_TIMEOUT = Integer.parseInt(MsinvokeAppConfig.getProperty("msinvoke.connection.timeout", "3000"));
    private static int READ_TIMEOUT = Integer.parseInt(MsinvokeAppConfig.getProperty("msinvoke.read.timeout", "30000"));
    private static int CONNECTION_REQUEST_TIMEOUT = Integer.parseInt(MsinvokeAppConfig.getProperty("msinvoke.connection.request.timeout", "5000"));
    public static boolean IS_PRINT_INVOKE_LOG = true;

    public MsinvokeBeanConfig() {
    }

    @Value("${msinvoke.log.isprint:true}")
    public void setIsPrintInvokeLog(boolean value) {
        log.info("变更接口调用信息日志打印开关值:" + value);
        IS_PRINT_INVOKE_LOG = value;
    }

    @Bean
    public FilterRegistrationBean<MsinvokeFilter> msinvokerFilterRegistration() {
        FilterRegistrationBean<MsinvokeFilter> registration = new FilterRegistrationBean();
        registration.setFilter(new MsinvokeFilter());
        registration.addUrlPatterns(new String[]{"/*"});
        registration.setOrder(5);
        return registration;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory msinvokeHttpRequestFactory() {
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            SSLContext sslContext = SSLContext.getDefault();
            httpClientBuilder.setSSLContext(sslContext);
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = (Registry)RegistryBuilder.create().register("http",
                    PlainConnectionSocketFactory.getSocketFactory()).register("https", sslConnectionSocketFactory).build();
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL);
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_MAX_PER_ROUTE);
            httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(REQUEST_RETRY_NUM, true));
            HttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            clientHttpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
            clientHttpRequestFactory.setReadTimeout(READ_TIMEOUT);
            clientHttpRequestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT);
            return clientHttpRequestFactory;
        } catch (Exception var9) {
            log.error("初始化HTTP连接池出错", var9);
            return null;
        }
    }

    @Bean
    @LoadBalanced
    RestTemplate msinvokeRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(this.msinvokeHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    RestTemplate ipTemplate() {
        RestTemplate restTemplate = new RestTemplate(this.msinvokeHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    MsinvokeClient msinvokeClient() {
        return new MsinvokeClient();
    }
}
