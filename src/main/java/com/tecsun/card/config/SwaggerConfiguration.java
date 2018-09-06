package com.tecsun.card.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 0214
 * @createTime 2018/9/6
 * @description
 */

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.tecsun.card.controller"))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

//    @Bean
//    public Docket getApiInfo() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("通过API接口文档")
//                .apiInfo(apiInfo("测试环境通过接口"))
//                .pathMapping("/")
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.tecsun.card.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo(String desc) {
//        return new ApiInfoBuilder()
//                .title(desc)
//                .contact(new Contact("姜家俊", "www.baidu.com", "807500966@qq.com"))
//                .version("1.0")
//                .description("API描述")
//                .build();
//    }
//
//    @Bean
//    public UiConfiguration getUiConfig() {
//        return new UiConfiguration(
//                null,
//                "none",
//                "alpha",
//                "schema",
//                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
//                true,
//                true);
//    }
}
