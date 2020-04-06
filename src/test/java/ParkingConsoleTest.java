import parking.ParkingConsole;
import parking.InvalidTicketException;
import parking.ParkingLotFullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingConsoleTest {

  @Test
  void should_return_ticket_information_when_park_given_init_and_car_and_general_boy() {
    ParkingConsole.init("A:2,B:2");

    String aTicket = ParkingConsole.park("A12098");
    assertEquals("A,1,A12098", aTicket);
    String bTicket = ParkingConsole.park("B16920");
    assertEquals("A,2,B16920", bTicket);
    String cTicket = ParkingConsole.park("C76129");
    assertEquals("B,1,C76129", cTicket);
  }

  @Test
  void should_throw_exception_with_message_when_park_given_init_and_car_and_general_boy() {
    ParkingConsole.init("A:1,B:1");
    ParkingConsole.park("A12908");
    ParkingConsole.park("B38201");
    assertThrows(ParkingLotFullException.class, () -> ParkingConsole.park("H12626"));
  }

  @Test
  void should_return_car_information_when_fetch_given_ticket_and_car() {
    ParkingConsole.init("A:8,B:10");

    String aTicket = ParkingConsole.park("A12198");
    String acar = ParkingConsole.fetch(aTicket);
    assertEquals("A12198", acar);

    String bTicket = ParkingConsole.park("B78210");
    String bCar = ParkingConsole.fetch(bTicket);
    assertEquals("B78210", bCar);

    String cTicket = ParkingConsole.park("C98201");
    String cCar = ParkingConsole.fetch(cTicket);
    assertEquals("C98201", cCar);
  }

  @Test
  void should_throw_exception_with_message_when_fetch_given_ticket_information_is_not_correct() {
    ParkingConsole.init("A:8,B:10");

    assertThrows(InvalidTicketException.class, () -> ParkingConsole.fetch("C,1,A12098"));
    assertThrows(InvalidTicketException.class, () -> ParkingConsole.fetch("A,9,A12098"), "停车券无效");
    assertThrows(InvalidTicketException.class, () -> ParkingConsole.fetch("B,-1,A12098"), "停车券无效");
  }

  @Test
  void should_throw_exception_with_message_when_fetch_given_spacee_has_no_car() {
    ParkingConsole.init("A:8,B:10");

    String aTicket = ParkingConsole.park("A12098");
    ParkingConsole.fetch(aTicket);

    assertThrows(InvalidTicketException.class, () -> ParkingConsole.fetch(aTicket));
  }

  @Test
  void should_throw_exception_with_message_when_fetch_given_space_is_other_car() {
    ParkingConsole.init("A:8,B:10");

    String aTicket = ParkingConsole.park("A12098");
    ParkingConsole.fetch(aTicket);
    ParkingConsole.park("B12598");

    assertThrows(InvalidTicketException.class, () -> ParkingConsole.fetch(aTicket));

  }
}