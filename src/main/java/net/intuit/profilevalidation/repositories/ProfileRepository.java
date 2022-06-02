package net.intuit.profilevalidation.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.intuit.profilevalidation.cache.CacheUtil;
import net.intuit.profilevalidation.constants.ProfileTableColumns;
import net.intuit.profilevalidation.database.DatabaseClientBean;
import net.intuit.profilevalidation.database.DbParams;
import net.intuit.profilevalidation.database.DbResult;
import net.intuit.profilevalidation.database.DbUtil;
import net.intuit.profilevalidation.models.Profile;
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
public class ProfileRepository {
    @Autowired
    private DatabaseClientBean databaseClientBean;
    @Autowired
    private DbUtil dbUtil;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private ObjectMapper objectMapper;
    private void saveProfileInCache(Profile businessProfile) {
        String cacheKey = CacheKeyUtil.getCacheKeyForProfile(businessProfile.getEmail());
        cacheUtil.set(cacheKey, Util.getStringFromJson(businessProfile));
    }
    private Profile getBusinessProfileFromCache(String email) {
        Profile businessProfile = null;
        String cacheKey = CacheKeyUtil.getCacheKeyForProfile(email);
        String profileJsonString = cacheUtil.get(cacheKey);
        if (profileJsonString == null) {
            return null;
        }
        try {
            businessProfile = objectMapper.readValue(profileJsonString, Profile.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return businessProfile;
    }
    public boolean createProfile(Profile businessProfile) {
        String sql = "INSERT INTO profile (email, company_name, legal_name, business_address, legal_address, tax_identifiers, website)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        DbResult dbResult = new DbResult();
        boolean success = true;
        try {
            Connection con = databaseClientBean.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, businessProfile.getEmail());
            preparedStatement.setString(2, businessProfile.getCompanyName());
            preparedStatement.setString(3, businessProfile.getLegalName());
            preparedStatement.setString(4, businessProfile.getBusinessAddress());
            preparedStatement.setString(5, businessProfile.getLegalAddress());
            preparedStatement.setString(6, businessProfile.getTaxIdentifiers());
            preparedStatement.setString(7, businessProfile.getWebsite());
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
            saveProfileInCache(businessProfile);
        }
        return success;
    }
    public boolean updateProfile(Profile businessProfile) {
        String sql = "UPDATE profile set company_name = ?, legal_name = ?, business_address = ?, legal_address = ?, tax_identifiers = ?, website = ? "
                + "WHERE email = ?";
        DbResult dbResult = new DbResult();
        boolean success = true;
        try {
            Connection con = databaseClientBean.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, businessProfile.getCompanyName());
            preparedStatement.setString(2, businessProfile.getLegalName());
            preparedStatement.setString(3, businessProfile.getBusinessAddress());
            preparedStatement.setString(4, businessProfile.getLegalAddress());
            preparedStatement.setString(5, businessProfile.getTaxIdentifiers());
            preparedStatement.setString(6, businessProfile.getWebsite());
            preparedStatement.setString(7, businessProfile.getEmail());
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
            saveProfileInCache(businessProfile);
        }
        return success;
    }
    public Profile getBusinessProfileFromResultSet(ResultSet resultSet) {
        Profile businessProfile = new Profile();
        try {
            businessProfile.setEmail(resultSet.getString(ProfileTableColumns.EMAIL));
            businessProfile.setCompanyName(resultSet.getString(ProfileTableColumns.COMPANY_NAME));
            businessProfile.setLegalName(resultSet.getString(ProfileTableColumns.LEGAL_NAME));
            businessProfile.setBusinessAddress(resultSet.getString(ProfileTableColumns.BUSINESS_ADDRESS));
            businessProfile.setLegalAddress(resultSet.getString(ProfileTableColumns.LEGAL_ADDRESS));
            businessProfile.setTaxIdentifiers(resultSet.getString(ProfileTableColumns.TAX_IDENTIFIERS));
            businessProfile.setWebsite(resultSet.getString(ProfileTableColumns.WEBSITE));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return businessProfile;

    }
    public Profile getBusinessProfile(String email) {
        Profile businessProfileFromCache  = getBusinessProfileFromCache(email);
        if (businessProfileFromCache != null) {
            log.info("found profile in cache");
            return businessProfileFromCache;
        }
        String sql = "SELECT * FROM profile WHERE email = '%s'";
        sql = String.format(sql, email);
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
        Profile businessProfile = getBusinessProfileFromResultSet(resultSet);
        dbResult.close();
        return businessProfile;
    }
    public List<Profile> getAllBusinessProfiles() {
        String sql = "SELECT * FROM profile";
        List<Profile> businessProfiles = new ArrayList<>();
        DbParams dbParams = new DbParams();
        dbParams.setSqlQuery(sql);
        DbResult dbResult = dbUtil.fetch(dbParams);
        if (dbResult == null) {
            return businessProfiles;
        }
        try {
            ResultSet resultSet = dbResult.getResultSet();
            while (resultSet.next()) {
                Profile businessProfile = getBusinessProfileFromResultSet(resultSet);
                if (businessProfile != null) {
                    businessProfiles.add(businessProfile);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return businessProfiles;
        }
        dbResult.close();
        return businessProfiles;
    }
}
