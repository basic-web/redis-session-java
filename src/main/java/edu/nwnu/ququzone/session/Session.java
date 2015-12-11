package edu.nwnu.ququzone.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * session model.
 *
 * @author Yang XuePing
 */
public class Session implements Map<String, String> {
    private static final Logger LOG = LoggerFactory.getLogger(Session.class);

    private String sessionId;

    private boolean saved = true;

    private Map<String, String> data;

    private SessionRepository repository;

    Session(SessionRepository repository, HttpServletRequest request,
            HttpServletResponse response, String cookieName) {
        this.repository = repository;
        data = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    sessionId = c.getValue();
                    break;
                }
            }
        }
        if (sessionId == null || "".equals(cookieName)
                || !lookupSession()) {
            create();
            response.setHeader("SET-COOKIE", cookieName + "=" + sessionId
                    + "; Path=/; HttpOnly");
        }
    }

    private void create() {
        sessionId = UUID.randomUUID().toString();
    }

    private boolean lookupSession() {
        Map<String, String> data = repository.get(sessionId);
        if (data == null || data.isEmpty()) {
            return false;
        }
        this.data.putAll(data);
        return true;
    }

    public void save() {
        if (!saved) {
            if (data.isEmpty()) {
                repository.delete(sessionId);
            } else {
                repository.save(sessionId, data);
            }
            saved = true;
        }
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return data.get(key);
    }

    @Override
    public String put(String key, String value) {
        this.saved = false;
        return data.put(key, value);
    }

    @Override
    public String remove(Object key) {
        this.saved = false;
        return data.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        this.saved = false;
        data.putAll(m);
    }

    @Override
    public void clear() {
        this.saved = false;
        data.clear();
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException("session unsupported keySet method.");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("session unsupported values method.");
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        throw new UnsupportedOperationException("session unsupported entrySet method.");
    }
}
