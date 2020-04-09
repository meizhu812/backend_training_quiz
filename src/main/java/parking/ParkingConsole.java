package parking;

import parking.exceptions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParkingConsole implements AutoCloseable {
    private static final String TOP_PROMPT
            = "【提示，可以输入EXIT退出程序或CANCEL中止当前操作】";
    private static final String MAIN_MENU_PROMPT
            = "1. 初始化停车场数据\n"
            + "2. 停车\n"
            + "3. 取车\n"
            + "4. 退出\n"
            + "请输入你的选择(1~4)：";
    private static final String INIT_PROMPT
            = "请输入初始化数据\n"
            + "格式为“停车场编号1：车位数，停车场编号2：车位数”，如“A:8,B:9”：";
    private static final String PARK_PROMPT
            = "请输入车牌号\n"
            + "格式为\"车牌号\" 如: \"A12098\"：";
    private static final String FETCH_PROMPT
            = "请输入停车券信息\n"
            + "格式为“停车场编号1，车位编号，车牌号”，如“A,1,A12098”：";

    private final Iterator<String> in;
    private final ParkingManager manager = new ParkingManager();

    public ParkingConsole() {
        this(new Scanner(System.in));
    }

    public ParkingConsole(Iterator<String> in) {
        this.in = in;
    }

    public void setConnection(Connection connection) {
        manager.setConnection(connection);
    }

    public void operateParking() throws SQLException {
        System.out.println(TOP_PROMPT);
        while (true) {
            try {
                System.out.println(MAIN_MENU_PROMPT);
                String input = in.next();
                Regex.MainOption.validate(input);
                handle(input);
            } catch (InvalidInput e) {
                System.out.println(e.getMessage());
            } catch (CancelEvent e) {
                System.out.println("操作中断！");
            } catch (ExitEvent e) {
                exit();
                break;
            }
        }
    }

    private void handle(String choice) throws SQLException, InvalidInput {
        switch (choice) {
            case "1":
                while (true) {
                    try {
                        System.out.println(INIT_PROMPT);
                        init(in.next());
                        System.out.println("停车位初始化成功！");
                        break;
                    } catch (InvalidInput e) {
                        System.out.println(e.getMessage());
                    }
                }
                break;
            case "2": {
                try {
                    System.out.println(PARK_PROMPT);
                    String ticket = park(in.next());
                    String[] ticketDetails = ticket.split(",");
                    System.out.printf("已将您的车牌号为%s的车辆停到%s停车场%s号车位，停车券为：%s，请您妥善保存。\n",
                            ticketDetails[2], ticketDetails[0], ticketDetails[1], ticket);
                } catch (ParkingLotFull | CarAlreadyInside e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "3": {
                try {
                    System.out.println(FETCH_PROMPT);
                    String car = fetch(in.next());
                    System.out.printf("已为您取到车牌号为%s的车辆，很高兴为您服务，祝您生活愉快!\n", car);
                    break;
                } catch (InvalidTicket e) {
                    System.out.println(e.getMessage());
                    break;
                }
            }
            case "4": {
                exit();
                break;
            }
        }
    }

    public void init(String initString) throws SQLException, InvalidInput {
        Matcher matcher = Regex.InitRegex.getMatcher(initString);
        List<ParkingSpace> initPlaces = Stream.concat(
                IntStream.rangeClosed(1, Integer.parseInt(matcher.group("countA")))
                        .mapToObj(n -> new ParkingSpace("A", n, null)),
                IntStream.rangeClosed(1, Integer.parseInt(matcher.group("countB")))
                        .mapToObj(n -> new ParkingSpace("B", n, null)))
                .collect(Collectors.toList());
        manager.initParkingPlaces(initPlaces);
    }

    public String park(String carNumber) throws SQLException, InvalidInput, ParkingLotFull {
        Regex.PlateNo.validate(carNumber);
        ParkingSpace space = manager.parkCar(carNumber);
        return String.format("%s,%d,%s", space.getRegion(), space.getSerial(), space.getCarNumber());
    }

    public String fetch(String ticketString) throws SQLException, InvalidTicket {
        try {
            Matcher matcher = Regex.Ticket.getMatcher(ticketString);
            ParkingSpace ticketSpace = new ParkingSpace(
                    matcher.group("region"),
                    Integer.parseInt(matcher.group("serial")),
                    matcher.group("plate"));
            return manager.fetchCar(ticketSpace);
        } catch (InvalidInput invalidInput) {
            throw new InvalidTicket("停车券格式错误");
        }
    }

    @Deprecated
    public String fetchOld(String ticketString) throws SQLException, InvalidTicket {
        try {
            Matcher matcher = Regex.Ticket.getMatcher(ticketString);
            ParkingSpace ticketSpace = new ParkingSpace(
                    matcher.group("region"),
                    Integer.parseInt(matcher.group("serial")),
                    matcher.group("plate"));
            return manager.fetchCarOld(ticketSpace);
        } catch (InvalidInput invalidInput) {
            throw new InvalidTicket("停车券格式错误");
        }
    }

    private void exit() {
        System.out.println("系统已退出");
    }

    @Override
    public void close() {
        manager.close();
    }
}
