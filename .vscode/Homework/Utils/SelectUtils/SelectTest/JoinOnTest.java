package Homework.Utils.SelectUtils.SelectTest;

import java.io.IOException;
import java.util.ArrayList;

import Homework.Components.StroageEngine.TableBufferHandler;
import Homework.Utils.SelectUtils.SelectProcessor;

public class JoinOnTest {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        TableBufferHandler table_Handler=new TableBufferHandler();
        ArrayList<String>[] Parsed_Message=new ArrayList[11];
        for(int i=0;i<11;i++) Parsed_Message[i]=new ArrayList<>();
        //Select Table2.col1,Table4.col2 From Table2 Join Table 4 Inner Table2.col2=Table4.col2
        Parsed_Message[0].add("Table2");
        Parsed_Message[1].add("Table4");
        Parsed_Message[2].add("RIGHT");
        Parsed_Message[3].add("Table2.col2");
        Parsed_Message[3].add("=");
        Parsed_Message[3].add("Table4.col2");
        Parsed_Message[4].add("Table2.col1");
        Parsed_Message[4].add("Table4.col2");
        String answer=new SelectProcessor(Parsed_Message, table_Handler).Run();
        System.out.println(answer);
        
    }
}