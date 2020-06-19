package com.karasik.util.annotations;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@NameBinding
@Target({METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizedResource {
}