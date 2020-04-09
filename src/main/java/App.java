import parking.ParkingConsole;
import parking.ScannerFilter;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(App.class.getResourceAsStream("jdbc.properties"));
        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
             ParkingConsole console = new ParkingConsole(new ScannerFilter())) {
            console.setConnection(connection);
            console.operateParking();
        }
    }
}
