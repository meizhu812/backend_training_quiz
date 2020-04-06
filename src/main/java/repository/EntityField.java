package repository;

import java.lang.reflect.Method;

public class EntityField {
    private boolean isKey;
    private Method setter;
    private Method getter;

    public EntityField(boolean isKey, Method getter, Method setter) {
        this.isKey = isKey;
        this.getter = getter;
        this.setter = setter;
    }

    public boolean isKey() {
        return isKey;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }
}
