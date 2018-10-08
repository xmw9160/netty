package com.xmw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        String path = App.class.getClassLoader().getResource("index.html").getPath();
        System.out.println(path);
        File file = new File("classpath://index.html");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s = reader.readLine();
        System.out.println(s);
    }
}
