package utils;

import java.math.BigDecimal;

public class NumUtil {
    public static final BigDecimal FLOAT_MAX = new BigDecimal(Float.MAX_VALUE);
    public static final BigDecimal DOUBLE_MAX = new BigDecimal(Double.MAX_VALUE);
    public static final BigDecimal INTEGER_MAX = new BigDecimal(Integer.MAX_VALUE);
    public static final BigDecimal LONG_MAX = new BigDecimal(Long.MAX_VALUE);

    public static boolean isInRange(Number number, BigDecimal min,
                                    BigDecimal max) {
        try {
            BigDecimal bigDecimal = null;
            if (number instanceof Integer || number instanceof Long) {
                bigDecimal = new BigDecimal(number.longValue());
            } else if (number instanceof Float || number instanceof Double) {
                bigDecimal = new BigDecimal(number.doubleValue());
            }else{
                bigDecimal = new BigDecimal(number.doubleValue());
            }
            return max.compareTo(bigDecimal) >= 0
                    && min.compareTo(bigDecimal) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInRange2(BigDecimal number, BigDecimal min,
                                    BigDecimal max) {
        try {
            return max.compareTo(number) >= 0
                    && min.compareTo(number) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
