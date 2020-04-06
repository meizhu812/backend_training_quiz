package parking;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Regex {
    //language=RegExp
    MainOption("[1234]", "选项"),
    //language=RegExp
    InitRegex("A:(?<countA>\\d{1,2}),B:(?<countB>\\d{1,2})", "初始化数据"),
    //language=RegExp
    PlateNo("[A-Z][0-9]{5}", "车牌号"),
    //language=RegExp
    Ticket("(?<region>[AB]),(?<serial>\\d{1,2}),(?<plate>[A-Z][0-9]{5})","停车票");

    public String getName() {
        return name;
    }

    private final Pattern pattern;
    private final String name;

    Regex(String regex, String name) {
        pattern = Pattern.compile(regex);
        this.name = name;
    }

    public boolean matches(String target) {
        return pattern.matcher(target).matches();
    }

    public void validate(String string) throws InvalidInput {
        if (!matches(string)) {
            throw new InvalidInput(name);
        }
    }

    public Matcher getMatcher(String target) throws InvalidInput {
        Matcher matcher = pattern.matcher(target);
        if (matcher.matches()) {
            return matcher;
        } else {
            throw new InvalidInput(name);
        }
    }
}
