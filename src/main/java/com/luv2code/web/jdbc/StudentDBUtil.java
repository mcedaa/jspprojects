package com.luv2code.web.jdbc;

import com.mysql.cj.jdbc.NClob;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Connection.*;

public class StudentDBUtil {


    private static Connection connection;

    public StudentDBUtil(Connection connection) {
        this.connection = connection;
    }

    public static void addStudent(Student theStudent) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            // get db connection
            //myConn=connection.createStatement().getConnection();
            String jdbcUrl = "jdbc:mysql://localhost:3306/web_student_tracker";
            String user = "root";
            String password = "123";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                myConn = DriverManager.getConnection(jdbcUrl, user, password);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


            // create sql for insert
            String sql = "insert into student "
                    + "(first_name, last_name, email) "
                    + "values (?, ?, ?)";

            myStmt = myConn.prepareStatement(sql);

            // set the param values for the student
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());

            // execute sql insert
            myStmt.execute();
        }
        finally {
             //clean up JDBC objects
             close(myConn, myStmt, null);
        }
    }

    public static void deleteStudent(String theStudentId) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            // convert student id to int
            int studentId = Integer.parseInt(theStudentId);
            // get db connection
            //myConn=connection.getConnection();
            String jdbcUrl = "jdbc:mysql://localhost:3306/web_student_tracker";
            String user = "root";
            String password = "123";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                myConn = DriverManager.getConnection(jdbcUrl, user, password);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // get connection to database
            //myConn = dataSource.getConnection();

            // create sql to delete student
            String sql = "delete from student where id=?";

            // prepare statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setInt(1, studentId);

            // execute sql statement
            myStmt.execute();
        }
        finally {
            // clean up JDBC code
            close(myConn, myStmt, null);
        }
    }




    public List<Student> getStudents() throws Exception {

        List<Student> students = new ArrayList<>();

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            // get a connection
            String jdbcUrl = "jdbc:mysql://localhost:3306/web_student_tracker";
            String user = "root";
            String password = "123";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                myConn = DriverManager.getConnection(jdbcUrl, user, password);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            //myConn=connection.createStatement().getConnection();

            // create sql statement
            String sql = "select * from student order by last_name";

            myStmt = this.connection.createStatement();

            // execute query
            myRs = myStmt.executeQuery(sql);

            // process result set
            while (myRs.next()) {

                // retrieve data from result set row
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                // create new student object
                Student tempStudent = new Student(id, firstName, lastName, email);

                // add it to the list of students
                students.add(tempStudent);
            }

            return students;
        }
        finally {
            // close JDBC objects
            close(connection, myStmt, myRs);
        }
    }

    private static void close(Connection connection, Statement myStmt, ResultSet myRs) {

        try {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (connection != null) {
                connection.close();   // doesn't really close it ... just puts back in connection pool
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public Student getStudent(String theStudentId) {
        Student theStudent = null;

        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;
        int studentId;

        try {
            // convert student id to int
            studentId = Integer.parseInt(theStudentId);

            // get connection to database
            String jdbcUrl = "jdbc:mysql://localhost:3306/web_student_tracker";
            String user = "root";
            String password = "123";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                myConn = DriverManager.getConnection(jdbcUrl, user, password);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // create sql to get selected student
            String sql = "select * from student where id=?";

            // create prepared statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setInt(1, studentId);
            //myStmt.setInt(1, studentId) ile studentId değeri sorgunun ? parametresine atanır.

            // execute statement
            myRs = myStmt.executeQuery();

            // retrieve data from result set row
            if (myRs.next()) {
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                // use the studentId during construction
                theStudent = new Student(studentId, firstName, lastName, email);
            }
            else {
                throw new Exception("Could not find student id: " + studentId);
            }

            return theStudent;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // clean up JDBC objects
            close(myConn, myStmt, myRs);
        }
    }

    public void updateStudent(Student theStudent) {
        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            // get db connection
            String jdbcUrl = "jdbc:mysql://localhost:3306/web_student_tracker";
            String user = "root";
            String password = "123";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                myConn = DriverManager.getConnection(jdbcUrl, user, password);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // create SQL update statement
            String sql = "update student "
                    + "set first_name=?, last_name=?, email=? "
                    + "where id=?";

            // prepare statement
            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());
            myStmt.setInt(4, theStudent.getId());
            // 1 2 3 4 sql sorgusundaki soru işaretlerinin sırasına göre numaralandrırılır

            // execute SQL statement
            myStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // clean up JDBC objects
            close(myConn, myStmt, null);
        }
    }
}
