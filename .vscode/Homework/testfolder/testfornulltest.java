package Homework.testfolder;
import Homework.Utils.Pair;
import java.io.IOException;
import java.util.ArrayList;

import Homework.Utils.MergeSort;

public class testfornulltest {
    public static void main(String[] args) throws IOException {
    A[] array= new A[5];
    for(int i=0;i<array.length;i++)
    {
        if(array[i]==null) System.out.println("null");
        else System.out.println(array[i]);
    }
    }
    public class A{
        public int a;
    }
}