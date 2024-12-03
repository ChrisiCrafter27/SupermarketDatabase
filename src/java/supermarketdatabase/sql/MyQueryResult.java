package supermarketdatabase.sql;

import supermarketdatabase.sql.lib.QueryResult;

public class MyQueryResult {
    private final String error;
    private final QueryResult queryResult;

    public MyQueryResult(String error, QueryResult queryResult) {
        this.error = error;
        this.queryResult = queryResult;
    }

    public boolean succeeded() {
        return error == null;
    }

    public String gerError() {
        return error;
    }

    public int rows() {
        return queryResult != null ? queryResult.getRowCount() : 0;
    }

    public int columns() {
        return queryResult != null ? queryResult.getColumnCount() : 0;
    }

    public String[] getTitles() {
        return queryResult != null ? queryResult.getColumnNames() : new String[]{};
    }

    public String[][] getRows() {
        return queryResult != null ? queryResult.getData() : new String[][]{};
    }
}
