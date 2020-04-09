package parking;

import parking.exceptions.CarAlreadyInside;
import parking.exceptions.InvalidTicketException;
import parking.exceptions.ParkingLotFullException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

public class ParkingAssistant {
    private final ParkingSpaceRepo spaceRepo;

    public ParkingAssistant(ParkingSpaceRepo spaceRepo) {
        this.spaceRepo = spaceRepo;
    }

    public ParkingSpace parkCar(String carNumber) throws SQLException {
        try {
            ParkingSpace nextPlace = spaceRepo.queryFirst("WHERE car_number IS NULL")
                    .orElseThrow(ParkingLotFullException::new);
            nextPlace.setCarNumber(carNumber);
            spaceRepo.updateByEntity(nextPlace);
            return nextPlace;
        } catch (SQLIntegrityConstraintViolationException e ){
            throw new CarAlreadyInside(carNumber);
        }
    }

    public String fetchCar(ParkingSpace ticket) throws SQLException {
        ParkingSpace empty = new ParkingSpace(ticket.getRegion(), ticket.getSerial(), null);
        if (spaceRepo.replaceByEntity(ticket, empty) == 0) {
            throw new InvalidTicketException("停车券无效");
        }
        return ticket.getCarNumber();
    }

    @Deprecated
    public String fetchCarOld(ParkingSpace ticket) throws SQLException {
        ParkingSpace parkedCar = spaceRepo.queryByKeys(ticket.getRegion(), ticket.getSerial())
                .orElseThrow(() -> new InvalidTicketException("没有这个车位"));
        String plateNo = Optional.ofNullable(parkedCar.getCarNumber()).orElseThrow(() -> new InvalidTicketException("该车位未停放车辆"));
        if (!plateNo.equals(ticket.getCarNumber())) {
            throw new InvalidTicketException("车牌号不匹配");
        }
        parkedCar.setCarNumber(null);
        spaceRepo.updateByEntity(parkedCar);
        return plateNo;
    }
}
