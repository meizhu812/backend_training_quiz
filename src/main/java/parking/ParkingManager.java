package parking;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ParkingManager implements AutoCloseable {
    private Connection connection;
    private ParkingStatusRepo statusRepo = new ParkingStatusRepo();
    private ParkingBuddy buddy;

    public ParkingManager() {
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        statusRepo.setConnection(connection);
        buddy = new ParkingBuddy(statusRepo);
    }

    public void initParkingPlaces(List<ParkingStatus> initPlaces) throws SQLException {
        statusRepo.init(initPlaces);
    }

    public void parkCar(int plateNo) throws SQLException {
        buddy.parkCar(plateNo);
    }

    @Override
    public void close() {
        connection = null;
        statusRepo.close();
    }
}
