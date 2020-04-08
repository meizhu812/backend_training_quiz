package parking;

import repository.Key;

public class ParkingSpace {
    @Key
    private String region;
    @Key
    private int serial;
    private String carNumber;

    public ParkingSpace() {
    }

    public ParkingSpace(String region, int serial, String carNumber) {
        this.region = region;
        this.serial = serial;
        this.carNumber = carNumber;
    }

    public String getRegion() {
        return region;
    }

    public int getSerial() {
        return serial;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setRegion(Object region) {
        this.region = (String) region;
    }

    public void setSerial(Object serial) {
        this.serial = (Integer) serial;
    }

    public void setCarNumber(Object carNumber) {
        this.carNumber = (String) carNumber;
    }
}
