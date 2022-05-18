package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse sr = (HttpServletResponse) servletResponse;
        sr.addHeader("Access-Control-Allow-Origin","*");
        sr.addHeader("Access-Control-Allow-Methods","DELETE,PUT");
        sr.addHeader("Access-Control-Allow-Headers","Content-Type");
        filterChain.doFilter(servletRequest, servletResponse);

    }


    @Override
    public void destroy() {

    }
}
