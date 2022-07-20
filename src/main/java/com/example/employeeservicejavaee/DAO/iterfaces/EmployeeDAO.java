package com.example.employeeservicejavaee.DAO.iterfaces;

import com.example.employeeservicejavaee.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeDAO {
    void addEmployee(Employee employee);
    void updateEmployee(Employee employee);
    Employee getEmployeeById(UUID uuid);
    List<Employee> findEmployeesByDepartmentId(UUID uuid);
    void deleteEmployeeById(UUID uuid);
}
