package Homework.Utils.SelectUtils.SelectTest;

import java.io.IOException;
import java.util.ArrayList;

import Homework.Components.StroageEngine.TableBufferHandler;
import Homework.Utils.SelectUtils.SelectProcessor;

public class Groupbytest {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
    TableBufferHandler table_Handler=new TableBufferHandler();
    ArrayList<String>[] Parsed_Message=new ArrayList[11];
    String[] str=new String[10];
    System.out.println(str[0]);
    for(int i=0;i<11;i++) Parsed_Message[i]=new ArrayList<>();
    Parsed_Message[0].add("Table2");
    Parsed_Message[2].add("col2");
    Parsed_Message[2].add("SUM(col1)");
    Parsed_Message[6].add("col2");
    Parsed_Message[7].add("COUNT");
    Parsed_Message[7].add("col2");
    Parsed_Message[7].add(">");
    Parsed_Message[7].add("2");
    SelectProcessor selct=new SelectProcessor(Parsed_Message,table_Handler);
        String answer=selct.Run();
        System.out.println(answer);
}
}