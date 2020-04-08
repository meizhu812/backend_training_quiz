package parking;

public class InvalidTicketException extends RuntimeException {
    public InvalidTicketException() {
        super("停车券无效");
    }
}
