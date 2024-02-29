package reviewme.be.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reviewme.be.config.interceptor.AuthInterceptor;
import reviewme.be.config.interceptor.LogInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(logInterceptor)
                .order(1)
                .addPathPatterns("/**");

        registry.addInterceptor(authInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/oauth",
                        "/swagger-ui/**",
                        "/api-docs/**",
                        "/user/refresh");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        // 페이징 요청 시 최대 페이지 사이즈 설정
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setMaxPageSize(30);
        resolvers.add(pageableResolver);
    }
}
