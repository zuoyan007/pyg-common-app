package pyg.daheng.base.util.invoke.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ZhanSSH
 * @date 2021/2/5 16:13
 */
public class DXBase62 {
    // 系统日志对象
    private static Logger logger = LoggerFactory.getLogger(DXBase62.class);
    private static char DXDIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z' };
    private static String DXDIGIT_STR = String.valueOf(DXDIGITS);
    private static Long[] SLOTS = { 56800235584L, 916132832L, 14776336L, 238328L, 3844L, 62L, 1L };
    private static Long[] REVERSE_SLOTS = { 1L, 62L, 3844L, 238328L, 14776336L, 916132832L, 56800235584L };

    /**
     * 62进制转10进制，不支持小数点，只能正整数
     *
     * @param input
     * @return
     */
    public static String dxDigits2Dec(String input) {
        if (input == null || input.length() <= 0) {
            return null;
        }
        long ret = 0L;
        int step = 0;
        String reverseInput = new StringBuffer(input).reverse().toString();
        for (String c : reverseInput.split("")) { // 从个位数开始计算
            int index = DXDIGIT_STR.indexOf(c);
            if (index <= -1) {
                return null;
            } else {
                ret += index * REVERSE_SLOTS[step];
            }
            step++;
        }
        return String.valueOf(ret);
    }

    /**
     * 10十进制转62进制，不支持小数点，只能正整数
     * https://www.freebuf.com/articles/others-articles/162484.html
     *
     * @param input
     * @return
     */
    public static String dec2DxDigits(String input) {
        long remainder = -1;
        StringBuffer sb = new StringBuffer();
        long sourceNum = Long.valueOf(input);
        long decimalNum = sourceNum;
        if (decimalNum > SLOTS[0]) {
            logger.error("number out of bound!");
            return null;
        }
        else if (decimalNum == 0) {
            return "0";
        }
        else if (decimalNum < 0) {
            logger.error("negative number not accecpted!");
            return null;
        }
        for (long power : SLOTS) {
            long divisor = 0;
            if (decimalNum >= power) {
                while (decimalNum >= power) { // 只用减法实现，只有一次运算，速度会更快
                    decimalNum = decimalNum - power;
                    divisor++;
                    if (decimalNum < power) {
                        sb.append(DXDIGITS[(int) divisor]);
                        break;
                    }
                }
            } else if (sb.toString().length() > 0) { // 头部除0，中间补0
                sb.append(DXDIGITS[0]);
            }
        }
        if (remainder > 0) {
            sb.append(DXDIGITS[(int) remainder]);
        }
        return sb.toString();
    }
}
