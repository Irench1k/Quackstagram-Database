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

    protected ResultSet executeQuery(String query, Object... params) throws Exception, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            setParameters(statement, params);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet;
            } else {
                return null;
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    protected List<ResultSet> executeQueryList(String query, Object... params) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<ResultSet> resultList = new ArrayList<>();

        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            setParameters(statement, params);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                resultList.add(resultSet);
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return resultList;
    }

    protected void executeUpdate(String query, Object... params) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(query);
            setParameters(statement, params);
            statement.executeUpdate();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
