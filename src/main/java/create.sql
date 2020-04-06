CREATE DATABASE parking_lot;
USE parking_lot;
CREATE TABLE parking_status
(
    region   CHAR    NOT NULL,
    serial   TINYINT NOT NULL,
    plate_no CHAR(6) NULL,
    PRIMARY KEY (region, serial),
    CONSTRAINT UNIQUE (plate_no)
);