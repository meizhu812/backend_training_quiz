package parking.exceptions;

public class InvalidInput extends Exception {
    public InvalidInput(String name) {
        super(String.format("输入错误！ -> 不合法的%s\n",name));
    }
}
