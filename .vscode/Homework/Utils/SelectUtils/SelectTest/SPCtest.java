package Homework.Utils.SelectUtils.SelectTest;

import java.io.IOException;
import java.util.ArrayList;

import Homework.Components.StroageEngine.TableBufferHandler;
import Homework.Utils.SelectUtils.SelectProcessor;

public class SPCtest {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {

        TableBufferHandler table_Handler=new TableBufferHandler();
        ArrayList<String>[] Parsed_Message=new ArrayList[9];
        for(int i=0;i<9;i++) Parsed_Message[i]=new ArrayList<>();
        String Message="SELECT col1,col2 FROM TABLE3 WHERE col1 < 10000 AND col1 > 1000";
        String[] Parsed=Message.split(" ");
        Parsed_Message[0].add("col1");
        Parsed_Message[0].add("col3");
        //
        Parsed_Message[3].add("col1");
        Parsed_Message[3].add("<");
        Parsed_Message[3].add("10000");
        Parsed_Message[3].add("AND");
        Parsed_Message[3].add("col3");
        Parsed_Message[3].add("<");
        Parsed_Message[3].add("34992879");
        SelectProcessor selct=new SelectProcessor(Parsed_Message,table_Handler);
        String answer=selct.Run();
        System.out.println(answer);
        
    }
}