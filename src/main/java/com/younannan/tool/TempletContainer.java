package com.younannan.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

//模板的增删读，依靠label来标记不同模板
public class TempletContainer {

    private String dirPath;
    public TempletContainer(String theDirPath){
        dirPath = theDirPath + "/";
    }

    public String read(String label){
        if(label == null || label.isEmpty()) {
            return "";
        }
        StringBuilder content = new StringBuilder("");
        try {
            String encoding = "UTF-8";
            File file = new File(dirPath + label);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    content.append(lineTxt);
                }
                read.close();
                return content.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public void add(String label, String content){
        if(label == null || label.isEmpty()
                || content == null) {
            return;
        }
        BufferedWriter bw = null;
        try {
            File file = new File(dirPath + label);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void delete(String label){
        if(label == null || label.isEmpty()) {
            return;
        }
        File file=new File(dirPath + label);
        if(file.exists()&&file.isFile()) {
            file.delete();
        }
    }
}
