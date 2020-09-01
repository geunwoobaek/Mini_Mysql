package Homework.Components.StroageEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import Homework.Utils.Eval;
import Homework.Utils.MergeSort;
import Homework.Utils.Pair;
import Homework.Utils.BPlusTree.LeafIterator;
import Homework.Utils.BPlusTree.PairBinarySearch;


public class TableBufferHandler implements DiskIo {
    private Map<String, TableBuffer> Tables; 
    private TableInfoBuffer Tbinfo;
    private String path = "/Users/user/Documents/GitHub/DP_2020_geunwoo/DP_2020_baek/.vscode/Homework/TableSpace/";


    public TableBufferHandler() throws IOException, CloneNotSupportedException {
        Tables = new HashMap<>();
        Tbinfo=new TableInfoBuffer();
        Read();
        // info= read tableinfo;
        // spilt parsetoint
    }

    //테이블추가하기
    public void put_table(String tablename,TableBuffer table)
    {
        Tables.put(tablename,table);
    }
    //테이블들사이즈반환
    public int total()
    {
        return Tables.size();
    }
    //개별테이블반환
    public TableBuffer getTable(String tablename)
    {
        return Tables.get(tablename);
    }
    //Tablesclass반환
    public Map<String, TableBuffer> getMap()
    {
        return Tables;
    }
    public String List_of_Table()
    {
        String list=new String();
        Iterator<String> keys = Tables.keySet().iterator();
        while( keys.hasNext() ){
            list+=(keys.next()+",");
        }
        return list;
    }
     //3,icol1,icol2,icol3
    public void create(String info_) throws FileNotFoundException, CloneNotSupportedException
	{   //info : TableName, colum1,colum2,colum3,colum4....
        String[] info = info_.split(",");//tableinfo파일을split로읽음 //icol1 icol2 icol3
        String path="/Users/user/Documents/GitHub/DP_2020_geunwoo/DP_2020_baek/.vscode/Homework/TableSpace/";
        String Tablepath=path+info[0]+".txt";
        OutputStream output = new FileOutputStream(Tablepath);
        TableBuffer tb;
        if(info[2].charAt(0)=='i') tb=new TableBuffer<Integer>(info);
        else tb=new TableBuffer<String>(info);
        Tables.put(info[0], tb);
        Tbinfo.put(info_);
    }
    //테이블삭제
    public String DropTable(String tablename)
    {
        Tables.remove(tablename);
        String filepath = path+tablename+".txt";
        File deleteFile = new File(filepath);
        deleteFile.delete(); 

        Tbinfo.delete(tablename);
        return "Success to Drop "+tablename;
    }



     @Override
     public void Read() throws FileNotFoundException, CloneNotSupportedException {
      
        Map<String, String[]> Info=Tbinfo.getMap();
        Iterator<String> keys = Info.keySet().iterator();
        while( keys.hasNext() ){
         String key=keys.next();
            TableBuffer table;
            if(Info.get(key)[2].charAt(0)=='i') table=new TableBuffer<Integer>(Info.get(key));
            else table=new TableBuffer<String>(Info.get(key));
            Tables.put(Info.get(key)[0],table);
          }
     }


     @Override
     public void Write() throws IOException {
        Iterator<String> keys = Tables.keySet().iterator();
        while( keys.hasNext() ){
            String key=keys.next();
            try {
                Tables.get(key).Write();   
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        Tbinfo.Write();
     }
     //1:테이블이름,2:바꿀attribute,3:바꿀값 4:command[3] example c2>10
	public String Update(String tablename, String attribute, String value, String[] command) {
        TableBuffer table=Tables.get(tablename);
        Character type=table.get_type_of_attribute(command[0]);
        Integer number=0;
        Object v2;
        if(type=='i') {v2=Integer.parseInt(command[2]);
        }
        else {v2=command[2];}
        int numberofdata=0;
        Eval eval=new Eval(v2,command[1]);
        if(command.length!=0){
            LeafIterator iterator=new LeafIterator<>(table.getDataSet());
            int Condition_num=table.get_attribute(command[0]);
            int Specific_number=table.get_attribute(attribute);
            Object Change_value;
            if(table.get_type_of_attribute(attribute)=='i')
            Change_value=Integer.parseInt(value);
            else Change_value=value;
            while(iterator.hasNext())
            {    
                Pair<Object,ArrayList<Object>> record=iterator.Next();
                Object col= record.value().get(Condition_num);
                if(col instanceof String)
                {
                    if(eval.result(col.toString())== true) 
                        {numberofdata++;record.value().set(Specific_number,Change_value);}
                }
                else{
                    if(eval.result((Integer)col)== true) 
                        {numberofdata++;record.value().set(Specific_number,Change_value);}
                }
            }
        }
        return(numberofdata+"data update");
    }
         //DELETE FROM snoopy WHERE c1 =3;
     //Dataset을 삭제해야하고 key값을삭제
     public String delete(String tablename, String[] cond) {
                TableBuffer table=Tables.get(tablename);
                String answer=new String();
                int Min=0;
                int Max=table.total();
                boolean onoff=false;
                Integer num=0;
                Integer attrnum=table.get_attribute(cond[0]);
                char type=table.get_type_of_attribute(cond[0]);
                Object target;
                if(type=='i') target=Integer.parseInt(cond[2]);
                else target=cond[3];
                String Operator=cond[1];
                Eval eval=new Eval(target, Operator);
                LeafIterator iterator=new LeafIterator<>(table.getDataSet());
                while(iterator.hasNext())
                {    
                    Pair<Object,ArrayList<Object>> record=iterator.Next();
                    Object col= record.value().get(attrnum);
                    if(col instanceof String)
                    {
                        if(eval.result(col.toString())== true) {num++;table.remove_data(record.index());
                            iterator.before();}
                    }
                    else{
                        if(eval.result((Integer)col)== true) {num++;table.remove_data(record.index());
                            iterator.before();
                        }
                    }
                }
                
                
        return(num+"data delete");
    }
    //From Where를처리함
    public  ArrayList<ArrayList<Object>> DataLoad(String TableName,String[] Where)
    {
        TableBuffer table=Tables.get(TableName);
        ArrayList<ArrayList<Object>> newdatalist=new ArrayList<ArrayList<Object>>();
        LeafIterator iterator=new LeafIterator<>(table.getDataSet());
        int Min=0;
        int Max=table.total();
        if(Where!=null)
        {
        String Operator=Where[1];
        Integer attrnum=table.get_attribute(Where[0]);
        char type=table.get_type_of_attribute(Where[0]);
        Object target;
        if(type=='i') target=Integer.parseInt(Where[2]);
        else target=Where[2];
        Eval eval=new Eval(target, Where[1]);
        while(iterator.hasNext())
        {    
            Pair<Object,ArrayList<Object>> record=iterator.Next();
            Object col= record.value().get(attrnum);
            if(col instanceof String)
            {
                if(eval.result(col.toString())== true) newdatalist.add(record.value());
            }
            else{
                if(eval.result((Integer)col)== true) newdatalist.add(record.value());
            }
        }
        }
        else{
            while(iterator.hasNext())
            {    
                Pair<Object,ArrayList<Object>> record=iterator.Next();
                newdatalist.add(record.value());
            }
        }
        return newdatalist;
    }

}