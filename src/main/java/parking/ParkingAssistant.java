package parking;

import parking.exceptions.CarAlreadyInside;
import parking.exceptions.InvalidTicket;
import parking.exceptions.ParkingLotFull;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

public class ParkingAssistant {
    private final ParkingSpaceRepo spaceRepo;

    public ParkingAssistant(ParkingSpaceRepo spaceRepo) {
        this.spaceRepo = spaceRepo;
    }

    public ParkingSpace parkCar(String carNumber) throws SQLException, ParkingLotFull, CarAlreadyInside {
        try {
            ParkingSpace nextPlace = spaceRepo.queryFirst("WHERE car_number IS NULL")
                    .orElseThrow(ParkingLotFull::new);
            nextPlace.setCarNumber(carNumber);
            spaceRepo.updateByEntity(nextPlace);
            return nextPlace;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new CarAlreadyInside(carNumber);
        }
    }

    public String fetchCar(ParkingSpace ticket) throws SQLException, InvalidTicket {
        ParkingSpace empty = new ParkingSpace(ticket.getRegion(), ticket.getSerial(), null);
        if (spaceRepo.replaceByEntity(ticket, empty) == 0) {
            throw new InvalidTicket("停车券无效");
        }
        return ticket.getCarNumber();
    }

    @Deprecated
    public String fetchCarOld(ParkingSpace ticket) throws SQLException, InvalidTicket {
        ParkingSpace parkedCar = spaceRepo.queryByKeys(ticket.getRegion(), ticket.getSerial())
                .orElseThrow(() -> new InvalidTicket("没有这个车位"));
        String plateNo = Optional.ofNullable(parkedCar.getCarNumber()).orElseThrow(() -> new InvalidTicket("该车位未停放车辆"));
        if (!plateNo.equals(ticket.getCarNumber())) {
            throw new InvalidTicket("车牌号不匹配");
        }
        parkedCar.setCarNumber(null);
        spaceRepo.updateByEntity(parkedCar);
        return plateNo;
    }
}
