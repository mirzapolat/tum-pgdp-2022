package pgdp.messenger;

import java.time.*;

public class List {
	private ListElement head;
	private ListElement tail;
	private int size;

	public boolean isEmpty() {
		return head == null;
	}

	/** Fügt die übergebene 'message' am übergebenen 'index' ein.
	 *  Wenn der 'index' out of bounds liegt oder die 'message' 'null' ist, geschieht nichts.
	 * @param index Ein beliebiger Integer
	 * @param message Eine beliebige Message-Referenz
	 */
	public void insertAt(int index, Message message) {
		if (index > size || index < 0 || message == null) {
			return;
		}
		if (head == null) {
			head = tail = new ListElement(message, null);
		} else if (index == 0) {
			head = new ListElement(message, head);
		} else if (index == size) {
			add(message);
			return;
		} else {
			ListElement prev = null;
			ListElement current = head;
			for (int i = 0; i < index; i++) {
				prev = current;
				current = current.getNext();
			}
			prev.setNext(new ListElement(message, current));
		}
		size++;
	}

	/** Fügt die übergebene 'message' am Ende dieser Liste hinzu. Wenn die Message 'null' ist, geschieht nichts.
	 * @param message Eine beliebige Message-Referenz
	 */
	public void add(Message message) {
		if (message == null) {
			return;
		}
		if (tail == null) {
			head = tail = new ListElement(message, null);
		} else {
			tail.setNext(new ListElement(message, null));
			tail = tail.getNext();
		}
		size++;
	}

	/** Entfernt die übergebene 'message' (das konkrete Objekt) aus der Liste.
	 *  Wenn es nicht enthalten ist (oder 'message == null' ist), geschieht nichts.
	 * @param message Eine beliebige Message-Referenz
	 */
	public void delete(Message message) {
		ListElement prev = null;
		ListElement current = head;
		while (current != null) {
			if (current.getMessage() == message) {
				if (prev == null) {
					head = head.getNext();
				} else {
					prev.setNext(current.getNext());
					if (current.getNext() == null) {
						tail = prev;
					}
				}
				size--;
				return;
			}
			prev = current;
			current = current.getNext();
		}
	}

	/** Gibt die aktuelle Größe dieser Liste zurück.
	 * @return Die Größe dieser Liste
	 */
	public int size() {
		return size;
	}

	/** Gibt die Message an der 'index'-ten Stelle dieser Liste zurück.
	 *  Wenn der übergebene 'index' out of bounds liegt, wird 'null' zurückgegeben.
	 * @param index Ein beliebiger Integer
	 * @return Die gefundene Message oder 'null'
	 */
	public Message getByIndex(int index) {
		if (index >= size || index < 0) {
			return null;
		}
		ListElement current = head;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		return current.getMessage();
	}

	/** Gibt die Message mit der übergebenen ID zurück, falls sie sich in der Liste befindet, 'null' sonst.
	 * @param id Ein beliebiger Long
	 * @return Die gefundene Message oder 'null'
	 */
	public Message getByID(long id) {
		ListElement current = head;
		while (current != null) {
			if (current.getMessage().getId() == id) return current.getMessage();
			current = current.getNext();
		}

		return null;
	}

	/** Merged die übergebenen Listen in eine große Liste. Diese wird dann zurückgegeben.
	 * @param input Ein beliebiges Array von beliebigen Listen
	 * @return Die gemergte Liste
	 */
	public static List megaMerge(List... input) {
		if (input.length == 0) return new List();
		return multiMerge(input, 0, input.length);
	}

	private static List multiMerge(List[] lists, int from, int to) {
		if (from == to) return new List();
		if (from == to-1) return lists[from];

		return merge(multiMerge(lists, from + 1, to), lists[from]);
	}

	private static List merge(List l1, List l2) {
		List result = new List();

		for (int i = 0; i < (l1.size() + l2.size()); i++) {
			if (l1.head == null) {
				result.add(l2.head.getMessage());
				l2.head = l2.head.getNext();
			}
			else if (l2.head == null) {
				result.add(l1.head.getMessage());
				l1.head = l1.head.getNext();
			}
			else {
				if (l1.head.getMessage().getTimestamp().isBefore(l2.head.getMessage().getTimestamp()) || l1.head.getMessage().getTimestamp().isEqual(l2.head.getMessage().getTimestamp())) {
					result.add(l1.head.getMessage());
					l1.head = l1.head.getNext();
				}
				else {
					result.add(l2.head.getMessage());
					l2.head = l2.head.getNext();
				}
			}
		}

		return result;
	}

	/** Gibt eine neue Liste zurück, die alle Messages dieser Liste, deren Time-Stamp zwischen 'start' (inklusive)
	 *  und 'end' (exklusive) liegt, in der gleichen Reihenfolge enthält.
	 * @param start Ein beliebiges LocalDateTime-Objekt
	 * @param end Ein beliebiges LocalDateTime-Objekt
	 * @return Die gefilterte Liste
	 */
	public List filterDays(LocalDateTime start, LocalDateTime end) {
		if (start == null || end == null || end.isBefore(start) || end.isEqual(start)) return new List();

		List result = new List();

		ListElement current = head;
		while (current != null) {
			if ((current.getMessage().getTimestamp().isEqual(start) || current.getMessage().getTimestamp().isAfter(start)) && current.getMessage().getTimestamp().isBefore(end)) {
				result.add(current.getMessage());
			}

			current = current.getNext();
		}

		return result;
	}

	/** Gibt eine neue Liste zurück, die alle Messages dieser Liste, deren Nutzer gleich 'user' ist, enthält.
	 * @param user Eine beliebige User-Referenz
	 * @return Die gefilterte Liste
	 */
	public List filterUser(User user) {
		List result = new List();

		ListElement current = head;
		while (current != null) {
			if (current.getMessage().getAuthor() == user) {
				result.add(current.getMessage());
			}

			current = current.getNext();
		}

		return result;
	}

	/** Gibt eine String-Repräsentation dieser Liste zurück. Dabei werden die String-Repräsentationen der einzelnen
	 *  Messages mit Zeilenumbrüchen aneinandergehängt.
	 * @return Die String-Repräsentation dieser Liste.
	 */
	public String toString() {
		ListElement current = head;
		String str = "";
		while (current != null) {
			str += current.getMessage().toString();
			str += "\n";

			current = current.getNext();
		}

		return str;
	}
}
