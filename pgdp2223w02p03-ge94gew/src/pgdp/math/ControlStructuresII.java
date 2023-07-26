package pgdp.math;

import java.util.Scanner;

public class ControlStructuresII {
	public static void main(String[] args) {
		// Zum Testen
		System.out.print("Eingabe: ");
		Scanner in = new Scanner(System.in);
		printPrimesUpTo(in.nextInt());
	}

	/*
	Gibt die Summe aller positiven Zahlen, die durch 3 oder 7 teilbar
	und kleiner oder gleich einer gegebenen natürlichen Zahl n sind, an.
	 */
	public static int threeAndSeven(int n) {
		if (n < 0) {
			System.out.println("Eingabe muss größer oder gleich 0 sein!");
			return -1;
		}

		int sum = 0;

		int ak = 3;
		while (ak <= n) {
			sum = sum + ak;
			ak = ak + 3;
		}

		ak = 7;
		while (ak <= n) {
			if (ak % 3 != 0) sum = sum + ak;
			ak = ak + 7;
		}

		return sum;
	}

	/*
	Gibt die ASCII Codes von beliebig vielen Zeichen ab einem beliebigen start-Zeichen aus.
	 */
	public static void printAsciiCodesFor(char start, int count) {
		String s = String.valueOf(start);
		int fa = s.codePointAt(0);

		for (int i = fa; i < fa + count; i++) {
			System.out.println("Der ASCII-Code von '" + Character.toString((char)i) + "' ist " + i + ".");
		}
	}

	public static void printMultiplicationTable(int n) {
		// TODO
	}

	/*
	Gibt alle Primzahlen bis zu einer bestimmten Zahl n aus.
	 */
	public static void printPrimesUpTo(int n) {
		if (n <= 1) return;

		System.out.print("2");
		for (int i = 3; i <= n; i++) {
			boolean isprime = true;
			for (int g = 2; g < i; g++)
				if (i % g == 0) {
					isprime = false;
					break;
				}
			if (isprime) System.out.print(" " + i);
		}
		
		System.out.print("\n");
	}
}