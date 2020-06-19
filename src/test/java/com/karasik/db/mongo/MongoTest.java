package com.karasik.db.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karasik.dao.MongoDao;
import com.karasik.model.UserDto;
import com.karasik.util.helpers.HashingHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.util.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.time.LocalDateTime;

@Slf4j
public class MongoTest {

    private static UserDto userDto;
    private static String timestamp;
    private static MongoDao mongoDao;

    @BeforeAll
    public static void classSetup() {
        mongoDao = new MongoDao();
    }

    @BeforeEach
    public void testSetup() {
        timestamp = LocalDateTime.now().toString();
        userDto = new UserDto();
        userDto.setName("Name-" + timestamp);
        userDto.setPassword("Password-" + timestamp);
        userDto.setEmail("Email-" + timestamp);
        userDto.setSalt(DatatypeConverter.printHexBinary(HashingHelper.generateSalt()));
    }

    @Test
    public void writeUserToMongo() throws JsonProcessingException {
        String userId = mongoDao.createUser(userDto);
        log.info("UserId: {}", userId);
        Assertions.assertNotNull(userId);
        Assertions.assertFalse(StringUtils.isBlank(userId));
    }

    @Test
    public void readUserFromMongo() throws JsonProcessingException {
        String userId = mongoDao.createUser(userDto);

        UserDto createdUser = mongoDao.getUserByEmail(userDto.getEmail());
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getEmail(), createdUser.getEmail());
        Assertions.assertEquals(userDto.getName(), createdUser.getName());
        Assertions.assertEquals(userDto.getSalt(), createdUser.getSalt());
        Assertions.assertEquals(userId, createdUser.getId());
    }
}
