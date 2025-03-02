package com.karasik.filters;

import com.karasik.dao.RedisDao;
import com.karasik.exception.UnauthorizedAccessException;
import com.karasik.model.SessionDto;
import com.karasik.util.annotations.AuthorizedResource;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.Objects;

@Provider
@AuthorizedResource
public class AuthorizationFilter implements ContainerRequestFilter, ContainerResponseFilter {

    RedisDao redisDao;

    @Inject
    protected AuthorizationFilter(RedisDao redisDao) {
        this.redisDao = redisDao;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        if (containerRequestContext.getCookies().containsKey("JSESSIONID")) {  // Contains a JSESSION Cookie
            SessionDto sessionDto = redisDao.getSession(String.valueOf(containerRequestContext.getCookies().get("JSESSIONID").getValue()));
            if (!Objects.isNull(sessionDto)) {  // THE JSESSION Cookie exists in Redis
                redisDao.updateSession(sessionDto.getSessionId());
                return;
            }
        }
        throw new UnauthorizedAccessException("You must be logged in to access this resource");
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {

    }
}
