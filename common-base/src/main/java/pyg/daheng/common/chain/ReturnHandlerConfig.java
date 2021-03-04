package pyg.daheng.common.chain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhanSSH
 * @date 2021/3/3 15:50
 */
@Configuration
public class ReturnHandlerConfig {

    @Bean(value = "getChainOfReturnHandler")
    public AbstractReturnHandler getChainOfReturnHandler() {
        return this.setMsAppReturnHandler();
    }

    @Bean(value = "setMsAppReturnHandler")
    public MsAppReturnHandler setMsAppReturnHandler() {
        return new MsAppReturnHandler(this.setResultReturnHandler());
    }

    @Bean(value = "setResultReturnHandler")
    public ResultReturnHandler setResultReturnHandler() {
        return new ResultReturnHandler(null);
    }

}
