package Homework.Utils.BPlusTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import Homework.Utils.Pair;

public class LeafNode<K extends Comparable<K>, V> extends Node {
	private int MaxDegree;
	private LeafNode<K, V> leftLeaf;
	private LeafNode<K, V> RightLeaf;
	private ArrayList<Pair<K, V>> KeyDataPairs;
	private Integer ChildPointerNumber;

	public LeafNode(int maxdegree, K key, V value, int num) {
		leftLeaf = null;
		RightLeaf = null;
		this.MaxDegree = maxdegree;
		KeyDataPairs = new ArrayList<Pair<K, V>>();
		KeyDataPairs.add(new Pair<K, V>(key, value));
		this.ChildPointerNumber = num;
	}

	public LeafNode(int maxdegree, ArrayList<Pair<K, V>> Copy, int num) {
		leftLeaf = null;
		RightLeaf = null;
		this.MaxDegree = maxdegree;
		KeyDataPairs = Copy;
		this.ChildPointerNumber = num;
	}

	public void SetChildNumber(Integer num) {
		ChildPointerNumber = num;
	}

	public Integer GetChildNumber() {
		return ChildPointerNumber;
	}

	public Integer insert(K key, V value) {
		int Position = new LeafNodeSearch<K, V>(this.KeyDataPairs).LowerBoundIndex(key);
		int check = KeyDataPairs.get(Position).index().compareTo(key);
		if (check == 0)
			return -1;
		else if (check < 0)
			Position++;
		KeyDataPairs.add(Position, new Pair<K, V>(key, value));
		return Position;

	}

	public boolean isfull() {
		return KeyDataPairs.size() == MaxDegree;
	}

	public LeafNode<K, V> split(Integer Start, Integer End, int num) {
		ArrayList<Pair<K, V>> CopyPair = new ArrayList<Pair<K, V>>();
		Integer Point = 0;
		for (int i = Start; i < End; i++) {
			CopyPair.add(new Pair<K, V>(KeyDataPairs.get(i).index(), KeyDataPairs.get(i).value()));
		}
		LeafNode<K, V> copy = new LeafNode<K, V>(MaxDegree, CopyPair, num);
		return copy;
	}

	public V find(Object key) {
		return null;
	}
	public void InitLeaf()
	{
		leftLeaf=null;
	}
	public K getFirstKey() {
		return KeyDataPairs.get(0).index();
	}
	public K getKey(int i) {
		return KeyDataPairs.get(i).index();
	}
	public Pair<K, V> getPair(Integer index) {
		return KeyDataPairs.get(index);
	}

	public void addPair(Pair<K, V> onePair) {
		KeyDataPairs.add(onePair);
	}

	public void setLeft(LeafNode<K, V> left) {
		this.leftLeaf = left;
	}

	public void setRight(LeafNode<K, V> right) {
		this.RightLeaf = right;
	}

	public LeafNode<K, V> getLeft() {
		return leftLeaf;
	}

	public LeafNode<K, V> getRight() {
		return RightLeaf;
	}

	public ArrayList<Pair<K, V>> getkeyDatPairs() {
		return KeyDataPairs;
	}

	public void remove(int start, int end) {
		KeyDataPairs.subList(start, end).clear();
	}

	public Boolean remove(K key) {
		Integer Point = new LeafNodeSearch<K, V>(KeyDataPairs).FindSameIndex(key);
		if (Point == -1)
			return false;
		remove(Point, Point + 1);
		return true;
	}

	public Integer getsize() {
		return KeyDataPairs.size();
	}

	public Integer getRightTotalSize() {
		if (RightLeaf != null)
			return getsize() + RightLeaf.getRightTotalSize();
		else
			return getsize();
	}
	public V LeafSearch(K key) {
		if (KeyDataPairs.size() != 0) {
			try {
				Integer Point = new LeafNodeSearch<K, V>(KeyDataPairs).LowerBoundIndex(key);
				int check = key.compareTo(KeyDataPairs.get(Point).index());
				if (check == 0)
					return KeyDataPairs.get(Point).value();
				else if (check > 0 && getRight() != null)
					return getRight().LeafSearch(key);
				else
					return null;
			} catch (Exception e) {
				// System.out.println();
			}
		}
		return null;
	}
	public Pair<Integer,LeafNode<K,V>> LeafCurrentSearch(K key) {
		if (KeyDataPairs.size() != 0) {
			try {
				Integer Point = new LeafNodeSearch<K, V>(KeyDataPairs).LowerBoundIndex(key);
				int check = key.compareTo(KeyDataPairs.get(Point).index());
				if (check <= 0)
					return new Pair(Point,this);
				else if (check > 0 && getRight() != null)
					return getRight().LeafCurrentSearch(key);
				else
					return new Pair(getsize()-1,this);
			} catch (Exception e) {
				// System.out.println();
			}
		}
		return null;
	}
	public void printall() {
		for (int i = 0; i < KeyDataPairs.size(); i++) {
			System.out.print(KeyDataPairs.get(i).index() + ",");
		}
		if (RightLeaf != null)
			RightLeaf.printall();
	}

	public boolean isDeficient() {
		return getsize() == 0;
	}

	public void AddPairArray(List<Pair<K, V>> aList) {
		KeyDataPairs.addAll(aList);
	}

	public List<Pair<K, V>> getPairArray(Integer start, Integer end)
	{
		return KeyDataPairs.subList(start, end);
	}

	public void printBackwards(int k) {

		if(this.RightLeaf==null||k==1)
		{
			for(int i=getsize()-1;i>=0;i--)
			{
				System.out.print(KeyDataPairs.get(i).index()+",");
			}
			if(this.leftLeaf!=null) leftLeaf.printBackwards(1);
		}
		else{
			RightLeaf.printBackwards(0);
		}
	}
}
