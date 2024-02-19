package reviewme.be.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final DefaultInterceptor defaultInterceptor;
    private final LoginOrNotInterceptor loginOrNotInterceptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        if (request.getMethod().equals("GET")) {
            return loginOrNotInterceptor.preHandle(request, response, handler);
        }

        defaultInterceptor.preHandle(request, response, handler);

        return true;
    }
}
