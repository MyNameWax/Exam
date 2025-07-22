package cn.rzpt.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class SimpleKeyGenerator {

    // 配置参数
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成登录密钥
     * @param candidateId 考生ID
     * @return 返回格式：salt:key
     */
    public static String generateLoginKey(String candidateId) {
        try {
            // 1. 生成随机盐
            byte[] salt = generateSalt();

            // 2. 基于PBKDF2生成密钥
            char[] idChars = candidateId.toCharArray();
            PBEKeySpec spec = new PBEKeySpec(idChars, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            // 3. 组合返回结果
            return Base64.getEncoder().encodeToString(salt) + ":" +
                   Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("密钥生成失败", e);
        }
    }

    /**
     * 验证登录密钥
     * @param candidateId 考生ID
     * @param storedKey 存储的密钥（salt:key格式）
     * @return 是否验证通过
     */
    public static boolean validateLoginKey(String candidateId, String storedKey) {
        try {
            // 1. 解析存储的密钥
            String[] parts = storedKey.split(":");
            if (parts.length != 2) return false;

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            // 2. 重新计算密钥
            char[] idChars = candidateId.toCharArray();
            PBEKeySpec spec = new PBEKeySpec(idChars, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            // 3. 比较结果
            return slowEquals(expectedHash, testHash);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return salt;
    }

    // 防止时序攻击的比较方法
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
