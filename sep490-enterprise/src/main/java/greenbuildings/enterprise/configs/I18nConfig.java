package greenbuildings.enterprise.configs;

import commons.springfw.impl.config.CommonI18nConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class I18nConfig {
    
    @Bean
    public LocaleResolver localeResolver() {
        return CommonI18nConfig.statelessLocaleResolver();
    }
    
}
