package quackstagram.utilities;

import java.sql.*;

public class DatabaseConnector {

    public static void connect() {
        try {
            System.out.println("BEFORE");
            String URL = "jdbc:mysql://localhost:3306/pcshop";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection myCon = DriverManager.getConnection(URL, "BCS1510", "BCS1510");
            Statement stat = myCon.createStatement();
            test(stat);
            System.out.println("AFTER");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static void test(Statement s) throws SQLException{
        String query = "SELECT model " + "FROM products " + "WHERE maker = 'B'";
        ResultSet set = s.executeQuery(query);
        while (set.next()) {
            System.out.println(set.getNString(1));
        }
    }

    public static void main(String[] args) {
        System.out.println("I WORK");
        connect();
    }
}
