package com.epam.rd.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        Map<String, String[]> parameter = req.getParameterMap();
        String expression = req.getParameter("expression").replaceAll(" ", "");
        while (!Pattern.matches("^[0-9*+-/()]+$", expression)){
            for (Map.Entry<String,String[]> entry : parameter.entrySet())
                expression = expression.replace(entry.getKey(), entry.getValue()[0]);
        }
        List<String> stringDeque=procesString(expression);
        String output=stringDeque.get(0);
        for(int i=stringDeque.size()-1;i>0;i--){
            String s1=stringDeque.get(i);
            String s2=s1;
            s1=s1.replace("(","");
            s1=s1.replace(")","");
            output=output.replace(s2,calcexpres(s1));
        }
        output=output.replace("(","");
        output=output.replace(")","");
        printWriter.println(calcexpres(output));
        printWriter.close();
    }
    private List<String> procesString(String s){
        List<String> stringDeque = new ArrayList<>();
        stringDeque.add(s);
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
                    stringDeque.add(s.substring(begin, ++i));
                    break;
                }
            }
            s=s.substring(++begin);
        }
        return stringDeque;
    }
    private String calc(int x,int y,String operator){
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
    private String[] delete(String[] s,int vitri){
        for(int i=vitri+2;i<s.length;i++)
            s[i-2]=s[i];
        return s;
    }
    private String addSpace(String s){
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
    private String calcexpres(String s){
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
