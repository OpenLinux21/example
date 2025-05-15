import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class ProgramD {
    public static void main(String[] args) {
        // 加密工具：对 ProgramB.class 文件进行加密处理
        String fileName = "ProgramB.class";
        byte[] originalBytes;
        try {
            originalBytes = Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            System.err.println("无法读取文件：" + fileName);
            return;
        }

        // 计算原始文件的 SHA-256 校验码
        String originalHash = Util.sha256Hex(originalBytes);

        // 生成加密数据：在每个字节后插入一个随机字节
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SecureRandom random = new SecureRandom();
        for (byte b : originalBytes) {
            outputStream.write(b);
            outputStream.write(random.nextInt(256)); // 插入随机字节
        }
        // 将 SHA-256 校验码以 ASCII 格式附加到文件末尾
        try {
            outputStream.write(originalHash.getBytes(StandardCharsets.US_ASCII));
        } catch (IOException e) {
            System.err.println("写入校验码时发生错误：" + e.getMessage());
            return;
        }

        byte[] encryptedBytes = outputStream.toByteArray();

        // 将加密后的数据写回 ProgramB.class 文件
        try {
            Files.write(Paths.get(fileName), encryptedBytes);
        } catch (IOException e) {
            System.err.println("写入加密文件失败：" + e.getMessage());
            return;
        }

        System.out.println("文件加密完成。");
    }
}
