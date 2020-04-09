package repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class EntityUtil<E> {
    protected final List<EntityField> entityFields;
    protected final Class<E> entityClass;

    EntityUtil(Class<E> entityClass) {
        this.entityClass = entityClass;
        entityFields = EntityInspector.getEntityFields(entityClass);
    }

    void setAll(PreparedStatement statement, E entity) throws SQLException {
        List<Method> allGetters = entityFields.stream()
                .map(EntityField::getGetter)
                .collect(Collectors.toList());
        setValues(statement, entity, allGetters);
    }

    void setUpdateByE(PreparedStatement statement, E entity) throws SQLException {
        List<Method> updateGetters = entityFields.stream()
                .sorted((f1, f2) -> Boolean.compare(f1.isKey(), f2.isKey()))
                .map(EntityField::getGetter)
                .collect(Collectors.toList());
        setValues(statement, entity, updateGetters);
    }

    void setReplaceByE(PreparedStatement statement, E oldEntity, E newEntity) throws SQLException {
        List<Method> newGetters = entityFields.stream()
                .filter(field -> !field.isKey())
                .map(EntityField::getGetter)
                .collect(Collectors.toList());
        List<Method> oldGetters = entityFields.stream()
                .map(EntityField::getGetter)
                .collect(Collectors.toList());
        setValues(statement, newEntity, newGetters);
        setValues(statement, oldEntity, oldGetters, newGetters.size());
    }

    void setKeyWheres(PreparedStatement statement, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
    }

    List<E> makeEntities(ResultSet resultSet) throws SQLException {
        List<E> resultList = new ArrayList<>();
        while (resultSet.next()) {
            resultList.add(makeEntity(resultSet));
        }
        return resultList;
    }

    E makeEntity(ResultSet resultSet) throws SQLException {
        E entity = getEmptyEntity();
        for (int i = 0, len = entityFields.size(); i < len; i++) {
            try {
                entityFields.get(i).getSetter().invoke(entity, resultSet.getObject(i + 1));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return entity;
    }

    E getEmptyEntity() {
        try {
            return entityClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void setValues(PreparedStatement statement, E entity, List<Method> getters) throws SQLException {
        setValues(statement, entity, getters, 0);
    }

    private void setValues(PreparedStatement statement, E entity, List<Method> getters, int offset) throws SQLException {
        for (int i = 0; i < getters.size(); i++) {
            try {
                Object data = getters.get(i).invoke(entity);
                statement.setObject(i + offset + 1, data);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
