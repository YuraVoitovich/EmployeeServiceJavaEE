package com.example.employeeservicejavaee.DAO.impl;

import com.example.employeeservicejavaee.DAO.ConnectionPool;
import com.example.employeeservicejavaee.DAO.exceptions.DepartmentDAOException;
import com.example.employeeservicejavaee.DAO.exceptions.EmployeeDAOException;
import com.example.employeeservicejavaee.DAO.iterfaces.DepartmentDAO;
import com.example.employeeservicejavaee.entity.Department;
import com.example.employeeservicejavaee.entity.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DepartmentDAOImpl implements DepartmentDAO {
    private final Logger logger = LogManager.getLogger();
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private void close(Statement statement) {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            logger.error("Error while closing statement");
            throw new EmployeeDAOException("Error while closing statement", e);
        }
    }

    private void close(PreparedStatement statement) {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            logger.error("Error while closing prepared statement");
            throw new EmployeeDAOException("Error while closing prepared statement", e);
        }
    }

    private void close(ResultSet set) {
        try {
            if (set != null) set.close();
        } catch (SQLException e) {
            logger.error("Error while closing result set");
            throw new EmployeeDAOException("Error while result set", e);
        }
    }

    @Override
    public void addDepartment(Department department) {
        Connection connection =  connectionPool.takeConnection();
        PreparedStatement statement = null;
        try {
            statement = connection
                    .prepareStatement("INSERT INTO department(id,name) " +
                            "values(?, ?);");
            statement.setObject(1, department.getUuid());
            statement.setString(2, department.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while adding department with id " + department.getUuid());
            throw new DepartmentDAOException(String.format("Error while adding department with id = %s", department.getUuid()), e);
        } finally {
            close(statement);
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public void updateDepartment(Department changedDepartment) {
        Department department = findDepartmentById(changedDepartment.getUuid());
        Connection connection = connectionPool.takeConnection();
        PreparedStatement statement = null;
        if (changedDepartment.getName() == null) {
            changedDepartment.setName(department.getName());
        }
        try {
            statement = connection
                    .prepareStatement("UPDATE department " +
                            "SET name = ?" +
                            "WHERE id = ?;");
            statement.setString(1, changedDepartment.getName());
            statement.setObject(2, changedDepartment.getUuid());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while updating department with id = " + changedDepartment.getUuid());
            throw new EmployeeDAOException(String.format("Error while updating department with id = %s", changedDepartment.getUuid()), e);
        } finally {
            close(statement);
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public Department findDepartmentById(UUID uuid) {
        Connection connection = connectionPool.takeConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Department department = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM department WHERE id = ?;");
            statement.setObject(1, uuid);
            resultSet = statement
                    .executeQuery();
            if (resultSet.next()) {
                department = new Department();
                department.setUuid((UUID) resultSet.getObject(1));
                department.setName(resultSet.getString(2));
            } else {
                logger.error("Department with uuid = " + uuid + " is not present");
                throw new EmployeeDAOException("Department with uuid = " + uuid + " is not present");
            }
        } catch (SQLException e) {
            logger.error("Error while reading department with id = " + uuid);
            throw new EmployeeDAOException(String.format("Error while reading department with id = %s", uuid), e);
        } finally {
            close(resultSet);
            close(statement);
            connectionPool.returnConnection(connection);
        }
        return department;
    }

    @Override
    public List<Department> getAllDepartments() {
        Connection connection = connectionPool.takeConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Department> employees = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM department;");
            resultSet = statement
                    .executeQuery();
            while (resultSet.next()) {
                Department department = new Department();
                department.setUuid((UUID) resultSet.getObject(1));
                department.setName(resultSet.getString(2));
                employees.add(department);
            }
        } catch (SQLException e) {
            logger.error("Error while reading departments");
            throw new EmployeeDAOException("Error while reading departments", e);
        } finally {
            close(resultSet);
            close(statement);
            connectionPool.returnConnection(connection);
        }
        return employees;
    }

    @Override
    public void deleteDepartmentById(UUID uuid) {
        Connection connection = connectionPool.takeConnection();
        PreparedStatement statement = null;
        try {
            statement =  connection.prepareStatement("DELETE FROM department WHERE id = ?;");
            statement.setObject(1, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while deleting department with id = " + uuid);
            throw new EmployeeDAOException(String.format("Error while deleting department with id = %s", uuid), e);
        } finally {
            close(statement);
            connectionPool.returnConnection(connection);
        }
    }
}
