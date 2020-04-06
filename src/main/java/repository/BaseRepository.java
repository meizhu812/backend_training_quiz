package repository;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class BaseRepository<E> implements AutoCloseable {
    protected final EntityUtil<E> entityUtil;
    protected final SqlUtil<E> sqlUtil;
    protected Connection connection;

    @SuppressWarnings("unchecked")
    public BaseRepository() {
        Class<E> entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        entityUtil = new EntityUtil<>(entityClass);
        sqlUtil = new SqlUtil<>(entityClass);
    }

    public void close() {
        setConnection(null);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public final void init(List<E> entities) throws SQLException {
        try {
            deleteAll();
            saveAll(entities);
        } catch (ZeroAffected ignored) {
        }
    }

    public final int saveAll(List<E> entities) throws SQLException, ZeroAffected {
        int count = 0;
        for (E entity : entities) {
            count += save(entity);
        }
        return count;
    }

    public final int save(E entity) throws SQLException, ZeroAffected {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.insert())) {
            entityUtil.setInsertValues(statement, entity);
            return validateAffected(statement.executeUpdate());
        }
    }


    public final List<E> queryAll() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.queryAll());
             ResultSet resultSet = statement.executeQuery()) {
            return entityUtil.makeEntities(resultSet);
        }
    }

    public final Optional<E> queryByKeys(Object... keys) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.queryByKeys())) {
            entityUtil.setWhereValues(statement, keys);
            try (ResultSet resultSet = statement.executeQuery()) {
                return entityUtil.makeEntities(resultSet).stream().findFirst();
            }
        }
    }

    public final List<E> customQuery(String condition) throws SQLException {
        String sql = String.format("%s %s", sqlUtil.queryAll(), condition);
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return entityUtil.makeEntities(resultSet);
        }
    }

    public final Optional<E> customQueryFirst(String condition) throws SQLException {
        String sql = String.format("%s %s LIMIT 1", sqlUtil.queryAll(), condition);
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return entityUtil.makeEntities(resultSet).stream().findFirst();
        }
    }

    public final int updateByEntity(E newEntity) throws SQLException, ZeroAffected {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.updateByEntity())) {
            entityUtil.setUpdateValues(statement, newEntity);
            return validateAffected(statement.executeUpdate());
        }
    }

    public final int deleteAll() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.deleteAll())) {
            return statement.executeUpdate();
        }
    }

    public final int deleteByKeys(Object... keys) throws SQLException, ZeroAffected {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.deleteByKeys())) {
            return validateAffected(statement.executeUpdate());
        }
    }

    private int validateAffected(int result) throws ZeroAffected {
        if (result == 0) {
            throw new ZeroAffected();
        } else {
            return result;
        }
    }
}
