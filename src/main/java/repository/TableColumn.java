package repository;

public class TableColumn {
    private boolean isKey;
    private String columnName;

    public TableColumn(boolean isKey, String columnName) {
        this.isKey = isKey;
        this.columnName = columnName;
    }

    public boolean isKey() {
        return isKey;
    }

    public String getColumnName() {
        return columnName;
    }
}
