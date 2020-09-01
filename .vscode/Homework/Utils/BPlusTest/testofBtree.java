package Homework.Utils.BPlusTest;

import java.io.IOException;
import java.util.ArrayList;

import Homework.Utils.BPlusTree.BPlusTree;


public class testofBtree {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        double Average=0;
        double AverageInsertTime=0;
        double AverageSearchTime=0;
        for(int times=0;times<100;times++)
       { 
        BPlusTree<Integer,Integer> btree=new BPlusTree<Integer,Integer>(1115);
        BPlusTree<String,Integer> btreeString=new BPlusTree<String,Integer>(3);
        ArrayList<Integer> Save=new ArrayList<Integer>(); 
        ArrayList<Integer> result=new ArrayList<Integer>();
        int MissingValue=0;
        long startTime = System.currentTimeMillis();    
        for(int i=0;i<100000;i++)
         {   
            Save.add((int)(Math.random()*2000000));
            btree.put(Save.get(i),i);
        }
        //System.out.println(btree.getindex(1455));
        double TotalTime = (double)(System.currentTimeMillis()-startTime)/1000;    
        AverageInsertTime+=TotalTime;
     //   System.out.println("insert time="+TotalTime+"seconds..");
        long startTime2 = System.currentTimeMillis();    
         for(int i=0;i<100000;i++)
         {
            if(btree.get(Save.get(i))==null) {MissingValue++;
            System.out.println(Save.get(i));
            btree.printAllNode();
            }
         }
         double TotalTime2 = (double)(System.currentTimeMillis()-startTime2)/1000;    
         System.out.println("searching time="+TotalTime2+"seconds..");
         double value=1-(double)(MissingValue)/50000;
    //     System.out.println("결측데이터수는 "+MissingValue+"개입니다"+"정확도는"+value);
        Average+=value;
        AverageSearchTime+=TotalTime2;
        }
        System.out.println("평균정확도는"+Average/100+"\r\n평균 삽입 시간은"+AverageInsertTime/100+"\r\n평균 탐색 시간은"+AverageSearchTime/100);
    }
}