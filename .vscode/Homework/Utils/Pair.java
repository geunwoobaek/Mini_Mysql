package Homework.Utils;

import java.util.LinkedList;
import java.util.List;

public class Pair<T1,T2>{
	
	private T1 index;
	private T2 value;
	public Pair() {
	}
	public Pair(T1 y, T2 x) {
		this.index = y;
		this.value = x;
	}
	public Pair(Pair<T1,T2> other) {
		this.index = other.index;
		this.value = other.value;
	}
	public T1 index() {
		return index;
	}
	public T2 value() {
		return value;
	}
	public void setindex(T1 index) {
		this.index=index;
	}
	public void setvalue(T2 value) {
		this.value=value;
	}
	public void change_second(T2 second) {
        this.value=second;
	}
}