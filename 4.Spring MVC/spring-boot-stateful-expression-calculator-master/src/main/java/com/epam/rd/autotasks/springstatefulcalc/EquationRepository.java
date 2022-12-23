package com.epam.rd.autotasks.springstatefulcalc;

import java.util.TreeMap;

public class EquationRepository{
    private TreeMap < String, String > mapEquation;

    public EquationRepository(){
        mapEquation = new TreeMap < > ();
    }

    public boolean setParam(String key,String value){
        boolean firstTime = true;
        if(mapEquation.containsKey(key)){
            removeParam(key);
            firstTime = false;
        }
        mapEquation.put(key,value);
        return firstTime;
    }

    public boolean removeParam(String key){
        if(mapEquation.containsKey(key)){
            mapEquation.remove(key);
            return true;
        }
        return false;
    }

    public String getKey(String key){
        if(mapEquation.containsKey(key)){
            return mapEquation.get(key);
        }
        return null;
    }
}
