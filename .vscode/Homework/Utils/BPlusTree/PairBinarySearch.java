package Homework.Utils.BPlusTree;

import java.util.ArrayList;

import Homework.Utils.Pair;

public class PairBinarySearch<K extends Comparable<K>,V extends Comparable<V>> {
    
    private int left = 0;
    private int right = 0;
    private int mid = 0;
    private ArrayList<Pair<K,V>> array;
    //사용방법   Data에는 사용할데이터의 인덱스와 attribute값 넣기
    public PairBinarySearch()
    {   ;
    }
    public PairBinarySearch(ArrayList<Pair<K,V>> array)
    {   
        this.array=array;
    }
    public void Set(ArrayList<Pair<K,V>> array)
    {   
        this.array=array;
    }
    //해당값의 Second만 넣음 그러면 index순번알려줌
    public int FindSameIndex(K target)//n보다 크거나 같은 순번찾기
    {  
        int compare= LowerBoundIndex(target);
        if(array.get(compare).index().compareTo(target)!=0)
        {
            return -1;
        }
        else return compare;
    }
    public int FindBigIndex(K target)
    {
        int compare= UpperBoundIndex(target);
        if(array.get(compare).index().compareTo(target)>0)
        {
        return compare;           
        }
        else return compare+1;
    }

    public int FindSmallIndex(K target) {
        int compare= LowerBoundIndex(target)-1;
        return compare;
    }

    public int LowerBoundIndex(K standard) // 같은값중 가장작은값
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

    public int UpperBoundIndex(K standard) //
    {
        left=0;
        right=array.size()-1;
        while (left < right) {
            mid = (left + right) / 2;
            int check = array.get(mid).index().compareTo(standard);
            if(check<=0) left=mid+1;
            else right=mid;
            }
        return right;
    }    
    public int FindSameValue(V target)//n보다 크거나 같은 순번찾기
    {  
        int compare= LowerBoundValue(target);
        if(array.get(compare).value()!=target)
        {
            return -1;
        }
        else return compare;
    }
    public int FindBigValue(V target)
    {
        int compare= UpperBoundValue(target);
        if(array.get(compare).value().compareTo(target)>0)
        {
        return compare;           
        }
        else return compare+1;
    }

    public int FindSmallValue(V target) {
        int compare= LowerBoundValue(target)-1;
        return compare;
    }

    public int LowerBoundValue(V standard) // 같은값중 가장작은값
    {
        left=0;
        right=array.size()-1;
        while (left < right) {
            mid = (left + right) / 2;
            int check = array.get(mid).value().compareTo(standard);
            if(check<0) left=mid+1;
            else right=mid;
        }
        return right;
    }

    public int UpperBoundValue(V standard) //
    {
        left=0;
        right=array.size()-1;
        while (left < right) {
            mid = (left + right) / 2;
            int check = array.get(mid).value().compareTo(standard);
            if(check<=0) left=mid+1;
            else right=mid;
            }
        return right;
    }
	public void InSert(Pair pair) {
        if(array.size()==0) {array.add(pair); return;}
        int point=LowerBoundIndex((K) pair.index());
        array.add(point,pair);
	}
	public void Delete(Pair pair) {
        int point=FindSameIndex((K) pair.index());
        array.remove(point);
	}    
    
}