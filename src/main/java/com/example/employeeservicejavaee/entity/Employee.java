package com.example.employeeservicejavaee.entity;

import java.util.Objects;
import java.util.UUID;

public class Employee {
    private UUID uuid;
    private String name;
    private String surname;
    private UUID departmentUUId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return uuid.equals(employee.uuid) && name.equals(employee.name) && surname.equals(employee.surname) && departmentUUId.equals(employee.departmentUUId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, surname, departmentUUId);
    }

    public UUID getDepartmentUUId() {
        return departmentUUId;
    }

    public void setDepartmentUUId(UUID departmentUUId) {
        this.departmentUUId = departmentUUId;
    }

    public Employee() {

    }

    public Employee(UUID uuid, String name, String surname, UUID departmentUUId) {
        this.uuid = uuid;
        this.name = name;
        this.surname = surname;
        this.departmentUUId = departmentUUId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
