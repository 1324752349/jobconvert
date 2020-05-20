package com.hc.jodconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "webconfig")
public class WebConfig extends WebMvcConfigurationSupport {

    private String pathPatterns;

    private String resourceLocations;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/tmp/**")
                .addResourceLocations("file:/Users/maxray/Downloads/tmp/");
    }

    @Bean
    public HttpMessageConverter responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    public String getPathPatterns() {
        return pathPatterns;
    }

    public void setPathPatterns(String pathPatterns) {
        this.pathPatterns = pathPatterns;
    }

    public String getResourceLocations() {
        return resourceLocations;
    }

    public void setResourceLocations(String resourceLocations) {
        this.resourceLocations = resourceLocations;
    }

}
