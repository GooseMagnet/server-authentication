package com.karasik.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karasik.model.SessionDto;
import com.karasik.util.connectors.RedisConnector;
import com.karasik.util.properies.PropertyReader;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import javax.inject.Inject;

@Slf4j
public class RedisDao {

    private final ObjectMapper objectMapper;

    public static final int defaultExpiryInSeconds = Integer.parseInt(PropertyReader.getProperty("redis.expiryInSeconds"));

    @Inject
    public RedisDao(ObjectMapper objectMapper) {
        log.info("Initialized RedisDao");
        this.objectMapper = objectMapper;
    }

    public SessionDto getSession(String sessionId) {
        try (Jedis jedis = RedisConnector.getConnection()) {
            String sessionDtoString = jedis.get(sessionId);
            return objectMapper.readValue(sessionDtoString, SessionDto.class);
        }  catch (JsonProcessingException | IllegalArgumentException e) {
            log.error("Unable to read Session Object from Redis", e);
            return null;
        }
    }

    public void createSession(SessionDto sessionDto) {
        this.createSession(sessionDto, defaultExpiryInSeconds);
    }

    public void createSession(SessionDto sessionDto, int expiryInSeconds) {
        try (Jedis jedis = RedisConnector.getConnection()) {
            jedis.setex(sessionDto.getSessionId(), expiryInSeconds, objectMapper.writeValueAsString(sessionDto));
        } catch (JsonProcessingException e) {
            log.error("Unable to write Session Object to Redis", e);
        }
    }

    public void deleteSession(String sessionId) {
        try (Jedis jedis = RedisConnector.getConnection()) {
            jedis.del(sessionId);
        }
    }

    public long getSessionTTL(String sessionId) {
        try (Jedis jedis = RedisConnector.getConnection()) {
            return jedis.ttl(sessionId);
        }
    }

    public void updateSession(String sessionId) {
        try (Jedis jedis = RedisConnector.getConnection()) {
            jedis.expire(sessionId, defaultExpiryInSeconds);
        }
    }
}
