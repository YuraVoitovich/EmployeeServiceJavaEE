package com.example.employeeservicejavaee;

import com.example.employeeservicejavaee.DAO.impl.EmployeeDAOImpl;
import com.example.employeeservicejavaee.DAO.iterfaces.EmployeeDAO;
import com.example.employeeservicejavaee.entity.Employee;

import java.io.*;
import java.util.UUID;
import javax.servlet.http.*;

public class HelloServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        EmployeeDAO employeeDAO = new EmployeeDAOImpl();
        employeeDAO.findEmployeesByDepartmentId(UUID.fromString("5621b656-d44a-477f-bfeb-b6a5f1b708a7"));
        Integer count = (Integer)session.getAttribute("count");
        if (count == null) {
            count = 0;
        }
        count++;
        session.setAttribute("count", count);
        PrintWriter writer = response.getWriter();
        writer.write("<html>");
        writer.write("<h1> count is " + count + "</h1>");
        writer.write("</html>");
    }

    public void destroy() {
    }
}