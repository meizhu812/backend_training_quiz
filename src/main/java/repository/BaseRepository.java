package repository;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
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
        deleteAll();
        saveAll(entities);
    }

    public final void saveAll(List<E> entities) throws SQLException {
        for (E entity : entities) {
            save(entity);
        }
    }

    public final void save(E entity) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.insert())) {
            entityUtil.setAll(statement, entity);
            statement.executeUpdate();
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
            entityUtil.setKeyWheres(statement, keys);
            try (ResultSet resultSet = statement.executeQuery()) {
                return entityUtil.makeEntities(resultSet).stream().findFirst();
            }
        }
    }

    public final List<E> query(String condition) throws SQLException {
        String sql = String.format("%s %s", sqlUtil.queryAll(), condition);
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return entityUtil.makeEntities(resultSet);
        }
    }

    public final Optional<E> queryFirst(String condition) throws SQLException {
        String sql = String.format("%s %s LIMIT 1", sqlUtil.queryAll(), condition);
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return entityUtil.makeEntities(resultSet).stream().findFirst();
        }
    }

    public final void updateByEntity(E newEntity) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.updateByEntity())) {
            entityUtil.setUpdateByE(statement, newEntity);
            statement.executeUpdate();
        }
    }

    public final int replaceByEntity(E oldEntity, E newEntity) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.replaceByEntity())) {
            entityUtil.setReplaceByE(statement, oldEntity, newEntity);
            return statement.executeUpdate();
        }
    }

    public final void update(E newEntity, String condition) throws SQLException {
        String sql = String.format("%s %s", sqlUtil.update(), condition);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            entityUtil.setAll(statement, newEntity);
            statement.executeUpdate();
        }
    }

    public final void deleteAll() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.deleteAll())) {
            statement.executeUpdate();
        }
    }

    public final void deleteByKeys(Object... keys) throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement(sqlUtil.deleteByKeys())) {
            statement.executeUpdate();
        }
    }
}
