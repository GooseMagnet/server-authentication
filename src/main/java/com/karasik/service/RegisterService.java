package com.karasik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karasik.dao.MongoDao;
import com.karasik.exception.InvalidObjectException;
import com.karasik.model.UserDto;
import com.karasik.util.helpers.HashingHelper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

@Slf4j
public class RegisterService {

    private final MongoDao mongoDao;

    @Inject
    public RegisterService(MongoDao mongoDao) {
        this.mongoDao = mongoDao;
    }

    private void verifyUserDtoNotNull(UserDto userDto) {
        if (Objects.isNull(userDto) ||
                Objects.isNull(userDto.getEmail()) ||
                Objects.isNull(userDto.getPassword()) ||
                Objects.isNull(userDto.getName())) {
            throw new InvalidObjectException("Invalid User Parameters");
        }
    }

    public UserDto createUser(UserDto userDto) {
        verifyUserDtoNotNull(userDto);

        userDto.setEmail(userDto.getEmail().toLowerCase());
        boolean userExistsByEmail = !Objects.isNull(mongoDao.getUserByEmail(userDto.getEmail()));
        if (!userExistsByEmail) {
            if (userDto.getPassword().length() >= 8) {
                byte[] salt = HashingHelper.generateSalt();
                try {
                    String hashedPassword = HashingHelper.generatePBKDF2WithHmacSHA512Hash(userDto.getPassword(), salt);
                    userDto.setPassword(hashedPassword);
                    userDto.setSalt(DatatypeConverter.printHexBinary(salt));
                    String userId = mongoDao.createUser(userDto);
                    userDto.setId(userId);
                    return userDto;
                } catch (InvalidKeySpecException e) {
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
