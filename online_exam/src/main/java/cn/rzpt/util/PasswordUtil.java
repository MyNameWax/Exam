package cn.rzpt.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {


    public static String encode(String password) {
        return BCrypt.hashpw(password,BCrypt.gensalt(12));
    }

    public static void main(String[] args) {
        System.out.println(encode("123456"));
    }

    /**
     * 验证密码
     * @param rawPassword 明文密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

}
