package com.sparta.secureschedulerappserver.exception;

import javax.security.sasl.AuthenticationException;

public class PasswordMismatchException extends AuthenticationException {
    public PasswordMismatchException() {
        super("Password Mismatch");
    }

}
