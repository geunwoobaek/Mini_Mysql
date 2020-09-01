package Homework.Utils;

import java.util.ArrayList;
import java.util.Map;

public class Eval {
    private Object standard;
    private Integer answer;
    private Map<Object, ArrayList<ArrayList<Object>>> StandardMap;
    public Eval(Map<Object, ArrayList<ArrayList<Object>>> map,String Operator){
        this.StandardMap=map;
        if(Operator.contains(">")) answer=1;
        else if(Operator.contains("=")) answer=0;
        if(Operator.contains("<")) answer=-1;
    }
    public Eval(Object a,String Operator){
        this.standard=a;
        if(Operator.contains(">")) answer=1;
        else if(Operator.contains("=")) answer=0;
        if(Operator.contains("<")) answer=-1;
    }
    public void set(Object standard)
    {
        this.standard=standard;
    }

    public boolean result(String target) 
    {   Integer check;
        check=standard.toString().compareTo(target);
        if(answer!=0)
        {
        if(check*answer>0) return true;
        else return false;
        }
        else return check==0;
    }
    public boolean result(int target) 
    {   Integer check;
        check=(Integer)target-(Integer)standard;
        if(answer!=0)
        {
        if(check*answer>0) return true;
        else return false;
        }
        else return check==0;
    }
}