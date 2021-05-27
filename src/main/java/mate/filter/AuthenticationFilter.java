package mate.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {
    private static final String driverId = "driverId";
    private Set<String> allowedUrls = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        allowedUrls.add("/drivers/login");
        allowedUrls.add("/drivers/add");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Long driverIdLong = (Long) session.getAttribute(driverId);
        String servletPath = req.getServletPath();
        if (allowedUrls.contains(servletPath)) {
            chain.doFilter(req, resp);
            return;
        }
        if (driverIdLong == null) {
            resp.sendRedirect("/drivers/login");
            return;
        }
        chain.doFilter(req, resp);
    }
}
