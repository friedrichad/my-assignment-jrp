package dal;

import model.BaseObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class DBContext<T extends BaseObject>{
    protected Connection connection;

    public DBContext() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=MyAssignmentPrj; encrypt=true;trustServerCertificate=true;";
            String user = "sa";
            String pass = "sa";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Kết nối DB thành công");
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy driver JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối DB lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đóng kết nối DB thành công!");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi đóng kết nối DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
public abstract ArrayList<T> list();
    public abstract T get(int id);
    public abstract void insert(T model);
    public abstract void update(T model);
    public abstract void delete(T model);
}
