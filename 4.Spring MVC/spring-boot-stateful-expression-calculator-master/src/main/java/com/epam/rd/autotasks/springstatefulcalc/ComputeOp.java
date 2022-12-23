package com.epam.rd.autotasks.springstatefulcalc;

public class ComputeOp {

    private static final String op = "^*/+-";

    private static int add(int a,int b){
        return a + b;
    }

    private static int sub(int a,int b){
        return a - b;
    }

    private static int mul(int a,int b){
        return a * b;
    }

    private static int div(int a,int b){
        return a / b;
    }

    protected static int process(char c,int a,int b){
        switch (c){
            case '+' : return add(a,b);
            case '-' : return sub(a,b);
            case '*' : return mul(a,b);
            case '/' : return div(a,b);
            default : return 0;
        }
    }

    protected static boolean isOperation(char c){
        return (op.indexOf(c) != -1);
    }
}
