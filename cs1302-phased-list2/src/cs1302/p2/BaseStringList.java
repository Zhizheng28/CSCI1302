package cs1302.p2;

import cs1302.adt.FancyStringList;
import cs1302.adt.StringList;

/**
 * BaseStringList.
 */
public abstract class BaseStringList implements FancyStringList {
    // Declare Varibles
    protected int size;

    /**
     * Constructor BaseStringList.
     */
    public BaseStringList() {
        this.size = 0;
    } // BaseStringList

    /**
     * Append an item the String List.
     * @param item - String
     * @return add the an item to the end of the list.
     */
    @Override
    public boolean append(String item) {
        return add(this.size,item);
    } // append

    /**
     * Check if the String List is empty or not.
     * @return true if it's is empty / false if it's not empty.
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    } // isEmpty

    /**
     * make string with a start, an end, and items sparated by a separator.
     * @param start - String
     * @param sep - String
     * @param end - String
     * @return string that has start, sep, and end.
     */
    @Override
    public String makeString(String start, String sep, String end) {
        if (this.size > 0) {
            String string = start;
            for (int i = 0; i < size - 1; i++) {
                string += get(i) + sep;
            } // for
            string += get(this.size - 1);
            return string + end;
        } else {
            return start + end;
        }
    } // makeString

    /**
     * prepend an item to the String List.
     * @param item - String
     * @return add an item to the beginning of the String List.
     */
    @Override
    public boolean prepend(String item) {
        return add(0,item);
    } // prepend

    /**
     * size of the String List.
     * @return the size of the String List.
     */
    @Override
    public int size() {
        return this.size;
    } // size

    /**
     * Convert String List to a specific format.
     * @return formatted String List.
     */
    @Override
    public String toString() {
        return makeString("[",", ","]");
    } // toString

    //FancyStringList Implementation

    /**
     * add items at specific index.
     * @param index - int
     * @param items - StringList
     * @return true if add method works
     */
    @Override
    public boolean add(int index, StringList items) {
        if (items == null) {
            throw new NullPointerException();
        } // if
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        } else {
            if (items == this) {
                FancyStringList temp = this.deepCopy(items);
                for (int i = temp.size() - 1; i >= 0; i--) {
                    add(index, temp.get(i));
                }
            } else {
                for (int j = items.size() - 1; j >= 0; j--) {
                    add(index, items.get(j));
                } // for
            } // if/else
        } // if
        return true;
    } // add

    /**
     * Appends items to a StringList.
     * @param items - StringList
     * @return true if append method works
     */
    @Override
    public boolean append(StringList items) {
        return add(this.size, items);
    } // append

    /**
     * Check if target exists at start and after start position.
     * @param start - int
     * @param target - String
     * @return true if target exist at start and after start index.
     */
    @Override
    public boolean contains(int start, String target) {
        for (int i = start; i < size; i++) {
            if (start >= 0 && get(i).equals(target)) {
                return true;
            } // if
        } // for
        return false;
    } // contains

    /**
     * indexOf of the index at start and after start position.
     * @param start - int
     * @param target - String
     * @return the index value of item at start and after start position / -1 if not
     */
    @Override
    public int indexOf(int start, String target) {
        for (int i = start; i < size; i++) {
            if (i >= start && start >= 0 && get(i).equals(target)) {
                return i;
            }
        } // for
        return -1;
    } // indexOf

    /**
     * prepend items to the StringList.
     * @param items - StringList
     * @return true if prepend method workds
     */
    @Override
    public boolean prepend(StringList items) {
        return add(0, items);
    } // prepend

    /**
     * abstract methody for deepCopy of FancyStringList constructor.
     * @param other - StringList
     * @return FancyStrinList constructor
     */
    public abstract FancyStringList deepCopy(StringList other);

} // BaseStringList
