package cs1302.p2;

import cs1302.adt.StringList;

/**
 * BaseStringList.
 */
public abstract class BaseStringList implements StringList {
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

} // BaseStringList
