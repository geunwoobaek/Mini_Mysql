package Homework.Components.StroageEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.crypto.Data;
import Homework.Utils.MergeSort;
import Homework.Utils.Pair;
import Homework.Utils.BPlusTree.BPlusTree;
import Homework.Utils.BPlusTree.BinarySearch;
import Homework.Utils.BPlusTree.LeafIterator;
import Homework.Utils.BPlusTree.PairBinarySearch;

//TableInfo.txt=Paramnum,row_num,Paramtype(,,,),DATA
//Table 데이터 접근방식 
//
public class TableBuffer<K extends Comparable<K>> implements DiskIo {
    private String[] info;// table이름//attribute갯수//Col....
    private String path = "/Users/user/Documents/GitHub/DP_2020_geunwoo/DP_2020_baek/.vscode/Homework/TableSpace/";
    private BPlusTree<K,ArrayList<Object>> DataSet;
    private Map<String, Integer> attribute; // key=attribute value=number //index포함
    private ArrayList<String> Origin;// 파싱안한데이터
    //private ArrayList<Pair>[] Sort_by_Attribute; // [1][2] [1]의경우는 attribute의 순번 이때 index제외
    // Pair(인덱스 값,해당 컬럽의 속성값)

    public TableBuffer(String[] info) throws FileNotFoundException, CloneNotSupportedException {
        int k = Integer.parseInt(info[1]);
        path += (info[0] + ".txt");
        this.info = info;
        DataSet = new BPlusTree<K, ArrayList<Object>>(50001);
    //    index = new ArrayList<Object>();
        attribute = new HashMap<String, Integer>();
        for (int i = 2; i < info.length; i++) {
            attribute.put(info[i].substring(1), i-2);
        }
        Read();
    }
    public TableBuffer() {
       // index = new ArrayList<Object>();
        DataSet = new BPlusTree<K, ArrayList<Object>>(50001);
        attribute = new HashMap<String, Integer>();
    }
    public int total() {
        return DataSet.getsize();
    }
    public ArrayList<K> getIndexList()
    {
        return DataSet.getIndexList();
    }
    public ArrayList<Pair<K, ArrayList<Object>>> getDataSetList()
    {
        return DataSet.getDataSetArrayList();
    }
    public Integer order_of_attribute(String name) //insert info
    {
        return attribute.get(name);
    }

    // command={1,2,3,4,5}
    public void insert_data(String command) throws CloneNotSupportedException {
        //redobuffer에추가
        String[] Data = command.split(",");
        ArrayList<Object> DataArray = new ArrayList<Object>(); //index포함데이터넣기
        for (int i = 0; i < Data.length; i++) {
            if (info[2 + i].charAt(0) == 'i')
                    DataArray.add(Integer.parseInt(Data[i]));
            else
                DataArray.add(Data[i]);
        }
        DataSet.put((K)DataArray.get(0), DataArray);
    }
    public Boolean compare (String attr,Integer number,String value)
    {
         Pair pair;
         int num=attribute.get(attr);
         char type=info[2+num].charAt(0);
         Object tar;
         if(type=='i') tar=Integer.parseInt(value);
         else tar=value;
        //  Object val=Sort_by_Attribute[num].get(number).value();
         return tar==value;
    }

     public Object get_Index(int i) {
         return DataSet.getindex(i);
     }

    public ArrayList<Object> getData(Object i) {
        return DataSet.get((K) i);
    }

    public BPlusTree<K, ArrayList<Object>> getDataSet() {
        return DataSet;
    }

    public Integer get_attribute(String str) { //전체attribute
        return attribute.get(str);
    }
    public Object getDatabyAttribute(String attr, Integer i) { // 전체attribute
        int order=attribute.get(attr);
        return DataSet.get((K)get_Index(i)).get(order);
    }
    public void setDatabyAttribute(String attr,Integer i,Object value) { //전체attribute
        int order=attribute.get(attr);
        DataSet.get((K)get_Index(i)).set(order,value); 
    }
    public Character get_type_of_attribute(String str)
    {
        return info[2+get_attribute(str)].charAt(0);
    }
    @Override
    public void Write() throws IOException {
        // 생성할 파일의 경로 및 파일명 으로 File 객체 생성
        File tablefile = new File(path);
        BufferedWriter fos = new BufferedWriter(new FileWriter(tablefile, false));
        LeafIterator first=new LeafIterator(DataSet);
        while(first.hasNext())
        {
            Pair data=first.Next();
            ArrayList<Object> record= (ArrayList<Object>) data.value();
            for(int i=0;i<record.size();i++)
            {
                fos.write(record.get(i).toString()+",");
            }
            fos.newLine();
        }
        fos.close(); // 파일을 닫는다.

    }

    @Override
    public void Read() throws FileNotFoundException, CloneNotSupportedException {
        // tableread
        File tablefile = new File(path);
        String table_str;
        Scanner tb_scan = new Scanner(tablefile);
        int now_index = 0;
        // 테이블읽는작업
        while (tb_scan.hasNextLine()) {
            table_str = tb_scan.nextLine();
            String[] tbinfo = table_str.split(","); // 테이블파일을 split로읽음
            ArrayList<Object> obj = new ArrayList<Object>();
        
            for (int i = 0; i < tbinfo.length; i++) { //data넣기
                if(info[2+i].charAt(0)=='i') obj.add(i, Integer.parseInt(tbinfo[i]));
                else obj.add(i,tbinfo[i]);
                // Sort_by_Attribute[i].add(new Pair(obj.get(0),obj.get(i)));
            }
            DataSet.put((K) obj.get(0), obj);// DataSet에 Data추가
        }
    }
    public void Sorting(String attrname)
    {
        int num=attribute.get(attrname);
        // new MergeSort(Sort_by_Attribute[num]);
    }

    public void finalize() throws IOException
    {
        Write();
    }

	public void remove_data(Object index_) {
     ArrayList DataArray=DataSet.get((K) index_);
     DataSet.remove((K) index_);
     
    }

	public ArrayList<Object> get(Object key) {
		return DataSet.get((K)key);
	}
	public int get_attribute_size() {
		return attribute.size();
	}
}
