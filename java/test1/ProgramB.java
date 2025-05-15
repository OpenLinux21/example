import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProgramB {
    public static void main(String[] args) {
        // 收集系统属性生成机器码
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String userName = System.getProperty("user.name");
        String machineCode = osName + osVersion + userName;
        String machineCodeHash = Util.sha256Hex(machineCode.getBytes());
        
        // 打印机器码的 SHA-256 值供用户发送给管理员
        System.out.println("机器码 (SHA-256): " + machineCodeHash);
        System.out.println("请将上面的机器码发送给管理员以获取密钥。");
        
        // 生成密钥及其校验值
        String key = Util.generateKey(machineCodeHash);
        // 对生成的密钥进行 Base64 编码后，再计算 SHA-256 得到密钥校验值（秘密存入内存）
        String keyHash = Util.sha256Hex(Util.base64Encode(key).getBytes());
        
        // 为了安全起见，下面的“复杂算法”只是多次调用 SHA-256 以示模拟，
        // 幽默地说：如果你能轻易破解这层“复杂防护”，也许你连保管贵宾晚宴的钥匙都难！
        
        // 等待用户输入从管理员处获得的密钥
        System.out.print("请输入从管理员处获得的密钥：");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String userInput = "";
        try {
            userInput = reader.readLine();
        } catch (Exception e) {
            System.err.println("读取输入失败：" + e.getMessage());
            return;
        }
        
        // 对用户输入的密钥同样处理
        String userKeyHash = Util.sha256Hex(Util.base64Encode(userInput).getBytes());
        
        // 比较校验值，并给出相应反馈
        if (userKeyHash.equals(keyHash)) {
            // 使用 ANSI 转义码输出绿色文本
            System.out.println("\033[32mAuth OK!\033[0m");
        } else {
            System.out.println("\033[31mAuth ERROR!\033[0m");
        }
    }
}

