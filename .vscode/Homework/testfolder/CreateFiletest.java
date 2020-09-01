package Homework.testfolder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CreateFiletest {
    public static void main(String[] args) throws IOException {
    String path = ".vscode/Homework/TableSpace/eaSDasDexample.txt";
    OutputStream output = new FileOutputStream(path);
}
}