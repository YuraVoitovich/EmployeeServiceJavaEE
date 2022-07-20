<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Main menu</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/add-employee" method="post">
        <button type="submit">Добавить работника</button>
        <button type="submit">Удалить работника</button>
        <button type="submit">Посмотреть всех работников по департаменту</button>
        <button type="submit">Изменить работника</button>
    </form>
<br/>

</body>
</html>