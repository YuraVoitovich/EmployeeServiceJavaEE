package com.example.employeeservicejavaee.servlets;

import com.example.employeeservicejavaee.DAO.DAOProvider;
import com.example.employeeservicejavaee.DAO.iterfaces.EmployeeDAO;
import com.example.employeeservicejavaee.entity.Department;
import com.example.employeeservicejavaee.entity.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SubmitAdding extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EmployeeDAO employeeDAO = DAOProvider.getInstance().getEmployeeDAO();
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String department = req.getParameter("department");
        List<Department> departmentList = (List<Department>) req.getSession().getAttribute("departments");
        Optional<Department> departmentOptional = departmentList.stream().filter(o -> o.getName().equals(department)).findFirst();
        if (departmentOptional.isPresent())
            employeeDAO.addEmployee(new Employee(UUID.randomUUID(), name, surname, departmentOptional.get().getUuid()));
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
