package net.intuit.profilevalidation.database;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Getter
@Setter
@Slf4j
public class DbResult {
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private Connection connection;
    private int rowsAffected;
    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
