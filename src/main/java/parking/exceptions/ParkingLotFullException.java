package parking.exceptions;

public class ParkingLotFullException extends RuntimeException {
    public ParkingLotFullException() {
        super("非常抱歉，由于车位已满，暂时无法为您停车！");
    }
}
