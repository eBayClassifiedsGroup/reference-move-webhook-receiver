//------------------------------------------------------------------
// Copyright 2020 mobile.de GmbH.
// Author/Developer: Philipp Bartsch
//
// This code is licensed under MIT license (see LICENSE for details)
//------------------------------------------------------------------
package org.example.move.webhookreceiver.config;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        List<ResponseMessage> defaultResponseMessages = getDefaultResponseMessages();
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(createApiInfo())
            .directModelSubstitute(ResponseEntity.class, Void.class)
            .globalResponseMessage(GET, defaultResponseMessages)
            .globalResponseMessage(DELETE, defaultResponseMessages)
            .globalResponseMessage(PUT, defaultResponseMessages)
            .globalResponseMessage(POST, defaultResponseMessages)
            .useDefaultResponseMessages(false)
            .useDefaultResponseMessages(false);
    }

    private List<ResponseMessage> getDefaultResponseMessages() {
        ResponseMessage forbiddenMessage = new ResponseMessageBuilder()
            .code(FORBIDDEN.value())
            .message("API client may not access the specified data.")
            .build();
        ResponseMessage internalErrorMessage = new ResponseMessageBuilder()
            .code(INTERNAL_SERVER_ERROR.value())
            .message("The given request led to an internal error. The issue has been logged and will be addressed.")
            .build();
        ResponseMessage moveUnavailableMessage = new ResponseMessageBuilder()
            .code(SERVICE_UNAVAILABLE.value())
            .message("MoVe platform is currently not fully available, please try again.")
            .build();

        return asList(forbiddenMessage, internalErrorMessage, moveUnavailableMessage);
    }

    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
            .title("Webhook Receiver")
            .build();
    }
}
