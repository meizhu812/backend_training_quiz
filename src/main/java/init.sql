CREATE DATABASE parking_lot;
USE parking_lot;
CREATE TABLE parking_space
(
    region   CHAR    NOT NULL,
    serial   TINYINT NOT NULL,
    car_number CHAR(6) NULL,
    PRIMARY KEY (region, serial),
    CONSTRAINT UNIQUE (car_number)
);