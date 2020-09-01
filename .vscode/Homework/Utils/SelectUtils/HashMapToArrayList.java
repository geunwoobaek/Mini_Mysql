package Homework.Utils.SelectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



public class HashMapToArrayList {
    private ArrayList<ArrayList<Object>> List;

    public HashMapToArrayList() {
        List = new ArrayList<ArrayList<Object>>();
    }

    public ArrayList<ArrayList<Object>> Change(HashMap<String, ArrayList> map) {
        Iterator<Entry<String, ArrayList>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            ArrayList<Object> entrySet=new ArrayList<Object>();
			Map.Entry<String, ArrayList> pair = (Map.Entry<String, ArrayList>) it.next();
            entrySet.add(pair.getKey());entrySet.addAll(pair.getValue());
            List.add(entrySet);
        }
        return List;
    }
}