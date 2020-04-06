import parking.InvalidInput;
import parking.Regex;

import java.util.Scanner;

public class Application {
    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        operateParking();
    }

    public static void operateParking() {
        while (true) {
            try {
                String input = getValidInput("1. 初始化停车场数据\n2. 停车\n3. 取车\n4. 退出\n请输入你的选择(1~4)：", Regex.MainOption);
                if (input.equals("4")) {
                    System.out.println("系统已退出");
                    break;
                }
                handle(input);
            } catch (InvalidInput e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void handle(String choice) {
        switch (choice) {
            case "1":
                System.out.println("请输入初始化数据\n格式为\"停车场编号1：车位数,停车场编号2：车位数\" 如 \"A:8,B:9\"：");
                String initInfo = SC.next();
                init(initInfo);
                break;
            case "2": {
                System.out.println("请输入车牌号\n格式为\"车牌号\" 如: \"A12098\"：");
                String carInfo = SC.next();
                String ticket = park(carInfo);
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

    public static void init(String initInfo) {

    }

    public static String park(String carNumber) {
        return "";
    }

    public static String fetch(String ticket) {
        return "";
    }

    private static String getValidInput(String prompt, Regex regex) throws InvalidInput {
        System.out.println(prompt);
        String input = SC.next();
        regex.validate(input);
        return input;
    }
}
