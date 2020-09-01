package Homework.Utils.BPlusTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import Homework.Utils.Pair;

public class BPlusTree<K extends Comparable<K>,V> {
    private LeafNode<K,V> firstleaf;//첫번째 leaf만가지고있는다.
    private InternalNode<K,V> root;
    private int MaxDegree;
    private int totalsize;
    public BPlusTree(int M)
    {
        this.MaxDegree=M;
        this.root=null;
        this.firstleaf=null;
        totalsize=0;
    }
    public Integer getsize()
    {
      return totalsize;
    }
    public void put(K key,V value) throws CloneNotSupportedException 
    {
    if(root==null) //root가 비어있을경우
    {
        if(firstleaf==null||firstleaf.getsize()==0) //LeafData가 비어있을경우 
        {   totalsize++;
           firstleaf=new LeafNode<K,V>(MaxDegree,key,value,0);
        }
        else if(firstleaf.getsize()!=MaxDegree)//리프노드가 꽉안찼을경우
        {
           if(firstleaf.insert(key,value)!=-1) totalsize++; //추가하기
           //System.out.println("insert");
        }
        else{     //leafnode가 꽉찼을경우 핸재 leaf노드의 parent만 맞춘다.leafnode의 1/2 1,2,3,4
          if(firstleaf.insert(key,value)!=-1) totalsize++;
          root=new InternalNode<K,V>(MaxDegree); //firstleaf를 두개로 분해해야한다
          LeafNode<K,V> rightHalf=firstleaf.split(MaxDegree/2+1,MaxDegree+1,1);//반만옆으로준다.
          firstleaf.remove(MaxDegree/2+1,MaxDegree+1);//중간부터끝까지삭제
          firstleaf.setRight(rightHalf);
          rightHalf.setLeft(firstleaf);
          root.SetKey(0,rightHalf.getFirstKey());
  
          firstleaf.addParent(root);
          rightHalf.addParent(root);
          root.appendChild(firstleaf,0);
          root.appendChild(rightHalf,1);
        }
    }
    else{
      if(root.insert(key,value)==true) totalsize++;//internalnode에 해당root를 넣음
      if(root.parent!=null)
      {
        root=root.parent;
        root.parent=null;
      }
    }    
    }
    public V LeafNode_Search(K Key)
    {
      return firstleaf.LeafSearch(Key);
    }
    //
    public Pair<Integer,LeafNode<K,V>> getcurrent(K key)
    { 
      if(root==null)
      {
      if(firstleaf!=null) return firstleaf.LeafCurrentSearch(key);
      else return null;
      }
      return root.CurrentSearch(key);
    }
    public V InternalNode_Search(K Key)
    {
      if(root==null) return null;
      return (V) root.find(Key);
    }
    public void remove(K key) 
    {
      if(root==null) //root가 비어있을경우
      {
        if(firstleaf==null) return ;
        else{
          if(firstleaf.remove(key)==true) totalsize--;
        }
      }
      else{ //root가 null이 아닐경우는
        if(root.delete(key)==true) totalsize--;
        if(root.KeysSize()==0){
          if(root.getChildPointers().get(0) instanceof InternalNode)
          {
            root= (InternalNode<K, V>) root.getChildPointers().get(0);
            root.parent=null;
          }
          else root=null;
        }
        if(firstleaf.getsize()==0&&firstleaf.getRight().getsize()!=0)
        {
          firstleaf=firstleaf.getRight();
          firstleaf.InitLeaf();
        }
      }
    }
    public Integer getTotalLeafSize()
    {
      return firstleaf.getRightTotalSize();
    }
    public V get(K key)
    {
      if(root==null)
      {
      if(firstleaf!=null) return firstleaf.LeafSearch(key);
      else return null;
      }
      return root.SearchKey(key);
    }
    public void printAllNode()
    {
      if(firstleaf!=null)
      {
        firstleaf.printall();
      }
    }
    public void printBackwards()
    {
      if(firstleaf!=null)
      {
      firstleaf.printBackwards(0);
      }
    }
    public ArrayList<K> getIndexList()
    { 
      ArrayList<K> list=new ArrayList<K>();
      LeafNode<K,V> now=firstleaf;
      while(now!=null)
      {
        for(int i=0;i<now.getsize();i++)
        {
          list.add(now.getKey(i));
        }
        now=now.getRight();
      }
      return list;
    }
    public ArrayList<Pair<K,V>> getDataSetArrayList()
    {
      ArrayList<Pair<K,V>> list=new ArrayList<Pair<K,V>>();
      LeafNode<K,V> now=firstleaf;
      while(now!=null)
      {
        for(int i=0;i<now.getsize();i++)
        {
          list.add(now.getPair(i));
        }
        now=now.getRight();
      }
      return list;
    }
    //indexnum3 찾는과정이고 노드사이즈가2일때  
    //01,23
    public LeafNode<K,V> getfirestLeafNode()
    {
      return firstleaf;
    }
    public K getindex(Integer num)
    { 
      LeafNode<K,V> now=firstleaf;
      while(now!=null)
      {
        if(now.getsize()<=num) {
          num-=now.getsize();
          now=now.getRight();
        }
        else
        {
          return now.getKey(num);
        }
      }
      return null;
    }
    public Pair<Integer,LeafNode<K,V>> getcurrent(Integer num)
    { 
      LeafNode<K,V> now=firstleaf;
      while(now!=null)
      {
        if(now.getsize()<=num) {
          num-=now.getsize();
          now=now.getRight();
        }
        else
        {
          return new Pair(num,now);
        }
      }
      return null;
    }
    public void BFS()
    {
      Queue<Node<K,V>> que = new LinkedList<Node<K,V>>();
      que.add(root);

      while(!que.isEmpty())
      {
        Node<K,V> now=que.poll();
        if(now instanceof InternalNode) 
        {
          ArrayList<K> keys=((InternalNode<K, V>) now).getKeys();
          for(int i=0;i<keys.size();i++)
          {
            if(now.parent!=null)
            System.out.print("부모노드키는 "+now.parent.getFirstKey()+"현재키는"+keys.get(i)+",");
            else{
              System.out.print(keys.get(i)+",");
            }
            que.add(((InternalNode<K, V>) now).getChildPointersbyNum(i));
          }
          que.add(((InternalNode<K, V>) now).getChildPointersbyNum(keys.size()));
        }
        else if(now instanceof LeafNode)
        {
          ArrayList<Pair<K,V>> keys=((LeafNode<K, V>) now).getkeyDatPairs();
          for(int i=0;i<keys.size();i++)
          {
            System.out.print("부모노드키는 "+now.parent.getFirstKey()+"현재키는"+keys.get(i).index()+",");
          }
        }
        else{
          System.out.print("___");
        }
        System.out.println("");
      }
    }
}