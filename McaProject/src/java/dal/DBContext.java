package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    protected Connection connection;

    public DBContext() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=MyAssignmentPrj; encrypt=true;trustServerCertificate=true;";
            String user = "sa";
            String pass = "sa";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Kết nối DB thành công");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Không tìm thấy driver JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Kết nối DB lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
