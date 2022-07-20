<%@ page import="com.example.employeeservicejavaee.entity.Department" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.StringJoiner" %><%--
  Created by IntelliJ IDEA.
  User: Yura
  Date: 20.07.2022
  Time: 17:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add employee</title>
    <form action="${pageContext.request.contextPath}/submit-adding" method="post">
        <p>Введите имя</p>
        <input type="text" name="name"/>
        <p>Введите фамилию</p>
        <input type="text" name="surname"/>
        <p>Введите департамент</p>
        <select name="department">
            <%
                StringBuilder sb = new StringBuilder();
                List<Department> departmentList = (List<Department>)session.getAttribute("departments");
                for (Department department : departmentList) {
                    sb.append("<option>");
                    sb.append(department.getName());
                    sb.append("</option>");
                }
            %>
            <%= sb.toString() %>
        </select>
        <button type="submit">Добавить</button>
    </form>
</head>
<body>

</body>
</html>
