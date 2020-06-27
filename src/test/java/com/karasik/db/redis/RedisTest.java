//package com.karasik.db.redis;
//
//import com.karasik.dao.RedisDao;
//import com.karasik.model.SessionDto;
//import org.junit.jupiter.api.*;
//
//import javax.inject.Inject;
//import java.time.LocalDateTime;
//
//public class RedisTest {
//
//    @Inject
//    private static RedisDao redisDao;
//
//    @Inject
//    private static SessionDto sessionDto;
//
//    private static String timestamp;
//
//    @BeforeEach
//    public void testSetup() {
//        timestamp = LocalDateTime.now().toString();
//        sessionDto.setUserId("UserId-" + timestamp);
//        sessionDto.setSessionId("SessionId-" + timestamp);
//    }
//
//    @AfterEach
//    public void testTeardown() {
//        redisDao.deleteSession(sessionDto.getSessionId());
//    }
//
//    @Test
//    public void writeSession() {
//        redisDao.createSession(sessionDto);
//        SessionDto newSession = redisDao.getSession(sessionDto.getSessionId());
//        Assertions.assertEquals(sessionDto, newSession);
//    }
//
//    @Test
//    public void writeSessionWithExpiry() throws InterruptedException {
//        redisDao.createSession(sessionDto, 3);
//        SessionDto newSession = redisDao.getSession(sessionDto.getSessionId());
//        Assertions.assertEquals(sessionDto, newSession);
//
//        Thread.sleep(5000);
//        newSession = redisDao.getSession(sessionDto.getSessionId());
//        Assertions.assertNull(newSession);
//    }
//
//    @Test
//    public void deleteSession() {
//        redisDao.createSession(sessionDto);
//        SessionDto newSession = redisDao.getSession(sessionDto.getSessionId());
//        Assertions.assertEquals(sessionDto, newSession);
//
//        redisDao.deleteSession(sessionDto.getSessionId());
//        newSession = redisDao.getSession(sessionDto.getSessionId());
//        Assertions.assertNull(newSession);
//    }
//
//    @Test
//    public void updateSession() {
//        redisDao.createSession(sessionDto, 10);
//        long originalTTL = redisDao.getSessionTTL(sessionDto.getSessionId());
//        Assertions.assertTrue(originalTTL > 0 && originalTTL <= 10);
//
//        redisDao.updateSession(sessionDto.getSessionId());
//        long updatedSessionTTL = redisDao.getSessionTTL(sessionDto.getSessionId());
//        Assertions.assertTrue(originalTTL <= updatedSessionTTL);
//    }
//}
