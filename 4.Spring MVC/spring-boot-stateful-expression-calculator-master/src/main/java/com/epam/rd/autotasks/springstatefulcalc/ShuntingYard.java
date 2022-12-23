package com.epam.rd.autotasks.springstatefulcalc;

import java.util.*;

public class ShuntingYard{

    private static TreeMap < Character, Integer > prior = new TreeMap < > ();
    static {
        prior.put('^',1);
        prior.put('*',2);
        prior.put('/',2);
        prior.put('-',3);
        prior.put('+',3);
    }

    protected static ArrayList < Character > getRPN(String equation){
        Stack < Integer > stk = new Stack < > ();
        ArrayList < Character > res = new ArrayList < > ();
        for(int i = 0;i < equation.length();++i){
            Character c = equation.charAt(i);
            if(c == ' '){
                continue;
            }
            if(c == '('){
                stk.push(i);
            } else if(c == ')'){
                while(!stk.empty()){
                    int top = stk.peek();
                    stk.pop();
                    if(prior.containsKey(equation.charAt(top))){
                        res.add(equation.charAt(top));
                    } else {
                        break;
                    }
                }
            } else if(prior.containsKey(c)){
                while(!stk.empty()){
                    int top = stk.peek();
                    if(prior.containsKey(equation.charAt(top)) && prior.get(equation.charAt(top)) <= prior.get(c)){
                        res.add(equation.charAt(top));
                        stk.pop();
                    } else {
                        break;
                    }
                }
                stk.push(i);
            } else {
                res.add(c);
            }
        }
        while(!stk.empty()){
            int top = stk.peek(); stk.pop();
            res.add(equation.charAt(top));
        }
        return res;
    }
}
