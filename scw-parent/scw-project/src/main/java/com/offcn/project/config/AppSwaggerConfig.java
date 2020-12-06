package com.offcn.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//swagger配置类
@Configuration
@EnableSwagger2 //开启swagger的配置
public class AppSwaggerConfig {

    //swagger的介绍方法
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("七易众筹项目接口文档")
                .description("项目模块接口描述")
                .contact("dbq")
                .version("1.0")
                .termsOfServiceUrl("www.ujiuye.com").build();
    }


    @Bean
    public Docket  createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.offcn.project.controller"))
                .paths(PathSelectors.any())
                .build();

    }
}
