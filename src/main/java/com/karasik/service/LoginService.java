package com.karasik.service;

import com.karasik.dao.MongoDao;
import com.karasik.dao.RedisDao;
import com.karasik.exception.*;
import com.karasik.model.SessionDto;
import com.karasik.model.UserDto;
import com.karasik.util.helpers.HashingHelper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.xml.bind.DatatypeConverter;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class LoginService {

    private final RedisDao redisDao;
    private final MongoDao mongoDao;

    @Inject
    public LoginService(RedisDao redisDao, MongoDao mongoDao) {
        this.redisDao = redisDao;
        this.mongoDao = mongoDao;
    }

    public NewCookie createSession(String jSessionId, UserDto userCredentials) {
        verifyUserProvidedCredentials(userCredentials);
        checkIfUserAlreadyLoggedIn(jSessionId);

        String userId = verifyCredentialsAgainstDatabase(userCredentials);
        if (!Objects.isNull(userId)) {
            SessionDto sessionDto = new SessionDto(jSessionId, userId);
            redisDao.createSession(sessionDto);
            if (!Objects.isNull(redisDao.getSession(jSessionId))) {
                return createCookie(jSessionId);
            }
        }
        throw new InvalidCredentialsException("Invalid Username or Password");
    }

    private NewCookie createCookie(String jSessionId) {
        Cookie cookie = new Cookie("JSESSIONID", jSessionId);
        NewCookie newCookie = new NewCookie(cookie, "", Integer.MAX_VALUE, new Date(Long.MAX_VALUE), false, true);
        return newCookie;
    }

    private void verifyUserProvidedCredentials(UserDto userCredentials) {
        if (Objects.isNull(userCredentials) || Objects.isNull(userCredentials.getEmail()) || Objects.isNull(userCredentials.getPassword())) {
            throw new InvalidCredentialsException("Invalid Username or Password");
        }
    }

    private void checkIfUserAlreadyLoggedIn(String jSessionId) {
        // Check if user is already logged in
        if (!Objects.isNull(redisDao.getSession(jSessionId))) {
            throw new AlreadyLoggedInException("Already Logged In");
        }
    }

    private String verifyCredentialsAgainstDatabase(UserDto userCredentials) {
        UserDto userInDb = mongoDao.getUserByEmail(userCredentials.getEmail());
        if (Objects.isNull(userInDb)) {
            throw new InvalidCredentialsException("Invalid Username or Password");
        }

        byte[] saltInDb = DatatypeConverter.parseHexBinary(userInDb.getSalt());
        String encryptedUserProvidedPassword = null;
        try {
            encryptedUserProvidedPassword = HashingHelper.generatePBKDF2WithHmacSHA512Hash(userCredentials.getPassword(), saltInDb);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        if (userInDb.getPassword().equals(encryptedUserProvidedPassword)) {
            return userInDb.getId();
        } else {
            return null;
        }
    }
}
