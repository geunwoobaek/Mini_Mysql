package Homework.testfolder;

import java.io.FileNotFoundException;
import java.io.IOException;

import Homework.Components.MysqlEngine;

public class EngineTest {
    private static MysqlEngine Engine;
    private static String message;

    public static void main(String[] args) throws IOException, CloneNotSupportedException {

        Engine = new MysqlEngine();
        // CreateTest();
        // InsertTest();
        UpdateTest();
        SelectTest();
        DeleteTest();
        DropTest();
        Engine.flush();
    }

    public static void CreateTest() throws FileNotFoundException, CloneNotSupportedException {
        Engine.Query("create Table3 (int col1,int col2,int col3) ");
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST1]: create Table3 (int col1,int col2,int col3) \r\n" + message);
        Engine.Query("create Table3 (int col1,int col2,int col3) ");
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST2]: create Table5 (int col1,int col2,int col3) \r\n" + message);
    }

    public static void DropTest() throws FileNotFoundException, CloneNotSupportedException {
        Engine.Query("drop Table5  ");
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST14]: drop Table5 \r\n" + message);
    }

    public static void DeleteTest() throws FileNotFoundException, CloneNotSupportedException {
        System.out.println("DELETE TEST-----------------------------------------------");
        Engine.Query("delete FROM Table3 WHERE col1<5678");
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST13]: delete FROM Table3 WHERE col1>12345678\r\n" + message);
    }

    public static void UpdateTest() throws FileNotFoundException, CloneNotSupportedException {
        Engine.Query("update FROM Table3 SET col2 = 18 WHERE col1>1323322");
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST1]: update FROM Table3 SET col2 = 18 WHERE col1>1323322\r\n" + message);
    }

    public static void SelectTest() throws FileNotFoundException, CloneNotSupportedException {
        Engine.Query("select col1,col2,col3 FROM Table4 WHERE col1=11");
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST2]: select col1,col2,col3 FROM Table4 WHERE col1=11" + "\r\n" + message);
        Engine.Query("select * FROM Table4 WHERE col1>5 AND col1 <30");
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST3]: select * FROM Table4 WHERE col1>5 AND col1 <30" + "\r\n" + message);
        Engine.Query("select COUNT(col1),col2,col3 FROM Table4");
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST4]: select AVG(col1),col2,col3 FROM Table4" + "\r\n" + message);
        Engine.Query("select col1,col2,col3 FROM Table3 WHERE col1>9900 ORDER BY col3 DESC");
        message = Engine.ShowMessage();
        System.out.println(
                "\r\n[TEST5]: select col1,col2,col3 FROM Table3 WHERE col1>9900 ORDER BY col3 DESC" + "\r\n" + message);
        JoinTest();
        GroupByTest();

    }

    public static void InsertTest() throws FileNotFoundException, CloneNotSupportedException {
        for (int i = 0; i < 100000; i++) {
            String message = "insert Table3 " + (int) (Math.random() * 123456789) + ",2,"
                    + (int) (Math.random() * 123456789);
            Engine.Query(message);
        }
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST3] :insert Table3 100000 RanDomData\r\n" + message);
        message = "insert Table1 19,19,hedoe,geuswoo,4";
        Engine.Query(message);
        message = Engine.ShowMessage();
        System.out.println("\r\n[TEST4]:insert Table1 19,19,hedoe,geuswoo,4 \r\n" + message);
    }

    public static void JoinTest() throws FileNotFoundException, CloneNotSupportedException {
        System.out.println("JOIN TEST------------------------------------------------");
        Engine.Query(
                "select Table2.col2,Table4.col2 FROM Table2 INNER JOIN Table4 ON Table2.col2=Table4.col2 WHERE col1>5");
        message = Engine.ShowMessage();
        System.out.println(
                "\r\n[TEST6]: select Table2.col2,Table4.col2 FROM Table2 INNER JOIN Table4 ON Table2.col2=Table4.col2 WHERE col1>5\r\n"
                        + message);
        Engine.Query(
                "select Table2.col1,Table2.col2,Table4.col2 FROM Table2 LEFT JOIN Table4 ON Table2.col2=Table4.col2 WHERE col1>10 ORDER BY Table2.col1 DESC");
        message = Engine.ShowMessage();
        System.out.println(
                "\r\n[TEST7]: select Table2.col1,Table2.col2,Table4.col2 FROM Table2 LEFT JOIN Table4 ON Table2.col2=Table4.col2 WHERE col1>10 ORDER BY Table2.col1 DESC\r\n"
                        + message);
        Engine.Query(
                "select Table4.col1,Table2.col2,Table4.col2 FROM Table2 RIGHT JOIN Table4 ON Table2.col2=Table4.col2 WHERE col1>10 ORDER BY Table4.col2");
        message = Engine.ShowMessage();
        System.out.println(
                "\r\n[TEST8]: select Table4.col1,Table2.col2,Table4.col2 FROM Table2 RIGHT JOIN Table4 ON Table2.col2=Table4.col2 WHERE col1>10 ORDER BY Table4.col2\r\n"
                        + message);
    }

    public static void GroupByTest() throws FileNotFoundException, CloneNotSupportedException {
        System.out.println("GROUPBY TEST------------------------------------------------------------------");
        Engine.Query("select col2 FROM Table2 WHERE col1>5 GROUP BY col2 HAVING COUNT(col2)>2");
        message = Engine.ShowMessage();
        System.out.println(
                "\r\n[TEST9].[select col2 FROM Table2 WHERE col1>5 GROUP BY col2 HAVING COUNT(col2)>2]\r\n" + message);
        Engine.Query("select col2,SUM(col1) FROM Table4 WHERE col1>5 GROUP BY col2 HAVING COUNT(col2)>2");
        message = Engine.ShowMessage();
        System.out.println(
                "\r\n[TEST10].[select col2,SUM(col1) FROM Table4 WHERE col1>5 GROUP BY col2 HAVING COUNT(col2)>2]\r\n"
                        + message);
        Engine.Query("select col2,COUNT(col1) FROM Table4 WHERE col1>5 GROUP BY col2 HAVING COUNT(col2)>2");
        message = Engine.ShowMessage();
        System.out.println(
                "\r\n[TEST11].[select col2,COUNT(col1) FROM Table4 WHERE col1>5 GROUP BY col2 HAVING COUNT(col2)>2]\r\n"
                        + message);
        Engine.Query("select COUNT(col1) FROM Table3 WHERE col1>5 GROUP BY col2 HAVING COUNT(col2)>2");
        message = Engine.ShowMessage();
        System.out.println(
                "\r\n[TEST12].[select COUNT(col1) FROM Table3 WHERE col1>5 GROUP BY col2 HAVING COUNT(col2)>2]\r\n"
                        + message);
    }
}
