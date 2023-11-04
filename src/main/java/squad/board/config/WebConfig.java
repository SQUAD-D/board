package squad.board.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import squad.board.argumentresolver.SessionAttributeArgumentResolver;
import squad.board.intercepter.SessionInterceptor;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig implements WebMvcConfigurer {
    private final SessionInterceptor sessionInterceptor;
    private final SessionAttributeArgumentResolver sessionAttributeArgumentResolver;

    // Session Interceptor 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("session interceptor 등록");
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/js/**", "/webjars/axios/1.4.0/**", "/", "/api/members/login", "/api/members/logout", "/api/members/id-validation", "/api/members/nick-validation", "/api/members");
    }

    // Session Attribute Argument Resolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(sessionAttributeArgumentResolver);
    }
}
