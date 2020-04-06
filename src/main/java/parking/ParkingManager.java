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

    public ParkingStatus parkCar(String plateNo) throws SQLException {
        return buddy.parkCar(plateNo);
    }

    public String fetchCar(ParkingStatus ticket) throws SQLException {
        return buddy.fetchCar(ticket);
    }

    @Override
    public void close() {
        connection = null;
        statusRepo.close();
    }
}
