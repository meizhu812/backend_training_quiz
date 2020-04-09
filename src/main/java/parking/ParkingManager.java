package parking;

import parking.exceptions.CarAlreadyInside;
import parking.exceptions.InvalidTicket;
import parking.exceptions.ParkingLotFull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ParkingManager implements AutoCloseable {
    private ParkingSpaceRepo statusRepo = new ParkingSpaceRepo();
    private ParkingAssistant buddy;

    public ParkingManager() {
    }

    public void setConnection(Connection connection) {
        statusRepo.setConnection(connection);
        buddy = new ParkingAssistant(statusRepo);
    }

    public void initParkingPlaces(List<ParkingSpace> initPlaces) throws SQLException {
        statusRepo.init(initPlaces);
    }

    public ParkingSpace parkCar(String plateNo) throws SQLException, ParkingLotFull, CarAlreadyInside {
        return buddy.parkCar(plateNo);
    }

    public String fetchCar(ParkingSpace ticket) throws SQLException, InvalidTicket {
        return buddy.fetchCar(ticket);
    }

    @Deprecated
    public String fetchCarOld(ParkingSpace ticket) throws SQLException, InvalidTicket {
        return buddy.fetchCarOld(ticket);
    }

    @Override
    public void close() {
        statusRepo.close();
    }
}
