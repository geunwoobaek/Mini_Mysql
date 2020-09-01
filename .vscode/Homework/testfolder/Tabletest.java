package Homework.testfolder;

import java.io.IOException;

import Homework.Components.StroageEngine.TableBuffer;
import Homework.Components.StroageEngine.TableBufferHandler;

public class Tabletest {
    public static void main(String[] args) throws IOException {
        TableBufferHandler tb=new TableBufferHandler();
        TableBuffer table=tb.getTable("Table1");
        for(int i=0;i<5;i++)
        {
            Object o=table.get_Index(i);
            System.out.println(table.getData(o));
        }
        
    }
}
