package edu.nwnu.ququzone.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * session builder.
 *
 * @author Yang XuePing
 */
public class SessionBuilder {
    private String cookieName = "sessionId";

    private String prefix = "sessionId";

    public SessionBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public SessionBuilder setCookieName(String cookieName) {
        this.cookieName = cookieName;
        return this;
    }

    public Session createSession(HttpServletRequest request, HttpServletResponse response) {
        return new Session(request, response, cookieName);
    }
}
