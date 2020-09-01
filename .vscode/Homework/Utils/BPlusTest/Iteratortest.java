package Homework.Utils.BPlusTest;

import java.io.IOException;
import java.util.ArrayList;

import Homework.Utils.BPlusTree.BPlusTree;
import Homework.Utils.BPlusTree.LeafIterator;

public class Iteratortest {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        BPlusTree<Integer,Integer> btree=new BPlusTree<Integer,Integer>(3);
        ArrayList<Integer> Save=new ArrayList<Integer>(); 
        for(int i=0;i<100;i++)
        {   
          Save.add((int)(Math.random()*1234));   
           btree.put(Save.get(i),i);
        }
    LeafIterator<Integer,Integer> iterator=new LeafIterator(btree);
    while(iterator.hasNext())
    {
        System.out.println(iterator.Next().index());
    }    
    }
}