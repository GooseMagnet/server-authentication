package com.karasik.exception;

import com.karasik.model.ErrorDto;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidObjectExceptionMapper implements ExceptionMapper<InvalidObjectException> {

    @Override
    public Response toResponse(InvalidObjectException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(ErrorDto.getException(Response.Status.BAD_REQUEST.getStatusCode(), e.getMessage()))
                .build();
    }
}
