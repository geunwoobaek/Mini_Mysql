package Homework.Components.StroageEngine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TableInfoBuffer implements DiskIo {

    private Map<String, String[]> tbinfo;
    private ArrayList<String> Origin; //write할것
    private String path = "/Users/user/Documents/GitHub/DP_2020_geunwoo/DP_2020_baek/.vscode/Homework/TableSpace/Tableinfo.txt";
    private int k = 0;

    public TableInfoBuffer() throws FileNotFoundException {
        tbinfo = new HashMap<String, String[]>();
        Origin = new ArrayList<String>();
        Read();
    }

    public void Save() throws FileNotFoundException {
        File file = new File(path);
        // 스캐너로 파일 읽기
        String str;
        Scanner scan = new Scanner(file);
        // tableinfo읽기
        while (scan.hasNextLine()) {
            str = scan.nextLine();
            String[] info = str.split(",");// tableinfo파일을split로읽음
            tbinfo.put(info[0], info); // info에넣음
        }
    }

    public Map<String, String[]> getMap() {
        return tbinfo;
    }

    public int value() {
        return k;
    }

    public String[] get(String Name) {
        return tbinfo.get(Name);
    }

    public void put(String info) {
        Origin.add(info);
        String[] Parsing = info.split(",");
        tbinfo.put(Parsing[0], Parsing);
    }

    public void delete(String Name) {
        tbinfo.remove(Name);
        for(int i=0;i<Origin.size();i++)
        {
            if(Origin.get(i).contains(Name))
            {
                Origin.remove(i);
                break;
            }
        }
    }

    public Integer total() {
        return tbinfo.size();
    }

    @Override
    public void Write() throws IOException {
       
        File tablefile = new File(path);
        BufferedWriter fos = new BufferedWriter(new FileWriter(tablefile,false));
        for(String data:Origin) 
        {    
            fos.write(data);
            fos.newLine();
        }
        fos.close(); // 파일을 닫는다.

    }

    @Override
    public void Read() throws FileNotFoundException {
        File file = new File(path);
        // 스캐너로 파일 읽기
        String str;
        Scanner scan = new Scanner(file);
        // tableinfo읽기
        while (scan.hasNextLine()) {
            str = scan.nextLine();
            Origin.add(str);
            String[] info = str.split(",");//tableinfo파일을split로읽음
            tbinfo.put(info[0],info); //info에넣음
        }
    }
    public void finalize() throws IOException
    {
        Write();
    }
}