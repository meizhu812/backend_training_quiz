package parking;

import parking.exceptions.ExitEvent;
import parking.exceptions.CancelEvent;

import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Consumer;

public class ScannerFilter implements Iterator<String> {
    private final Scanner sc;

    public ScannerFilter() {
        this(new Scanner(System.in));
    }

    public ScannerFilter(Scanner scanner) {
        sc = scanner;
    }

    public String next() {
        return filter(sc.next());
    }

    private String filter(String input) {
        switch (input) {
            case "EXIT":
                throw new ExitEvent();
            case "CANCEL":
                throw new CancelEvent();
            default:
                return input;
        }
    }

    @Override
    public boolean hasNext() {
        return sc.hasNext();
    }

    @Override
    public void remove() {
        sc.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super String> action) {
        sc.forEachRemaining(action);
    }
}
