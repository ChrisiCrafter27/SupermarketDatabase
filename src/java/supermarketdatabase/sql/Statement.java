package supermarketdatabase.sql;

import java.util.Arrays;
import java.util.function.Function;

public class Statement<R> {
    private final String statement;
    private final Function<MyQueryResult, R> returnValueMapper;

    public Statement(String statement, Function<MyQueryResult, R> returnValueMapper) {
        this.statement = statement;
        this.returnValueMapper = returnValueMapper;
    }

    public R execute(MyDatabaseConnector databaseConnector) {
        MyQueryResult result = databaseConnector.execute(statement);
        if(result.succeeded()) {
            System.out.println(statement + " -> " + Arrays.deepToString(result.getRows()));
        } else {
            System.err.println(statement + " -> " + result.gerError());
        }
        return returnValueMapper.apply(result);
    }
}
