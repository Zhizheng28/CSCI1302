package cs1302.p2;

import cs1302.adt.StringList;
import cs1302.adt.Node;

/**
 *LinkedStringList.
 */
public class LinkedStringList extends BaseStringList {
    //Declare Varibles
    private Node head;

    /**
     * Constructor LinkedStringList.
     */
    public LinkedStringList() {
        this.head = null;
    } // LinkedStringList

    /**
     * add a item to the Linked String List at specific index.
     * @param index - int
     * @param item - String
     * @return true if an item is been added.
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
        if (index == 0) {
            Node temp = new Node(item,head);
            head = temp;
        } else {
            Node temp = head;
            for (int i = 0; i < index - 1; i++) {
                temp = temp.getNext();
            } // for
            Node addNode = new Node(item, temp.getNext());
            temp.setNext(addNode);
        } // if/else
        size++;
        return true;
    } // add

    /**
     * clear the Linked String List.
     */
    @Override
    public void clear() {
        this.head = null;
        size = 0;
    } // clear

    /**
     * get item from the specific index in the Linked String List.
     * @param index - int
     * @return get item
     */
    @Override
    public String get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node temp = head;
        if (index == 0) {
            return head.getItem();
        } else {
            for (int i = 0; i < index; i++) {
                temp = temp.getNext();
            } // for
            return temp.getItem();
        } // if/else
    } // index

    /**
     * remove an item from Linked String List at specific index.
     * @param index - int
     * @return removed item
     */
    @Override
    public String remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } // if
        Node temp = head;
        String removeNode;
        if (index == 0) {
            head = head.getNext();
            removeNode = temp.getItem();
            size--;
        } else {
            for (int i = 0; i < index - 1; i++) {
                temp = temp.getNext();
            } // for
            removeNode = temp.getNext().getItem();
            temp.setNext(temp.getNext().getNext());
            size--;
        }
        return removeNode;
    } // remove

    /**
     * slice the Linked String List from stat to stop.
     * @param start - int
     * @param stop - int
     * @return Sliced Linked String List
     */
    @Override
    public StringList slice(int start, int stop) {
        if (start < 0 || stop > size() || start > stop) {
            throw new IndexOutOfBoundsException();
        } // if
        StringList linkedSlice = new LinkedStringList();
        Node temp = head;
        int counter = stop - start;
        for (int i = 0; i < start; i++) {
            temp = temp.getNext();
        } // for
        for (int j = 0; j < counter; j++) {
            linkedSlice.add(j, temp.getItem());
            temp = temp.getNext();
        } // for
        linkedSlice.toString();
        return linkedSlice;
    } // slice

} // LinkedStringList
