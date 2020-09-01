package Homework.testfolder;

import java.io.IOException;
import java.util.ArrayList;

import Homework.Utils.MergeSort;

public class Sorttest {
    public static void main(String[] args) throws IOException {
        ArrayList<Object> array=new ArrayList<>();
        for(int i=0;i<1000000;i++)
        {  
          array.add((int)(Math.random()*123456789));
        }
        Object[] arr2=new MergeSort(array).get();

        for(int i=0;i<100;i++)
        {
            System.out.println(arr2[99999-i]);
        }
    }
}