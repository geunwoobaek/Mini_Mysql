package Homework.Utils.SelectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.GroupLayout.Group;

import Homework.Components.StroageEngine.TableBuffer;
import Homework.Components.StroageEngine.TableBufferHandler;
import Homework.Utils.Eval;
import Homework.Utils.MergeSort;
import Homework.Utils.Pair;
import Homework.Utils.BPlusTree.LeafIterator;

public class SelectProcessor {
    private ArrayList<ArrayList<Object>> RecordSet;
    private ArrayList<ArrayList<Object>> OnRecordSet;
    private ArrayList<Object> finalRecord;
    private Boolean GroupOrderCheck = false;
    private TableBufferHandler handler;
    private TableBuffer table;
    private TableBuffer JoinTable;
    private TableBuffer OrderTable;
    private String[] OwnerofAttribute;
    private Map<Object, ArrayList<ArrayList<Object>>> GroupMap;
    private Map<Object, ArrayList<ArrayList<Object>>> JoinGroupMap;
    private ArrayList<CalculateMap> GroupattrMap;
    private ArrayList<Integer> Cal_attr;// Count,SUm계산할attr들
    private ArrayList<Integer> OnCal_attr;// Count,SUm계산할attr들
    private ArrayList<String>[] Parsed_Message;
    private ArrayList<LeafIterator> iteratorList;
    private Boolean ScanMethod = false; // 0이면 Iterating방식 1이면 인덱싱
    private Boolean SelectMode = false;
    private ArrayList<Integer> WhereNumber; // where에 해당하는 attribute의 number
    private String TableName;
    private ArrayList<String> Attribute;
    private ArrayList<String> Join; // Left,Right,Inner,Full
    private ArrayList<String> On;
    private ArrayList<String> Where;
    private ArrayList<String> Group;
    private ArrayList<String> Having; // COUNT TABLENAME colum명
    private ArrayList<String> DISTINCT;
    private ArrayList<String> ORDERBY;
    private ArrayList<String> ORDERFORM;
    private Integer StandardAttriubte;
    private ArrayList<Integer> Attrnum;
    private ArrayList<Integer> OnAttrnum;
    private Integer GroupBynum;
    private Integer TableAttrnum;
    private Integer JoinTableAttrnum;
    private Integer ORDERNUM;
    private String answer;
    private Integer OnMode;
    private Integer numberofRecord = 0;

    // ArrayList[]
    // Arraylist[0]=123,[1]='>',[2]=213,
    // Message= String[0]=FROM:TABLE
    // Message= String[1]=JoinTable
    // Message= String[2]=JOIN종류
    // Message= String[3]=On관련 조건들
    // Message= String[4]=Attribute
    // Message= String[5]=WHERE:1>2AND2>3 , 2<3OR3>4
    // message= String[6]=GROUP
    // Message= String[7]=Having
    // Message= String[8]=DISTINCT
    // Message= String[9]=ORDERBY
    // message= String[10]=ORDERFORM
    public SelectProcessor(ArrayList<String>[] Parsed_Message, TableBufferHandler handler) {
        this.handler = handler;
        RecordSet = new ArrayList<ArrayList<Object>>();
        OnRecordSet = new ArrayList<ArrayList<Object>>(); // OnRecord;
        finalRecord = new ArrayList<Object>();
        answer = new String();
        int i = 0;
        TableName = Parsed_Message[0].get(0);
        this.table = handler.getTable(Parsed_Message[i++].get(0));
        if (Parsed_Message[i].size() != 0)
            this.JoinTable = handler.getTable(Parsed_Message[i].get(0));
        i++;
        Join = Parsed_Message[i++]; // 2
        On = Parsed_Message[i++]; // 3
        Attribute = Parsed_Message[i++]; // 4
        Where = Parsed_Message[i++]; // 5
        Group = Parsed_Message[i++]; // 6
        Having = Parsed_Message[i++]; // 7
        DISTINCT = Parsed_Message[i++]; // 8
        ORDERBY = Parsed_Message[i++]; // 9
        ORDERFORM = Parsed_Message[i++]; // 10
        WhereNumber = new ArrayList<>();
        iteratorList = new ArrayList<>();
        Attrnum = new ArrayList<>();
        OnAttrnum = new ArrayList<>();
        Cal_attr = new ArrayList<Integer>();
        OwnerofAttribute = new String[Attribute.size()];
        GroupMap = new HashMap<>();
        if (Group.size() > 0)
            GroupBynum = table.get_attribute(Group.get(0));
        GroupattrMap = new ArrayList<CalculateMap>();
        if (Having.size() > 0) {
            String have = Having.get(0);
            String[] to = have.split("\\(");
            Having.set(0, to[0]);
            Having.add(1, to[1].substring(0, to[1].length() - 1));
        }
        if (On.size() != 0) {
            JoinGroupMap = new HashMap<>();
            String[] tablecol = On.get(0).split("[.]"); // 첫번째를 table1의 colum 3 3번째를 table2의 colum
            String[] tablecol2 = On.get(2).split("[.]");
            if (tablecol[0].compareTo(TableName) == 0) {
                TableAttrnum = table.get_attribute(tablecol[1]);
                JoinTableAttrnum = JoinTable.get_attribute(tablecol2[1]);
            } else {
                TableAttrnum = table.get_attribute(tablecol2[1]);
                JoinTableAttrnum = JoinTable.get_attribute(tablecol[1]);
            }
            OnMode = 0;
            if (Join.get(0).compareTo("LEFT") == 0)
                OnMode = 1;
            if (Join.get(0).compareTo("RIGHT") == 0)
                OnMode = 2;
            if (Join.get(0).compareTo("FULL") == 0)
                OnMode = 3;
        }
        if (ORDERBY.size() != 0) {
            OrderTable = table;
            String[] col = ORDERBY.get(0).split("[.]");
            if (col.length == 1)
                ORDERNUM = table.get_attribute(col[0]);
            else {
                OrderTable = handler.getTable(col[0]);
                ORDERNUM = OrderTable.get_attribute(col[1]);
            }
        }

        for (int k = 0; k < Attribute.size(); k++) {
            String attr = Attribute.get(k);
            if (attr.equals("*")) {
                for (int ati = 0; ati < table.get_attribute_size(); ati++) {
                    Attrnum.add(ati);
                }
                break;
            }
            if (attr.contains("COUNT")) {
                Attrnum.add(-1);
                String col = attr.substring(6, attr.length() - 1);
                String[] TableAndCol = col.split(".");
                if (TableAndCol.length == 0)
                    Cal_attr.add(table.get_attribute(col));
                else {
                    OwnerofAttribute[k] = TableAndCol[0];
                    TableBuffer tb = handler.getTable(TableAndCol[0]);
                    Cal_attr.add(tb.get_attribute(TableAndCol[1]));
                }
                CalculateMap GroupCntMap = new CountMap();
                GroupattrMap.add(GroupCntMap);
            } else if (attr.contains("SUM")) {
                Attrnum.add(-1);
                String col = attr.substring(4, attr.length() - 1);
                String[] TableAndCol = col.split(".");
                if (TableAndCol.length == 0)
                    Cal_attr.add(table.get_attribute(col));
                else {
                    OwnerofAttribute[k] = TableAndCol[0];
                    TableBuffer tb = handler.getTable(TableAndCol[0]);
                    Cal_attr.add(tb.get_attribute(TableAndCol[1]));
                }
                CalculateMap GroupSumMap = new SumMap();
                GroupattrMap.add(GroupSumMap);
            } else if (attr.contains("AVG")) {
                Attrnum.add(-1);
                String col = attr.substring(4, attr.length() - 1);
                String[] TableAndCol = col.split(".");
                if (TableAndCol.length == 0)
                    Cal_attr.add(table.get_attribute(col));
                else {
                    OwnerofAttribute[k] = TableAndCol[0];
                    TableBuffer tb = handler.getTable(TableAndCol[0]);
                    Cal_attr.add(tb.get_attribute(TableAndCol[1]));
                }
                CalculateMap GroupAvgMap = new AvgMap();
                GroupattrMap.add(GroupAvgMap);
            } else {
                String[] TableAndCol = attr.split("[.]");
                if (TableAndCol.length == 1)
                    Attrnum.add(table.get_attribute(attr));
                else {
                    OwnerofAttribute[k] = TableAndCol[0];
                    TableBuffer tb = handler.getTable(TableAndCol[0]);
                    Attrnum.add(tb.get_attribute(TableAndCol[1]));
                }
            }
        }
        for (int h = 0; h < OwnerofAttribute.length; h++)
            if (OwnerofAttribute[h] == null)
                OwnerofAttribute[h] = TableName;
    }

    public void Optimzier() {
        // where와 on에 쓰이는 attribute가 인덱스조건인지 다른조건들이 있는지 확인하게된다.
        Integer num = null;
        Integer num2 = null;
        // On= table1.col1=table2.col2
        if (On.size() != 0) {
            if (OnMode < 2) // inner left일경우 jointable을 그룹핑 //right일경우 일바table을 grouping
            {
                JoinGroupMap = MakingGroupMap(JoinTable, JoinTableAttrnum);
            }
        }
        if (Where.size() != 0) // where메서드가 있을경우
        {
            num = table.get_attribute(Where.get(0));
            WhereNumber.add(num);
            try {
                num2 = table.get_attribute(Where.get(4));
                WhereNumber.add(num2);
            } catch (Exception e) {
            }
            try {
                if (num == 0 && num2 == null) {
                    addItbyWhere(0);
                    if (Where.get(1).equals("="))
                        ScanMethod = true;
                } else if ((num == 0 || num2 == 0) && (Where.get(3).compareTo("AND") == 0)) { // 현재는 AND만처리함
                    if (num == 0 && Where.get(1).contains("=") || (num2 == 0 && Where.get(5).contains("=")))
                        ScanMethod = true;// Indexing
                    if (num == 0)
                        addItbyWhere(0);
                    if (num2 == 0)
                        addItbyWhere(4);

                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        if (Group.size() == 0 && ORDERBY.size() == 0 && On.size() == 0) // Having 조인이 오지않는경우임
            SelectMode = true;
    }

    private Map<Object, ArrayList<ArrayList<Object>>> MakingGroupMap(TableBuffer tb, Integer groupbynum) {
        LeafIterator Start = new LeafIterator(tb.getDataSet());
        Map<Object, ArrayList<ArrayList<Object>>> groupmap = new HashMap<Object, ArrayList<ArrayList<Object>>>();
        while (Start.hasNext()) {
            Pair pair = Start.Next();
            ArrayList<Object> record = (ArrayList<Object>) pair.value();
            Object key = record.get(groupbynum);
            ArrayList<ArrayList<Object>> nowGroup = groupmap.get(record.get(groupbynum));
            if (nowGroup == null) {
                nowGroup = new ArrayList<ArrayList<Object>>();
                nowGroup.add(record);
                groupmap.put(key, nowGroup);
            } else {
                nowGroup.add(record);
            }
        }
        return groupmap;
    }

    // <또는,>일경우
    // ParameticSearching 할수있도록 bound지정해준다
    // 해당인덱스를 가지고있는 leafnode를찾는다.
    public void addItbyWhere(int i) {
        try {
            Object target;
            char type = table.get_type_of_attribute(Where.get(i));
            LeafIterator iterator;
            if (type == 'i')
                target = Integer.parseInt(Where.get(i + 2));
            else
                target = Where.get(i + 2);
            iterator = new LeafIterator(table.getDataSet(), target);
            if(Where.get(i+1).equals(">")&&iterator.sameIndex(Where.get(i+2)))
             iterator.Next();
            iteratorList.add(iterator);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String Run() {
        Optimzier();
        if (ScanMethod == true)// indexingScan
        {
            FirstProcessing(table.get(iteratorList.get(0).Next().index()));
        } else { // FullScan or ParameticSCan
            // Where처리
            // if(iteratorList!=null)
            boolean isWhere = false;
            if (iteratorList.size() == 2) // COL > 2 AND 2 > 3
            {
                if (Where.get(1).contains(">") && Where.get(2).contains("<")) {
                    Scan(iteratorList.get(0), iteratorList.get(1), false);
                } else
                    Scan(iteratorList.get(1), iteratorList.get(0), false);
            } else if (iteratorList.size() == 1) { // iterating이 있을경우
                if (Where.size() > 4)
                    isWhere = true;
                if (Where.get(1).contains(">"))
                    Scan(iteratorList.get(0), null, isWhere);
                else
                    Scan(null, iteratorList.get(0), isWhere);
            } else if (WhereNumber.size() == 0)
                Scan(null, null, false);
            else {
                Scan(null, null, true);
            }
        }
        answer += (numberofRecord + "Record");
        return answer;
    }

    // Scan 할 시작지점 끝지점 넣으면 스캔하고 select까지 한번에 처리할수있으면한다.
    public void Scan(LeafIterator start, LeafIterator end, boolean isWhere) {
        LeafIterator Start = start;
        LeafIterator End = end;
        if (Start == null)
            Start = new LeafIterator(table.getDataSet());

        if (!isWhere) // state=true일때 따로 where절 체크필요x
        {
            if (Cal_attr.size() == 0) {
                while (Start.notsame(end) && numberofRecord < 101) {
                    Pair pair = Start.Next();
                    FirstProcessing((ArrayList<Object>) pair.value());
                }
            } else {
                int size = 0;
                if (Group.size() == 0) {
                    Pair first;
                    if (Start.notsame(end)) {
                        size++;
                        first = Start.Next();
                        Init(first);
                    }
                    while (Start.notsame(end)) {
                        size++;
                        Pair pair = Start.Next();
                        FirstProcessing((ArrayList<Object>) pair.value());
                    }
                    CNT_SUM_AVG_SELECT(size);
                } else {
                    while (Start.notsame(end)) {
                        size++;
                        Pair pair = Start.Next();
                        FirstProcessing((ArrayList<Object>) pair.value());
                    }
                }
            }
        } else { // where체크해야할때
            if (WhereNumber.size() == 1) {
                Object target = attrCasting(table, Where.get(0), Where.get(2));
                Eval eval = new Eval(target, Where.get(1));
                while (Start.notsame(end) && numberofRecord < 101) {
                    Pair<Object, ArrayList<Object>> pair = Start.Next();
                    Object col = pair.value().get(WhereNumber.get(0));
                    if (col instanceof String) {
                        if (eval.result(col.toString()) == true)
                            FirstProcessing((ArrayList<Object>) pair.value());
                    } else {
                        if (eval.result((Integer) col) == true)
                            FirstProcessing((ArrayList<Object>) pair.value());
                    }
                }
            } else if (WhereNumber.size() == 2) {
                Object target = attrCasting(table, Where.get(0), Where.get(2));
                Object target2 = attrCasting(table, Where.get(4), Where.get(6));
                Eval eval = new Eval(target, Where.get(1));
                Eval eval2 = new Eval(target2, Where.get(5));
                while (Start.notsame(end) && numberofRecord < 101) {
                    Pair<Object, ArrayList<Object>> pair = Start.Next();
                    Object col = pair.value().get(WhereNumber.get(0));
                    Object col2 = pair.value().get(WhereNumber.get(1));
                    Boolean Check = false, Check2 = false;
                    if (col instanceof String) {
                        if (eval.result(col.toString()) == true)
                            Check = true;
                    } else {
                        if (eval.result((Integer) col) == true)
                            Check = true;
                    }
                    if (col2 instanceof String) {
                        if (eval2.result(col2.toString()) == true)
                            Check2 = true;
                    } else {
                        if (eval2.result((Integer) col2) == true)
                            Check2 = true;
                    }
                    if (Check == true && Check2 == true) {
                        FirstProcessing((ArrayList<Object>) pair.value());
                    }
                }
            }
        }
        if (On.size() != 0) {
            if (ORDERBY.size() == 0)
                SelectMode = true;
            if (SelectMode == false) {

            }
            if (OnMode < 2) { // table이 그룹이 아닐경우
                if (RecordSet.size() > 0) {
                    for (int i = 0; i < RecordSet.size(); i++) {
                        OnProcessing(RecordSet.get(i));
                    }
                } else {
                    // Set entrySet = GroupMap.entrySet();
                    // Iterator it = entrySet.iterator();
                    // while (it.hasNext() && numberofRecord < 101) {
                    // Map.Entry me = (Map.Entry) it.next();
                    // Integer result = CNT_SUM_AVG((ArrayList<ArrayList<Object>>) me.getValue(),
                    // GroupBynum,
                    // calculate_type);
                    // if (eval.result(result))
                    // SecondProcessing((ArrayList<ArrayList<Object>>) me.getValue());
                    // }
                }

            } else if (OnMode >= 2) {
                LeafIterator Join_Start = new LeafIterator(JoinTable.getDataSet());
                while (Join_Start.hasNext() && numberofRecord < 101) {
                    Pair Joinpair = Join_Start.Next();
                    OnProcessing((ArrayList<Object>) Joinpair.value());
                }
            }
        }
        // Where까지체크한후 having체크
        if (Group.size() != 0) {

            if (ORDERBY.size() == 0 && On.size() == 0)
                SelectMode = true;
            Set entrySet = GroupMap.entrySet();
            Iterator it = entrySet.iterator();
            if (Having.size() != 0) // Orederby가 없다면 having과 select함께처리
            {
                Object calculate_type;
                if (Having.get(0).compareTo("COUNT") == 0)
                    calculate_type = "Count";
                else if (Having.get(0).compareTo("SUM") == 0)
                    calculate_type = 10;// sum
                else
                    calculate_type = true;// average;

                Object target = Integer.parseInt(Having.get(3));
                Eval eval = new Eval(target, Having.get(2));

                while (it.hasNext() && numberofRecord < 101) {
                    Map.Entry me = (Map.Entry) it.next();
                    Integer result = CNT_SUM_AVG((ArrayList<ArrayList<Object>>) me.getValue(), GroupBynum,
                            calculate_type);
                    if (eval.result(result))
                        SecondProcessing((ArrayList<ArrayList<Object>>) me.getValue());
                }
            } else {
                while (it.hasNext() && numberofRecord < 101) {
                    Map.Entry me = (Map.Entry) it.next();
                    SecondProcessing((ArrayList<ArrayList<Object>>) me.getValue());
                }
            }
        }
        if (ORDERBY.size() != 0) {
            int k = 0;
            if (OnRecordSet.size() != 0) {
                RecordSet = OnRecordSet;
                for (int i = 0; i < RecordSet.get(0).size(); i++) {
                    if (Attrnum.size() > i)
                        Attrnum.set(i, i);
                    else
                        Attrnum.add(i);
                }
                ORDERNUM = RecordSet.get(0).size() - 1;
                k++;
            }
            Object[][] OrderArray = new MergeSort(RecordSet, ORDERNUM).getdouble();
            if (ORDERFORM.size() == 0) {
                while (numberofRecord < RecordSet.size() && numberofRecord < 101) {
                    int j = 0;
                    answer += (numberofRecord + 1) + ": ";
                    while (j < Attrnum.size() - k) {
                        answer += (OrderArray[numberofRecord][Attrnum.get(j)].toString() + " ");
                        j++;
                    }
                    numberofRecord++;
                    answer += "\n";
                }
            } else {
                while (numberofRecord < RecordSet.size() && numberofRecord < 101) {
                    int j = 0;
                    answer += (numberofRecord + 1) + ": ";
                    while (j < Attrnum.size() - k) {
                        answer += (OrderArray[RecordSet.size() - 1 - numberofRecord][Attrnum.get(j)].toString() + " ");
                        j++;
                    }
                    numberofRecord++;
                    answer += "\n";
                }
            }
        }

    }

    private void Init(Pair first) {
        ArrayList<Object> v = (ArrayList<Object>) first.value();
        for (int i = 0; i < v.size(); i++)
            finalRecord.add(v.get(i));
    }

    private void OnProcessing(ArrayList<Object> Joinrecord) {
        Object JoinAttr = Joinrecord.get(JoinTableAttrnum); // jointable의 값
        ArrayList<ArrayList<Object>> JoinGroupMapRecord = JoinGroupMap.get(JoinAttr);
        if (JoinGroupMapRecord != null) // 교집합
            NotnullProcess(JoinGroupMapRecord, Joinrecord);
        else if (SelectMode == true) {
            if (OnMode == 1) // Left
            {
                answer += (++numberofRecord + ": ");
                for (int i = 0; i < OwnerofAttribute.length; i++) // select출력
                {
                    if (handler.getTable(OwnerofAttribute[i]) == table) {
                        answer += Joinrecord.get(Attrnum.get(i));
                    } else {
                        answer += "NULL";
                    }
                    answer += " ";
                }
                answer += "\n";
            } else if (OnMode == 2) // Right
            {
                answer += (++numberofRecord + ": ");
                for (int i = 0; i < OwnerofAttribute.length; i++) // select출력
                {
                    if (handler.getTable(OwnerofAttribute[i]) == JoinTable) {
                        answer += Joinrecord.get(Attrnum.get(i));
                    } else {
                        answer += "NULL";
                    }
                    answer += " ";
                }
                answer += "\n";
            }
        } else {
            ArrayList<Object> obj = new ArrayList<Object>();
            if (OnMode == 1) // Left
            {
                for (int i = 0; i < OwnerofAttribute.length; i++) // select출력
                {
                    if (handler.getTable(OwnerofAttribute[i]) == table) {
                        obj.add(Joinrecord.get(Attrnum.get(i)));
                    } else {
                        obj.add("NULL");
                    }
                }
                if (OrderTable == table)
                    obj.add(Joinrecord.get(ORDERNUM));
                else
                    obj.add(0);
            } else if (OnMode == 2) // Right
            {
                for (int i = 0; i < OwnerofAttribute.length; i++) // select출력
                {
                    if (handler.getTable(OwnerofAttribute[i]) == JoinTable) {
                        obj.add(Joinrecord.get(Attrnum.get(i)));
                    } else {
                        obj.add("NULL");
                    }
                }
                if (OrderTable == JoinTable)
                    obj.add(Joinrecord.get(ORDERNUM));
                else
                    obj.add(0);
            }
            OnRecordSet.add(obj);

        }
    }

    // GroupBy processing계산
    private void SecondProcessing(ArrayList<ArrayList<Object>> Group) {
        if (SelectMode == true) {
            int j = 0;
            int h = 0;
            numberofRecord++;
            answer += (Integer.toString(numberofRecord) + ": ");
            while (j < Attrnum.size()) {
                if (Attrnum.get(j) == -1) {
                    answer += GroupattrMap.get(h).Print(Group, Cal_attr.get(h++));
                } else
                    answer += (Group.get(0).get(Attrnum.get(j)).toString());
                j++;
                answer += " ";
            }
            answer += "\n";
        } else {
            int j = 0;
            int h = 0;
            ArrayList<Object> obj = new ArrayList<>();
            while (j < Attrnum.size()) {
                if (Attrnum.get(j) == -1) {
                    obj.add(GroupattrMap.get(h).Print(Group, Cal_attr.get(h++)));
                } else
                    obj.add(Group.get(0).get(Attrnum.get(j)).toString());
                j++;
            }
            RecordSet.add(obj);
        }
    }

    public Object attrCasting(TableBuffer tb, String attr, String value) {
        char type = tb.get_type_of_attribute(attr);
        Object target;
        if (type == 'i')
            target = Integer.parseInt(value);
        else
            target = value;
        return target;
    }

    public Integer CNT_SUM_AVG(ArrayList<ArrayList<Object>> target, int attrnum, Object c) // count
    {
        return target.size();
    }

    public Integer CNT_SUM_AVG(ArrayList<ArrayList<Object>> target, int attrnum, Integer a) // sum
    {
        Integer sumInteger = 0;
        for (int i = 0; i < target.size(); i++) {
            sumInteger += (Integer) target.get(i).get(attrnum);
        }
        return sumInteger;
    }

    public Integer CNT_SUM_AVG(ArrayList<ArrayList<Object>> target, int attrnum, boolean c) // average
    {
        Integer aVGInteger = 0;
        for (int i = 0; i < target.size(); i++) {
            aVGInteger += (Integer) target.get(i).get(attrnum);
        }
        return aVGInteger / target.size();
    }

    public void OrderBy() {

    }

    public void FirstProcessing(ArrayList<Object> obj) {
        if (SelectMode == true) {
            int j = 0;
            if (Cal_attr.size() == 0) {
                answer += ++numberofRecord + ": ";
                while (j < Attrnum.size()) {
                    answer += (obj.get(Attrnum.get(j)).toString() + " ");
                    j++;
                }
                answer += "\n";
            } else {
                while (j < Attrnum.size()) {
                    if (Attrnum.get(j) == -1) {
                        Integer num = (Integer) finalRecord.get(j) + (Integer) obj.get(j);
                        finalRecord.set(j, num);
                    }
                    j++;
                }
            }
        } else {
            if (On.size() != 0 && OnMode >= 2) { // Right
                ArrayList<ArrayList<Object>> nowGroup = JoinGroupMap.get(obj.get(TableAttrnum));
                if (nowGroup == null) {
                    nowGroup = new ArrayList<ArrayList<Object>>();
                    nowGroup.add(obj);
                    JoinGroupMap.put(obj.get(TableAttrnum), nowGroup);
                } else {
                    nowGroup.add(obj);
                }
            } else if (Group.size() != 0) {
                ArrayList<ArrayList<Object>> nowGroup = GroupMap.get(obj.get(GroupBynum));
                if (nowGroup == null) {
                    nowGroup = new ArrayList<ArrayList<Object>>();
                    nowGroup.add(obj);
                    GroupMap.put(obj.get(GroupBynum), nowGroup);
                } else {
                    nowGroup.add(obj);
                }
            } else {
                RecordSet.add(obj);
            }
        }
    }

    public void DISTINCT() {

    }

    public void CNT_SUM_AVG_SELECT(Integer size) {
        answer += "1: ";
        for (int i = 0; i < Attribute.size(); i++) {
            if (Attrnum.get(i) == -1) {
                if (Attribute.get(i).contains("AVG")) {
                    Integer num = (Integer) finalRecord.get(i) / size;
                    finalRecord.set(i, num);
                } else if (Attribute.get(i).contains("COUNT")) {
                    finalRecord.set(i, size);
                }
            }
            answer += finalRecord.get(i) + " ";
        }
        // answer += "\n 1 Record Success \n";
    }

    public void NotnullProcess(ArrayList<ArrayList<Object>> JoinGroupMapRecord, ArrayList<Object> Joinrecord) {
        if (SelectMode == true) {
            for (int j = 0; j < JoinGroupMapRecord.size(); j++) {

                answer += (++numberofRecord + ": ");
                for (int i = 0; i < OwnerofAttribute.length; i++) // select출력
                {
                    if (handler.getTable(OwnerofAttribute[i]) == table) {
                        answer += Joinrecord.get(Attrnum.get(i));
                    } else {
                        answer += JoinGroupMapRecord.get(j).get(Attrnum.get(i));
                    }
                    answer += " ";
                }
                answer += "\n";

            }
        } else {
            for (int j = 0; j < JoinGroupMapRecord.size(); j++) {
                ArrayList<Object> obj = new ArrayList<Object>();
                for (int i = 0; i < OwnerofAttribute.length; i++) // select출력
                {
                    if (handler.getTable(OwnerofAttribute[i]) == table) {
                        obj.add(Joinrecord.get(Attrnum.get(i)));
                    } else {
                        obj.add(JoinGroupMapRecord.get(j).get(Attrnum.get(i)));
                    }
                }
                if (OnMode < 2) {
                    if (OrderTable == table)
                        obj.add(Joinrecord.get(ORDERNUM));
                    else
                        obj.add(0);
                    OnRecordSet.add(obj);
                } else {
                    if (OrderTable == JoinTable)
                        obj.add(Joinrecord.get(ORDERNUM));
                    else
                        obj.add(0);
                    OnRecordSet.add(obj);
                }
            }
        }
    }
}