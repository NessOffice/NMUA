package mua;

class ListWrapper implements Iinput {
    private int ptr = 0;
    public String[] list;
    public ListWrapper(String list) {
        this.list = list.split(" ");
    }
    @Override
    public boolean hasNext() {
        return ptr != list.length;
    }
    @Override
    public String next() {
        return list[ptr++];
    }
}
