package Homework.testfolder;
import Homework.Utils.Pair;
import java.io.IOException;
import java.util.ArrayList;

import Homework.Utils.MergeSort;

public class testmerge {
    public static void main(String[] args) throws IOException {
    ArrayList<Pair> array=new ArrayList<Pair>();
    array.add(new Pair(2,7));
    array.add(new Pair(1,9));
    array.add(new Pair(3,4));
    new MergeSort(array,true);
    System.out.println("");
    //System.out.println((int)(a));
    }
}