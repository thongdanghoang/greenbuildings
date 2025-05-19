package greenbuildings.commons.springfw.impl.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import static freemarker.template.Configuration.VERSION_2_3_33;


@Configuration
public class MailConfig {

    @Bean
    FreeMarkerConfigurer freeMarkerConfigurer() {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(VERSION_2_3_33);
        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/mailTemplates");
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setLocalizedLookup(true);
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }
}
