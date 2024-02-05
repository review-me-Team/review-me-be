//package reviewme.be.config.interceptor;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import reviewme.be.user.dto.response.UserProfileResponse;
//import reviewme.be.user.entity.User;
//import reviewme.be.user.exception.ManipulatedTokenException;
//import reviewme.be.user.exception.NoValidBearerFormatException;
//import reviewme.be.user.service.JWTService;
//import reviewme.be.user.service.UserService;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Component
//@RequiredArgsConstructor
//public class LoginOrNotInterceptor implements HandlerInterceptor {
//
//    private final JWTService jwtService;
//    private final UserService userService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             Object handler) {
//
//        if (request.getMethod().equals("OPTIONS")) {
//            return true;
//        }
//
//        if (request.getMethod().equals("GET")) {
//            return true;
//        }
//
//        String header = request.getHeader("Authorization");
//
//        if (header == null) {
//            User user = User.builder().build();
//            request.setAttribute("user", user);
//            return true;
//        }
//
//        Pattern pattern = Pattern.compile("^Bearer\\s(.+)$");
//        Matcher matcher;
//        try {
//            matcher = pattern.matcher(header);
//        } catch (NullPointerException e) {
//            throw new NoValidBearerFormatException("Authorization header가 존재하지 않습니다.");
//        }
//
//        String token = matcher.group(1);
//
//        if (jwtService.validateJwtIsExpired(token)) {
//            throw new NoValidBearerFormatException("유효 기간이 만료된 토큰입니다.");
//        }
//
//        if (jwtService.validateJwtIsManipulated(token)) {
//            throw new ManipulatedTokenException("조작된 토큰입니다.");
//        }
//
//        UserProfileResponse loggedInUser = jwtService.extractUserFromJwt(token, UserProfileResponse.class);
//        User user = userService.getUserById(loggedInUser.getId());
//        request.setAttribute("user", user);
//
//        return true;
//    }
//}
