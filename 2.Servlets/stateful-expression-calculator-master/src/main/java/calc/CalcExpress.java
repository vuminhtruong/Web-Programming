package calc;

import java.util.ArrayList;
import java.util.List;

public class CalcExpress {
    public static List<String> processString(String s){
        List<String> list = new ArrayList<>();
        list.add(s);
        while (s.contains("(")) {
            int begin=s.indexOf("(");
            int sum = 1;
            for (int i = begin+1; i < s.length(); i++) {
                if(s.charAt(i)=='('){
                    ++sum;
                }
                else if(s.charAt(i)==')'){
                    --sum;
                }
                if(s.charAt(i)==')'&&sum==0){
                    list.add(s.substring(begin, ++i));
                    break;
                }
            }
            s=s.substring(++begin);
        }
        return list;
    }
    private static String calc(int x, int y, String operator){
        int result=0;
        switch (operator){
            case "*":
                result=x*y;
                break;
            case "/":
                result=x/y;
                break;
            case "+":
                result=x+y;
                break;
            case "-":
                result=x-y;
                break;
        }
        return String.valueOf(result);
    }
    private static void delete(String[] s, int vitri){
        for(int i=vitri+2;i<s.length;i++)
            s[i-2]=s[i];
    }
    private static String addSpace(String s){
        //s=s.replace(" ","");
        StringBuilder output= new StringBuilder();
        for(int i=0;i<s.length();i++){
            char c=s.charAt(i);
            if(c=='*'||c=='/'||c=='+'){
                output.append(" ").append(c).append(" ");
            }
            else if(c=='-'){
                if(i!=0&&Character.isDigit(s.charAt(i-1)))
                    output.append(" ").append(c).append(" ");
                else
                    output.append(c);
            }
            else
                output.append(c);
        }
        return output.toString();
    }
    public static String calcexpres(String s){
        s=addSpace(s);
        String[] temp=s.split(" ");
        int len=temp.length;
        int i=1;
        while (i<len-1){
            if(temp[i].equals("*")||temp[i].equals("/")){
                temp[i-1]=calc(Integer.parseInt(temp[i-1]),Integer.parseInt(temp[i+1]),temp[i]);
                delete(temp, i);
                i=1;len-=2;
            }
            else
                i+=2;
        }
        i=1;
        while (i<len-1){
            if(temp[i].equals("-")||temp[i].equals("+")){
                temp[i-1]=calc(Integer.parseInt(temp[i-1]),Integer.parseInt(temp[i+1]),temp[i]);
                delete(temp, i);
                i=1;len-=2;
            }
            else
                i+=2;
        }
        return temp[0];
    }
}

