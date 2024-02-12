package reviewme.be.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
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
                        "/login/oauth",
                        "/user/refresh");
////
//        registry.addInterceptor(loginOrNotInterceptor)
//                .order(3)
//                .addPathPatterns("/resume");
    }
}
