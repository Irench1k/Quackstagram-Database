package quackstagram.utilities;

import quackstagram.utilities.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class BaseRepository {

    protected Connection getConnection() throws SQLException, ClassNotFoundException {
        return DatabaseConnector.connect();
    }

    protected void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
        DatabaseConnector.close(connection, statement, resultSet);
    }

    protected ResultSet executeQuery(Connection connection, PreparedStatement statement, String query, Object... params) throws Exception {
        statement = connection.prepareStatement(query);
        setParameters(statement, params);
        return statement.executeQuery();
    }

    protected List<ResultSet> executeQueryList(Connection connection, PreparedStatement statement, String query, Object... params) throws Exception {
        List<ResultSet> resultList = new ArrayList<>();
        statement = connection.prepareStatement(query);
        setParameters(statement, params);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            resultList.add(resultSet);
        }

        return resultList;
    }

    protected void executeUpdate(Connection connection, PreparedStatement statement, String query, Object... params) throws Exception {
        statement = connection.prepareStatement(query);
        setParameters(statement, params);
        statement.executeUpdate();
    }

    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
