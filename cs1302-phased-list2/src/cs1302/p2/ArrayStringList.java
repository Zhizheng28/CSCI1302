package cs1302.p2;

import cs1302.adt.StringList;
import cs1302.adt.FancyStringList;

/**
 *ArrayStringList.
 */
public class ArrayStringList extends BaseStringList {
    //Declare varibles
    private String [] items;

    /**
     * constructor for ArrayStringList.
     */
    public ArrayStringList() {
        this.items = new String [66];
        size = 0;
    } // ArrayStringList

    /**
     * constuctor for a copy ArrayStringList.
     * @param other - StringList
     */
    public ArrayStringList(StringList other) {
        if (other == null) {
            throw new NullPointerException();
        } // if
        size = other.size();
        items = new String [size];
        for (int i = 0; i < size; i++) {
            String temp = other.get(i);
            items[i] = temp;
        } // for
    } // ArrayStringList - copy

    /**
     * add an item to the Array String List at specific index.
     * @param index - int
     * @param item - String
     * @return true if an item is been added
     */
    @Override
    public boolean add(int index, String item) {
        if (item == null) {
            throw new NullPointerException();
        } // if
        if (item == "") {
            throw new IllegalArgumentException();
        } // if
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        } // if
        if (index == size || items[items.length - 1] != null || items[index] != null) {
            items = increaseSize();
        } // if
        if (index >= 0 || index <= size) {
            String temp = items[index];
            items[index] = item;
            for (int i = 1; i <= size - index; i++) {
                item = items[index + i];
                items[index + i] = temp;
                temp = item;
            } // for
        } // if
        size++;
        return true;
    } // add

    /**
     * clears the Array String List.
     */
    @Override
    public void clear() {
        items = new String [66];
        size = 0;
    } // clear

    /**
     * Get an item from a specific array index.
     * @param index - int
     * @return get item.
     */
    @Override
    public String get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return items[index];
    } // get

    /**
     * Remove an item from a specific array index.
     * @param index - int
     * @return removed item.
     */
    @Override
    public String remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } // if
        String temp = this.items[index];
        for (int i = index; i < size; i++) {
            items[i] = items[i + 1];
        } // for
        size--;
        return temp;
    } // remove

    /**
     * slice ArrayString List from start to end.
     * @param start - int
     * @param stop - int
     * @return Sliced Array String List
     */
    @Override
    public StringList slice(int start, int stop) {
        if (start < 0 || stop > size() || start > stop) {
            throw new IndexOutOfBoundsException();
        } // if
        StringList arraySlice = new ArrayStringList();
        int counter = stop - start;
        for (int i = 0; i < counter; i++) {
            arraySlice.add(i,items[start++]);
        } // for
        arraySlice.toString();
        return arraySlice;
    } // slice

    /**
     * increaseSize - increase size of the array by 10.
     * @return expanded Array.
     */
    private String[] increaseSize() {
        String[] addArray = new String[size + 10];
        for (int i = 0; i < size; i++) {
            addArray[i] = items[i];
        } // for
        return addArray;
    } // increaseSize

    // FancyStringList implementation

    /**
     * slice FancyStringList from start to end by step.
     * @param start - int
     * @param stop - int
     * @param step - int
     * @return Sliced FancyStringList
     */
    @Override
    public FancyStringList slice(int start, int stop, int step) {
        if (start < 0 || stop > size() || start > stop || step < 1) {
            throw new IndexOutOfBoundsException();
        }
        FancyStringList arraySlice = new ArrayStringList();
        // int index = 0;
        for (int i = start; i < stop; i += step) {
            arraySlice.append(get(i));
        } // for
        return arraySlice;
    } // slice

    /**
     * reverse fancyStringList.
     * @return reversed FancyStringList
     */
    public FancyStringList reverse() {
        FancyStringList revList = new ArrayStringList();
        for (int i = size - 1; i >= 0; i--) {
            revList.append(get(i));
        } // for
        return revList;
    } // reverse

    @Override
    public FancyStringList deepCopy(StringList other) {
        return new ArrayStringList(other);
    }
} // ArrayStringList
