package com.tu.codeguard.config;

import java.lang.reflect.Method;

import com.tu.codeguard.utils.ApiConstants;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * The {@code CustomRequestMappingHandlerMapping} is used to override the mapping for @RequestMapping
 * and add the "/api" prefix to the path.
 */
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private static final String CONTROLLER_PACKAGE = ".controller";

    @Override
    protected RequestMappingInfo getMappingForMethod(@NotNull Method method, @NotNull Class<?> handlerType) {
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);

        if (mappingInfo == null) {
            return null;
        }

        if (handlerType.getPackageName()
                .endsWith(CONTROLLER_PACKAGE)) {
            String className = handlerType.getSimpleName().toLowerCase();

            StringBuilder pathSegment = new StringBuilder(ApiConstants.API_URL);
            if (className.startsWith(ApiConstants.PUBLIC_URL)) {
                pathSegment.append(ApiConstants.PUBLIC_URL);
            } else if (className.startsWith(ApiConstants.ADMIN_URL)) {
                pathSegment.append(ApiConstants.ADMIN_URL);
            }

            RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
            config.setPatternParser(PathPatternParser.defaultInstance);
            return RequestMappingInfo.paths(pathSegment.toString())
                    .options(config)
                    .build()
                    .combine(mappingInfo);
        }

        return mappingInfo;
    }
}