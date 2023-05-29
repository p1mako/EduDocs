package by.fpmibsu.edudocs.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(asyncSupported = true, urlPatterns = {"/*"})
public class CORSInterceptor implements Filter {

    private static final String[] allowedOrigins = {
            "http://localhost:4200", "http://127.0.0.1:4200"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest request) || !(servletResponse instanceof HttpServletResponse response)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String requestOrigin = request.getHeader("Origin");
        if (isAllowedOrigin(requestOrigin)) {
            response.addHeader("Access-Control-Allow-Origin", requestOrigin);
            response.addHeader("Access-Control-Allow-Headers", "*");
            response.addHeader("Access-Control-Allow-Methods",
                    "GET, OPTIONS, HEAD, PUT, POST, DELETE");

            if (request.getMethod().equals("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                return;
            }
        }
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private boolean isAllowedOrigin(String origin) {
        return true;
//        return Arrays.asList(allowedOrigins).contains(origin);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }
}
