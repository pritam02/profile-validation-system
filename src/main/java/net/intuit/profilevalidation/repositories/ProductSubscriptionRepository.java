package net.intuit.profilevalidation.repositories;

import lombok.extern.slf4j.Slf4j;
import net.intuit.profilevalidation.database.DatabaseClientBean;
import net.intuit.profilevalidation.database.DbResult;
import net.intuit.profilevalidation.database.DbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Slf4j
@Repository
public class ProductSubscriptionRepository {
    @Autowired
    private DatabaseClientBean databaseClientBean;
    @Autowired
    private DbUtil dbUtil;
    public boolean subscribeToProduct(String userEmail, int productId) {
        String sql = "INSERT INTO product_subscriptions (email, product_id)"
                + "VALUES(?, ?)";
        DbResult dbResult = new DbResult();
        boolean success = true;
        try {
            Connection con = databaseClientBean.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, userEmail);
            preparedStatement.setInt(2, productId);
            dbResult.setConnection(con);
            dbResult.setPreparedStatement(preparedStatement);
            dbUtil.update(dbResult);
            if (dbResult.getRowsAffected() < 1) {
                success = false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            success = false;
        } finally {
            dbResult.close();
        }
        return success;
    }
}
