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

    public String[][] getRows() {
        return queryResult != null ? queryResult.getData() : new String[][]{};
    }
}
