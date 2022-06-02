package net.intuit.profilevalidation.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.intuit.profilevalidation.cache.CacheUtil;
import net.intuit.profilevalidation.constants.ProductTableColumns;
import net.intuit.profilevalidation.database.DatabaseClientBean;
import net.intuit.profilevalidation.database.DbParams;
import net.intuit.profilevalidation.database.DbResult;
import net.intuit.profilevalidation.database.DbUtil;
import net.intuit.profilevalidation.models.Product;
import net.intuit.profilevalidation.utils.CacheKeyUtil;
import net.intuit.profilevalidation.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ProductRepository {
    @Autowired
    private DatabaseClientBean databaseClientBean;
    @Autowired
    private DbUtil dbUtil;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private ObjectMapper objectMapper;
    public boolean insertProduct(String productName) {
        String sql = "INSERT INTO product (product_name)"
                + "VALUES(?)";
        DbResult dbResult = new DbResult();
        boolean success = true;
        try {
            Connection con = databaseClientBean.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, productName);
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
    public Product getProductFromResultSet(ResultSet resultSet) {
        Product product = new Product();
        try {
            product.setId(resultSet.getInt(ProductTableColumns.ID));
            product.setName(resultSet.getString(ProductTableColumns.PRODUCT_NAME));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return product;

    }
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        List<Product> products = new ArrayList<>();
        DbParams dbParams = new DbParams();
        dbParams.setSqlQuery(sql);
        DbResult dbResult = dbUtil.fetch(dbParams);
        if (dbResult == null) {
            return products;
        }
        try {
            ResultSet resultSet = dbResult.getResultSet();
            while (resultSet.next()) {
                Product product = getProductFromResultSet(resultSet);
                if (product != null) {
                    products.add(product);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return products;
        }
        dbResult.close();
        return products;
    }
    private Product getProductFromCache(int productId) {
        String cacheKey = CacheKeyUtil.getCacheKeyForProduct(productId);
        String productStringFromCache = cacheUtil.get(cacheKey);
        if (productStringFromCache == null) {
            return null;
        }
        Product product = null;
        try {
            product = objectMapper.readValue(productStringFromCache, Product.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return product;
    }
    private void storeProductInCache(Product product) {
        if (product == null) {
            return;
        }
        String cacheKey = CacheKeyUtil.getCacheKeyForProduct(product.getId());
        cacheUtil.set(cacheKey, Util.getStringFromJson(product));
    }
    public Product getProductById(int productId) {
        Product productFromCache = getProductFromCache(productId);
        if (productFromCache != null) {
            return productFromCache;
        }
        String sql = "SELECT * FROM product WHERE ID = %d";
        sql = String.format(sql, productId);
        DbParams dbParams = new DbParams();
        dbParams.setSqlQuery(sql);
        DbResult dbResult = dbUtil.fetch(dbParams);
        if (dbResult == null) {
            return null;
        }
        ResultSet resultSet = dbResult.getResultSet();
        try {
            resultSet.next();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        Product product = getProductFromResultSet(resultSet);
        dbResult.close();
        storeProductInCache(product);
        return product;
    }
}
