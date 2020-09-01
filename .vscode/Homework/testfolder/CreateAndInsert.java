package Homework.testfolder;

import java.io.FileNotFoundException;
import java.io.IOException;

import Homework.Components.MysqlEngine;

public class CreateAndInsert {
    private static MysqlEngine Engine;
    private static String message;
    public static void main(String[] args) throws IOException, CloneNotSupportedException {

        Engine = new MysqlEngine();
        CreateTest();
        InsertTest("Table3");
        Engine.flush();
    }
    public static void CreateTest() throws FileNotFoundException, CloneNotSupportedException {
        Engine.Query("create Table3 (int col1,int col2,int col3) ");
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST1]: create Table3 (int col1,int col2,int col3) \r\n" + message);
    }
    public static void InsertTest(String TableName) throws FileNotFoundException, CloneNotSupportedException {
        for (int i = 0; i < 100000; i++) {
            String message = "insert "+TableName +" "+(int) (Math.random() * 123456789) + ",2,"
                    + (int) (Math.random() * 123456789);
            Engine.Query(message);
        }
        message = Engine.ShowMessage();
        System.out.println("\r\n :insert "+TableName+" 100000 RanDomData\r\n" + message);
    }
}