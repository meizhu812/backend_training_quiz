package parking;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParkingConsole {
    private final Iterator<String> in;
    public static final String MAIN_MENU_PROMPT = "1. 初始化停车场数据\n2. 停车\n3. 取车\n4. 退出\n请输入你的选择(1~4)：";
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
        while (true) {
            try {
                String input = getValidInput(MAIN_MENU_PROMPT, Regex.MainOption);
                handle(input);
            } catch (InvalidInput e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handle(String choice) throws SQLException, InvalidInput {
        switch (choice) {
            case "1":
                while (true) {
                    try {
                        init(getInput("请输入初始化数据\n格式为\"停车场编号1：车位数,停车场编号2：车位数\" 如 \"A:8,B:9\"："));
                        System.out.println("停车位初始化成功！");
                        break;
                    } catch (InvalidInput e) {
                        System.out.println(e.getMessage());
                    }
                }
                break;
            case "2": {
                String ticket = park(getValidInput("请输入车牌号\n格式为\"车牌号\" 如: \"A12098\"：", Regex.PlateNo));
                String[] ticketDetails = ticket.split(",");
                System.out.format("已将您的车牌号为%s的车辆停到%s停车场%s号车位，停车券为：%s，请您妥善保存。\n", ticketDetails[2], ticketDetails[0], ticketDetails[1], ticket);
                break;
            }
            case "3": {
                String car = fetch(getInput("请输入停车券信息\n格式为\"停车场编号1,车位编号,车牌号\" 如 \"A,1,8\"："));
                System.out.format("已为您取到车牌号为%s的车辆，很高兴为您服务，祝您生活愉快!\n", car);
                break;
            }
            case "4": {
                System.out.println("系统已退出");
                setConnection(null);
                break;
            }
        }
    }

    public void init(String initInfo) throws SQLException, InvalidInput {
        Matcher matcher = Regex.InitRegex.getMatcher(initInfo);
        List<ParkingStatus> initPlaces = Stream.concat(
                IntStream.rangeClosed(1, Integer.parseInt(matcher.group("countA")))
                        .mapToObj(n -> new ParkingStatus("A", n, null)),
                IntStream.rangeClosed(1, Integer.parseInt(matcher.group("countB")))
                        .mapToObj(n -> new ParkingStatus("B", n, null)))
                .collect(Collectors.toList());
        manager.initParkingPlaces(initPlaces);
    }

    public String park(String carNumber) throws SQLException {
        ParkingStatus ticket = manager.parkCar(carNumber);
        return String.format("%s,%d,%s", ticket.getRegion(), ticket.getSerial(), ticket.getPlateNo());
    }

    public String fetch(String ticket) throws SQLException, InvalidInput {
        Matcher matcher = Regex.Ticket.getMatcher(ticket);
        ParkingStatus parkingTicket = new ParkingStatus(
                matcher.group("region"),
                Integer.parseInt(matcher.group("serial")),
                matcher.group("plate"));
        return manager.fetchCar(parkingTicket);
    }

    private String getValidInput(String prompt, Regex regex) throws InvalidInput {
        System.out.println(prompt);
        String input = in.next();
        regex.validate(input);
        return input;
    }

    private String getInput(String prompt) {
        System.out.println(prompt);
        return in.next();
    }
}
