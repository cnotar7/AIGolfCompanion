package com.cnotar7.projects.aigolfcompanion.exception;

import javax.naming.AuthenticationException;

public class UserAlreadyExistsException extends AuthenticationException {
    public UserAlreadyExistsException(final String message) {
        super(message);
    }
}
