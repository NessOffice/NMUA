package mua;

import java.util.ArrayList;
import java.util.List;

class ListWrapper implements Iinput {
    private int ptr;
    private List<String> list;
    static int nxt(String str, int lft, char chLft, char chRgt) {
        int cnt = 0;
        for(int i = lft;;i++) {
            if(str.charAt(i) == chLft) cnt++;
            if(str.charAt(i) == chRgt) cnt--;
            if(cnt == 0) return i;
        }
    }
    public ListWrapper(String str) {
        ptr = 0;
        list = new ArrayList<String>();

        while(str.contains("\n")) str = str.replace("\n", " ");
        while(str.contains("\t")) str = str.replace("\t", " ");
        while(str.contains("  ")) str = str.replace("  ", " ");
        while(str.charAt(0) == ' ') str = str.substring(1);
        while(str.charAt(str.length()-1) == ' ') str = str.substring(0, str.length()-1);

        str = str.substring(1, str.length()-1);
        if(str.isEmpty()) return;

        while(str.charAt(0) == ' ') str = str.substring(1);
        while(str.charAt(str.length()-1) == ' ') str = str.substring(0, str.length()-1);
        if(str.isEmpty()) return;

        int lst = 0;
        for(int i = 0;i < str.length();i++) {
            if(str.charAt(i) == '[') {
                lst = i;
                i = nxt(str, i , '[', ']');
                list.add(str.substring(lst, i+1));
                lst = i+1;
            }
            if(str.charAt(i) == '(') {
                lst = i;
                i = nxt(str, i , '(', ')');
                list.add(str.substring(lst, i+1));
                lst = i+1;
            }
            if(str.charAt(i) == ' ') {
                if(lst < i)
                    list.add(str.substring(lst, i));
                lst = i+1;
            }
        }
        if(lst < str.length())
            list.add(str.substring(lst));
    }
    @Override
    public boolean hasNext() {
        return ptr != list.size();
    }
    @Override
    public String next() {
        return list.get(ptr++);
    }
    public void concat(ListWrapper list2) {
        for(int i = 0;i < list2.list.size();i++) {
            list.add(list2.list.get(i));
        }
    }
    public void append(String item) {
        list.add(item);
    }
    public String toString(int beginIndex, int endIndex) {
        String res = "[";
        for(int i = beginIndex;i < endIndex;i++) {
            res += list.get(i);
            if(i < list.size()-1) {res += " ";}
        }
        res += "]";
        return res;
    }
    public String toString(int beginIndex) {
        return this.toString(beginIndex, list.size());
    }
    public String toString() {
        return this.toString(0, list.size());
    }
    public String getFirst() {
        return list.get(0);
    }
    public String getLast() {
        return list.get(list.size()-1);
    }
    public String butFirst() {
        return this.toString(1);
    }
    public String butLast() {
        return this.toString(0, list.size()-1);
    }
}
