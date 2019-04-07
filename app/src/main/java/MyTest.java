import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class MyTest {
    public static void main(String[] args) {

        String[] fileNames = {"receiver/MyJpushReceiver.java", "CircleProgress.java",
                "DeviceHelper.java", "FileUtils.java",
                "Loading.java", "LogUtil.java",
                "MainActivity.java", "MyWebview.java",
                "PopView.java", "SharedApplication.java"};
        for (int i = 0; i < fileNames.length; i++) {

            File file = new File("/Users/linj/Documents/GitHub/dabaoji/app/src/main/java/com/axiba/chiji/" + fileNames[i]);
            if (file.exists()) {
                String encoding = "UTF-8";
                Long filelength = file.length();
                byte[] filecontent = new byte[filelength.intValue()];
                try {
                    FileInputStream in = new FileInputStream(file);
                    in.read(filecontent);
                    in.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String content = new String(filecontent, encoding);
                    int startIndex = content.indexOf("//sign--start");
                    int endIndex = content.indexOf("//sign--end");
                    String newContent = content.replace(content.substring(startIndex, endIndex),
                            "//sign--start\n" + createCode(new Random().nextInt(1000) + 1500) + "\n    ");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(newContent.getBytes());
                    fileOutputStream.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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
