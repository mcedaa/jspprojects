

<%@ page import="java.util.List" %>
<%@ page import="com.luv2code.web.jdbc.Student" %>
<%@ page import="com.luv2code.web.jdbc.StudentControllerServlet" %>
<%@ page import="com.luv2code.web.jdbc.StudentDBUtil" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Student Tracker App</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<%
    List<Student> theStudents = (List<Student>)  request.getAttribute("STUDENT_LIST");


%>

<body>
<div id="wrapper">
    <div id="header">
        <h2>FooBar University</h2>
    </div>
</div>

<div id="container">
    <div id="content">
        <!-- put new buttun -- Add student -->
        <input type="button" value="Add Student"
        onclick="window.location.href='add-student-form.jsp'; return false;"
        class="add-student-button">
        <table>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Action</th>
            </tr>

            <c:forEach var="tempStudent" items="${STUDENT_LIST}" >
                <!-- set up a link each student -->
                <c:url var="tempLink" value="StudentController-servlet">
                    <c:param name="command" value="LOAD" />
                    <c:param name="studentId" value="${tempStudent.id}"/>
                </c:url>

                <c:url var="deleteLink" value="StudentController-servlet">
                    <c:param name="command" value="DELETE"/>
                    <c:param name="studentId" value="${tempStudent.id}"/>
                </c:url>


                <tr>
                    <td> ${tempStudent.firstName} </td>
                    <td> ${tempStudent.lastName} </td>
                    <td> ${tempStudent.email} </td>
                    <td>
                        <a href="${tempLink}">Update</a>
                        |
                        <a href="${deleteLink}"
                           onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">
                            Delete</a>
                    </td>
                </tr>

            </c:forEach>
            <!--
            <% for (Student tempStudent :  theStudents){%>
                <tr>
                    <td> <%= tempStudent.getFirstName()%></td>
                    <td> <%= tempStudent.getLastName()%></td>
                    <td> <%= tempStudent.getEmail()%></td>
                </tr>


            <% } %>
            -->
        </table>

    </div>

</div>










<!--
<c:forEach var="student" items="${STUDENT_LIST}">
    <p>${student.firstName} ${student.lastName}</p>
</c:forEach>
-->
<!--<%= request.getAttribute("STUDENT_LIST") %>-->
</body>
</html>
