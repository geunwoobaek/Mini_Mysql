package Homework.testfolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import Homework.Components.StroageEngine.TableBuffer;
import Homework.Components.StroageEngine.TableBufferHandler;

public class LoadTest {

    public static void main(String[] args) throws IOException {
        TableBufferHandler Tbs=new TableBufferHandler();
        Map<String, TableBuffer> tables=Tbs.getMap();
        Iterator<String> keys = tables.keySet().iterator();
            while( keys.hasNext() ){
                String key = keys.next();
                TableBuffer tb=tables.get(key);
                ArrayList<Object> index=tb.getIndexMap();
                Map<Object,ArrayList<Object>> Data=tb.getDataSet();
                System.out.println("table이름은 "+key);
                for(int i=0;i<index.size();i++)
                {   
                    System.out.println("index="+index.get(i)+Data.get(index.get(i).toString()));
                }
            }
    }
}
//   