package com.karasik.resource;

import com.karasik.exception.InvalidObjectException;
import com.karasik.model.UserDto;
import com.karasik.service.RegisterService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/users")
public class RegisterResource {
    RegisterService registerService = new RegisterService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTest(UserDto userDto) {
        if (Objects.isNull(userDto) ||
                Objects.isNull(userDto.getEmail()) ||
                Objects.isNull(userDto.getPassword()) ||
                Objects.isNull(userDto.getName())) {
            throw new InvalidObjectException("Invalid User Parameters");
        }
        UserDto createdUser = registerService.createUser(Objects.requireNonNull(userDto));
        return Response.status(Response.Status.CREATED)
                .entity(createdUser)
                .build();
    }
}
