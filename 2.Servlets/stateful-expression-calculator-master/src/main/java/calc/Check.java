package calc;

public class Check {
    public static boolean checkExpression(String s){
        for(int i=0;i<s.length()-1;i++){
            char s1=s.charAt(i);
            char s2=s.charAt(i+1);
            if(Character.isLetter(s1)&&Character.isLetter(s2))
                return false;
        }
        return true;
    }
    public static boolean checkVariable(String s){
        if(s.length()==1&&s.charAt(0)>='a'&&s.charAt(0)<='z'){
            return true;
        }
        try {
            int value = Integer.parseInt(s);
            return value >= -10000 && value <= 10000;
        }catch (Exception e){
            return false;
        }
    }
}
