package com.example.employeeservicejavaee.DAO.exceptions;

import java.sql.SQLException;

public class DepartmentDAOException extends RuntimeException {
    public DepartmentDAOException() {
        super();
    }

    public DepartmentDAOException(String message) {
        super(message);
    }

    public DepartmentDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
