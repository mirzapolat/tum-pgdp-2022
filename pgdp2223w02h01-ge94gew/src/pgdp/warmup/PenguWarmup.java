package pgdp.warmup;

public class PenguWarmup {

	protected PenguWarmup() {
		throw new UnsupportedOperationException();
	}

	/*
	Gibt Informationen über einen Pinguin zurück.
	 */
	public static void penguInfoOut(int penguin) {
		if (penguin < 0) {
			System.out.println("Penguin " + penguin + " is not a known penguin!");
			return;
		}

		System.out.println("Penguin: " + penguin);

		if (penguin % 2 == 0) {
			System.out.println("This penguin is a male.");
		}
		else {
			System.out.println("This penguin is a female.");
		}
	}

	/*
	Gibt die Evolution eines Pinguins zurück.
	 */
	public static int penguEvolution(int penguin, int years) {

		int pinguYears = 0;
		for (int i = 0; i < years; i++) {


			cause:
			if (pinguYears > 0 && pinguYears < 6) {
				pinguYears++;
			}
			else if (pinguYears == 6) {
				penguin = (penguin * 3) + 1;
				pinguYears = 0;
			}
			else if (penguin % 2 == 0) {
				for (int a = 2; a <= penguin; a = a * 2) {
					if (a == penguin) {
						penguin = 1;
						break cause;
					}
				}
				penguin = penguin / 2;
			}
			else {

				if (penguin % 7 == 0) {
					pinguYears = 1;
				}
				else {
					penguin = (penguin * 3) + 1;
				}
			}
		}

		return penguin;
	}

	public static int penguSum(int penguin) {
		char [] figures = String.valueOf(penguin).toCharArray();
		int sum = 0;

		for (char x:figures) {
			sum += x - '0';
		}

		return sum;
	}

	public static long penguPermutation(long n, long k) {
		if (n <= k) {
			return 1;
		}
		else {
			return n * penguPermutation(n - 1, k);
		}
	}

	public static long penguPowers(int x, int i) {
		long dieZahl = x;

		for (long a = 0; a < i-1; a++) {
			dieZahl = multiply(dieZahl, x);
		}

		return dieZahl;
	}

	public static long multiply(long a, long b) {
		long x = a;
		for (long i = 0; i < b - 1; i++) {
			x += a;
		}
		return x;
	}
	/*	Die Inhalte der main()-Methode beeinflussen nicht die Bewertung dieser Aufgabe
	 *	(es sei denn natürlich, sie verursachen Compiler-Fehler).
	 */
	public static void main(String[] args) {

		System.out.println(penguPowers(1337, 2));
		System.out.println(penguPowers(3, 4));
	}

}
