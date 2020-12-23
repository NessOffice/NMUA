package mua;

import java.util.*;

public class Main {
    static HashMap<String, String> map = new HashMap<String, String>();

    public static String parseArithmeticExpr(Scanner in, String cmd) {
        String op1, op2;
        double d1, d2;
        op1 = parseExpr(in);
        op2 = parseExpr(in);
        d1 = Double.parseDouble(op1);
        d2 = Double.parseDouble(op2);
        switch(cmd) {
            case "add": return "" + (d1 + d2);
            case "sub": return "" + (d1 - d2);
            case "mul": return "" + (d1 * d2);
            case "div": return "" + (d1 / d2);
            case "mod": return "" + (d1 % d2);
            default: return "[Error]parseArithmeticExpr";
        }
    }

    public static String parseExpr(Scanner in) {
        String cmd, op1, op2, res;
        cmd = in.next();

        char fstChar = cmd.charAt(0);

        if(fstChar == '-' || ('0' <= fstChar && fstChar <= '9')) { // number
            return cmd;
        }
        if(fstChar == '"') { // word
            return cmd.substring(1);
        }
        // list
        if(cmd.equals("true") || cmd.equals("false")) { // bool
            return cmd;
        }

        if(fstChar == ':') {
            return map.get(cmd.substring(1));
        }

        switch(cmd) {
            case "add": return parseArithmeticExpr(in, cmd);
            case "sub": return parseArithmeticExpr(in, cmd);
            case "mul": return parseArithmeticExpr(in, cmd);
            case "div": return parseArithmeticExpr(in, cmd);
            case "mod": return parseArithmeticExpr(in, cmd);
            case "thing":
                op1 = parseExpr(in);
                return map.get(op1);
            case "print":
                res = parseExpr(in);
                System.out.println(res);
                return res;
            case "make":
                op1 = parseExpr(in);
                op2 = parseExpr(in);
                map.put(op1, op2);
                return op2;
            case "read":
                return in.next();
            default:
                return "[ERROR]parseExpr";
        }
    }
    public static void main(String[] args) {
        var in = new Scanner(System.in);
        while(in.hasNext()) {
            parseExpr(in);
        }
        in.close();
    }
}
