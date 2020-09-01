package Homework.testfolder;

import java.io.IOException;

public class typecheck {
    public static void main(String[] args) throws IOException {
       Object o=2;
       if(o.getClass().getName().equals("java.lang.Integer"))
       System.out.println(o.getClass().getName()); 
    }
}