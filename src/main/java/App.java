import parking.ParkingConsole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/parking_lot?serverTimezone=UTC",
                "root", "root")) {
            ParkingConsole console = new ParkingConsole();
            console.setConnection(connection);
            console.operateParking();
        }
    }
}
