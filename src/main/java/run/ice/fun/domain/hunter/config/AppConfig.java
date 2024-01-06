package run.ice.fun.domain.hunter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author DaoDao
 */
@Data
@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Value("${spring.application.name:}")
    private String application;

    @Value("${app.check-api:}")
    private String checkApi;

}
