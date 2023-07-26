package pgdp.ds;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Stack {
	private Stack next;
	private final int[] mem;
	private int top;

	public Stack(int capacity) {
		next = null;
		mem = new int[capacity];
		top = -1;
	}

	public boolean isEmpty() {
		return top == -1;
	}

	public boolean isFull() {
		return top == mem.length -1;
	}

	public boolean push(int n) {
		if (isFull()) return false;
		mem[++top] = n;
		return true;
	}

	public Stack getNext() {
		return next;
	}

	public Stack createNext() {
		next = new Stack(mem.length * 2);
		return next;
	}

	public void deleteNext() {
		next = null;
	}

	public int top() {
		if (isEmpty()) return Integer.MIN_VALUE;
		return mem[top];
	}

	public int pop() {
		if (isEmpty()) return Integer.MIN_VALUE;
		return mem[top--];
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{\ncapacity = ").append(mem.length).append(",\n");
		sb.append("mem = [")
				.append(IntStream.range(0, top + 1).mapToObj(x -> "" + mem[x]).collect(Collectors.joining(", ")))
				.append("],\n");
		sb.append("next = ").append(next == null ? "null" : "\n" + next.toString()).append("\n}\n");
		return sb.toString();
	}
}
