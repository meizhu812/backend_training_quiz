import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import parking.*;
import org.junit.jupiter.api.Test;
import parking.exceptions.CarAlreadyInside;
import parking.exceptions.InvalidInput;
import parking.exceptions.InvalidTicket;
import parking.exceptions.ParkingLotFull;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ParkingConsoleTest {
    private static final ParkingConsole console = new ParkingConsole();
    private static Connection connection;

    @BeforeAll
    static void beforeAll() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(App.class.getResourceAsStream("jdbc.properties"));
        connection = DriverManager.getConnection(
                properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        console.setConnection(connection);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        console.close();
        connection.close();
    }

    @Test
    void should_return_ticket_information_when_park_given_init_and_car_and_general_boy() throws SQLException, InvalidInput {
        console.init("A:2,B:2");

        String aTicket = console.park("A12098");
        assertEquals("A,1,A12098", aTicket);
        String bTicket = console.park("B16920");
        assertEquals("A,2,B16920", bTicket);
        String cTicket = console.park("C76129");
        assertEquals("B,1,C76129", cTicket);
    }

    @Test
    void should_throw_exception_with_message_when_park_given_init_and_car_and_general_boy() throws SQLException, InvalidInput {
        console.init("A:1,B:1");
        console.park("A12908");
        console.park("B38201");
        assertThrows(ParkingLotFull.class, () -> console.park("H12626"));
    }

    @Test
    void should_throw_exception_with_message_that_when_parking_a_car_that_is_already_inside() throws SQLException, InvalidInput {
        console.init("A:2,B:2");
        console.park("A12345");
        assertEquals(assertThrows(CarAlreadyInside.class, () -> console.park("A12345")).getMessage(), "错误：车牌号为A12345的车辆已入场");
    }

    @Test
    void should_return_car_information_when_fetch_given_ticket_and_car() throws SQLException, InvalidInput, InvalidTicket {
        console.init("A:8,B:10");

        String aTicket = console.park("A12198");
        String acar = console.fetch(aTicket);
        assertEquals("A12198", acar);

        String bTicket = console.park("B78210");
        String bCar = console.fetch(bTicket);
        assertEquals("B78210", bCar);

        String cTicket = console.park("C98201");
        String cCar = console.fetch(cTicket);
        assertEquals("C98201", cCar);
    }

    @Test
    void should_throw_exception_with_message_when_fetch_given_ticket_information_is_not_correct() throws SQLException, InvalidInput {
        console.init("A:8,B:10");

        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetch("C,1,A12098")).getMessage(), "停车券格式错误");
        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetch("A,9,A12098")).getMessage(), "停车券无效");
        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetch("B,-1,A12098")).getMessage(), "停车券格式错误");
    }

    @Test
    void should_throw_exception_with_message_when_fetch_given_spacee_has_no_car() throws SQLException, InvalidInput {
        console.init("A:8,B:10");

        String aTicket = console.park("A12098");
        assertDoesNotThrow(() -> console.fetch(aTicket));

        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetch(aTicket)).getMessage(), "停车券无效");
    }

    @Test
    void should_throw_exception_with_message_when_fetch_given_space_is_other_car() throws SQLException, InvalidInput {
        console.init("A:8,B:10");

        String aTicket = console.park("A12098");
        assertDoesNotThrow(() -> console.fetch(aTicket));
        console.park("B12598");

        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetch(aTicket)).getMessage(), "停车券无效");
    }

    @Deprecated
    @Test
    void deprecated_should_throw_exception_with_message_when_fetch_given_ticket_information_is_not_correct() throws SQLException, InvalidInput {
        console.init("A:8,B:10");

        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetchOld("C,1,A12098")).getMessage(), "停车券格式错误");
        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetchOld("A,9,A12098")).getMessage(), "没有这个车位");
        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetchOld("B,-1,A12098")).getMessage(), "停车券格式错误");
    }

    @Deprecated
    @Test
    void deprecated_should_throw_exception_with_message_when_fetch_given_spacee_has_no_car() throws SQLException, InvalidInput {
        console.init("A:8,B:10");

        String aTicket = console.park("A12098");
        assertDoesNotThrow(() -> console.fetchOld(aTicket));

        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetchOld(aTicket)).getMessage(), "该车位未停放车辆");
    }

    @Deprecated
    @Test
    void deprecated_should_throw_exception_with_message_when_fetch_given_space_is_other_car() throws SQLException, InvalidInput {
        console.init("A:8,B:10");

        String aTicket = console.park("A12098");
        assertDoesNotThrow(() -> console.fetchOld(aTicket));
        console.park("B12598");

        assertEquals(assertThrows(InvalidTicket.class, () -> console.fetchOld(aTicket)).getMessage(), "车牌号不匹配");
    }
}