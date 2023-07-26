package ds.queue;

public class Deque {

	private Element head;
	private Element tail;

	private int size;

	public Deque() {
		head = null;
		tail = null;
	}

	public boolean isEmpty() { return head == null; }

	public int size() {return size;}

	public void addFirst(int number) {
		Element e = new Element(number);
		size++;

		if (head == null) {
			head = tail = e;
			return;
		}

		e.next = head;
		head = e;
		head.next.prev = e;
	}

	public int removeFirst() {
		if (head == null) return Integer.MIN_VALUE;
		int returnValue = head.val;

		if (head.next == null){
			head = tail = null;
		}
		else {
			head = head.next;
			head.prev = null;
		}

		size--;
		return returnValue;
	}

	public int getFirst() {
		if (head == null) return Integer.MIN_VALUE;
		return head.val;
	}

	public void addLast(int number) {
		Element e = new Element(number);
		size++;

		if (tail == null) {
			head = tail = e;
			return;
		}

		e.prev = tail;
		tail = e;
		tail.prev.next = e;
	}

	public int removeLast() {
		if (tail == null) return Integer.MIN_VALUE;
		int returnValue = tail.val;

		if (tail.prev == null){
			head = tail = null;
		}
		else {
			tail = tail.prev;
			tail.next = null;
		}

		size--;
		return returnValue;
	}

	public int getLast() {
		if (tail == null) return Integer.MIN_VALUE;
		return tail.val;
	}

	@Override
	public String toString() {
		if (isEmpty()) {
			return "{}";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (Element current = head; current != null; current = current.next) {
			sb.append(current.val).append(", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append("}");
		return sb.toString();
	}

	private class Element {
		Element prev;
		Element next;
		int val;

		Element(int v) {
			val = v;
		}

		Element(int v, Element n) {
			val = v;
			next = n;
		}

		Element(Element p, int v) {
			val = v;
			prev = p;
		}
	}

}
