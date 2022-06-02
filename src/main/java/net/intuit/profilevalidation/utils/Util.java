package net.intuit.profilevalidation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Util {
    public static boolean isValidString(String str) {
        return str != null && !str.isEmpty();
    }
    public static String getCanonicalHash(String input) {
        return Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
    }
    public static String getStringFromJson(Object obj) {
        String result;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            result = "";
        }
        return result;
    }
}
