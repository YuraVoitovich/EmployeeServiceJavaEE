package com.example.employeeservicejavaee.servlets;

import com.example.employeeservicejavaee.DAO.DAOProvider;
import com.example.employeeservicejavaee.DAO.impl.DepartmentDAOImpl;
import com.example.employeeservicejavaee.DAO.iterfaces.DepartmentDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddEmployeeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DepartmentDAO departmentDAO = DAOProvider.getInstance().getDepartmentDAO();
        req.getSession().setAttribute("departments", departmentDAO.getAllDepartments());
        req.getRequestDispatcher("/add-employee.jsp").forward(req, resp);

    }

}
