import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProgramC {
    public static void main(String[] args) {
        // 管理员工具：输入机器码（通常为 ProgramB 输出的 SHA-256 值），生成对应密钥
        System.out.print("请输入机器码 (SHA-256)：");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String machineCodeHash = "";
        try {
            machineCodeHash = reader.readLine();
        } catch (Exception e) {
            System.err.println("读取输入失败：" + e.getMessage());
            return;
        }
        String key = Util.generateKey(machineCodeHash);
        System.out.println("生成的密钥为：" + key);
    }
}

