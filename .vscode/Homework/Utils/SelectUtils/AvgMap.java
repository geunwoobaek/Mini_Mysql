package Homework.Utils.SelectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AvgMap extends CalculateMap {
    private Map<String,Integer> map; //key는 그룹할 부분의 colum, value의 key는 계산할 colum value=계산
    public AvgMap()
    {
        this.map=new HashMap();
    }
    @Override
    public void Put(String ColumName,Integer answer)
    {
        map.put(ColumName,answer);
    }
    @Override
    public void run(String ColumName,ArrayList<ArrayList<Object>> List,Integer attrnum)
    {   Integer result=0;
        for(int i=0;i<List.size();i++)
            result+=(Integer)List.get(i).get(attrnum);
        result/=List.size();
        map.put(ColumName,result);
    }
    @Override
    public Integer Print(ArrayList<ArrayList<Object>> List,Integer attrnum)
    {
        Integer result=0;
        for(int i=0;i<List.size();i++)
        result+=(Integer)List.get(i).get(attrnum);
        result/=List.size();
        return result;
    }
    @Override
    public Integer get(String ColumName)
    {
        return map.get(ColumName);
    }
}