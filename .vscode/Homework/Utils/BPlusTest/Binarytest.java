package Homework.Utils.BPlusTest;

import java.io.IOException;
import java.util.ArrayList;

import Homework.Utils.BPlusTree.BinarySearch;

public class Binarytest {
    public static void main(String[] args) throws IOException {
        ArrayList<Integer> array= new ArrayList<Integer>();
        array.add(1);
        array.add(3);
        array.add(6);
        array.add(18);
        array.add(19);
        array.add(21);
        BinarySearch<Integer> Binary=new BinarySearch<Integer>();
        Binary.Set(array);
        System.out.println(Binary.LowerBound(-1));
        System.out.println(Binary.FindBig(19));
        System.out.println(Binary.FindSmall(19));
        System.out.println(Binary.LowerBound(19));
        System.out.println(Binary.UpperBound(19));
        ///////
        System.out.println(Binary.FindBig(20));
        System.out.println(Binary.FindSmall(20));
        System.out.println(Binary.LowerBound(20));
        System.out.println(Binary.UpperBound(20));
}
}