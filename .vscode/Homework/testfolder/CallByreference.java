package Homework.testfolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Homework.Utils.Pair;

public class CallByreference {
    public static void main(String[] args) throws IOException {
        Object a=2;
        Object c=a;
        c=(Integer)c+2;
        ArrayList<Object> Arr=new ArrayList<Object>();
        Arr.add(a);
        Object d=Arr.get(0);
        d=c;
        // Map<Integer,Integer> map=new HashMap<Integer,Integer>();
        // map.put(1,3);
        // Map<Integer,Integer> map2=map;
        // add(map2);
        // System.out.println(map.get(1));
        //int
        //  Pair A=new Pair(1,2);
        // int k= (int) A.second();
        // k++;
        // System.out.println(A.second());
        // k++;
        // System.out.println(A.second());
        Map<Integer,Integer> map=new HashMap<Integer,Integer>();
        map.put(1,2);
        int pair=map.get(1);
        pair=3;
        Pair P=new Pair(1,2);
        int k= (int) P.index();
        k++;
        //integer
        // ArrayList<Integer> AL=new ArrayList<Integer>();
        // AL.add(1);
        // ArrayList<Integer> AL2=new ArrayList<Integer>();
        // AL2.add(2);
        // ArrayList<Integer> AL3=new ArrayList<Integer>();
        // AL3.add(3);
        // AL=AL2;
        // AL2=AL3;
        // AL3.add(9);
        System.out.println();

        // //Class
        // ArrayList<Pair> Origin = new ArrayList<Pair>();
        // Origin.add(new Pair(1,2));
        // Pair pair=new Pair(1,3);
        // pair=Origin.get(0);
        // pair.change_second(6);
        // tclass t= new tclass(AL2);
        // System.out.println();
    }
    public static void add(Map<Integer,Integer> map3)
    {
    map3.put(1,2);
    }
    public static class AA{
        public int k=2;
    }
}