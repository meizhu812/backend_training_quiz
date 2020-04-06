package parking;

import repository.Key;

public class ParkingStatus {
    @Key
    private String region;
    @Key
    private int serial;
    private String plateNo;

    public ParkingStatus() {
    }

    public ParkingStatus(String region, int serial, String plateNo) {
        this.region = region;
        this.serial = serial;
        this.plateNo = plateNo;
    }

    public String getRegion() {
        return region;
    }

    public int getSerial() {
        return serial;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setRegion(Object region) {
        this.region = (String) region;
    }

    public void setSerial(Object serial) {
        this.serial = (Integer) serial;
    }

    public void setPlateNo(Object plateNo) {
        this.plateNo = (String) plateNo;
    }
}
