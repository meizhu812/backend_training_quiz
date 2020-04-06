package parking;

import java.sql.Connection;

public class ParkingManager implements AutoCloseable {
    private Connection connection;
    private ParkingStatusRepo statusRepo = new ParkingStatusRepo();

    public ParkingManager() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        statusRepo.setConnection(connection);
    }

    @Override
    public void close(){
        connection = null;
        statusRepo.close();
    }
}
