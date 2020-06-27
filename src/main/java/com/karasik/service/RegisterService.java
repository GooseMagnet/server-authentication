package com.karasik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
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
                Strings.isNullOrEmpty(userDto.getEmail()) ||
                Strings.isNullOrEmpty(userDto.getPassword()) ||
                Strings.isNullOrEmpty(userDto.getName())) {
            throw new InvalidObjectException("Invalid User Parameters");
        }
    }

    public UserDto createUser(UserDto userDto) {
        verifyUserDtoNotNull(userDto);

        UserDto newUser = null;
        try {
            newUser = (UserDto) userDto.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        newUser.setEmail(newUser.getEmail().toLowerCase());
        boolean userExistsByEmail = !Objects.isNull(mongoDao.getUserByEmail(newUser.getEmail()));
        if (!userExistsByEmail) {
            if (newUser.getPassword().length() >= 8 && newUser.getPassword().length() <= Byte.MAX_VALUE) {
                byte[] salt = HashingHelper.generateSalt();
                try {
                    String hashedPassword = HashingHelper.generatePBKDF2WithHmacSHA512Hash(newUser.getPassword(), salt);
                    newUser.setPassword(hashedPassword);
                    newUser.setSalt(DatatypeConverter.printHexBinary(salt));
                    String userId = mongoDao.createUser(newUser);
                    newUser.setId(userId);
                    return newUser;
                } catch (InvalidKeySpecException e) {
                    log.error("Unable to create a password hash", e);
                } catch (JsonProcessingException e) {
                    log.error("Unable to write user to database", e);
                }
            } else {
                throw new InvalidObjectException(String.format("Password length must be between 8 and %s characters", Byte.MAX_VALUE));
            }
        } else {
            throw new InvalidObjectException("User already exists with this email");
        }
        throw new InvalidObjectException("Unable to create user");
    }
}
