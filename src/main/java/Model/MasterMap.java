package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class MasterMap {

    public HashMap<String, ArrayList<String>> keyValuePair;
    public HashMap<String, Integer> indexMapping;

    public MasterMap() {
        keyValuePair = new HashMap();
        indexMapping = new HashMap();
    }

    public String getRandomKey()
    {
        String result;
        int size=keyValuePair.size();
        Random random=new Random();
        Set<String> keySet= keyValuePair.keySet();
        ArrayList<String> newList=new ArrayList();
        newList.addAll(keySet);
        int randomIndex=random.nextInt(size);
        result=newList.get(randomIndex);
        return result;
    }

    public void addMapping(String k, String v) {
        ArrayList<String> valueList = keyValuePair.get(k);
        if (valueList == null) {
            valueList = new ArrayList();
            keyValuePair.put(k, valueList);
        }
        valueList.add(v);
    }

    public String nextWord(String k)
    {
        String result=null;
        Integer index=indexMapping.get(k);
        if(index==null || index== (keyValuePair.get(k).size()))
        {
            index=new Integer(0);
        }
        indexMapping.put(k,index+1);
        return keyValuePair.get(k).get(index);
    }

}
