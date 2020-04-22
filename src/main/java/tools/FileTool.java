package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTool {

    public void saveFile(String fileName,String text,String filePath){
        File dir = new File(filePath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File txt = new File(filePath + File.separator + fileName);
        if (!txt.exists()) {
            try {
                txt.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte bytes[] = new byte[512];
        bytes = text.getBytes();
        int b = bytes.length;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(txt);
            fos.write(bytes);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
