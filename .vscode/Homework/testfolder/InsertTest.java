package Homework.testfolder;

import java.io.IOException;

import Homework.Components.StroageEngine.TableBuffer;
import Homework.Components.StroageEngine.TableBufferHandler;

public class InsertTest {
        public static void main(String[] args) throws IOException, CloneNotSupportedException {
            TableBufferHandler Tbs=new TableBufferHandler();
            TableBuffer table=Tbs.getTable("Table1");
            int a=2,b=4;
            for(int i=6;i<100000;i++)
            {   
                String command=i+","+a+",a,b,"+b; //int,int,a,b,int
                table.insert_data(command);
                a--;
                b--;
            }
            table.Write();
            System.out.println();   
           }
        }
