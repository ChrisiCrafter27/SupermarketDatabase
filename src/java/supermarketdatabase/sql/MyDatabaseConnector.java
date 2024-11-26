package supermarketdatabase.sql;

import supermarketdatabase.sql.lib.DatabaseConnector;

public class MyDatabaseConnector {
    private final DatabaseConnector connector;
    private final String path;

    public MyDatabaseConnector(String path) {
        this.connector = new DatabaseConnector(null, -1, path, null, null);
        this.path = path;
    }

    public MyQueryResult execute(String statement) {
        connector.executeStatement(statement);
        return new MyQueryResult(connector.getErrorMessage(), connector.getCurrentQueryResult());
    }

    public String getPath() {
        return path;
    }
}
