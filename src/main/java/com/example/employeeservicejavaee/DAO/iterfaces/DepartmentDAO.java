package com.example.employeeservicejavaee.DAO.iterfaces;

import com.example.employeeservicejavaee.entity.Department;

import java.util.List;
import java.util.UUID;

public interface DepartmentDAO {
    void addDepartment(Department department);
    void updateDepartment(Department department);
    void deleteDepartmentById(UUID uuid);
    Department findDepartmentById(UUID uuid);
    List<Department> getAllDepartments();
}
