package mua;

import java.util.*;

public class Main {
    static HashMap<String, String> globalVarMap = new HashMap<String, String>();
    static int sp = 0;

    static String isnumber(String v) { // not ROBUST!
        for(int i = 0;i < v.length();i++) {
            char ch = v.charAt(i);
            if(!((ch == '-') || (ch == '.') || ('0' <= ch && ch <= '9'))) return "false";
        }
        return "true";
    }
    static String islist(String v) {
        return (v.charAt(0) == '[' && v.charAt(v.length()-1) == ']') ? "true" : "false";
    }
    static String isbool(String v) {
        return ("true".equals(v) || "false".equals(v)) ? "true" : "false";
    }
    static String isword(String v) { // not ROBUST!
        return ("false".equals(isnumber(v)) && "false".equals(islist(v)) && "false".equals(isbool(v))) ? "true" : "false";
    }
    static String isempty(String v) { // not ROBUST!
        return ("[]".equals(v) || "".equals(v)) ? "true" : "false";
    }
    static String isname(String v, HashMap<String, String> varMap) {
        return (varMap.containsKey(v) || globalVarMap.containsKey(v)) ? "true" : "false";
    }
    static String thing(String v, HashMap<String, String> varMap) {
        return varMap.containsKey(v) ? varMap.get(v) : globalVarMap.get(v);
    }

    static <T extends Iinput> String parseArithmeticExpr(T in, HashMap<String, String> varMap, String cmd) {
        String op1, op2;
        double d1, d2;
        op1 = parseExpr(in, varMap);
        op2 = parseExpr(in, varMap);
        if("true".equals((isnumber(op1))) && "true".equals((isnumber(op2)))) {
            d1 = Double.parseDouble(op1);
            d2 = Double.parseDouble(op2);
            switch(cmd) {
                case "add": return "" + (d1 + d2);
                case "sub": return "" + (d1 - d2);
                case "mul": return "" + (d1 * d2);
                case "div": return "" + (d1 / d2);
                case "mod": return "" + (d1 % d2);
                case "eq": return (Math.abs(d1-d2) < 0.00001) ? "true" : "false";
                case "gt": return d1 > d2 ? "true" : "false";
                case "lt": return d1 < d2 ? "true" : "false";
                default: return "[Error]parseArithmeticExpr";
            }
        }
        if("true".equals((isword(op1))) && "true".equals((isword(op2)))) {
            switch(cmd) {
                case "eq": return op1.compareTo(op2) == 0 ? "true" : "false";
                case "gt": return op1.compareTo(op2) > 0 ? "true" : "false";
                case "lt": return op1.compareTo(op2) < 0 ? "true" : "false";
                default: return "[Error]parseArithmeticExpr";
            }
        }
        return "[Error]parseArithmeticExpr";
    }

    static <T extends Iinput> String parseLogicExpr(T in, HashMap<String, String> varMap, String cmd) { // not robust
        String op1 = parseExpr(in, varMap), op2;
        switch(cmd) {
            case "and": op2 = parseExpr(in, varMap);return "true".equals(op1) && "true".equals(op2) ? "true" : "false";
            case  "or": op2 = parseExpr(in, varMap);return "true".equals(op1) || "true".equals(op2) ? "true" : "false";
            case "not": return "true".equals(op1) ? "false" : "true";
            default: return "[Error]parseLogicExpr";
        }
    }

    static String run(ListWrapper in, HashMap<String, String> varMap) {
        String res = "";
        int sp_copy = sp++;
        while(in.hasNext()) {
            res = parseExpr(in, varMap);
            if(sp_copy == sp) break;
        }
        return res;
    }

    static <T extends Iinput> String runFunc(T in, String cmd, HashMap<String, String> varMap) {
        int cnt = 0, lft1, lft2, rgt1;
        String argString, funcCode;
        HashMap<String, String> localVarMap = new HashMap<String, String>();

        lft1 = 1;
        while(cmd.charAt(lft1) != '[') lft1++;
        for(rgt1 = lft1;rgt1 < cmd.length();rgt1++) {
            if(cmd.charAt(rgt1) == '[') cnt++;
            if(cmd.charAt(rgt1) == ']') cnt--;
            if(cnt == 0) break;
        }
        argString = cmd.substring(lft1, rgt1+1);
        lft2 = rgt1;
        while(cmd.charAt(lft2) != '[') lft2++;
        funcCode = cmd.substring(lft2, cmd.length()-1);

        if("false".equals(isempty(argString))) {
            String[] argList = argString.substring(1, argString.length()-1).split(" ");
            for(int i = 0;i < argList.length;i++) {
                localVarMap.put(argList[i], parseExpr(in, varMap));
            }
        }
        return run(new ListWrapper(funcCode), localVarMap);
    }

    static String wrapInfixExpr(String v) { // mostly used for ":"
        return (v.contains("+") || v.contains("-") || v.contains("*") || v.contains("/") || v.contains("%")) ? "("+v+")" : v;
    }
    static String parseInfixExpr(String v, HashMap<String, String> varMap) {
        v = v.substring(1, v.length()-1);
        if("true".equals(isnumber(v))) return v;

        // kill all spaces around +-*/%
        while(v.contains(" +")) v = v.replace(" +", "+"); while(v.contains("+ ")) v = v.replace("+ ", "+"); // v = v.replaceAll("\s*\\u002B\s*", "+");
        while(v.contains(" -")) v = v.replace(" -", "-"); while(v.contains("- ")) v = v.replace("- ", "-"); // v = v.replaceAll("\s*-\s*", "-");
        while(v.contains(" *")) v = v.replace(" *", "*"); while(v.contains("* ")) v = v.replace("* ", "*"); // v = v.replaceAll("\s*\\u002A\s*", "*");
        while(v.contains(" /")) v = v.replace(" /", "/"); while(v.contains("/ ")) v = v.replace("/ ", "/"); // v = v.replaceAll("\s*/\s*", "/");
        while(v.contains(" %")) v = v.replace(" %", "%"); while(v.contains("% ")) v = v.replace("% ", "%"); // v = v.replaceAll("\s*%\s*", "%");

        while(true) { // while there are brackets
            int lft = -1, rgt = -1, tmp = 0;
            for(int i = 0;i < v.length();i++) { // find brackets
                char ch = v.charAt(i);
                if(lft == -1 && ch == '(') {lft = i;}
                if(ch == '(') {tmp++;}
                if(ch == ')') {tmp--;}
                if(tmp == 0 && ch == ')') {rgt = i;break;}
            }
            if(lft == -1) {break;}
            String part1 = v.substring(0, lft);
            String part2 = parseInfixExpr(v.substring(lft, rgt+1), varMap);
            String part3 = v.substring(rgt+1);
            v = part1 + part2 + part3;
        }
        for(int i = 0;i < v.length();i++) { // do all +-
            char ch = v.charAt(i);
            if(ch == '+' || (ch == '-' && i != 0 && v.charAt(i-1) != '*' && v.charAt(i-1) != '/' && v.charAt(i-1) != '%')) {
                double d1 = Double.parseDouble(parseExpr(new ListWrapper("["+wrapInfixExpr(v.substring(0,i))+"]"), varMap));
                double d2 = Double.parseDouble(parseExpr(new ListWrapper("["+wrapInfixExpr(v.substring(i+1))+"]"), varMap));
                switch(ch) {
                    case '+': return "" + (d1 + d2);
                    case '-': return "" + (d1 - d2);
                }
            }
        }
        for(int i = 0;i < v.length();i++) { // do all */%
            char ch = v.charAt(i);
            if(ch == '*' || ch == '/' || ch == '%') {
                double d1 = Double.parseDouble(parseExpr(new ListWrapper("["+wrapInfixExpr(v.substring(0,i))+"]"), varMap));
                double d2 = Double.parseDouble(parseExpr(new ListWrapper("["+wrapInfixExpr(v.substring(i+1))+"]"), varMap));
                switch(ch) {
                    case '*': return "" + (d1 * d2);
                    case '/': return "" + (d1 / d2);
                    case '%': return "" + (d1 % d2);
                }
            }
        }
        return "[ERROR]parseInfixExpr";
    }

    static <T extends Iinput> String parseExpr(T in, HashMap<String, String> varMap) {
        String cmd, op1, op2, op3, res;
        cmd = in.next();

        char fstChar = cmd.charAt(0);

        if(fstChar == '-' || ('0' <= fstChar && fstChar <= '9')) { // number
            return cmd;
        }
        if(fstChar == '"') { // word
            return cmd.substring(1);
        }
        if(fstChar == '[') { // list
            int cnt = 0, lft = 0;
            while(true) {
                for(int i = lft;i < cmd.length();i++) {
                    if(cmd.charAt(i) == '[') cnt++;
                    if(cmd.charAt(i) == ']') cnt--;
                }
                lft = cmd.length();
                if(cnt == 0) break;
                cmd += (" " + in.next());
            }
            return cmd;
        }
        if(fstChar == '(') { // infix, not ROBUST!
            int lft = 0, rgt = 0;
            for(int i = 0;i < cmd.length();i++) {
                if(cmd.charAt(i) == '(') lft++;
                if(cmd.charAt(i) == ')') rgt++;
            }
            while(lft > rgt) {
                op1 = in.next();
                for(int i = 0;i < op1.length();i++) {
                    if(op1.charAt(i) == '(') lft++;
                    if(op1.charAt(i) == ')') rgt++;
                }
                cmd += (" " + op1);
            }
            return parseInfixExpr(cmd, varMap);
        }
        if(cmd.equals("true") || cmd.equals("false")) { // bool
            return cmd;
        }
        if(fstChar == ':') {
            return thing(cmd.substring(1), varMap);
        }

        switch(cmd) {
            // +-*/%
            case "add": return parseArithmeticExpr(in, varMap, cmd);
            case "sub": return parseArithmeticExpr(in, varMap, cmd);
            case "mul": return parseArithmeticExpr(in, varMap, cmd);
            case "div": return parseArithmeticExpr(in, varMap, cmd);
            case "mod": return parseArithmeticExpr(in, varMap, cmd);
            // =><
            case "eq": return parseArithmeticExpr(in, varMap, cmd);
            case "gt": return parseArithmeticExpr(in, varMap, cmd);
            case "lt": return parseArithmeticExpr(in, varMap, cmd);
            // &|~
            case "and": return parseLogicExpr(in, varMap, cmd);
            case  "or": return parseLogicExpr(in, varMap, cmd);
            case "not": return parseLogicExpr(in, varMap, cmd);
                
            case "thing": return thing(parseExpr(in, varMap), varMap);
            case "print":
                res = parseExpr(in, varMap);
                System.out.println(res);
                return res;
            case "make":
                op1 = parseExpr(in, varMap);
                op2 = parseExpr(in, varMap);
                varMap.put(op1, op2);
                return op2;
            case "read": return in.next();
            case "erase": return varMap.remove(parseExpr(in, varMap));
            case "isname": return isname(parseExpr(in, varMap), varMap);
            case "isnumber": return isnumber(parseExpr(in, varMap));
            case "isword": return isword(parseExpr(in, varMap));
            case "islist": return islist(parseExpr(in, varMap));
            case "isempty": return isempty(parseExpr(in, varMap));
            case "isbool": return isbool(parseExpr(in, varMap));
            case "if":
                op1 = parseExpr(in, varMap);
                op2 = parseExpr(in, varMap);
                op3 = parseExpr(in, varMap);
                if("false".equals(isbool(op1))) return "[ERROR]if - opcode is not bool";
                return run(new ListWrapper("true".equals(op1) ? op2 : op3), varMap);
            case "run":
                op1 = parseExpr(in, varMap);
                if("false".equals(islist(op1))) return "[ERROR]run - not list";
                return run(new ListWrapper(op1), varMap);
            case "return":
                sp--;
                return parseExpr(in, varMap);
            case "export":
                op1 = parseExpr(in, varMap);
                return globalVarMap.put(op1, thing(op1, varMap));
            default: // custom function
                if("false".equals(isname(cmd, varMap))) return "[ERROR]custom - nonexist function";
                return runFunc(in, thing(cmd, varMap), varMap);
        }
    }

    public static void main(String[] args) {
        ScannerWrapper in = new ScannerWrapper();
        while(in.hasNext()) {
            parseExpr(in, globalVarMap);
        }
    }
}
