package com.epam.rd.autotasks.springstatefulcalc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@SessionAttributes("equationInfo")
@RequestMapping("/calc")
public class MVCTask001{

    @PutMapping("/{key}")
    public ResponseEntity< String > putMethod(@ModelAttribute("equationInfo") EquationRepository equationInfo, @PathVariable String key, @RequestBody String value){
        int check = validateData(key,value);
        if(check != 0){
            return ResponseEntity.status(check == 1 ? HttpStatus.BAD_REQUEST : HttpStatus.FORBIDDEN).body(null);
        } else {
            boolean firstTime = equationInfo.setParam(key, value);
            return ResponseEntity.status((firstTime ? HttpStatus.CREATED : HttpStatus.OK)).body(null);
        }
    }

    @DeleteMapping("/{param}")
    public ResponseEntity < String > deleteMethod(@ModelAttribute("equationInfo") EquationRepository equationInfo,@PathVariable String param){
        equationInfo.removeParam(param);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/result")
    public ResponseEntity < String > getMethod(@ModelAttribute("equationInfo") EquationRepository equationInfo){
        String equation = equationInfo.getKey("expression");
        if(equation == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("LACK OF DATA");
        } else {
            ArrayList < Character > rpn = ShuntingYard.getRPN(equation);
            Stack < Integer > ans = new Stack < > ();
            boolean canSolve = true;
            for(Character c : rpn){
                if(ComputeOp.isOperation(c)){
                    int b = ans.peek(); ans.pop();
                    int a = ans.peek(); ans.pop();
                    ans.push(ComputeOp.process(c,a,b));
                } else {
                    String tmp = String.valueOf(c);
                    while(equationInfo.getKey(tmp) != null){
                        tmp = equationInfo.getKey(tmp);
                    }
                    if(tmp.length() == 1 && !Character.isDigit(tmp.charAt(0))){
                        canSolve = false;
                        break;
                    } else {
                        ans.push(Integer.parseInt(tmp));
                    }
                }
            }
            if(canSolve){
                return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(ans.peek()));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("LACK OF DATA");
            }
        }
    }

    @ModelAttribute("equationInfo")
    public EquationRepository getEquationInfo(){
        //just call once in @Controller
        return new EquationRepository();
    }

    private int validateData(String key,String value){
        int canPass = 0;
        if(key.equals("expression")){
            canPass = (isEquation(value) ? 0 : 1);
        } else {
            Pattern pattern = Pattern.compile("^[a-z]$|^-*[0-9]+$");
            Matcher matcher = pattern.matcher(value);
            boolean found = matcher.find();
            if (!found) {
                canPass = 1;
            } else {
                if (!Character.isLetter(value.charAt(0))) {
                    if (!inRange(Integer.parseInt(value))) {
                        canPass = 2;
                    }
                }
            }
        }
        return canPass;
    }

    private boolean isEquation(String equation){
        String op = "+-*/";
        for(int i = 0;i < op.length();++i){
            if(equation.indexOf(op.charAt(i)) != -1){
                return true;
            }
        }
        return false;
    }

    private boolean inRange(int value){
        return (-10000 <= value && value <= 10000);
    }
}
