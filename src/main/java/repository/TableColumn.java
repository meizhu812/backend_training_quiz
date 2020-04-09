package repository;

final class TableColumn {
    private final boolean isKey;
    private final String columnName;

    TableColumn(boolean isKey, String columnName) {
        this.isKey = isKey;
        this.columnName = columnName;
    }

    boolean isKey() {
        return isKey;
    }

    String getColumnName() {
        return columnName;
    }
}
