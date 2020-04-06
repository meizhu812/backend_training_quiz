package parking;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Regex {
    MainOption("[1234]", "选项"),
    InitRegex("A:(?<countA>\\d{1,2}),B:(?<countB>\\d{1,2})", "初始化数据");

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

    public Optional<Matcher> getMatcher(String target) {
        Matcher matcher = pattern.matcher(target);
        if (matcher.matches()) {
            return Optional.of(matcher);
        } else {
            return Optional.empty();
        }
    }
}
