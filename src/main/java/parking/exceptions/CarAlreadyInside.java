package parking.exceptions;

public class CarAlreadyInside extends Exception {
    public CarAlreadyInside(String carNumber) {
        super(String.format("错误：车牌号为%s的车辆已入场", carNumber));
    }
}
