package reviewme.be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class DevCorsConfig implements Filter {

    @Value("${SERVER_URL}")
    private String allowedOrigins;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (request.getHeader("Origin") != null) {
            if (request.getHeader("Origin").contains(allowedOrigins)) {
                response.setHeader("Access-Control-Allow-Origin", allowedOrigins);
            } else {
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
            }
        }
        else {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","GET, POST, PATCH, DELETE, HEAD, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "10");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
