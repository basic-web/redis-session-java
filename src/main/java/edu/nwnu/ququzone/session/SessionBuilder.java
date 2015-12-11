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

    private SessionRepository repository = new DefaultSessionRepositoryFactory().getSessionRepository();


    public SessionBuilder setCookieName(String cookieName) {
        this.cookieName = cookieName;
        return this;
    }

    public SessionBuilder setSessionRepository(SessionRepository repository) {
        if (repository != null) {
            this.repository = repository;
        }
        return this;
    }

    public Session createSession(HttpServletRequest request, HttpServletResponse response) {
        return new Session(repository, request, response, cookieName);
    }
}
