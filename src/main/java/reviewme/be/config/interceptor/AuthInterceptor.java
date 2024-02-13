package reviewme.be.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import reviewme.be.user.dto.response.UserProfileResponse;
import reviewme.be.user.entity.User;
import reviewme.be.user.exception.ManipulatedTokenException;
import reviewme.be.user.exception.NoValidBearerFormatException;
import reviewme.be.user.service.JWTService;
import reviewme.be.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JWTService jwtService;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        if (request.getMethod().equals("OPTIONS") || request.getMethod().equals("GET")) {
            return true;
        }

        String header = request.getHeader("Authorization");

        if (header == null) {
            throw new NoValidBearerFormatException("Authorization header가 존재하지 않습니다.");
        }

        if (request.getHeader("Authorization").split(" ").length != 2) {
            throw new NoValidBearerFormatException("Bearer 토큰이 존재하지 않습니다.");
        }

        String jwt = request.getHeader("Authorization").split(" ")[1];

        if (jwtService.validateJwtIsManipulated(jwt)) {
            throw new ManipulatedTokenException("조작된 토큰입니다.");
        }

        if (jwtService.validateJwtIsExpired(jwt)) {
            throw new NoValidBearerFormatException("유효 기간이 만료된 토큰입니다.");
        }

        UserProfileResponse loggedInUser = jwtService.extractUserFromJwt(jwt, UserProfileResponse.class);
        User user = userService.getUserById(loggedInUser.getId());
        request.setAttribute("user", user);

        return true;
    }
}
