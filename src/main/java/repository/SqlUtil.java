package repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class SqlUtil<E> {
    protected final String tableName;
    protected final List<TableColumn> tableColumns;

    SqlUtil(Class<E> entityClass) {
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
                getKeyWheres());
    }

    String update() {
        return String.format("UPDATE %s SET %s",
                tableName,
                getAllSets());
    }

    String updateByEntity() {
        return String.format("UPDATE %s SET %s WHERE %s",
                tableName,
                getValuesSets(),
                getKeyWheres());
    }

    String replaceByEntity() {
        return String.format("UPDATE %s SET %s WHERE %s",
                tableName,
                getValuesSets(),
                getAllWheres());
    }

    String deleteAll() {
        return String.format("DELETE FROM %s", tableName);
    }

    String deleteByKeys() {
        return String.format("%s WHERE %s",
                deleteAll(),
                getKeyWheres());
    }

    protected String getJoinedColumns() {
        return tableColumns.stream()
                .map(TableColumn::getColumnName)
                .collect(Collectors.joining(", "));
    }

    protected String getPlaceholders() {
        return String.join(", ", Collections.nCopies(tableColumns.size(), "?"));
    }

    protected String getValuesSets() {
        return tableColumns.stream()
                .filter(column -> !column.isKey())
                .map(TableColumn::getColumnName)
                .map(col -> col + "=?")
                .collect(Collectors.joining(", "));
    }

    protected String getAllSets() {
        return tableColumns.stream()
                .map(TableColumn::getColumnName)
                .map(col -> col + "=?")
                .collect(Collectors.joining(", "));
    }

    protected String getKeyWheres() {
        return tableColumns.stream()
                .filter(TableColumn::isKey)
                .map(TableColumn::getColumnName)
                .map(col -> col + "=?")
                .collect(Collectors.joining(" AND "));
    }

    protected String getAllWheres() {
        return tableColumns.stream()
                .map(TableColumn::getColumnName)
                .map(col -> col + "=?")
                .collect(Collectors.joining(" AND "));
    }
}
