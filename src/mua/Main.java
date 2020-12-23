package mua;

import java.util.*;

public class Main {
    static HashMap<String, String> variableMap = new HashMap<String, String>();

    public static String isnumber(String v) { // not ROBUST!
        for(int i = 0;i < v.length();i++) {
            char ch = v.charAt(i);
            if(!((ch == '-') || (ch == '.') || ('0' <= ch && ch <= '9'))) return "false";
        }
        return "true";
    }
    public static String islist(String v) {
        return (v.charAt(0) == '[' && v.charAt(v.length()-1) == ']') ? "true" : "false";
    }
    public static String isbool(String v) {
        return ("true".equals(v) || "false".equals(v)) ? "true" : "false";
    }
    public static String isword(String v) { // not ROBUST!
        return ("false".equals(isnumber(v)) && "false".equals(islist(v)) && "false".equals(isbool(v))) ? "true" : "false";
    }
    public static String isempty(String v) { // not ROBUST!
        if("true".equals(islist(v))) return ("[]".equals(v)) ? "true" : "false";
        return "false";
    }
    public static String isfunc(String v) {
        switch(v) {
            case "add": return "true";
            default: return "false";
        }
    }

    public static <T extends Iinput> String parseArithmeticExpr(T in, String cmd) {
        String op1, op2;
        double d1, d2;
        op1 = parseExpr(in);
        op2 = parseExpr(in);
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

    public static <T extends Iinput> String parseLogicExpr(T in, String cmd) { // not robust
        String op1 = parseExpr(in), op2;
        switch(cmd) {
            case "and": op2 = parseExpr(in);return "true".equals(op1) && "true".equals(op2) ? "true" : "false";
            case "or": op2 = parseExpr(in);return "true".equals(op1) || "true".equals(op2) ? "true" : "false";
            case "not": return "true".equals(op1) ? "false" : "true";
            default: return "[Error]parseLogicExpr";
        }
    }

    public static String run(ListWrapper in) {
        String res = "";
        while(in.hasNext()) {
            res = parseExpr(in);
        }
        return res;
    }

    public static String wrapInfixExpr(String v) { // mostly used for ":"
        return (v.contains("+") || v.contains("-") || v.contains("*") || v.contains("/") || v.contains("%")) ? "("+v+")" : v;
    }
    public static String parseInfixExpr(String v) {
        v = v.substring(1, v.length()-1);
        if("true".equals(isnumber(v))) return v;

        // kill all spaces around +-*/%
        while(v.contains(" +")) v = v.replace(" +", "+"); while(v.contains("+ ")) v = v.replace("+ ", "+");
        while(v.contains(" -")) v = v.replace(" -", "-"); while(v.contains("- ")) v = v.replace("- ", "-");
        while(v.contains(" *")) v = v.replace(" *", "*"); while(v.contains("* ")) v = v.replace("* ", "*");
        while(v.contains(" /")) v = v.replace(" /", "/"); while(v.contains("/ ")) v = v.replace("/ ", "/");
        while(v.contains(" %")) v = v.replace(" %", "%"); while(v.contains("% ")) v = v.replace("% ", "%");
        // sb PTA, cnm
        // v = v.replaceAll("\s*\\u002B\s*", "+");
        // v = v.replaceAll("\s*-\s*", "-");
        // v = v.replaceAll("\s*\\u002A\s*", "*");
        // v = v.replaceAll("\s*/\s*", "/");
        // v = v.replaceAll("\s*%\s*", "%");

        // run it as until there is no prefix operator except for ":"
        if(v.contains(" ")) return run(new ListWrapper(v));

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
            String part2 = parseInfixExpr(v.substring(lft, rgt+1));
            String part3 = v.substring(rgt+1);
            v = part1 + part2 + part3;
        }
        for(int i = 0;i < v.length();i++) { // do all +-
            char ch = v.charAt(i);
            if(ch == '+' || (ch == '-' && i != 0 && v.charAt(i-1) != '*' && v.charAt(i-1) != '/' && v.charAt(i-1) != '%')) {
                double d1 = Double.parseDouble(parseExpr(new ListWrapper(wrapInfixExpr(v.substring(0, i)))));
                double d2 = Double.parseDouble(parseExpr(new ListWrapper(wrapInfixExpr(v.substring(i+1)))));
                switch(ch) {
                    case '+': return "" + (d1 + d2);
                    case '-': return "" + (d1 - d2);
                }
            }
        }
        for(int i = 0;i < v.length();i++) { // do all */
            char ch = v.charAt(i);
            if(ch == '*' || ch == '/' || ch == '%') {
                double d1 = Double.parseDouble(parseExpr(new ListWrapper(wrapInfixExpr(v.substring(0, i)))));
                double d2 = Double.parseDouble(parseExpr(new ListWrapper(wrapInfixExpr(v.substring(i+1)))));
                switch(ch) {
                    case '*': return "" + (d1 * d2);
                    case '/': return "" + (d1 / d2);
                    case '%': return "" + (d1 % d2);
                }
            }
        }
        return "[ERROR]parseInfixExpr";
    }

    public static <T extends Iinput> String parseExpr(T in) {
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
            while(cmd.charAt(cmd.length()-1) != ']') {
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
            return parseInfixExpr(cmd);
        }
        if(cmd.equals("true") || cmd.equals("false")) { // bool
            return cmd;
        }
        if(fstChar == ':') {
            return variableMap.get(cmd.substring(1));
        }

        switch(cmd) {
            // +-*/%
            case "add": return parseArithmeticExpr(in, cmd);
            case "sub": return parseArithmeticExpr(in, cmd);
            case "mul": return parseArithmeticExpr(in, cmd);
            case "div": return parseArithmeticExpr(in, cmd);
            case "mod": return parseArithmeticExpr(in, cmd);
            // =><
            case "eq": return parseArithmeticExpr(in, cmd);
            case "gt": return parseArithmeticExpr(in, cmd);
            case "lt": return parseArithmeticExpr(in, cmd);
            // &|~
            case "and": return parseLogicExpr(in, cmd);
            case "or": return parseLogicExpr(in, cmd);
            case "not": return parseLogicExpr(in, cmd);
                
            case "thing": return variableMap.get(parseExpr(in));
            case "print":
                res = parseExpr(in);
                System.out.println(res);
                return res;
            case "make":
                op1 = parseExpr(in);
                op2 = parseExpr(in);
                variableMap.put(op1, op2);
                return op2;
            case "read": return in.next();
            case "erase": return variableMap.remove(parseExpr(in));
            case "isname": return variableMap.containsKey(parseExpr(in)) ? "true" : "false";
            case "isnumber": return isnumber(parseExpr(in));
            case "isword": return isword(parseExpr(in));
            case "islist": return islist(parseExpr(in));
            case "isempty": return isempty(parseExpr(in));
            case "isbool": return isbool(parseExpr(in));
            case "if":
                op1 = parseExpr(in);
                op2 = parseExpr(in);
                op3 = parseExpr(in);
                if("false".equals(isbool(op1))) return "[ERROR]if";
                return "true".equals(op1) ? run(new ListWrapper(op2.substring(1, op2.length()-1))) : run(new ListWrapper(op3.substring(1, op3.length()-1)));
            case "run":
                op1 = parseExpr(in);
                if("false".equals(islist(op1)) || "true".equals(isempty(op1))) return "[ERROR]run";
                return run(new ListWrapper(op1.substring(1, op1.length()-1)));
            default: return "[ERROR]parseExpr";
        }
    }

    public static void main(String[] args) {
        ScannerWrapper in = new ScannerWrapper();
        while(in.hasNext()) {
            parseExpr(in);
        }
    }
}
