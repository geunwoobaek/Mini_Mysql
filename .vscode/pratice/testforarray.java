package pratice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class testforarray {
    public static void main(String[] args) throws IOException {
    {
    Integer[] array=new Integer[30000000];
    long startTime = System.currentTimeMillis();   
    for(int i=0;i<30000000;i++)
    {
        array[i]=i;
    }
    for(int i=0;i<30000000;i++)
    {   
        if(i%2==0) array[i]=0;
    }
    double TotalTime = (double)(System.currentTimeMillis()-startTime)/1000; 
    System.out.println(TotalTime);
    startTime = System.currentTimeMillis();   
    for(int i=0;i<30000000;i++)
    {   if(i%2!=0)
        array[i]=i;
    }
     TotalTime = (double)(System.currentTimeMillis()-startTime)/1000; 
     System.out.println(TotalTime);
    }
    }
}   