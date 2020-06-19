package com.karasik.exception;

import com.karasik.model.ErrorDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthorizedAccessExceptionMapper implements ExceptionMapper<UnauthorizedAccessException> {

    @Override
    public Response toResponse(UnauthorizedAccessException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity(ErrorDto.getException(Response.Status.UNAUTHORIZED.getStatusCode(), e.getMessage()))
                .build();
    }
}
