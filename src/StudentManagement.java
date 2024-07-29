import java.sql.*;
import java.util.Scanner;

public class StudentManagement {

    Scanner scanner = new Scanner(System.in);
    String url = "DATABASE URL";
    String user = "DATABASE USERNAME";
    String password = "DATABASE PASSWORD";
    Connection con = null;

    StudentManagement() throws SQLException{
        if(connectDB()){
            System.out.println("Connection Successful");
            System.out.println("-------- Student Management System -------");
            home();
        }else{
            System.out.println("Failed to connect to Database!");
        }
    }

    private void home() throws SQLException{
        System.out.println("1 -> Add new Student" +
                "\n 2 -> Update a Student " +
                "\n 3 -> Delete a Student " +
                "\n 4 -> Get a Student" +
                "\n 5 -> Get All Students" +
                "\n 6 => Exit");

        System.out.print("Enter your Choice: ");
        int userChoice = scanner.nextInt();
        switch(userChoice) {
            case 1 -> addStudent();
            case 2 -> updateStudent();
            case 3 -> deleteStudent();
            case 4 -> getStudent();
            case 5 -> getAllStudent();
            case 6 -> {
                con.close();
                System.out.println("Exited Student Management");
            }
            default -> {
                System.out.println("Invalid Input");
                home();
            }
        }
    }

    // Connection to the Student Database
    private boolean connectDB(){
        try{
            con = DriverManager.getConnection(url, user, password);
            return true;
        }catch(Exception e){
            System.out.println("Connection failed" + e);
        }
        return false;
    }
    private void addStudent() throws SQLException{
        System.out.print("Enter Student ID: ");
        int id = scanner.nextInt();
        System.out.print("Enter Student Name: ");
        String name = scanner.next();
        System.out.print("Enter GPA: ");
        float gpa = scanner.nextFloat();

        PreparedStatement pst = con.prepareStatement("INSERT INTO Student VALUE(?, ?, ?);");
        pst.setInt(1, id);
        pst.setString(2, name);
        pst.setFloat(3, gpa);

        pst.executeUpdate();
        System.out.println("Student Added Successfully");

        home();
    }

    private void updateStudent() throws SQLException{
        System.out.print("Enter Student Id: ");
        int id = scanner.nextInt();
        System.out.println("To Update, Id -> 1, Name -> 2, GPA -> 3, home -> 4");
        int choice = scanner.nextInt();

        switch(choice){
            case 1 -> updateId(id);
            case 2 -> updateName(id);
            case 3 -> updateGPA(id);
            case 4 -> home();
            default -> {
                System.out.println("Invalid Input");
                updateStudent();
                return;
            }
        }
        home();
    }

    private void updateName(int id) throws SQLException{
        System.out.print("Enter Name: ");
        String name = scanner.next();
        PreparedStatement pst = con.prepareStatement("UPDATE Student SET stu_name=? WHERE stu_id=?;");
        pst.setString(1, name);
        pst.setInt(2, id);

        pst.executeUpdate();

        System.out.println("Updated name successfully.");
    }

    private void updateId(int id) throws SQLException{
        System.out.print("Enter Student Id: ");
        int newId = scanner.nextInt();
        PreparedStatement pst = con.prepareStatement("UPDATE Student SET stu_id=? WHERE stu_id=?;");
        pst.setInt(1, newId);
        pst.setInt(2, id);

        pst.executeUpdate();

        System.out.println("Updated newId successfully.");
    }

    private void updateGPA(int id) throws SQLException{
        System.out.print("Enter GPA: ");
        float gpa = scanner.nextFloat();
        PreparedStatement pst = con.prepareStatement("UPDATE Student SET gpa=? WHERE stu_id=?;");
        pst.setFloat(1, gpa);
        pst.setInt(2, id);

        pst.executeUpdate();

        System.out.println("Updated GPA successfully.");
    }
    private void deleteStudent() throws SQLException{
        System.out.print("Enter Student Id: ");
        int id = scanner.nextInt();

        Statement st = con.createStatement();
        st.executeUpdate("DELETE FROM Student WHERE stu_id=" + id + ";");
        System.out.println("Deleted Student Successfully");

        home();
    }

    private void printStudent(ResultSet rs) throws SQLException{
        while(rs.next()){
            System.out.println("Id: " + rs.getInt(1) +
                    ", Name: " + rs.getString(2) +
                    ", GPA: " + rs.getFloat(3));
        }
    }
    private void getStudent() throws SQLException{
        System.out.print("Enter Student Id: ");
        int id = scanner.nextInt();
        PreparedStatement pst = con.prepareStatement("SELECT * FROM Student WHERE stu_id=?");
        pst.setInt(1,id);
        ResultSet rs = pst.executeQuery();

        printStudent(rs);
        home();
    }

    private void getAllStudent() throws SQLException{
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Student");

        printStudent(rs);
        home();
    }
}
