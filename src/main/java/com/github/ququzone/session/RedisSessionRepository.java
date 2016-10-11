package com.github.ququzone.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis session repository.
 *
 * @author Yang XuePing
 */
public class RedisSessionRepository implements SessionRepository {
    private static Logger LOG = LoggerFactory.getLogger(RedisSessionRepository.class);

    private int timeout = (int) TimeUnit.DAYS.toSeconds(10);

    private String prefix = "sessions:";

    private JedisPool pool;

    public RedisSessionRepository(JedisPool pool) {
        this.pool = pool;
    }

    public void setPrefix(String prefix) {
        if (prefix != null && !"".equals(prefix)) {
            this.prefix = prefix;
        }
    }

    @Override
    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void save(String id, Map<String, String> session) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hmset(prefix + id, session);
            jedis.expire(prefix + id, timeout);
        } catch (Exception e) {
            LOG.error("save session to redis exception.", e);
        }
    }

    @Override
    public Map<String, String> get(String id) {
        try (Jedis jedis = pool.getResource()) {
            Map<String, String> data = jedis.hgetAll(prefix + id);
            jedis.expire(prefix + id, timeout);
            return data;
        } catch (Exception e) {
            LOG.error("get session from redis exception.", e);
            return null;
        }
    }

    @Override
    public void delete(String id) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(prefix + id);
        } catch (Exception e) {
            LOG.error("delete session from redis exception.", e);
        }
    }

    @Override
    public void removeByKV(String key, String value) {
        new Thread(() -> {
            try (Jedis jedis = pool.getResource()) {
                ScanParams scanParams = new ScanParams();
                scanParams.match("sessions:*");
                String cursor = "0";
                do {
                    ScanResult<String> result = jedis.scan(cursor, scanParams);
                    cursor = result.getStringCursor();
                    result.getResult().forEach(sid -> {
                        if (value.equals(jedis.hget(sid, key))) {
                            jedis.del(sid);
                        }
                    });
                } while (!"0".equals(cursor));
            } catch (Exception e) {
                LOG.error("remove session from redis exception.", e);
            }
        }).start();
    }
}
