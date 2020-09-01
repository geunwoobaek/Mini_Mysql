package Homework.Utils.BPlusTest;

import java.io.IOException;
import java.util.ArrayList;

import Homework.Utils.Pair;
import Homework.Utils.BPlusTree.BPlusTree;
import Homework.Utils.BPlusTree.PairBinarySearch;

public class LeafNodeTest {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        BPlusTree<Integer, Integer> btree = new BPlusTree<Integer, Integer>(50001);
        ArrayList<Integer> Save = new ArrayList<Integer>();
        for (int i = 0; i < 1000; i++) {
            Save.add((int) (Math.random() * 1234));
            btree.put(Save.get(i), i);
        }
        ArrayList list = btree.getDataSetArrayList();
        System.out.println();
        new PairBinarySearch(list).InSert(new Pair(144, 2));
        System.out.println();
    }

}