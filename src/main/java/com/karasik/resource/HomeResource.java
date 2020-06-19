package com.karasik.resource;

import com.karasik.util.annotations.AuthorizedResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/home")
@AuthorizedResource
public class HomeResource {

    @GET
    public String test() {
        return "Test";
    }
}
