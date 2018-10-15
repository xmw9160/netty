package com.xmw.java.base.bio.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author xmw.
 * @date 2018/6/10 11:33.
 */
public class ReadFile {
    public static void main(String[] args) throws Exception {
        FileInputStream input = new FileInputStream("d://info.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String nameLine = reader.readLine();
        String ageLine = reader.readLine();
        String emailLine = reader.readLine();
        String phoneLine = reader.readLine();
        String endLine = reader.readLine();
        System.out.println(nameLine);
        System.out.println(ageLine);
        System.out.println(emailLine);
        System.out.println(phoneLine);
        System.out.println(endLine);
    }
}
