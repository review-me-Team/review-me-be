package reviewme.be.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import reviewme.be.user.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class LoginOrNotInterceptor implements HandlerInterceptor {

    private final DefaultInterceptor defaultInterceptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String header = request.getHeader("Authorization");

        if (header == null) {
            User anonymous = User.builder().id(0L).build();
            request.setAttribute("user", anonymous);
            return true;
        }

        defaultInterceptor.preHandle(request, response, handler);

        return true;
    }
}
