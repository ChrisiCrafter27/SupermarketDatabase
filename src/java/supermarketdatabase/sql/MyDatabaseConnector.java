package supermarketdatabase.sql;

import supermarketdatabase.sql.lib.DatabaseConnector;

import java.nio.file.Path;

public class MyDatabaseConnector {
    private final DatabaseConnector connector;
    private final Path path;

    public MyDatabaseConnector(Path path) {
        this.connector = new DatabaseConnector(null, -1, path.toString(), null, null);
        this.path = path;
    }

    public MyQueryResult execute(String statement) {
        connector.executeStatement(statement);
        return new MyQueryResult(connector.getErrorMessage(), connector.getCurrentQueryResult());
    }

    public Path getPath() {
        return path;
    }
}
