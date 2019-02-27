package cc.lovezhy.netease.sale.config;

import cc.lovezhy.netease.sale.common.JSONConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer(JSONConverter jsonConverter,
                                             HandlerInterceptor handlerInterceptor,
                                             HandlerMethodArgumentResolver userInfoArgumentResolver) {
        return new WebMvcConfigurer() {
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(jsonConverter);
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(handlerInterceptor)
                        .addPathPatterns("/**")
                        .excludePathPatterns("/css/*", "/js/*");
            }

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(userInfoArgumentResolver);
            }
        };
    }
}
