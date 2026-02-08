import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateTripsDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "mysql";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                    Statement stmt = conn.createStatement()) {

                String sql = "CREATE DATABASE IF NOT EXISTS trips";
                stmt.executeUpdate(sql);
                System.out.println("✅ Database 'trips' created successfully or already exists.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
