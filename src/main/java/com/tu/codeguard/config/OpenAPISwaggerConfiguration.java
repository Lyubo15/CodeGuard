package com.tu.codeguard.config;

import com.tu.codeguard.utils.ApiConstants;
import com.tu.codeguard.utils.Constants;
import com.tu.codeguard.utils.ProfileChecker;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.List;

@Configuration
public class OpenAPISwaggerConfiguration {

    private final ProfileChecker profileChecker;

    public OpenAPISwaggerConfiguration(ProfileChecker profileChecker) {
        this.profileChecker = profileChecker;
    }

    @Bean
    GroupedOpenApi publicGroup() {
        return GroupedOpenApi.builder()
                .group("Public")
                .addOpenApiCustomizer(publicApiCustomizer())
                .pathsToMatch(ApiConstants.API_URL + ApiConstants.PUBLIC_URL + ApiConstants.INCLUDE_ALL_URL)
                .build();
    }

    @Bean
    GroupedOpenApi adminGroup() {
        return GroupedOpenApi.builder()
                .group("Admin")
                .addOpenApiCustomizer(adminApiCustomizer())
                .pathsToMatch(ApiConstants.API_URL + ApiConstants.ADMIN_URL + ApiConstants.INCLUDE_ALL_URL)
                .build();
    }

    @Bean
    public OpenApiCustomizer publicApiCustomizer() {
        String jwt = "JWT";

        return openApi -> {
            if (profileChecker.isProd()) {
                openApi.setServers(List.of(new Server().url("https://api.codeguardai.org")));
            }

            openApi.addSecurityItem(new SecurityRequirement().addList(jwt))
                    .info(apiInfo().title(Constants.PUBLIC_TITLE))
                    .getPaths().values().forEach(this::responses);

            openApi.getComponents()
                    .addSecuritySchemes(jwt, new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .in(SecurityScheme.In.HEADER)
                            .scheme("bearer")
                            .bearerFormat(jwt)
                            .name(jwt));
        };
    }

    @Bean
    public OpenApiCustomizer adminApiCustomizer() {
        String basic = "Basic";

        return openApi -> {
            if (profileChecker.isProd()) {
                openApi.setServers(List.of(new Server().url("https://api.codeguardai.org")));
            }

            openApi.addSecurityItem(new SecurityRequirement().addList(basic))
                    .info(apiInfo().title(Constants.ADMIN_TITLE))
                    .getPaths().values().forEach(this::responses);

            openApi.getComponents()
                    .addSecuritySchemes(basic, new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .in(SecurityScheme.In.HEADER)
                            .scheme("basic")
                            .name(basic));
        };
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ForwardedHeaderFilter());
        return bean;
    }

    private void responses(PathItem pathItem) {
        pathItem.readOperations().
                forEach(operation -> {
                    ApiResponses apiResponses = operation.getResponses();
                    ApiResponse apiResponse201 = new ApiResponse().description("Created");
                    apiResponses.addApiResponse("201", apiResponse201);
                    ApiResponse apiResponse400 = new ApiResponse().description("Bad Request");
                    apiResponses.addApiResponse("400", apiResponse400);
                    ApiResponse apiResponse401 = new ApiResponse().description("Unauthorized");
                    apiResponses.addApiResponse("401", apiResponse401);
                    ApiResponse apiResponse403 = new ApiResponse().description("Forbidden");
                    apiResponses.addApiResponse("403", apiResponse403);
                    ApiResponse apiResponse404 = new ApiResponse().description("Not Found");
                    apiResponses.addApiResponse("404", apiResponse404);
                    ApiResponse apiResponse500 = new ApiResponse().description("Internal Server Error");
                    apiResponses.addApiResponse("500", apiResponse500);
                });
    }

    private Info apiInfo() {
        Info info = new Info();
        info.setTitle("Code Guard API");
        info.setDescription("API for sending source code for review");
        info.setContact(contactBuilder());
        return info;
    }

    private Contact contactBuilder() {
        Contact contact = new Contact();
        contact.setName(Constants.CODE_GUARD_TEAM);
        contact.setEmail(Constants.CODE_GUARD_MAIL);
        return contact;
    }
}
