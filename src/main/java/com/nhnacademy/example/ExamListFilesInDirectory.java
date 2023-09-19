package com.nhnacademy.example;

import java.io.File;

public class ExamListFilesInDirectory {
    public static void main(String[] args) {
        File dir = new File("./");

        for (File file : dir.listFiles()) {
            System.out.println(String.format("%s : %s", file.getName(), file.isDirectory() ? "dir" : "file"));
        }
    }
}
