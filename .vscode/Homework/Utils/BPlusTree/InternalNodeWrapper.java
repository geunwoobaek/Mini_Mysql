package Homework.Utils.BPlusTree;

public class InternalNodeWrapper<K extends Comparable<K>, V extends Comparable<V>> {
    
   InternalNode<K,V> e;
   InternalNodeWrapper(InternalNode<K,V> e)   
   {
    this.e = e;
   }
}