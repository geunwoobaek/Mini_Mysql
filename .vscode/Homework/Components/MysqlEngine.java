package Homework.Components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.lang.model.util.ElementScanner14;

import Homework.Components.StroageEngine.TableBufferHandler;
import Homework.Utils.Pair;
import Homework.Utils.SelectUtils.SelectProcessor;

public class MysqlEngine {
    private String[] command;
    private TableBufferHandler table_Handler;
    private ArrayList<Pair<String, String>> BinaryLog;
    private Pair<String, String> oneTransaction;
    private String message;
    private long starttime;

    public MysqlEngine() throws IOException, CloneNotSupportedException {
        table_Handler = new TableBufferHandler();
        message = new String();
        oneTransaction = new Pair<String, String>();
        BinaryLog = new ArrayList<Pair<String, String>>();
    }

    public void Query(String s) throws FileNotFoundException, CloneNotSupportedException {
        // Parsing
        message = null;
        oneTransaction.setindex(s);
        // s = s.replaceAll(">", " > ");
        // s = s.replaceAll("=", " = ");
        // s = s.replaceAll("<", " < ");
        String[] Fragments = s.split("\\s");
        // SELECT c1,c2,c3 FROM snoopy;
        starttime = System.currentTimeMillis();
        if (Fragments[0].equals("select"))
            Select(Fragments);
        else if (Fragments[0].equals("insert"))
            Insert(Fragments);
        else if (Fragments[0].equals("update"))
            Update(Fragments);
        else if (Fragments[0].equals("delete"))
            Delete(Fragments);
        else if (Fragments[0].equals("create"))
            Create(Fragments);
        else if (Fragments[0].equals("drop"))
            Drop(Fragments);
        else if (Fragments[0].equals("show"))
            ShowTables(Fragments);
        else
            message = "잘못된명령";
        long totalTime = System.currentTimeMillis() - starttime;
        double time = (double) totalTime / 1000;
        message += "\r\nresult time=" + time;
    }

    public void ShowTables(String[] Fragments) {
        message = table_Handler.List_of_Table();

        oneTransaction.setvalue(message);
        BinaryLog.add(new Pair(oneTransaction));
    }

    public void Select(String[] Fragments) {
        ArrayList<String>[] Parsed_Message = new ArrayList[11];
        Integer OrderOfNum = 1;
        Pair<Integer, Integer> pair = new Pair<Integer, Integer>(1, 4);
        for (int i = 0; i < 11; i++)
            Parsed_Message[i] = new ArrayList<String>();
        Parsed_Message[0].add(Fragments[3]);
        String[] fd = Fragments[1].split(",");
        for (int i = 0; i < fd.length; i++)
            Parsed_Message[4].add(fd[i]);
        Integer parse_num = 1;
        Integer Fragment_num = 4;
        Boolean check = true;
        while (Fragment_num<Fragments.length) {
            
            if (Fragments[Fragment_num].equals("JOIN")) {
                Parsed_Message[1].add(Fragments[Fragment_num + 1]);
                Parsed_Message[2].add(Fragments[Fragment_num - 1]);
                CommandSpilt(Parsed_Message[3], Fragments[Fragment_num + 3]);
                Fragment_num += 4;
       
                if(Fragment_num==Fragments.length) break;
            }
            if (Fragments[Fragment_num].equals("WHERE")) {
                CommandSpilt(Parsed_Message[5], Fragments[Fragment_num + 1]);
                Fragment_num += 2;
                if(Fragment_num==Fragments.length) break;
                if(Fragments[Fragment_num].equals("AND")||Fragments[Fragment_num].equals("OR"))
                {
                    CommandSpilt(Parsed_Message[5], Fragments[Fragment_num + 1]);
                    Fragment_num += 2;
                }
                if(Fragment_num==Fragments.length) break;
               
            }
            if (Fragments[Fragment_num].equals("GROUP")) {
                Parsed_Message[6].add(Fragments[Fragment_num + 2]);
                Fragment_num += 3;
               
                if(Fragment_num==Fragments.length) break;
            }
            if (Fragments[Fragment_num].equals("HAVING")) {
                CommandSpilt(Parsed_Message[7], Fragments[Fragment_num + 1]);
                Fragment_num += 2;
              
                if(Fragment_num==Fragments.length) break;
            }
            if (Fragments[Fragment_num].equals("ORDER")) {
                Parsed_Message[9].add(Fragments[Fragment_num + 2]);
                Fragment_num += 3;
             
                if(Fragment_num==Fragments.length) break;
            }
            if (Fragments[Fragment_num].equals("DESC")) {
                Parsed_Message[10].add(Fragments[Fragment_num++]);
                
                if(Fragment_num==Fragments.length) break;
            }
            Fragment_num++;
        }
        message = new SelectProcessor(Parsed_Message, table_Handler).Run();
        oneTransaction.setvalue(message);
        BinaryLog.add(new Pair(oneTransaction));
        // SELECT * FROM snoopy
        // SELECT c1,c2,c3 FROM snoopy;
        // • SELECT COUNT(*) FROM snoopy;
        // • SELECT c1,c2,c3 FROM snoopy WHERE c1=1;
        // • select c1,c2,c3 FROM snoopy where c1>1;
        // • SELECT c1,c2,c3 from snoopy ORDER BY c1;
        // SELECT c1,c2,c3 from snoopy WHERE c1 =1ORDER BY c1;
    }

    public void CommandSpilt(ArrayList<String> arrayList, String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '>' || str.charAt(i) == '=' || str.charAt(i) == '<') {
                arrayList.add(str.substring(0, i));
                arrayList.add(str.substring(i, i + 1));
                arrayList.add(str.substring(i + 1, str.length()));
                break;
            }
        }
    }
    
    public void Create(String[] Fragments) throws FileNotFoundException, CloneNotSupportedException {

        // Create TableName (int col1,int col2,int col3)
        // 4: 1 5: 2 6:3
        // Table2,4,icol1,icol2,icol3,icol4
        int total = Fragments.length - 3; // index제외 attribute수
        String info = new String();
        info += (Fragments[1] + ','); // TableName
        info += (Integer.toString(total) + ',');
        Character Type = Fragments[2].charAt(1);
        for (int i = 3; i < Fragments.length; i++) {
            int length = Fragments[i].length();
            for (int cursor = 0; cursor < length; cursor++) {
                if (Fragments[i].charAt(cursor) == ',' || Fragments[i].charAt(cursor) == ')') {
                    info += Type;
                    info += (Fragments[i].substring(0, cursor) + ',');
                    try {
                        Type = Fragments[i].charAt(cursor + 1);
                    } catch (Exception e) {
                    }

                    break;
                }
            }
        }
        info = info.substring(0, info.length() - 1);// 마지막문자삭제
        table_Handler.create(info);
        message = "Success to create" + Fragments[1];
        oneTransaction.setvalue(message);
        BinaryLog.add(new Pair(oneTransaction));
    }

    // DELETE FROM snoopy WHERE c1 = 3 and c2>3
    public void Delete(String[] Fragments) {

        message = table_Handler.delete(Fragments[2], CommandSpilt(Fragments[4]));
        oneTransaction.setvalue(message);
        BinaryLog.add(new Pair(oneTransaction));

    }

    // Drop TableName
    public void Drop(String[] Fragments) {
        table_Handler.DropTable(Fragments[1]);
        message = "Success to Drop" + Fragments[1];
        oneTransaction.setvalue(message);
        BinaryLog.add(new Pair(oneTransaction));
    }

    // INSERT snoopy 1,2,aaa;
    public void Insert(String[] Fragments) {
        try {
            table_Handler.getTable(Fragments[1]).insert_data(Fragments[2]);
            message = "Success to Insert";
        } catch (Exception e) {
            message = "it isn't exit.";
            System.out.println(e);
        }
        oneTransaction.setvalue(message);
        BinaryLog.add(new Pair(oneTransaction));
    }

    public void Update(String[] Fragments) { // 1:테이블이름,2:바꿀attribute,3:바꿀값 4:command[3] example c2>10

        // • UPDATE FROM snoopy SET c2 =10 WHERE c1 =1;
        // plus) UPDATE FROM snoopy SET c3 = 'bbb' WHERE c2 >10;
        message = table_Handler.Update(Fragments[2], Fragments[4], Fragments[6], CommandSpilt(Fragments[8]));
        oneTransaction.setvalue(message);
        BinaryLog.add(new Pair(oneTransaction));
    }

    public String ShowMessage() {
        return message;
    }

    public void flush() throws IOException {
        table_Handler.Write();
        File tablefile = new File(
                "/Users/user/Documents/GitHub/DP_2020_geunwoo/DP_2020_baek/.vscode/Homework/Components/StroageEngine/Binaryfile.txt");
        BufferedWriter fos = new BufferedWriter(new FileWriter(tablefile, false));
        for (int i = 0; i < BinaryLog.size(); i++) {
            fos.write(BinaryLog.get(i).index());
            fos.newLine();
            fos.write(BinaryLog.get(i).value());
            fos.newLine();
        }
        fos.close(); // 파일을 닫는다.
    }

    public String[] CommandSpilt(String str) {
        String[] command = new String[3];
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '>' || str.charAt(i) == '=' || str.charAt(i) == '<') {
                command[0] = str.substring(0, i);
                command[1] = str.substring(i, i + 1);
                command[2] = str.substring(i + 1, str.length());
                break;
            }
        }
        return command;
    }


}