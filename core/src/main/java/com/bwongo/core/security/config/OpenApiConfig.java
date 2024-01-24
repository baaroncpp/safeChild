package com.bwongo.core.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/10/23
 **/
@OpenAPIDefinition(info = @Info(title = "Safe Child Core API",
        description = "List APIs for operations that can be performed on web ui", version = "v1"),
        security = @SecurityRequirement(name = "security_auth"))
@SecurityScheme(name = "security_auth", type = SecuritySchemeType.OAUTH2,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "jwt",
        paramName = "Authorization",
        scheme = "bearer",
        flows = @OAuthFlows(password = @OAuthFlow(
                tokenUrl = "${springdoc.oAuthFlow.password.tokenUrl}", scopes = {})))
public class OpenApiConfig {
}
