package net.intuit.profilevalidation.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.intuit.profilevalidation.cache.CacheUtil;
import net.intuit.profilevalidation.constants.ProductValidationColumns;
import net.intuit.profilevalidation.constants.ValidationStatus;
import net.intuit.profilevalidation.database.DatabaseClientBean;
import net.intuit.profilevalidation.database.DbParams;
import net.intuit.profilevalidation.database.DbResult;
import net.intuit.profilevalidation.database.DbUtil;
import net.intuit.profilevalidation.models.Product;
import net.intuit.profilevalidation.models.ProductValidation;
import net.intuit.profilevalidation.utils.CacheKeyUtil;
import net.intuit.profilevalidation.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class ProductValidationRepository {
    @Autowired
    private DatabaseClientBean databaseClientBean;
    @Autowired
    private DbUtil dbUtil;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

    private void addValidationDataForUserAndProductToCache(String userEmail, int product_id, String status) {
        String cacheKey = CacheKeyUtil.getCacheKeyForProfileValidationStatus(userEmail);
        String validationDataInString = cacheUtil.get(cacheKey);
        Map<Integer, ProductValidation> productValidationMap;
        if (validationDataInString == null) {
            productValidationMap = new HashMap<>();
        } else {
            TypeReference<Map<Integer, ProductValidation>> typeRef = new TypeReference<Map<Integer, ProductValidation>>() {
            };
            try {
                productValidationMap = objectMapper.readValue(validationDataInString, typeRef);
            } catch (Exception e) {
                log.error(e.getMessage());
                productValidationMap = new HashMap<>();
            }
        }
        ProductValidation productValidation = new ProductValidation();
        Product product = productRepository.getProductById(product_id);
        productValidation.setProductId(product_id);
        productValidation.setProductName(product.getName());
        productValidation.setValidationStatus(status);
        productValidationMap.put(product_id, productValidation);
        cacheUtil.set(cacheKey, Util.getStringFromJson(productValidationMap));
    }

    private void resetValidationDataForUserInCache(String userEmail) {
        String cacheKey = CacheKeyUtil.getCacheKeyForProfileValidationStatus(userEmail);
        String validationDataInString = cacheUtil.get(cacheKey);
        if (validationDataInString == null) {
            return;
        }
        Map<Integer, ProductValidation> productValidationMap;
        try {
            productValidationMap = objectMapper.readValue(validationDataInString, new TypeReference<Map<Integer, ProductValidation>>(){});
        } catch (Exception e) {
            log.error(e.getMessage());
            productValidationMap = new HashMap<>();
        }
        for (Integer productId : productValidationMap.keySet()) {
            ProductValidation productValidation = productValidationMap.get(productId);
            productValidation.setValidationStatus(ValidationStatus.IN_PROGRESS);
            productValidationMap.put(productId, productValidation);
        }
        cacheUtil.set(cacheKey, Util.getStringFromJson(productValidationMap));
    }
    private List<ProductValidation> getValidationDataForUserInCache(String userEmail) {
        String cacheKey = CacheKeyUtil.getCacheKeyForProfileValidationStatus(userEmail);
        String validationDataInString = cacheUtil.get(cacheKey);
        if (validationDataInString == null) {
            return null;
        }
        Map<Integer, ProductValidation> productValidationMap = null;
        try {
            productValidationMap = objectMapper.readValue(validationDataInString, new TypeReference<Map<Integer, ProductValidation>>(){});
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (productValidationMap == null) {
            return null;
        }
        List<ProductValidation> productValidations = new ArrayList<>();
        for (Integer productId : productValidationMap.keySet()) {
            ProductValidation productValidation = productValidationMap.get(productId);
            productValidations.add(productValidation);
        }
        return productValidations;
    }

    public boolean initialiseValidationForUserAndProduct(String userEmail, int product_id) {
        String sql = "INSERT INTO product_validation (profile_id, product_id, status)"
                + "VALUES(?, ?, ?)";
        DbResult dbResult = new DbResult();
        boolean success = true;
        try {
            Connection con = databaseClientBean.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, userEmail);
            preparedStatement.setInt(2, product_id);
            preparedStatement.setString(3, ValidationStatus.IN_PROGRESS);
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
        if (success) {
            addValidationDataForUserAndProductToCache(userEmail, product_id, ValidationStatus.IN_PROGRESS);
        }
        return success;
    }

    public boolean resetValidationOfAllProductsForUser(String userEmail) {
        String sql = "UPDATE product_validation set status = ? "
                + "WHERE profile_id = ?";
        DbResult dbResult = new DbResult();
        boolean success = true;
        try {
            Connection con = databaseClientBean.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, ValidationStatus.IN_PROGRESS);
            preparedStatement.setString(2, userEmail);
            dbResult.setConnection(con);
            dbResult.setPreparedStatement(preparedStatement);
            dbUtil.update(dbResult);
        } catch (Exception e) {
            log.error(e.getMessage());
            success = false;
        } finally {
            dbResult.close();
        }
        if (success) {
            resetValidationDataForUserInCache(userEmail);
        }
        return success;
    }

    public boolean setValidationStatusForUserAndProduct(String userEmail, int product_id, String validationStatus) {
        String sql = "UPDATE product_validation set status = ? "
                + "WHERE profile_id = ? AND product_id = ?";
        DbResult dbResult = new DbResult();
        boolean success = true;
        try {
            Connection con = databaseClientBean.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, validationStatus);
            preparedStatement.setString(2, userEmail);
            preparedStatement.setInt(3, product_id);
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
        if (success) {
            addValidationDataForUserAndProductToCache(userEmail, product_id, validationStatus);
        }
        return success;
    }

    private ProductValidation getProductValidationStatusFromResultSet(ResultSet resultSet) {
        ProductValidation productValidation = new ProductValidation();
        try {
            productValidation.setProductId(resultSet.getInt(ProductValidationColumns.PRODUCT_ID));
            productValidation.setProductName(resultSet.getString(ProductValidationColumns.PRODUCT_NAME));
            productValidation.setValidationStatus(resultSet.getString(ProductValidationColumns.VALIDATION_STATUS));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return productValidation;

    }

    public List<ProductValidation> getValidationStatusForAllProducts(String userEmail) {
        List<ProductValidation> productValidationDataFromCache = getValidationDataForUserInCache(userEmail);
        if (productValidationDataFromCache != null) {
            log.info("product validation info came from cache");
            return productValidationDataFromCache;
        }
        List<ProductValidation> productValidationData = new ArrayList<>();
        String sql = "SELECT a.profile_id, a.product_id, b.product_name, a.status FROM product_validation a JOIN product b ON (a.product_id = b.ID AND a.profile_id = '%s')";
        sql = String.format(sql, userEmail);
        DbParams dbParams = new DbParams();
        dbParams.setSqlQuery(sql);
        DbResult dbResult = dbUtil.fetch(dbParams);
        if (dbResult == null) {
            return productValidationData;
        }
        try {
            ResultSet resultSet = dbResult.getResultSet();
            while (resultSet.next()) {
                ProductValidation productValidation = getProductValidationStatusFromResultSet(resultSet);
                if (productValidation != null) {
                    productValidationData.add(productValidation);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return productValidationData;
        }
        dbResult.close();
        return productValidationData;
    }
}
