package app.config;

import app.controller.converters.StringToRestaurantSorterConverter;
import app.controller.converters.StringToSortDirectionConverter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("app.controller")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToRestaurantSorterConverter());
        registry.addConverter(new StringToSortDirectionConverter());
    }

}
