<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Loans</title></head>
<body>
<h1>Loans</h1>
<table border="1">
    <tr><th>ID</th><th>Book ID</th><th>Member ID</th><th>Loan Date</th><th>Due Date</th><th>Return Date</th></tr>
    <c:forEach var="loan" items="${loans}">
        <tr>
            <td>${loan.id}</td>
            <td>${loan.bookId}</td>
            <td>${loan.memberId}</td>
            <td>${loan.loanDate}</td>
            <td>${loan.dueDate}</td>
            <td>${loan.returnDate}</td>
        </tr>
    </c:forEach>
</table>
<h2>Create a loan</h2>
<form method="post" action="${pageContext.request.contextPath}/loans">
    Book ID: <input type="text" name="bookId" /><br/>
    Member ID: <input type="text" name="memberId" /><br/>
    <input type="submit" value="Create Loan" />
</form>
<h2>Return a loan</h2>
<form method="post" action="${pageContext.request.contextPath}/loans">
    <input type="hidden" name="action" value="return" />
    Loan ID: <input type="text" name="loanId" /><br/>
    <input type="submit" value="Mark Returned" />
</form>
</body>
</html>
