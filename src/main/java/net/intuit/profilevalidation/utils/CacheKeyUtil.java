package net.intuit.profilevalidation.utils;

public class CacheKeyUtil {
    public static String getCacheKeyForProfile(String userEmail) {
        String prefix = "profile-";
        String key = prefix + userEmail;
        return Util.getCanonicalHash(key);
    }
    public static String getCacheKeyForProfileValidationStatus(String userEmail) {
        String prefix = "profile-validation-";
        String key = prefix + userEmail;
        return Util.getCanonicalHash(key);
    }
    public static String getCacheKeyForProduct(int productId) {
        String prefix = "profile-validation-";
        String key = prefix + productId;
        return Util.getCanonicalHash(key);
    }
}
