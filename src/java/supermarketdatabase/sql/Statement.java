package supermarketdatabase.sql;

import java.util.function.Function;

public class Statement<R> {
    private final String statement;
    private final Function<MyQueryResult, R> returnValueMapper;

    public Statement(String statement, Function<MyQueryResult, R> returnValueMapper) {
        this.statement = statement;
        this.returnValueMapper = returnValueMapper;
    }

    public R execute(MyDatabaseConnector databaseConnector) {
        return returnValueMapper.apply(databaseConnector.execute(statement));
    }
}
