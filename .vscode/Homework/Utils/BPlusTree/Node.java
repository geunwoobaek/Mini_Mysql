package Homework.Utils.BPlusTree;

public class Node<K extends Comparable<K>,V >{

InternalNode<K,V> parent;

public void addParent(InternalNode<K, V> copy) {
    this.parent=copy;
}

}
