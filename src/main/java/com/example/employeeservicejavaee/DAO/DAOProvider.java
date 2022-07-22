package com.example.employeeservicejavaee.DAO;

import com.example.employeeservicejavaee.DAO.impl.DepartmentDAOImpl;
import com.example.employeeservicejavaee.DAO.impl.EmployeeDAOImpl;
import com.example.employeeservicejavaee.DAO.iterfaces.DepartmentDAO;
import com.example.employeeservicejavaee.DAO.iterfaces.EmployeeDAO;

public class DAOProvider {
    private final EmployeeDAO employeeDAO;
    private final DepartmentDAO departmentDAO;

    private static class InstanceHolder {
        private final static DAOProvider INSTANCE = new DAOProvider();
    }
    private DAOProvider() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.departmentDAO = new DepartmentDAOImpl();
    }

    public static DAOProvider getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public EmployeeDAO getEmployeeDAO() {
        return employeeDAO;
    }

    public DepartmentDAO getDepartmentDAO() {
        return departmentDAO;
    }
}
