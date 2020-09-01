package Homework.testfolder;

import java.io.IOException;

public class Timertest {
    public static void main(String[] args) throws IOException {
    long startTime = System.currentTimeMillis();    
    for(int i=0;i<100000000;i++){
        i++;i--;
    }
    long totalTime = System.currentTimeMillis() - startTime;
    double time = (double)totalTime/1000;
    System.out.println("Total time = " + time);
    System.out.println();
}
}