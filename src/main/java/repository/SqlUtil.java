package repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SqlUtil<E> {
    protected final String tableName;
    protected final List<TableColumn> tableColumns;

    public SqlUtil(Class<E> entityClass) {
        tableName = EntityInspector.getTableName(entityClass);
        tableColumns = EntityInspector.getTableColumns(entityClass);
    }

    String insert() {
        return String.format("INSERT INTO %s(%s) VALUE (%s)",
                tableName,
                getJoinedColumns(),
                getPlaceholders());
    }

    String queryAll() {
        return String.format("SELECT %s FROM %s",
                getJoinedColumns(),
                tableName);
    }

    String queryByKeys() {
        return String.format("%s WHERE %s",
                queryAll(),
                getWhereKeyColumns());
    }

    String update() {
        return String.format("UPDATE %s SET %s",
                tableName,
                getSetAllColumns());
    }

    String updateByEntity() {
        return String.format("UPDATE %s SET %s WHERE %s",
                tableName,
                getSetValuesColumns(),
                getWhereKeyColumns());
    }

    String replaceByEntity() {
        return String.format("UPDATE %s SET %s WHERE %s",
                tableName,
                getSetValuesColumns(),
                getWhereAllColumns());
    }

    String deleteAll() {
        return String.format("DELETE FROM %s", tableName);
    }

    String deleteByKeys() {
        return String.format("%s WHERE %s",
                deleteAll(),
                getWhereKeyColumns());
    }

    protected String getJoinedColumns() {
        return tableColumns.stream()
                .map(TableColumn::getColumnName)
                .collect(Collectors.joining(", "));
    }

    public String getPlaceholders() {
        return String.join(", ", Collections.nCopies(tableColumns.size(), "?"));
    }

    public String getSetValuesColumns() {
        return tableColumns.stream()
                .filter(column -> !column.isKey())
                .map(TableColumn::getColumnName)
                .map(col -> col + "=?")
                .collect(Collectors.joining(", "));
    }

    public String getSetAllColumns() {
        return tableColumns.stream()
                .map(TableColumn::getColumnName)
                .map(col -> col + "=?")
                .collect(Collectors.joining(", "));
    }

    public String getWhereKeyColumns() {
        return tableColumns.stream()
                .filter(TableColumn::isKey)
                .map(TableColumn::getColumnName)
                .map(col -> col + "=?")
                .collect(Collectors.joining(" AND "));
    }

    public String getWhereAllColumns() {
        return tableColumns.stream()
                .map(TableColumn::getColumnName)
                .map(col -> col + "=?")
                .collect(Collectors.joining(" AND "));
    }
}

