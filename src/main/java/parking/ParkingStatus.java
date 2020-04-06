package parking;

import repository.Key;

public class ParkingStatus {
    @Key
    private String region;
    @Key
    private String serial;
    @Key
    private String plateNo;

    public ParkingStatus() {
    }

    public String getRegion() {
        return region;
    }

    public String getSerial() {
        return serial;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }
}
