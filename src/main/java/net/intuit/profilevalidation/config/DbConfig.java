package net.intuit.profilevalidation.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.intuit.profilevalidation.database.DatabaseClientBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Configuration
@PropertySource("classpath:database.properties")
@ComponentScan(basePackages = "net.intuit.profilevalidation")
@Getter
@Setter
public class DbConfig {
    @Value("${mysql_driver}")
    private String driver;
    @Value("${mysql_username}")
    private String username;
    @Value("${mysql_password}")
    private String password;
    @Value("${mysql_min_pool_size}")
    private int minPoolSize;
    @Value("${mysql_max_pool_size}")
    private int maxPoolSize;
    @Value("${mysql_max_idle_time_excess_connections}")
    private int maxIdleTimeExcessConnections;
    @Value("${mysql_db_url}")
    private String dbConnectionUrl;

    @Bean
    public DatabaseClientBean databaseClientBean() {
        DatabaseClientBean databaseClientBean = new DatabaseClientBean(getDataSource());
        return databaseClientBean;
    }

    private ComboPooledDataSource getDataSource() {
        String dbUrl = this.dbConnectionUrl;
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(driver);
            dataSource.setJdbcUrl(dbUrl);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setMinPoolSize(minPoolSize);
            dataSource.setMaxPoolSize(maxPoolSize);
            dataSource.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
            dataSource.setMaxConnectionAge(4 * 60 * 60);
        } catch (Exception e) {
            log.error("Error Configuring Data Source", e);
        }
        return dataSource;
    }


}
