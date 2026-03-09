package lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseSQL {

    protected Connection connection;

    public BaseSQL() {
        connect();
    }

    private void connect() {
        if (this.connection == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/farmacia";
                String user = "gerente2";
                String password = "1234";

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Conexión unica exitosa");

            } catch (SQLException e) {
                System.out.println("Error de conexión: " + e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
