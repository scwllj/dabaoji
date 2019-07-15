import com.axiba.chiji.SecretUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class MyTest {
    public static void main(String[] args) {
        String aaa = "http://www.baidu.com";
        String key ="2B2D63DE7D22A392799992BC51F01DF683628BDB";
        String ecrypt =SecretUtil.decrypt(key,aaa);
        String deCrypt =SecretUtil.decrypt(key,ecrypt);
        System.out.printf(ecrypt+"_____"+deCrypt);
    }

    private static final Random random = new Random();
    private static final String SEED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SEED_LENTH = SEED.length();

    public static String generateName() {
        int charLen = random.nextInt(7) + 7;
        StringBuilder builder = new StringBuilder(charLen);
        for (int i = 0; i < charLen; i++) {
            int index = random.nextInt(SEED_LENTH);
            builder.append(SEED.charAt(index));
        }
        return builder.toString();
    }

    public static final int functionCount = 2000;

    public static String createCode(int functionCount) {
        String lastFunctionName = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < functionCount; i++) {
            String functionName = generateName() + "()";
            String stringParamName = generateName();
            String boolParamName = generateName();
            builder.append("    private int " + functionName + " {\n" +
                    "        String " + stringParamName + " = \"" + generateName() + "\";\n" +
                    "        boolean " + boolParamName + " = " + stringParamName + ".contains(\"" + random.nextInt(10) + "\");\n" +
                    "        return " + boolParamName + " ? 2 : " + ("".equals(lastFunctionName) ? "0" : lastFunctionName) + ";\n" +
                    "" +
                    "    }\n");
            lastFunctionName = functionName;
        }

        builder.append("    private int generateCode() {\n" +
                "        return " + lastFunctionName + ";\n" +
                "    }");
        return builder.toString();
    }
}
