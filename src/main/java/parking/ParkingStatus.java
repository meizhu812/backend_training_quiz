package parking;

import repository.Key;

public class ParkingStatus {
    @Key
    private String region;
    @Key
    private int serial;
    @Key
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

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }
}
