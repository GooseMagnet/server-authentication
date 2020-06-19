package com.karasik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karasik.dao.MongoDao;
import com.karasik.exception.InvalidObjectException;
import com.karasik.model.UserDto;
import com.karasik.util.helpers.HashingHelper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

@Slf4j
public class RegisterService {

    MongoDao mongoDao = new MongoDao();

    public UserDto createUser(UserDto userDto) {
        // check password length
        userDto.setEmail(userDto.getEmail().toLowerCase());
        boolean userExistsByEmail = !Objects.isNull(mongoDao.getUserByEmail(userDto.getEmail()));
        if (!userExistsByEmail) {
            if (!Objects.isNull(userDto.getPassword()) && userDto.getPassword().length() >= 8) {
                byte[] salt = HashingHelper.generateSalt();
                try {
                    String hashedPassword = HashingHelper.generatePBKDF2WithHmacSHA512Hash(userDto.getPassword(), salt);
                    userDto.setPassword(hashedPassword);
                    userDto.setSalt(DatatypeConverter.printHexBinary(salt));
                    String userId = mongoDao.createUser(userDto);
                    userDto.setId(userId);
                    return userDto;
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    log.error("Unable to create a password hash", e);
                } catch (JsonProcessingException e) {
                    log.error("Unable to write user to database", e);
                }
            } else {
                throw new InvalidObjectException("Password length must be 8 or more characters");
            }
        } else {
            throw new InvalidObjectException("User already exists with this email");
        }
        throw new InvalidObjectException("Unable to create user");
    }
}
