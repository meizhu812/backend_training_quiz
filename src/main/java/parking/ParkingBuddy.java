package parking;

import repository.ZeroAffected;

import java.sql.Connection;
import java.sql.SQLException;

public class ParkingBuddy {
    private final ParkingStatusRepo statusRepo;

    public ParkingBuddy(ParkingStatusRepo statusRepo) {
        this.statusRepo = statusRepo;
    }

    public ParkingStatus parkCar(String carPlate) throws SQLException {
        ParkingStatus nextPlace = statusRepo.customQueryFirst("plate_no IS NULL").orElseThrow(ParkingLotFullException::new);
        nextPlace.setPlateNo(carPlate);
        try {
            statusRepo.updateByEntity(nextPlace);
            return nextPlace;
        } catch (ZeroAffected zeroAffected) {
            throw new SQLException(zeroAffected);
        }
    }
}
