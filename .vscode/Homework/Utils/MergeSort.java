package Homework.Utils;

import Homework.Utils.Pair;
import Homework.Utils.BPlusTree.BPlusTree;
import Homework.Utils.BPlusTree.LeafIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

    

public class MergeSort {

    private ArrayList<ArrayList<Object>> array;
    private ArrayList<ArrayList<Object>> array2;
    private Object[][] doublearray;
    private Object[][] doublearray2;
    private Object[] array1_;
    private Object[] array2_;
    private int attr;
    Boolean type=true;//true일경우 수 false일경우문자열
    public MergeSort(ArrayList<ArrayList<Object>> array,int attr)
    {   this.attr=attr;
        doublearray=new Object[array.size()][];
        doublearray2=new Object[array.size()][];
        for(int i=0;i<array.size();i++)
        {
            doublearray[i]=array.get(i).toArray(new Object[array.get(i).size()]);
            doublearray2[i]=new Object[array.get(i).size()];
        }

        partition(0,array.size()-1);
    }
    public MergeSort(ArrayList<Object> singlearray)
    {  
        array1_=singlearray.toArray(new Object[singlearray.size()]);
        array2_=new Object[singlearray.size()];
        single_partition(0,singlearray.size()-1);
    }
    public Object[] get()
    {
        return array2_;
    }
    public Object[][] getdouble()
    {
        return doublearray;
    }
    public void run(ArrayList<ArrayList<Object>> array,int attr)
    {   this.attr=attr;
        this.array=array;
        single_partition(0,array.size()-1);
    }

    private void single_partition(int left, int right) {
        int mid;
	if (left < right)
	{
		mid = (left + right) / 2; 
		single_partition(left, mid);
		single_partition(mid + 1, right);
		single_merge(left, right);
	}
    }

    private void single_merge(int left, int right)
{
	int mid = (left + right) / 2;

	int i = left;
	int j = mid + 1;
	int k = left;
	while (i <= mid && j <= right)
	{
        if(array1_[i] instanceof Integer)
		{
        if ((Integer)array1_[i] <= (Integer)array1_[j]) 
        array2_[k++] = array1_[i++]; 
		else
        array2_[k++] = array1_[j++];
        }
        else{
            if(array1_[i].toString().compareTo(array1_[j].toString())<=0)
            array2_[k++] = array1_[i++]; 
            else
            array2_[k++] = array1_[j++];
        }
	}

	int tmp = i>mid ? j : i;
	
	while(k<=right) array2_[k++] = array1_[tmp++];

	for (int it=left;it<=right;it++) array1_[it] = array2_[it];
}
    public void partition(int left, int right)
    {   
    int mid;
	if (left < right)
	{
		mid = (left + right) / 2; 
		partition(left, mid);
		partition(mid + 1, right);
		merge(left, right);
	}
    }

    public void merge(int left, int right)
    {   
        int mid = (left + right) / 2;

	int i = left;
	int j = mid + 1;
	int k = left;
	while (i <= mid && j <= right)
	{
        if(doublearray[i][attr] instanceof Integer)
		{
        if ((Integer)doublearray[i][attr]<= (Integer)doublearray[j][attr]) 
        doublearray2[k++] = doublearray[i++]; 
		else
        doublearray2[k++] = doublearray[j++];
        }
        else{
            if(doublearray[i][attr].toString().compareTo(doublearray[j][attr].toString())<=0)
            doublearray2[k++] = doublearray[i++]; 
            else
            doublearray2[k++] = doublearray[j++];
        }
	}

	int tmp = i>mid ? j : i;
	
	while(k<=right) doublearray2[k++] = doublearray[tmp++];

	for (int it=left;it<=right;it++) doublearray[it] = doublearray2[it];
    }
   
}