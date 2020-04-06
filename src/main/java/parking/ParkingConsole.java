package parking;

import parking.InvalidInput;
import parking.ParkingManager;
import parking.ParkingStatus;
import parking.Regex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.stream.IntStream;

public class ParkingConsole {
    private static final Scanner SC = new Scanner(System.in);
    public static final String MAIN_MENU_PROMPT = "1. 初始化停车场数据\n2. 停车\n3. 取车\n4. 退出\n请输入你的选择(1~4)：";
    private static final ParkingManager MANAGER = new ParkingManager();

    public void setConnection(Connection connection) {
        MANAGER.setConnection(connection);
    }

    public void operateParking() throws SQLException {
        while (true) {
            try {
                String input = getValidInput(MAIN_MENU_PROMPT, Regex.MainOption);
                if (input.equals("4")) {
                    System.out.println("系统已退出");
                    break;
                }
                handle(input);
            } catch (InvalidInput e) {
                System.out.println(e.getMessage());
            }
        }
        setConnection(null);
    }

    private void handle(String choice) throws SQLException, InvalidInput {
        switch (choice) {
            case "1":
                while (true) {
                    try {
                        String input = getValidInput("请输入初始化数据\n格式为\"停车场编号1：车位数,停车场编号2：车位数\" 如 \"A:8,B:9\"：", Regex.InitRegex);
                        init(input);
                        System.out.println("停车位初始化成功！");
                        break;
                    } catch (InvalidInput e) {
                        System.out.println(e.getMessage());
                    }
                }
                break;
            case "2": {
                String input = getValidInput("请输入车牌号\n格式为\"车牌号\" 如: \"A12098\"：", Regex.PlateNo);
                String ticket = park(input);
                String[] ticketDetails = ticket.split(",");
                System.out.format("已将您的车牌号为%s的车辆停到%s停车场%s号车位，停车券为：%s，请您妥善保存。\n", ticketDetails[2], ticketDetails[0], ticketDetails[1], ticket);
                break;
            }
            case "3": {
                System.out.println("请输入停车券信息\n格式为\"停车场编号1,车位编号,车牌号\" 如 \"A,1,8\"：");
                String ticket = SC.next();
                String car = fetch(ticket);
                System.out.format("已为您取到车牌号为%s的车辆，很高兴为您服务，祝您生活愉快!\n", car);
                break;
            }
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void init(String initInfo) throws SQLException {
        Matcher matcher = Regex.InitRegex.getMatcher(initInfo).get();
        int countA = Integer.parseInt(matcher.group("countA"));
        int countB = Integer.parseInt(matcher.group("countB"));
        List<ParkingStatus> initPlaces = new ArrayList<>();
        IntStream.rangeClosed(1, countA)
                .mapToObj(n -> new ParkingStatus("A", n, null))
                .forEach(initPlaces::add);
        IntStream.rangeClosed(1, countB)
                .mapToObj(n -> new ParkingStatus("B", n, null))
                .forEach(initPlaces::add);
        MANAGER.initParkingPlaces(initPlaces);
    }

    public String park(String carNumber) {
        return "";
    }

    public String fetch(String ticket) {
        return "";
    }

    private String getValidInput(String prompt, Regex regex) throws InvalidInput {
        System.out.println(prompt);
        String input = SC.next();
        regex.validate(input);
        return input;
    }
}
