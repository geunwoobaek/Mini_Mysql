package Homework.Utils.BPlusTree;

import java.util.ArrayList;

import Homework.Utils.Pair;

public class LeafNodeSearch<Object extends Comparable<? super Object>, V> {

    private int left = 0;
    private int right = 0;
    private int mid = 0;
    private ArrayList<Pair<Object, V>> array;

    // 사용방법 Data에는 사용할데이터의 인덱스와 attribute값 넣기
    public LeafNodeSearch() {
        ;
    }

    public LeafNodeSearch(ArrayList<Pair<Object, V>> array)
    {   
        this.array=array;
    }
    public void Set(ArrayList<Pair<Object,V>> array)
    {   
        this.array=array;
    }
    //해당값의 Second만 넣음 그러면 index순번알려줌
    public int FindSameIndex(Object target)//n보다 크거나 같은 순번찾기
    {  
        int compare= LowerBoundIndex(target);
        if(array.get(compare).index().compareTo(target)!=0)
        {
            return -1;
        }
        else return compare;
    }
    public int FindBigIndex(Object target)
    {
        int compare= UpperBoundIndex(target);
        if(array.get(compare).index().compareTo(target)==1)
        {
        return compare;           
        }
        else return compare+1;
    }

    public int FindSmallIndex(Object target) {
        int compare= LowerBoundIndex(target)-1;
        return compare;
    }

    public int LowerBoundIndex(Object standard) // 같은값중 가장작은값
    {
        left=0;
        right=array.size()-1;
        while (left < right) {
            mid = (left + right) / 2;
            int check = array.get(mid).index().compareTo(standard);
            if(check<0) left=mid+1;
            else right=mid;
        }
        return right;
    }

    public int UpperBoundIndex(Object standard) //
    {
        left=0;
        right=array.size()-1;
        while (left < right) {
            mid = (left + right) / 2;
            int check = array.get(mid).index().compareTo(standard);
            if(check!=1) left=mid+1;
            else right=mid;
            }
        return right;
    }    
    
}