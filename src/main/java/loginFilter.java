import entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "loginFilter")
public class loginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        String loginURL = request.getContextPath() + "/login.xhtml";
        String registerURL = request.getContextPath() + "/register.xhtml";
        boolean loggedIn = (session != null) && (session.getAttribute("user_m") != null);
        if (!loggedIn && !request.getRequestURI().equals(loginURL) && !request.getRequestURI().equals(registerURL)) {
            response.sendRedirect(loginURL);
        } else {
            if (loggedIn) {
                User user = (User) session.getAttribute("user_m");
                if (user.getU_status() == -1) {
                    response.sendRedirect(loginURL);
                }
            }
            chain.doFilter(req, resp);

        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
