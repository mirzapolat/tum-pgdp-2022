package pgdp.ds;

public class MultiStack {

	private final Stack stacks;

	public MultiStack() {
		stacks = new Stack(1);
	}

	public void push(int n) {
		push(n, stacks);
	}

	private void push(int n, Stack current) {
		if (current.push(n)) return;
		else if (current.getNext() != null) push(n, current.getNext());
		else current.createNext().push(n);
	}

	public int top() {
		return top(stacks);
	}

	private int top(Stack current) {
		if (current.getNext() != null) return top(current.getNext());
		else return current.top();
	}

	public int pop() {
		return pop(stacks);
	}

	private int pop(Stack current) {
		if (current.getNext() != null) {
			if (current.getNext().getNext() == null) {
				int value = current.getNext().pop();
				if (current.getNext().isEmpty()) current.deleteNext();
				return value;
			}
			else return pop(current.getNext());
		}
		else return current.pop();
	}

	@Override
	public String toString() {
		return stacks.toString();
	}

}
