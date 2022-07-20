package com.example.employeeservicejavaee.DAO.impl;

import com.example.employeeservicejavaee.DAO.ConnectionPool;
import com.example.employeeservicejavaee.DAO.exceptions.EmployeeDAOException;
import com.example.employeeservicejavaee.DAO.iterfaces.EmployeeDAO;
import com.example.employeeservicejavaee.entity.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class EmployeeDAOImpl implements EmployeeDAO {
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
    public void addEmployee(Employee employee) {
        Connection connection =  connectionPool.takeConnection();
        PreparedStatement statement = null;
        try {
            statement = connection
                    .prepareStatement("INSERT INTO employee(id,name,surname,department) " +
                            "values(?, ?, ?, ?::uuid);");
            statement.setObject(1, employee.getUuid());
            statement.setString(2, employee.getName());
            statement.setString(3, employee.getSurname());
            statement.setObject(4, employee.getDepartmentUUId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while adding employee with id = %s");
            throw new EmployeeDAOException(String.format("Error while adding employee with id = %s", employee.getUuid()), e);
        } finally {
            close(statement);
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public void updateEmployee(Employee changedEmployee) {
        Employee employee = getEmployeeById(changedEmployee.getUuid());
        Connection connection = connectionPool.takeConnection();
        PreparedStatement statement = null;
        if (changedEmployee.getName() == null) {
            changedEmployee.setName(employee.getName());
        }
        if (changedEmployee.getSurname() == null) {
            changedEmployee.setSurname(employee.getSurname());
        }
        if (changedEmployee.getDepartmentUUId() == null) {
            changedEmployee.setDepartmentUUId(employee.getDepartmentUUId());
        }
        try {
            statement = connection
                    .prepareStatement("UPDATE employee " +
                            "SET name = ?, surname = ?, department = ? " +
                            "WHERE id = ?;");
            statement.setString(1, changedEmployee.getName());
            statement.setString(2, changedEmployee.getSurname());
            statement.setObject(3, changedEmployee.getDepartmentUUId());
            statement.setObject(4, changedEmployee.getUuid());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while updating employee with id = %s");
            throw new EmployeeDAOException(String.format("Error while updating employee with id = %s", employee.getUuid()), e);
        } finally {
            close(statement);
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public Employee getEmployeeById(UUID uuid) {
        Connection connection = connectionPool.takeConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Employee employee = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM employee WHERE id = ?;");
            statement.setObject(1, uuid);
            resultSet = statement
                    .executeQuery();
            if (resultSet.next()) {
                employee = new Employee();
                employee.setUuid((UUID) resultSet.getObject(1));
                employee.setName(resultSet.getString(2));
                employee.setSurname(resultSet.getString(3));
                employee.setDepartmentUUId((UUID) resultSet.getObject(4));
            } else {
                logger.error("Employee with uuid = " + uuid + " is not present");
                throw new EmployeeDAOException("Employee with uuid = " + uuid + " is not present");
            }
        } catch (SQLException e) {
            logger.error("Error while reading employee with id = %s");
            throw new EmployeeDAOException(String.format("Error while reading employee with id = %s", uuid), e);
        } finally {
            close(resultSet);
            close(statement);
            connectionPool.returnConnection(connection);
        }
        return employee;
    }

    @Override
    public List<Employee> findEmployeesByDepartmentId(UUID uuid) {
        Connection connection = connectionPool.takeConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Employee> employees = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM employee WHERE department = ?;");
            statement.setObject(1, uuid);
            resultSet = statement
                    .executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setUuid((UUID) resultSet.getObject(1));
                employee.setName(resultSet.getString(2));
                employee.setSurname(resultSet.getString(3));
                employee.setDepartmentUUId((UUID) resultSet.getObject(4));
                employees.add(employee);
            }
        } catch (SQLException e) {
            logger.error("Error while reading employees with department id = %s");
            throw new EmployeeDAOException(String.format("Error while reading employees with department id = %s", uuid), e);
        } finally {
            close(resultSet);
            close(statement);
            connectionPool.returnConnection(connection);
        }
        return employees;
    }

    @Override
    public void deleteEmployeeById(UUID uuid) {
        Connection connection = connectionPool.takeConnection();
        PreparedStatement statement = null;
        try {
            statement =  connection.prepareStatement("DELETE FROM employee WHERE id = ?;");
            statement.setObject(1, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while deleting employee with id = %s");
            throw new EmployeeDAOException(String.format("Error while deleting employee with id = %s", uuid), e);
        } finally {
            close(statement);
            connectionPool.returnConnection(connection);
        }
    }
}
