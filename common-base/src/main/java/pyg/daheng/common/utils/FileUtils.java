package pyg.daheng.common.utils;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

/**
 * @Description:
 * @ClassName: FileUtils
 * @Author: ZhanSSH
 * @Date: 2021/1/25 14:48
 */
public class FileUtils {
    public static InputStream base64ToInputStream(String base64) {
        try {
            if (base64.contains(",")) {
                String[] baseStrs = base64.split(",");
                base64 = baseStrs[1];
            }

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(base64);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, base64).getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MultipartFile base64ToMultipartFile(String base64) {
        try {
            if (base64.contains(",")) {
                String[] baseStrs = base64.split(",");
                base64 = baseStrs[1];
            }

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(base64);

            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, base64);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
