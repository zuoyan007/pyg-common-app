package pyg.daheng.common.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig
 * @author ZhanSSH
 * @date 2021/1/14 16:55
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("pyg.daheng.common.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    /**配置在线文档的基本信息  http://localhost:8868/swagger-ui.html#*/
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("springboot利用swagger构建api文档")
                .description("简单优雅的restfun风格")
                .termsOfServiceUrl("https://pyg.daheng.com")
                .version("1.0")
                .build();

    }
}
