package cn.rzpt.util;

public class LuhnUtil {

    public static char calculateCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (char) ((10 - (sum % 10)) % 10 + '0');
    }

    public static boolean validateCheckDigit(String number, char checkDigit) {
        return calculateCheckDigit(number) == checkDigit;
    }
}
