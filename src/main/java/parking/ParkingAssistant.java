package parking;

import parking.exceptions.InvalidTicketException;
import parking.exceptions.ParkingLotFullException;

import java.sql.SQLException;
import java.util.Optional;

public class ParkingAssistant {
    private final ParkingSpaceRepo spaceRepo;

    public ParkingAssistant(ParkingSpaceRepo spaceRepo) {
        this.spaceRepo = spaceRepo;
    }

    public ParkingSpace parkCar(String carPlate) throws SQLException {
        ParkingSpace nextPlace = spaceRepo.queryFirst("WHERE car_number IS NULL")
                .orElseThrow(ParkingLotFullException::new);
        nextPlace.setCarNumber(carPlate);
        spaceRepo.updateByEntity(nextPlace);
        return nextPlace;
    }

    public String fetchCar(ParkingSpace ticket) throws SQLException {
        ParkingSpace empty = new ParkingSpace(ticket.getRegion(), ticket.getSerial(), null);
        if (spaceRepo.replaceByEntity(ticket, empty) == 0) {
            throw new InvalidTicketException();
        }
        return ticket.getCarNumber();
    }

    @Deprecated
    public String fetchCarOld(ParkingSpace ticket) throws SQLException {
        ParkingSpace parkedCar = spaceRepo.queryByKeys(ticket.getRegion(), ticket.getSerial())
                .orElseThrow(InvalidTicketException::new);
        String plateNo = Optional.ofNullable(parkedCar.getCarNumber()).orElseThrow(InvalidTicketException::new);
        if (!plateNo.equals(ticket.getCarNumber())) {
            throw new InvalidTicketException();
        }
        parkedCar.setCarNumber(null);
        spaceRepo.updateByEntity(parkedCar);
        return plateNo;
    }
}
