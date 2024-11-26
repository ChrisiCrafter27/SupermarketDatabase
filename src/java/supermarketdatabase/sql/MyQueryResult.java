package supermarketdatabase.sql;

import supermarketdatabase.sql.lib.QueryResult;

public class MyQueryResult {
    private final String error;
    private final QueryResult queryResult;

    public MyQueryResult(String error, QueryResult queryResult) {
        this.error = error;
        this.queryResult = queryResult;
    }

    public boolean asFailed() {
        return error != null;
    }

    public String gerError() {
        return error;
    }

    public int rows() {
        return queryResult.getRowCount();
    }

    public int columns() {
        return queryResult.getColumnCount();
    }

    public String[] getTitles() {
        return queryResult.getColumnNames();
    }

    public String[][] getRows() {
        return queryResult.getData();
    }
}
