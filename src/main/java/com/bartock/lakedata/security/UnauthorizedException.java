package com.bartock.lakedata.security;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = -6413431045116012803L;

    public UnauthorizedException() {

    }

    public UnauthorizedException(String message) {
        super(message);
    }

}
