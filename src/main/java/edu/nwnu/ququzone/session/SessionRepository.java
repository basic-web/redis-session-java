package edu.nwnu.ququzone.session;

import java.util.Map;

/**
 * session repository.
 *
 * @author Yang XuePing
 */
public interface SessionRepository {
    void save(String id, Map<String, String> session);

    Map<String, String> get(String id);

    void delete(String id);
}
