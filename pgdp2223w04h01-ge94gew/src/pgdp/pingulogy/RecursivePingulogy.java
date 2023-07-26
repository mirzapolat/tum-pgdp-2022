package pgdp.pingulogy;

public class RecursivePingulogy {

	private static long[] storage = new long[270];
	private static int previousP0, previousP1, previousP2;

	private static long storageHelper(int n, int p0, int p1, int p2) {

		if (p0 != previousP0 || p1 != previousP1 || p2 != previousP2) {
			storage = new long[270];
			previousP0 = p0;
			previousP1 = p1;
			previousP2 = p2;
		}

		if (storage[n + 123] == 0) {
			storage[n + 123] = pinguSequenceRec(n, p0, p1, p2);
		}

		return storage[n + 123];
	}

	// task 1
	public static long pinguSequenceRec(int n, int p0, int p1, int p2) {

		if (p0 == 0 && p1 == 0 && p2 == 0) return 0;

		if (n == 0) return p0;
		else if (n == 1) return p1;
		else if (n == 2) return p2;

		if (n < 0) {
			return 2 * pinguSequenceRec(-n, p0, p1, p2);
		}
		else {
			return storageHelper(n - 1, p0, p1, p2)
					- storageHelper(n - 2, p0, p1, p2)
					+ 2 * storageHelper(n - 3, p0, p1, p2);
		}
	}

	// task 2
	// Hint: pinguF and pinguM are not static (and must not be changed to it!)
	// more information in the main-method below
	public int pinguF(int n) {
		if (n == 0) return 1;
		return n - pinguM(pinguF(n-1));
	}

	public int pinguM(int n) {
		if (n == 0) return 0;
		return n - pinguF(pinguM(n-1));
	}

	// task 3
	private static int zwischenergebnis = 0;

	public static int pinguCode(int n, int m) {
		if (n == 0) {
			int b = zwischenergebnis;
			zwischenergebnis = 0;
			return b + m;
		}

		if ((n + zwischenergebnis) % 2 == 0) {
			zwischenergebnis += n/2;
			return pinguCode(m, n/2);
		}
		else {
			zwischenergebnis += m;
			return pinguCode(--n, m/2);
		}
	}

	// task 4
	public static String pinguDNA(int f, int m) {

		if (f == 0 && m == 0) return "";
		else if (f == 0) {
			return pinguDNA(f, m/2) + "A";
		}
		else if (m == 0) {
			return pinguDNA(f/2, m) + "T";
		}

		if (m % 2 == f % 2) {
			if (f > m) return pinguDNA(f/2, m/2) + "GT";
			else if (m > f) return pinguDNA(f/2, m/2) + "GA";
			else return pinguDNA(f/2, m/2) + "GC";
		}
		else if (m % 2 == 1) {
			return pinguDNA(f/2, m/2) + "AC";
		}
		else {
			return pinguDNA(f/2, m/2) + "TC";
		}
	}

	public static void main(String[] args) {
		// switch value to test other tasks
		int testTask = 1;

		switch (testTask) {
		case 1:
			System.out.println("Task 1 example output");
			for (int i = 0; i < 145; i++) {
				System.out.println(i + ": " + pinguSequenceRec(i, 1, 1, 2));
			}
			break;
		case 2:
			/**
			 * For better testing, pinguF and pinguM are not static. 
			 * Hence, you have to initialize a new RecursivePingulogy Object and 
			 * call the methods on that instance, as you can see below.
			 * You will learn more about object-oriented-programming in the lecture
			 * and week 05+.
			 */
			RecursivePingulogy rp = new RecursivePingulogy();
			System.out.print("Task 2 example output\npinguF: ");
			for (int i = 0; i < 10; i++) {
				System.out.print(rp.pinguF(i) + ", ");
			}
			System.out.print("\npingM: ");
			for (int i = 0; i < 10; i++) {
				System.out.print(rp.pinguM(i) + ", ");
			}
			break;
		case 3:
			System.out.println("Task 3 example output");
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					System.out.println(i + ", " + j + ": " + pinguCode(i, j));
				}
				System.out.println("----------");
			}
			break;
		case 4:
			System.out.println("Task 4 example output");
			System.out.println("pinguDNA(21, 25) = " + pinguDNA(21, 25));
			break;
		default:
			System.out.println("There are only 4 tasks!");
			break;
		}
	}
}
