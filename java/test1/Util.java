import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Util {
    /**
     * 计算数据的 SHA-256 值，并以 16 进制字符串返回
     */
    public static String sha256Hex(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 算法不可用", e);
        }
    }

    /**
     * 对字符串进行 Base64 编码
     */
    public static String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    /**
     * 根据传入的 SHA-256 字符串生成格式为 XXXX-XXXX-XXXX-XXXX 的密钥。
     * 为增强复杂性，采用多轮 SHA-256 运算与固定盐值进行混合运算。
     */
    public static String generateKey(String hash) {
        // 固定盐值
        String salt = "S0m3R@nd0mS@lt";
        // 分别取原 hash 的不同部分与盐值混合，再做二次 SHA-256 处理
        String part1 = sha256Hex((hash.substring(0, 8) + salt).getBytes());
        String part2 = sha256Hex((hash.substring(8, 16) + salt).getBytes());
        String part3 = sha256Hex((hash.substring(16, 24) + salt).getBytes());
        String part4 = sha256Hex((hash.substring(24, 32) + salt).getBytes());

        // 对每部分再进行多轮运算以增加复杂度
        String group1 = sha256Hex((sha256Hex(part1.getBytes()) + salt).getBytes()).substring(0, 4);
        String group2 = sha256Hex((sha256Hex(part2.getBytes()) + salt).getBytes()).substring(0, 4);
        String group3 = sha256Hex((sha256Hex(part3.getBytes()) + salt).getBytes()).substring(0, 4);
        String group4 = sha256Hex((sha256Hex(part4.getBytes()) + salt).getBytes()).substring(0, 4);

        return (group1 + "-" + group2 + "-" + group3 + "-" + group4).toUpperCase();
    }
}
