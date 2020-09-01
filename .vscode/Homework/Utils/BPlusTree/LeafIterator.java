package Homework.Utils.BPlusTree;

import java.util.ArrayList;

import Homework.Utils.Pair;

public class LeafIterator<K extends Comparable<K>, V> {
    private LeafNode<K,V> current;
    private Integer Point;
    ArrayList<Pair<K, V>> data;
    public LeafIterator(BPlusTree btree)
    {
        current=btree.getfirestLeafNode();
        data=current.getkeyDatPairs();
        Point=0;
    }
    public LeafIterator(BPlusTree btree,int start)
    {   Pair p=btree.getcurrent(start);
        current= (LeafNode<K, V>) p.value();
        Point= (Integer) p.index();
        data=current.getkeyDatPairs();
    }
    public LeafIterator(BPlusTree btree,Object key)
    {   
        Pair<Integer,LeafNode<K,V>> now=btree.getcurrent((K)key);
        Point=now.index();
        current=now.value();
        data=current.getkeyDatPairs();
    }
    public Pair<K, V> Next()
    {
        if(Point<data.size()) return data.get(Point++);
        else {
            current=current.getRight();
            data=current.getkeyDatPairs();
            Point=0;
            return data.get(Point++);
        }
    }
    public void before()
    {
        Point--;
    }
    public boolean hasNext()
    {
       if((Point==data.size())&&current.getRight()==null) return false;
        else return true;
    }
    public K getIndex()
    {
        return data.get(Point).index();
    }
    public boolean notsame(LeafIterator target)
    {  
        try {
            if(target==null) 
            return this.hasNext();
            if(target.getIndex()!=this.getIndex()) 
            return true;
            else return false;
        } catch (Exception e) {
            return false;
        }
    }
	public boolean sameIndex(Object obj) {
		return data.get(Point).index().toString().equals(obj);
	}
}