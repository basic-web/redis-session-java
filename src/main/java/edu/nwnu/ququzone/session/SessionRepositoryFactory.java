package edu.nwnu.ququzone.session;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * session repository factory.
 *
 * @author Yang XuePing
 */
public class SessionRepositoryFactory {
    private static SessionRepository repository;

    public static SessionRepository getSessionRepository() {
        synchronized (SessionRepositoryFactory.class) {
            if (repository != null) {
                return repository;
            }
            repository = new RedisSessionRepository(new JedisPool(new JedisPoolConfig(), "redis.host.name"));
            return repository;
        }
    }
}
