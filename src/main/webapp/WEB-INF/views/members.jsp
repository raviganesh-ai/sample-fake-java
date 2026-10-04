<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Members</title></head>
<body>
<h1>Members</h1>
<table border="1">
    <tr><th>ID</th><th>Full Name</th><th>Email</th><th>Membership Level</th></tr>
    <c:forEach var="member" items="${members}">
        <tr>
            <td>${member.id}</td>
            <td>${member.fullName}</td>
            <td>${member.email}</td>
            <td>${member.membershipLevel}</td>
        </tr>
    </c:forEach>
</table>
<h2>Add a member</h2>
<form method="post" action="${pageContext.request.contextPath}/members">
    Full name: <input type="text" name="fullName" /><br/>
    Email: <input type="text" name="email" /><br/>
    Membership level: <input type="text" name="membershipLevel" /><br/>
    <input type="submit" value="Add Member" />
</form>
</body>
</html>
