<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Books</title></head>
<body>
<h1>Books</h1>
<table border="1">
    <tr><th>ID</th><th>Title</th><th>Author</th><th>ISBN</th><th>Available</th></tr>
    <c:forEach var="book" items="${books}">
        <tr>
            <td>${book.id}</td>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.isbn}</td>
            <td>${book.available}</td>
        </tr>
    </c:forEach>
</table>
<h2>Add a book</h2>
<form method="post" action="${pageContext.request.contextPath}/books">
    Title: <input type="text" name="title" /><br/>
    Author: <input type="text" name="author" /><br/>
    ISBN: <input type="text" name="isbn" /><br/>
    <input type="submit" value="Add Book" />
</form>
</body>
</html>
