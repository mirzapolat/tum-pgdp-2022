package pgdp.datastructures;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

public class QuarternarySearchTree<T extends Comparable<T>> implements Iterable<T> {
	private QuarternaryNode<T> root;

	public QuarternarySearchTree() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public int size() {
		return root == null ? 0 : root.size();
	}

	public boolean contains(T value) {
		if (isEmpty()) {
			return false;
		}
		return root.contains(value);
	}

	public void insert(T value) {
		if (value == null) {
			return;
		}
		if (isEmpty()) {
			root = new QuarternaryNode<T>(value);
		} else {
			root.insert(value);
		}
	}

	@Override
	public String toString() {
		if (isEmpty()) {
			return "[]";
		} else {
			return root.toString();
		}
	}

	public String toGraphvizString() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {\n");
		sb.append(root == null ? "" : root.toGraphvizStringHelper());
		sb.append("}");
		return sb.toString();
	}

	public QuarternaryNode<T> getRoot() {
		return root;
	}

	public void setRoot(QuarternaryNode<T> root) {
		this.root = root;
	}

	private static class Element<T extends Comparable<T>> {
		QuarternaryNode<T> Node;
		int worked;

		public Element(QuarternaryNode<T> node, int worked){
			this.Node = node;
			this.worked = worked;
		}
	}

	@Override
	public Iterator<T> iterator() {

		final QuarternaryNode<T> myroot = this.root;

		return new Iterator<T>() {

			ArrayList<Element<T>> parents = new ArrayList<>();
			Element<T> current = new Element<>(myroot, 0);

			T temp;
			boolean tempHasNext = false;

			@Override
			public boolean hasNext() {
				if (myroot == null) return false;
				if (tempHasNext) return true;
				if (myroot.getNodeSize() == 0) return false;

				while (true) {
					if (current.Node == null) {
						if (parents.isEmpty()) return false;
						current = parents.get(parents.size() - 1);
						parents.remove(parents.size() - 1);
					}

					me:
					switch (current.worked) {
						case 0, 2, 4, 6 -> {
							parents.add(new Element<>(current.Node, current.worked + 1));
							int i = switch (current.worked) {
								case 0 -> 0;
								case 2 -> 1;
								case 4 -> 2;
								case 6 -> 3;
								default -> 4;
							};
							current = new Element<>(current.Node.getChild(i), 0);
						}
						case 1, 3, 5 -> {
							int i = switch (current.worked++) {
								case 1 -> 0;
								case 3 -> 1;
								case 5 -> 2;
								default -> 4;
							};

							if (current.Node.getValue(i) != null){
								temp = current.Node.getValue(i);
								tempHasNext = true;
								return true;
							}

						}
						default -> {
							if (parents.isEmpty()) return false;
							current = parents.get(parents.size() - 1);
							parents.remove(parents.size() - 1);
						}
					}
				}
			}

			@Override
			public T next() {
				if (temp == null) {
					if (hasNext()) return temp;
					else throw new NoSuchElementException("Trying to call next on an empty QuarternarySearchTreeIterator");
				}
				T a = temp;
				temp = null;
				tempHasNext = false;
				return a;
			}
		};
	}

	public static void main(String[] args) {
		int[] values = new int[] { 8, 4, 12, 1, 5, 9, 13, 3, 7, 11, 15, 2, 6, 10, 14};
		QuarternarySearchTree<Integer> n = new QuarternarySearchTree<Integer>();
		for (int i : values) {
			n.insert(i);
		}
		//System.out.println(n.toGraphvizString());


		for (int i : n) {
			System.out.print(i + " - ");
		}
	}
}
