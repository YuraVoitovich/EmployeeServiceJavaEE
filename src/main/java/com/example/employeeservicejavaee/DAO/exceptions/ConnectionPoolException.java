package com.example.employeeservicejavaee.DAO.exceptions;

import java.io.IOException;

public class ConnectionPoolException extends RuntimeException {
    public ConnectionPoolException() {
        super();
    }

    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
