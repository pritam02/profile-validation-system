package net.intuit.profilevalidation.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.sql.Connection;

@Slf4j
public class DatabaseClientBean {
    private ComboPooledDataSource dataSource;
    public DatabaseClientBean(ComboPooledDataSource dataSource) {
        this.dataSource = dataSource;
    }
    public Connection getConnection() {
        try {
            Connection connection = this.dataSource.getConnection();
            return connection;
        } catch (Exception e) {
            log.error("Error in getting connection", e);
        }
        return null;
    }
    @PreDestroy
    private void closeClient() {
        try {
            this.dataSource.close();
        } catch (Exception e) {
            log.error("Error in closing database connection", e);
        }
    }

}
