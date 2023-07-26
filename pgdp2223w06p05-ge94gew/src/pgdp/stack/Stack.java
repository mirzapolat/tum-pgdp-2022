package pgdp.stack;

import pgdp.stack.StackElement;

public class Stack {
    private StackElement first;
    private int size;

    public void push(int number) {
        StackElement se = new StackElement(number);
        size++;

        if (first == null) {
            first = se;
            return;
        }

        se.setNext(first);
        first = se;
    }

    public int pop() {
        if (first == null) return -1;

        int returnValue = first.getValue();
        size--;

        if (size == 0) first = null;
        else first = first.getNext();

        return returnValue;
    }

    public int[] toArray() {
        int[] queueAsArray = new int[size];
        StackElement current = first;
        for(int i = 0; current != null; current = current.getNext(), i++) {
            queueAsArray[i] = current.getValue();
        }
        return queueAsArray;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
