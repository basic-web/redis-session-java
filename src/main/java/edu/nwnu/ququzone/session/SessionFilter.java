package edu.nwnu.ququzone.session;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * self session filter.
 *
 * @author Yang XuePing
 */
public class SessionFilter implements Filter {
    public static final String SESSION_KEY = "session";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        Session session = new SessionBuilder().createSession((HttpServletRequest) request, (HttpServletResponse) response);
        request.setAttribute(SESSION_KEY, session);
        chain.doFilter(request, response);
        session.save();
    }

    @Override
    public void destroy() {
    }
}

