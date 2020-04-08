package parking.exceptions;

public class InvalidTicketException extends RuntimeException {
    public InvalidTicketException() {
        super("停车券无效");
    }
}
