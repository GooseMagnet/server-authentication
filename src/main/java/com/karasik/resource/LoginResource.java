package com.karasik.resource;

import com.karasik.exception.InvalidCredentialsException;
import com.karasik.exception.InvalidObjectException;
import com.karasik.exception.UnauthorizedAccessException;
import com.karasik.model.UserDto;
import com.karasik.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("/login")
public class LoginResource {

    @Context
    HttpServletRequest httpServletRequest;

    private final LoginService loginService;

    @Inject
    public LoginResource(LoginService loginService) {
        this.loginService = loginService;
    }

    @POST
    public Response createSessionOnGet(UserDto userDto) {
        NewCookie newCookie = loginService.createSession(httpServletRequest.getSession().getId(), userDto);
        if (!Objects.isNull(newCookie)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Login successful\"}")
                    .cookie(newCookie)
                    .build();
        } else {
            throw new InvalidCredentialsException("Invalid Username or Password");
        }
    }
}
