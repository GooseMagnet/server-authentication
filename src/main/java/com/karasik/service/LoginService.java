package com.karasik.service;

import com.karasik.dao.MongoDao;
import com.karasik.dao.RedisDao;
import com.karasik.exception.AlreadyLoggedInException;
import com.karasik.exception.UnauthorizedAccessException;
import com.karasik.model.SessionDto;
import com.karasik.model.UserDto;
import com.karasik.util.helpers.HashingHelper;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class LoginService {

    private RedisDao redisDao = new RedisDao();
    private MongoDao mongoDao = new MongoDao();

    public NewCookie createSession(String jSessionId, UserDto userCredentials) {
        // Check if user is already logged in
        if (!Objects.isNull(redisDao.getSession(jSessionId))) {
            throw new AlreadyLoggedInException("Already Logged In");
        }
        // QUERY MONGO FOR USER ID
        UserDto userInDb = mongoDao.getUserByEmail(userCredentials.getEmail());
        byte[] saltInDb = DatatypeConverter.parseHexBinary(userCredentials.getSalt());
        try {
            String password = HashingHelper.generatePBKDF2WithHmacSHA512Hash(userInDb.getPassword(), saltInDb);
            if (password.equals(userCredentials.getPassword())) {
                SessionDto sessionDto = new SessionDto(jSessionId, userInDb.getId());
                redisDao.createSession(sessionDto);
                if (!Objects.isNull(redisDao.getSession(jSessionId))) {
                    return createCookie(jSessionId);
                }
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new UnauthorizedAccessException("Incorrect email or password");
        }
        return null;
    }

    private NewCookie createCookie(String jSessionId) {
        Cookie cookie = new Cookie("JSESSIONID", jSessionId);
        NewCookie newCookie = new NewCookie(cookie, "", Integer.MAX_VALUE, new Date(Long.MAX_VALUE), false, true);
        return newCookie;
    }
}
