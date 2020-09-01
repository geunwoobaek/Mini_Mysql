package Homework.Utils.BPlusTest;

import java.io.IOException;
import java.util.ArrayList;

import Homework.Utils.BPlusTree.BPlusTree;

public class IntegrationTest {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        for (int it = 1; it <= 4; it++) {
            BPlusTree<Integer, Integer> btree = new BPlusTree<Integer, Integer>(12500*it+1);
            ArrayList<Integer> Save = new ArrayList<Integer>();
            // 755,83,349,802,983,897,335,810,207,320
            long startTime = System.currentTimeMillis();
            int MissingValue = 0;
            try {
                for (int i = 0; i < 1000000; i++) {;
                    Save.add((int) (Math.random() * 123456789));
                    btree.put(Save.get(i), i);
                }
                double TotalTime = (double) (System.currentTimeMillis() - startTime) / 1000;
                long startTime2 = System.currentTimeMillis();
                for (int i = 0; i < 1000000; i++) {
                    if (btree.get(Save.get(i)) == null) {
                        MissingValue++;
                        System.out.println(Save.get(i));
                        btree.printAllNode();
                    }
                }
            //    System.out.println(btree.getsize());
                double TotalTime2 = (double) (System.currentTimeMillis() - startTime2) / 1000;
                long startTime3 = System.currentTimeMillis();
                for (int i = 0; i < 1000000; i++) {
                    btree.remove(Save.get(i));
                }
              //  System.out.println(btree.getsize());
                double TotalTime3 = (double) (System.currentTimeMillis() - startTime3) / 1000;
                System.out.println("ORDER="+12500*it+" 삽입시간= " + TotalTime + "," + " 탐색시간= " + TotalTime2 + " 삭제시간= " + TotalTime3);
            } catch (Exception e) {
                System.out.println();
            }
        }
    }

}