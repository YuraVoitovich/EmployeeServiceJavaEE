package com.example.employeeservicejavaee.servlets;

import com.example.employeeservicejavaee.DAO.impl.DepartmentDAOImpl;
import com.example.employeeservicejavaee.DAO.iterfaces.DepartmentDAO;
import com.example.employeeservicejavaee.DAO.iterfaces.EmployeeDAO;
import com.example.employeeservicejavaee.entity.Employee;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class AddEmployeeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DepartmentDAO departmentDAO = new DepartmentDAOImpl();
        req.getSession().setAttribute("departments", departmentDAO.getAllDepartments());
        req.getRequestDispatcher("/add-employee.jsp").forward(req, resp);
    }

}
