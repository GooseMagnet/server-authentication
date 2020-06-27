package com.karasik.resource;

import com.karasik.model.UserDto;
import com.karasik.service.RegisterService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/register")
public class RegisterResource {

    private final RegisterService registerService;

    @Inject
    public RegisterResource(RegisterService registerService) {
        this.registerService = registerService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTest(UserDto userDto) {
        UserDto createdUser = registerService.createUser(userDto);
        return Response.status(Response.Status.CREATED)
                .entity(createdUser)
                .build();
    }
}
