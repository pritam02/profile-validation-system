package net.intuit.profilevalidation.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Slf4j
@Component
public class DbUtil {
    @Autowired
    private DatabaseClientBean databaseClientBean;
    public DbResult update(DbResult dbResult) {
        try {
            int rowsChanged = dbResult.getPreparedStatement().executeUpdate();
            dbResult.setRowsAffected(rowsChanged);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return dbResult;
    }
    public DbResult fetch(DbParams params) {

        //db util
        DbResult dbResult = new DbResult();
        try {
            String sp = params.getSqlQuery();
            Connection connection = databaseClientBean.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sp);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            dbResult.setResultSet(resultSet);
            dbResult.setPreparedStatement(preparedStatement);
            dbResult.setConnection(connection);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return dbResult;
    }
}
