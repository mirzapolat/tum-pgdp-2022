package pgdp.queue;

public class Queue {
    private QueueElement first;
    private QueueElement last;
    private int size;

    public void push(int number) {
        size++;
        QueueElement qe = new QueueElement(number);

        if (first == null) {
            first = last = qe;
            return;
        }

        last.setNext(qe);
        last = qe;
    }

    public int pop() {
        if (first == null) return -1;

        int returnValue = first.getValue();
        size--;

        if (first == last) first = last = null;
        else first = first.getNext();

        return returnValue;
    }

    public int[] toArray() {
        int[] queueAsArray = new int[size];
        QueueElement current = first;
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
