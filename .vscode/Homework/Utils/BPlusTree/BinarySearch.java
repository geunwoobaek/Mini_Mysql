package Homework.Utils.BPlusTree;

import java.util.ArrayList;

public class BinarySearch<K extends Comparable<K>> {
    
    private int left = 0;
    private int right = 0;
    private int mid = 0;
    private ArrayList<K> array;
    //사용방법   Data에는 사용할데이터의 인덱스와 attribute값 넣기
    public BinarySearch()
    {   ;
    }
    public BinarySearch(ArrayList<K> array)
    {   
        this.array=array;
    }
    public void Set(ArrayList<K> array)
    {   
        this.array=array;
    }
    //해당값의 Second만 넣음 그러면 index순번알려줌
    public int FindSame(K target)//n보다 크거나 같은 순번찾기
    {  
        int compare= LowerBound(target);
        if(array.get(compare)!=target)
        {
            return -1;
        }
        else return compare;
    }
    public int FindBig(K target)
    {
        int compare= UpperBound(target);
        if(array.get(compare).compareTo(target)==1)
        {
        return compare;           
        }
        else return compare+1;
    }

    public int FindSmall(K target) {
        int compare= LowerBound(target)-1;
        return compare;
    }

    public int LowerBound(K standard) // 같은값중 가장작은값
    {
        left=0;
        right=array.size()-1;
        while (left < right) {
            mid = (left + right) / 2;
            int check = array.get(mid).compareTo(standard);
            if(check<0) left=mid+1;
            else right=mid;
        }
        return right;
    }

    public int UpperBound(K standard) //
    {
        left=0;
        right=array.size()-1;
        while (left < right) {
            mid = (left + right) / 2;
            int check = array.get(mid).compareTo(standard);
            if(check!=1) left=mid+1;
            else right=mid;
            }
        return right;
    }    
    
}