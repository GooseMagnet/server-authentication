package com.karasik.resource;

import com.karasik.dao.RedisDao;
import com.karasik.exception.AlreadyLoggedInException;
import com.karasik.exception.InvalidObjectException;
import com.karasik.exception.UnauthorizedAccessException;
import com.karasik.model.SessionDto;
import com.karasik.model.UserDto;
import com.karasik.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
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

    LoginService loginService = new LoginService();

    @POST
    public Response createSessionOnGet(UserDto userDto) {
        if (Objects.isNull(userDto) || Objects.isNull(userDto.getName()) || Objects.isNull(userDto.getPassword())) {
            throw new InvalidObjectException("Missing email or password");
        }
        NewCookie newCookie = loginService.createSession(httpServletRequest.getSession().getId(), userDto);
        if (!Objects.isNull(newCookie)) {
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Login successful\"}")
                    .cookie(newCookie)
                    .build();
        } else {
            throw new UnauthorizedAccessException("Incorrect email or password");
        }
    }
}
