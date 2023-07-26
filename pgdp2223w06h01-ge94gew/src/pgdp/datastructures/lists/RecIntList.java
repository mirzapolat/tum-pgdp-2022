package pgdp.datastructures.lists;

import java.util.Arrays;

public class RecIntList {
	private RecIntListElement head;

	public RecIntList() {
		head = null;
	}

	public void append(int value) {
		if (head == null) {
			head = new RecIntListElement(value);
		} else {
			head.append(value);
		}
	}

	public int get(int idx) {
		if (head == null) {
			System.out.println("Invalid index: list is empty!");
			return Integer.MAX_VALUE;
		}
		return head.get(idx);
	}

	public int size() {
	    /**
		 * can be rewritten as
		 * if(head==null)
		 * return 0;
		 * else
		 * return head.size();
		*/
		return head == null ? 0 : head.size();
	}

	public boolean insert(int value, int idx) {
		if (head == null) {
			if (idx == 0) {
				append(value);
				return true;
			} else {
				System.out.println("You may only insert at index 0 to a empty list!");
				return false;
			}
		}
		return head.insert(value, idx);
	}

	@Override
	public String toString() {
		if (head != null) {
			return "List: [" + head.toString() + "]";
		} else {
			return "Empty list";
		}
	}

	public String toConnectionString() {
		if (head != null) {
			return "List: [" + head.toConnectionString() + "]";
		} else {
			return "Empty list";
		}
	}

	public long[] countThresh(int threshold) {
		return countThresh(threshold, head, new long[] {0, 0, 0});
	}

	private long[] countThresh(int threshold, RecIntListElement e, long[] n) {
		if (e == null) {
			return n;
		}
		if (e.get(0) < threshold) {
			n[0] += e.get(0);
		} else if (e.get(0) == threshold) {
			n[1] += e.get(0);
		} else {
			n[2] += e.get(0);
		}
		return countThresh(threshold, e.getNext(), n);
	}

	public void kinguinSort(boolean increasing) {
		kinguinSort(increasing, head);
	}

	private void kinguinSort(boolean increasing, RecIntListElement e) {
		if (e == null) return;
		if (e.getNext() == null) return;


		if (e.getNext().get(0) == e.get(0) || e.getNext().get(0) > e.get(0) == increasing) {
			kinguinSort(increasing, e.getNext());
		}
		else {
			RecIntListElement n = e.getNext().getNext();
			if (n != null) n.setPrev(e);
			e.setNext(n);
			kinguinSort(increasing, e);
		}
	}

	public void reverse() {
		reverse(head, null);
	}

	private void reverse(RecIntListElement e, RecIntListElement by) {
		if (e == null) {
			head = by;
			return;
		}

		e.setPrev(e.getNext());
		reverse(e.getPrev(), e);
		e.setNext(by);
	}

	public static void zip(RecIntList l1, RecIntList l2) {
		if (l1.head == null) {
			if (l2.head == null) return;
			l1.head = l2.head;
			l2.head = l2.head.getNext();
		}

		zip(null, l1.head, l2.head, true);
	}

	private static void zip(RecIntListElement last, RecIntListElement c1, RecIntListElement c2, boolean firstList) {
		if (c1 == null && c2 == null) return;

		if (c2 == null) {
			c1.setPrev(last);
			if (last != null) last.setNext(c1);
			zip(c1, c1.getNext(), null, firstList);
		}
		else if (c1 == null) {
			c2.setPrev(last);
			if (last != null) last.setNext(c2);
			zip(c2, null, c2.getNext(), firstList);
		}
		else if (firstList) {
			c1.setPrev(last);
			if (last != null) last.setNext(c1);
			zip(c1, c1.getNext(), c2, !firstList);
		}
		else {
			c2.setPrev(last);
			if (last != null) last.setNext(c2);
			zip(c2, c1, c2.getNext(), !firstList);
		}
	}

	public static void main(String[] args) {
		/*
		// countThresh example
		RecIntList countThreshExample = new RecIntList();
		for (int i = 1; i <= 5; i++) {
			countThreshExample.append(i);
		}
		System.out.println(Arrays.toString(countThreshExample.countThresh(3)));


		// kinguinSort example (1)
		RecIntList kinguinSortExample = new RecIntList();
		int[] kinguinSortvalues = new int[] { 3, 2, 4, 7, 1, 6, 5, 9, 8 };
		for (int i : kinguinSortvalues) {
			kinguinSortExample.append(i);
		}
		kinguinSortExample.kinguinSort(true); // false for example (2)
		System.out.println(kinguinSortExample);

		// reverse example
		RecIntList reverseExample = new RecIntList();
		for (int i = 1; i < 6; i++) {
			reverseExample.append(i);
		}
		System.out.println(reverseExample);
		reverseExample.reverse();
		System.out.println(reverseExample); */

		// zip example
		RecIntList l1 = new RecIntList();
		RecIntList l2 = new RecIntList();
		for (int i = 1; i <= 5; i += 2) {
			l1.append(i);
			l2.append(i + 1);
		}
		l1.append(7);
		l1.append(8);
		RecIntList.zip(l1, l2);
		System.out.println(l1);
	}
}
