package trainedge.htmleditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Abc on 09-01-2017.
 */

public class FileHandler {

    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public static void writeIntoFile(File f, String text){
        try {
            FileOutputStream writeIntoFile = new FileOutputStream(f);
            byte[] changeToByte = text.getBytes();
            writeIntoFile.write(changeToByte);
            writeIntoFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteFileRecusively(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteFileRecusively(child);

        fileOrDirectory.delete();
    }

    public static void getTemplateForHtml(File f) {
        String s = "<html>\n" +
                "<head></head>\n" +
                "<body>Hello</body>" +
                "</html>";
        writeIntoFile(f,s);
    }

    public static void getTemplateForCss(File f) {
        String s = ".className{\n" +
                "property:value;\n" +
                "}";
       writeIntoFile(f,s);

    }
}
