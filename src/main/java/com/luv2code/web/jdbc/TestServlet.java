package com.luv2code.web.jdbc;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import jakarta.annotation.Resource;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;


//import javax.annotation.Resource;
@WebServlet(name = "TestServlet", value = "/Test-servlet")
public class TestServlet extends HttpServlet {

    //define datasource/connection pool for Resource Injection
    //String url = "jdbc:mysql://localhost:3306/your_database_name";

    //@Resource(name = "jdbc/web_student_tracker")
    //private DataSource dataSource;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        // Step 1:  Set up the printwriter
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

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
            myConn = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //myConn = dataSource.getConnection();

            // Step 3:  Create a SQL statements
            String sql = "select * from web_student_tracker.student";
            myStmt = myConn.createStatement();

            // Step 4:  Execute SQL query
            myRs = myStmt.executeQuery(sql);

            // Step 5:  Process the result set
            //List<String> studentEmails = new ArrayList<>();
            while (myRs.next()) {
                String email = myRs.getString("email");
                //studentEmails.add(email);
                out.println(email);


            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


}



