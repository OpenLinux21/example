import java.io.*;
import java.nio.file.*;

public class ProgramA {
    public static void main(String[] args) {
        // ProgramA 用于解密经过 ProgramD 加密后的 ProgramB.class 文件并执行程序
        String encryptedFileName = "ProgramB.class";
        byte[] backup;
        try {
            // 读取加密文件到内存中以便稍后恢复
            backup = Files.readAllBytes(Paths.get(encryptedFileName));
        } catch (IOException e) {
            System.err.println("无法读取加密文件：" + encryptedFileName);
            return;
        }
        
        // 解密过程：移除每个字节后插入的随机字节，同时提取附加的 SHA-256 校验码
        byte[] decrypted;
        try {
            decrypted = decryptProgramB(backup);
        } catch (Exception e) {
            System.err.println("解密失败：" + e.getMessage());
            return;
        }
        
        // 验证解密后文件的 SHA-256 校验码
        int appendedLength = 64; // SHA-256 校验码以 64 个 ASCII 字符表示
        int decryptedLength = decrypted.length - appendedLength;
        byte[] originalBytes = new byte[decryptedLength];
        System.arraycopy(decrypted, 0, originalBytes, 0, decryptedLength);
        String computedHash = Util.sha256Hex(originalBytes);
        String appendedHash = new String(decrypted, decryptedLength, appendedLength);
        if (!computedHash.equals(appendedHash)) {
            System.err.println("校验码不匹配，文件可能被篡改。");
            return;
        }
        
        // 将解密后的内容写回 ProgramB.class 以供执行
        try {
            Files.write(Paths.get(encryptedFileName), originalBytes);
        } catch (IOException e) {
            System.err.println("写入解密文件失败：" + e.getMessage());
            return;
        }
        
        // 调用 java 命令执行 ProgramB
        try {
            Process process = new ProcessBuilder("java", "ProgramB").inheritIO().start();
            process.waitFor();
        } catch (Exception e) {
            System.err.println("执行 ProgramB 失败：" + e.getMessage());
        }
        
        // 执行完毕后恢复原始加密文件，避免用户察觉加密逻辑
        try {
            Files.write(Paths.get(encryptedFileName), backup);
        } catch (IOException e) {
            System.err.println("恢复加密文件失败：" + e.getMessage());
        }
    }
    
    /**
     * 解密方法：从加密数据中移除每个原始字节后插入的随机字节，
     * 并将附加的 SHA-256 校验码保留在数据末尾。
     */
    private static byte[] decryptProgramB(byte[] encryptedData) throws Exception {
        if (encryptedData.length < 64) {
            throw new Exception("加密文件过短。");
        }
        // 最后 64 字节为附加的 SHA-256 校验码
        int encryptedContentLength = encryptedData.length - 64;
        if (encryptedContentLength % 2 != 0) {
            throw new Exception("加密文件格式错误。");
        }
        int originalLength = encryptedContentLength / 2;
        byte[] original = new byte[originalLength];
        for (int i = 0, j = 0; i < encryptedContentLength; i += 2, j++) {
            original[j] = encryptedData[i];
        }
        // 将原始数据与附加的校验码合并后返回
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(original);
        outputStream.write(encryptedData, encryptedContentLength, 64);
        return outputStream.toByteArray();
    }
}

