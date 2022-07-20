package com.example.employeeservicejavaee.DAO.exceptions;

public class EmployeeDAOException extends RuntimeException {
    public EmployeeDAOException() {
        super();
    }

    public EmployeeDAOException(String message) {
        super(message);
    }

    public EmployeeDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
