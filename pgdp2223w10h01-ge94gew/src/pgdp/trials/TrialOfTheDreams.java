package pgdp.trials;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TrialOfTheDreams {

	protected TrialOfTheDreams() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Picks the specified {@code lock} using iterative deepening search.
	 * 
	 * @param lock arbitrary {@code Function} that takes a key ({@code byte} array)
	 *             and checks if it opens the lock.
	 * @return {@code byte} array containing the combination to open the lock.
	 */
	public static byte[] lockPick(Function<byte[], Boolean> lock) {
		byte[] result = null;
		int depth = 1;
		do {
			if ((result = lockPick(lock, depth)) != null) {
				break;
			}
			depth++;
		} while (depth < Integer.MAX_VALUE);
		return result;
	}

	/**
	 * Picks the specified {@code lock} up to the specified depth using depth first
	 * search.
	 * 
	 * @param lock   arbitrary {@code Function} that takes a key ({@code byte}
	 *               array) and checks if it opens the lock.
	 * @param maxlen maximum length of the combinations to be checked.
	 * @return {@code byte} array containing the combination to open the lock or
	 *         {@code null} if no such combination exists.
	 */
	public static byte[] lockPick(Function<byte[], Boolean> lock, int maxlen) {
		List<Byte> result = lockPick(bytes -> {
			byte[] b = new byte[bytes.length];
			for (int i = 0; i < bytes.length; i++) b[i] = bytes[i];
			return lock.apply(b);
		}, new ArrayList<>(), maxlen);

		if (result == null) return null;
		if (result.size() == 0) return null;
		byte[] by = new byte[result.size()];
		for (int i = 0; i < result.size(); i++) by[i] = result.get(i);
		return by;
	}

	private static List<Byte> lockPick(Function<Byte[], Boolean> lock, List<Byte> key, int maxlen) {
		if (key.size() >= maxlen) return null;

		for (int i = 0; i <= 255; i++) {
			List<Byte> copy = new ArrayList<>(key); // Erstellt kopie des schlüssels
			copy.add((byte) (i & 0xFF));	// Fügt die neue Zahl zur Kopie hinzu
			Byte[] toByte = copy.toArray(copy.toArray(new Byte[0]));	// Konvertiert in Byte Array

			if (lock.apply(toByte)) return copy;	// Schaut, ob der Key schon funktioniert
			List<Byte> next = lockPick(lock, copy, maxlen);	// wenn nicht, wird die nächste instanz aufgerufen
			if (next != null) return next;	// wenn die nächste instanz null zurück gibt, wird auch null zurückgegeben
		}

		return null; // wenn kein schlüssel gefunden wurde, wird eine
	}

}
