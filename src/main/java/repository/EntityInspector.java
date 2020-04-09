package repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class EntityInspector {
    private EntityInspector() {
    }

    static List<EntityField> getEntityFields(Class<?> entityClass) {
        boolean hasKey = false;
        List<EntityField> entityFields = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            boolean isKey = Optional.ofNullable(field.getAnnotation(Key.class)).isPresent();
            hasKey = isKey || hasKey;
            Method getter = getGetter(field, entityClass);
            Method setter = getSetter(field, entityClass);
            entityFields.add(new EntityField(isKey, getter, setter));
        }
        if (!hasKey) {
            throw new RuntimeException("Missing key field/column");
        }
        return entityFields;
    }

    static String getTableName(Class<?> entityClass) {
        return Optional.ofNullable(entityClass.getAnnotation(Table.class))
                .map(Table::value)
                .orElse(toSqlIdentifier(entityClass.getSimpleName()));
    }

    static List<TableColumn> getTableColumns(Class<?> entityClass) {
        List<TableColumn> tableColumns = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            boolean isKey = Optional.ofNullable(field.getAnnotation(Key.class)).isPresent();
            String columnName = Optional.ofNullable(field.getAnnotation(Column.class))
                    .map(Column::value)
                    .orElse(toSqlIdentifier(field.getName()));
            tableColumns.add(new TableColumn(isKey, columnName));
        }
        return tableColumns;
    }

    static Method getSetter(Field field, Class<?> entityClass) {
        String fieldName = field.getName();
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return entityClass.getMethod(setterName, Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Missing setter");
        }
    }

    static Method getGetter(Field field, Class<?> entityClass) {
        String prefix;
        if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
            prefix = "is";
        } else {
            prefix = "get";
        }
        String fieldName = field.getName();
        String getterName = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return entityClass.getMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Missing getter");
        }
    }

    private static String toSqlIdentifier(String javaIdentifier) {
        String result = javaIdentifier
                .chars()
                .mapToObj(c -> c > 44 && c < 91  // A-Z?
                        ? "_" + (char) (c + 32)  // -> _a-_z
                        : "" + (char) c
                ).collect(Collectors.joining());
        if (result.startsWith("_")) {
            return result.substring(1); // Clip first "_"
        }
        return result;
    }
}
