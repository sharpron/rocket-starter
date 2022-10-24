package rocket.starter.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * Open API config.
 *
 * @author ron
 */
@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer")
@OpenAPIDefinition(
    info = @Info(title = "My API", version = "v1", description = "文档描述"),
    security = @SecurityRequirement(name = "bearerAuth"))
public class OpenApi3Config implements InitializingBean {

  @Override
  public void afterPropertiesSet() {
    SpringDocUtils.getConfig()
        .replaceWithClass(
            org.springframework.data.domain.Pageable.class,
            org.springdoc.core.converters.models.Pageable.class);
  }
}
