package mua;

import java.util.*;

class ScannerWrapper implements Iinput {
    static Scanner in = new Scanner(System.in);
    @Override
    public boolean hasNext() {
        return in.hasNext();
    }
    @Override
    public String next() {
        return in.next();
    }
}
