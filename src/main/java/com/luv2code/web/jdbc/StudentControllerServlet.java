package com.luv2code.web.jdbc;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;

@WebServlet(name = "list_students.jsp", value = "/StudentController-servlet")
public class StudentControllerServlet extends HttpServlet {

    //constructor
    private static final long serialVersionUID = 1L;

    private StudentDBUtil studentDbUtil;

    private Connection connection;
/*
    public void init() throws ServletException {
        super.init();

        // create our student db util ... and pass in the conn pool / datasource
        }

*/



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // Step 2:  Get a connection to the database
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        //DB bağlantısı

        String jdbcUrl = "jdbc:mysql://localhost:3306/web_student_tracker";
        String user = "root";
        String password = "123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        // list the students ... in mvc fashion

        try {
            studentDbUtil = new StudentDBUtil(connection);

            //listStudents(request,response);
        }
        catch (Exception exc) {
            throw new ServletException(exc);
        }



        try{
            //read the command parameter
            String theCommand = request.getParameter("command");
            //if the command is missing then default to listing students
            if (theCommand == null){
                theCommand="LIST";

            }
            //route to the appropriate method
            switch(theCommand){

                case "LIST":
                    listStudents(request,response);
                    break;
                case "ADD":
                    addStudent(request,response);
                    break;
                case "LOAD":
                    loadStudent(request,response);
                    break;
                case "UPDATE":
                    updateStudent(request,response);
                    break;
                case "DELETE":
                    deleteStudent(request,response);
                    break;
                default:
                    listStudents(request,response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //read student id  from form data
        String theStudentId=request.getParameter("studentId");
        //delete student from database
        StudentDBUtil.deleteStudent(theStudentId);
        //send them back to "list students" page
        listStudents(request,response);
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
        // read student info from form data
        int id = Integer.parseInt(request.getParameter("studentId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // create a new student object
        Student theStudent = new Student(id, firstName, lastName, email);

        // perform update on database
        studentDbUtil.updateStudent(theStudent);

        // send them back to the "list students" page
        listStudents(request, response);
    }

    private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
        // read student id from form data
        String theStudentId = request.getParameter("studentId");

        // get student from database (db util)
        Student theStudent = studentDbUtil.getStudent(theStudentId);

        // place student in the request attribute
        request.setAttribute("THE_STUDENT", theStudent);

        // send to jsp page: update-student-form.jsp
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/update-student-form.jsp");
        dispatcher.forward(request, response);
    }


    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        //DB bağlantısı

        String jdbcUrl = "jdbc:mysql://localhost:3306/web_student_tracker";
        String user = "root";
        String password = "123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //read students info from form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        //create a new student object
        Student theStudent = new Student(firstName,lastName,email);
        //add the student to the database
        StudentDBUtil.addStudent(theStudent);
        //send back to main page (the Student list
        listStudents(request,response);

    }
    private void listStudents(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        //DB bağlantısı

        String jdbcUrl = "jdbc:mysql://localhost:3306/web_student_tracker";
        String user = "root";
        String password = "123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        // get students from db util
        List<Student> students = studentDbUtil.getStudents();

        // add students to the request
        request.setAttribute("STUDENT_LIST", students);


        // send to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("list_students.jsp");
        dispatcher.forward(request, response);
    }


}
