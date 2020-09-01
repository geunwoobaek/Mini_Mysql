package Homework.Utils.BPlusTree;

import java.util.ArrayList;
import java.util.List;

import Homework.Utils.Pair;

public class InternalNode<K extends Comparable<K>, V> extends Node {
    private int MaxDegree;
    private ArrayList<K> keys;
    private ArrayList<Node<K, V>> ChildPointers; // child는 key보다 1커야함
    private Integer ChildPointerNumber;

    public InternalNode(int MaxDegree) {
        this.MaxDegree = MaxDegree;
        keys = new ArrayList<K>();
        // ChildPointers=(Node<K,V>[])new Node[MaxDegree + 1];// Childs는 수가 더많음
        ChildPointers = new ArrayList<Node<K, V>>();
    }

    public InternalNode(InternalNode<K, V> parent) {
        this.parent = parent;
    }

    public InternalNode(int maxDegree, ArrayList<K> copyPair, ArrayList<Node<K, V>> ChildPointers, int num) {
        this.MaxDegree = MaxDegree;
        keys = copyPair;
        ChildPointerNumber = num;
        this.ChildPointers = ChildPointers;
    }

    public void Clear() {
        keys = null;
        ChildPointers = null;
        ChildPointerNumber = null;
    }

    public void appendChild(Node<K, V> Child, Integer Position) {
        if (Child instanceof InternalNode) {
            ChildPointers.add(Position, Child);
            Child.addParent(this);
        } else if (Child instanceof LeafNode) {
            ChildPointers.add(Position, Child);
            Child.addParent(this);
        }
    }

    public void appendChild(Node<K, V> Child) {
        if (Child instanceof InternalNode) {
            ChildPointers.add(Child);
        } else if (Child instanceof LeafNode) {
            ChildPointers.add(Child);
        }
    }

    public void appendParent(Node<K, V> Parent) {
        this.parent = (InternalNode) Parent;
    }

    public K getFirstKey() {
        return keys.get(0);
    }

    // public void addKey(K key)
    // {
    // keys.add(key);
    // }
    public void SetKey(int position, K value) {
        if (keys.size() >= position)
            keys.add(value);
        else
            keys.set(position, value);
    }

    public Integer findPosition(K key) // 같은값중 가장작은값
    {
        Integer Position = new BinarySearch<K>(keys).LowerBound(key);
        // if(만약 같을 경우에는 같은값
        if (keys.get(Position).compareTo(key) <= 0)
            Position++;
        return Position;
    }

    public Integer findRemovePosition(K key) {
        Integer Position = new BinarySearch<K>(keys).LowerBound(key);
        return Position;
    }

    public Boolean insert(K key, V value) throws CloneNotSupportedException {
        Integer Position = findPosition(key);
        if (ChildPointers.get(Position) instanceof InternalNode) {
           return ((InternalNode<K, V>) ChildPointers.get(Position)).insert(key, value);
        } else if (ChildPointers.get(Position) instanceof LeafNode) {
            LeafNode<K, V> leaf = (LeafNode<K, V>) ChildPointers.get(Position);
            boolean fullCheck = leaf.isfull();
            Integer Pos = leaf.insert(key, value); // leaf에 집어넣기 이때 leaf
            if (Pos == -1) {
                // System.out.println("이미 들어온 값입니다");
                return false;
            }
            // else if(Pos==leaf.getsize()-1) keys.set(0,key);

            if (fullCheck) // 집어 넣었을때 LeafNode가 넘쳤을때
            {
                boolean ChildPointerfullCheck = isfull();
                LeafNode<K, V> HalfRight = leaf.split(MaxDegree / 2 + 1, MaxDegree + 1, leaf.GetChildNumber() + 1);
                HalfRight.setLeft(leaf);
                HalfRight.setRight(leaf.getRight());
                if(leaf.getRight()!=null) leaf.getRight().setLeft(HalfRight);
                leaf.setRight(HalfRight);
                leaf.remove(MaxDegree / 2 + 1, MaxDegree + 1); // 반쪽제거
                HalfRight.addParent(this); // 부모로추가
                ChildPointers.add(addKey(HalfRight.getFirstKey()), HalfRight);// childpointer목록에추가
                // internalnode의 키가 가득찰때
                if (ChildPointerfullCheck) // childpointer가 가득찼을때
                {
                    BottomUp_Merge(this);
                }
            }
        }
        return true;
    }

    public void BottomUp_Merge(InternalNode<K, V> full) // 왼쪽leaf노드와 오른쪽leaf노드를 넣었을때 합치는 기능함
    {
        if (full.parent != null) {
            InternalNode<K, V> Right = full.split(MaxDegree / 2 + 2, MaxDegree + 1, 1);
            Right.addParent(full.parent);
            full.parent.appendChild(Right, full.parent.addKey(Right.getLeastKey()));
            // parent.addChild(Right);
            full.Keyremove(MaxDegree / 2 + 1, MaxDegree / 2 + 2);
            if (full.parent.isSpace()) // parent가 꽉찼을경우
            {
                BottomUp_Merge(full.parent);
            }
        } else if (full.parent == null) // parent만들기
        {
            InternalNode<K, V> Parent = new InternalNode<K, V>(MaxDegree);
            InternalNode<K, V> Left = full.split(0, MaxDegree / 2 + 1, 1);
            full.addParent(Parent);
            Left.addParent(Parent);
            // 부모가 key넣기
            Parent.addKey(full.getLeastKey());
            full.keys.remove(0);
            Parent.appendChild(Left, 0);
            Parent.appendChild(full, 1);
            // System.out.println();
        }
    }

    public ArrayList<K> getKeys() {
        return keys;
    }

    public Integer KeysSize() {
        return keys.size();
    }

    private InternalNode<K, V> split(int Start, int End, int num) {
        ArrayList<K> CopyPair = new ArrayList<K>();
        InternalNode<K, V> copy = new InternalNode<K, V>(MaxDegree);
        for (int i = Start; i < End; i++) {
            copy.addKey(keys.get(i));
            copy.appendChild(this.ChildPointers.get(i));
            this.ChildPointers.get(i).addParent(copy);
        }
        copy.appendChild(this.ChildPointers.get(End));
        this.ChildPointers.get(End).addParent(copy);
        Keyremove(Start, End);
        Childremove(Start, End + 1);
        return copy;
    }

    public void Keyremove(int start, int end) {
        keys.subList(start, end).clear();
    }

    public void Childremove(int start, int end) {
        ChildPointers.subList(start, end).clear();
    }

    private boolean isfull() {
        return keys.size() == MaxDegree;
    }

    public Object find(Object key) {
        return null;
    }

    public InternalNode<K, V> getParent() {
        return parent;
    }

    public K getKeybyNum(Integer num) {
        return keys.get(num);
    }

    public boolean isSpace() {
        return keys.size() - 1 == MaxDegree;
    }

    public Integer addKey(K key)// key값 정렬및 childpointer들어가야하는값 반환
    {
        if (keys.size() == 0) {
            keys.add(0, key);
            return 1;
        }
        int Position = new BinarySearch<K>(keys).LowerBound(key);// 1 3 5 11 14인데 15를찾는데
        int check = keys.get(Position).compareTo(key);
        if (check == 0)
            return -1;
        if (check < 0)
            Position++;
        keys.add(Position, key);
        return Position + 1;
    }

    public void addChild(Node<K, V> Child) {
        if (Child instanceof InternalNode) {
            K Childkey = ((InternalNode<K, V>) Child).getfirstKey();
            int Position = new BinarySearch<K>(keys).LowerBound(Childkey);// 1 3 5 11 14인데 15를찾는데
            int check = keys.get(Position).compareTo(Childkey);
            if (check == 0) {
                System.out.println("already exits");
                return;
            }
            if(check<0) ChildPointers.add(Child);
            else ChildPointers.add(Position, Child);
        } else if (Child instanceof LeafNode) {
            ChildPointers.add(Child);
        }
    }

    public K getfirstKey() {
        return keys.get(0);
    }

    public ArrayList<Node<K, V>> getChildPointers() {
        return ChildPointers;
    }

    public Node<K, V> getChildPointersbyNum(Integer i) {
        return ChildPointers.get(i);
    }

    public K getLeastKey() {
        Node<K, V> node = ChildPointers.get(0);
        if (node instanceof LeafNode) {
            return ((LeafNode<K, V>) node).getFirstKey();
        }
        return ((InternalNode<K, V>) node).getLeastKey();
    }

    // 10 20 30
    // 17 23 30 34 30
    public V SearchKey(K key) {
        int point = new BinarySearch(keys).LowerBound(key);
        int check = key.compareTo(keys.get(point));
        if (check < 0)
            point = 0;
        else
            point++;
        Node<K, V> Child = ChildPointers.get(point);

        if (Child instanceof InternalNode) {
            return ((InternalNode<K, V>) Child).SearchKey(key);
        } else {
            return ((LeafNode<K, V>) Child).LeafSearch(key);
        }
    }
    public Pair<Integer,LeafNode<K,V>> CurrentSearch(K key) {
        int point = new BinarySearch(keys).LowerBound(key);
        int check = key.compareTo(keys.get(point));
        if (check < 0)
            point = 0;
        else
            point++;
        Node<K, V> Child = ChildPointers.get(point);

        if (Child instanceof InternalNode) {
            return ((InternalNode<K, V>) Child).CurrentSearch(key);
        } else {
            return ((LeafNode<K, V>) Child).LeafCurrentSearch(key);
        }
    }
    //funcitoin 이때 부모노드로부터 노드를 주고 child도 전해줌 
    //이작업이 연쇄적으로 일어날수 있기에 재귀함수
    public void UnderFlowMerge(K key) {
        InternalNode<K,V> pNode=this.parent;
        Integer Check=pNode.findPosition(key);//pnode에서 탐색할차일드순번
        InternalNode<K,V> leftSibling=null;
        InternalNode<K,V> RightSibling=null;
        if(Check!=0) leftSibling= (InternalNode<K, V>) pNode.getChildPointersbyNum(Check - 1);
        if(Check!=pNode.KeysSize()) RightSibling= (InternalNode<K, V>) pNode.getChildPointersbyNum(Check + 1);
        if(leftSibling!=null&&leftSibling.KeysSize()>1)
        {
            this.keys.add(pNode.getKeybyNum(Check-1)); //키에 부모노드로부터 가져오기
            pNode.keys.set(Check-1, leftSibling.getKeybyNum(leftSibling.KeysSize()-1)); //부모노드에있는키값변경
            this.ChildPointers.add(0,leftSibling.getChildPointers().get(leftSibling.KeysSize())); //childpointer에 자녀받기
            leftSibling.getChildPointers().get(leftSibling.KeysSize()).addParent(this);
            int point=leftSibling.KeysSize();
            leftSibling.getChildPointers().remove(point);//나눠준자녀삭제
            leftSibling.Keyremove(leftSibling.KeysSize()-1, leftSibling.KeysSize());
        }
        else if(RightSibling!=null&&RightSibling.KeysSize()>1)
        {
            this.keys.add(pNode.getKeybyNum(Check)); //키에 부모노드로부터 가져오기
            pNode.keys.set(Check, RightSibling.getFirstKey()); //부모노드에있는키값변경
            this.ChildPointers.add(RightSibling.getChildPointers().get(0)); //childpointer에 자녀받기
            RightSibling.getChildPointers().get(0).addParent(this);
            RightSibling.getChildPointers().remove(0);//나눠준자녀삭제
            RightSibling.Keyremove(0,1);
        }
      else{//right left전부다 크기가 1일떄
        K leastkey=null; 
         if(leftSibling!=null)
         {
            leftSibling.addKey(pNode.getKeybyNum(Check-1)); //left=child1번
            pNode.Keyremove(Check-1, Check);
            leftSibling.ChildPointers.add(ChildPointers.get(0));
            ChildPointers.get(0).addParent(leftSibling);
            ChildPointers.remove(0);
            pNode.Childremove(Check,Check+1);
            leastkey=leftSibling.getLeastKey();
         }
         else if(RightSibling!=null)
         {
            RightSibling.addKey(pNode.getKeybyNum(Check));//right=child3번 
            pNode.Keyremove(Check, Check+1);
            RightSibling.ChildPointers.add(0,ChildPointers.get(0));
            ChildPointers.get(0).addParent(RightSibling);
            ChildPointers.remove(0);
            pNode.Childremove(Check,Check+1);
            leastkey=RightSibling.getLeastKey();
         }
         
         if(pNode.KeysSize()==0)
         {
            if(pNode.parent==null) return;
            else{
                parent.UnderFlowMerge(leastkey);
            }
         }  
      }

        
    }


  //11을찾는데 10 12
    private List<K> getArray(int start, int end) {
    return keys.subList(start, end);
}

private void AddKeyArray( List<K> klist) {
keys.addAll(klist);    
}

public Boolean delete(K key) {
    Integer Position=findPosition(key);//들어갈 위치를 찾음
    //끝까지찾아가서 삭제하기
    if(ChildPointers.get(Position) instanceof InternalNode) //자식이 internalnode면 다시재귀
    {   
     return ((InternalNode<K,V>) ChildPointers.get(Position)).delete(key);
    }
    else if(ChildPointers.get(Position) instanceof LeafNode)
    {   
        LeafNode<K,V> leaf=(LeafNode<K,V>) ChildPointers.get(Position) ;
        
        if(leaf.remove(key)==false) 
            return false; //해당키가 없을경우
        if(leaf.isDeficient()==true) //제거했을떄 LeafNode가 비었을경우
        {    
                    //leaf노드가비어있을때 부모노드로 부터확인
        
          Integer RemovePos=findRemovePosition(key);
          Integer Check=keys.get(RemovePos).compareTo(key);//해당internal노드의 index가 해당key보다 클때 
          if(Check>0) //오른쪽에서 빌릴때
          {
            LeafNode<K,V> right=leaf.getRight();
            if(right.getsize()>1)
            {
            leaf.AddPairArray(right.getPairArray(0,right.getsize()/2));
            right.remove(0,right.getsize()/2);
            keys.set(RemovePos,right.getFirstKey());
            findReferenceParent(key,leaf.getFirstKey());
            }
            else
            {
                Keyremove(Position,Position+1);
                ChildPointers.remove(0);
                right.setLeft(leaf.getLeft());
                if(leaf.getLeft()!=null) leaf.getLeft().setRight(right);
                if(this.parent==null) 
                {   
                    leaf.parent=null;
                    right.parent=null;
                }
                else if(this.KeysSize()==0){ 
                    UnderFlowMerge(right.getFirstKey());  //funcitoin 이때 부모노드로부터 노드를 주고 child도 전해줌 
                                         //이작업이 연쇄적으로 일어날수 있기에 재귀함수
                }
            }
          }
          else if(Check<=0) //왼쪽에서 빌릴때
          { 
            LeafNode<K,V> left=leaf.getLeft();
            if(left!=null&&left.getsize()>1)
            {
            leaf.AddPairArray(left.getPairArray(left.getsize()/2,left.getsize()));
            left.remove(left.getsize()/2,left.getsize());
            keys.set(RemovePos,leaf.getFirstKey());
            }
            else
            {
                Keyremove(Position-1,Position);
                int pos=Position;
                ChildPointers.remove(pos);
                left.setRight(leaf.getRight());
                if(leaf.getRight()!=null) leaf.getRight().setLeft(left);
                if(this.parent==null) 
                {   
                    leaf.parent=null;
                }
                else if(this.KeysSize()==0){ 
                    UnderFlowMerge(left.getFirstKey()); 
                }
            }
          }
        }
        else{
            findReferenceParent(key,leaf.getFirstKey()); 
        }
    }
    return true;
}

private boolean isDeficient() {
    return this.keys.size()==0;
}

public void findReferenceParent(K key1, K key2) // 이때 key1는 바꿀키, key2는 바꿀값 
{  
   if(this.KeysSize()==0&&parent!=null) parent.findReferenceParent(key1, key2);
   if(KeysSize()==0) return;
   Integer Position=this.findRemovePosition(key1);
   Integer Check=keys.get(Position).compareTo(key1);
   if(Check!=0) { 
    if(parent==null) return;
    this.parent.findReferenceParent(key1,key2);
   }
   else {
       this.keys.set(Position, key2);
   }
}
}
