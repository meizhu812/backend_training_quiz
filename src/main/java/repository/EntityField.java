package repository;

import java.lang.reflect.Method;

final class EntityField {
    private final boolean isKey;
    private final Method setter;
    private final Method getter;

    EntityField(boolean isKey, Method getter, Method setter) {
        this.isKey = isKey;
        this.getter = getter;
        this.setter = setter;
    }

    boolean isKey() {
        return isKey;
    }

    Method getGetter() {
        return getter;
    }

    Method getSetter() {
        return setter;
    }
}
