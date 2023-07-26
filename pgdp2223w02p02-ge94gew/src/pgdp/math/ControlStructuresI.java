package pgdp.math;

import java.util.Scanner;

public class ControlStructuresI {
	public static void main(String[] args) {
	    // Für Experimente und fürs Testen der Funktionen
		Scanner in = new Scanner(System.in);
		if (isPalindrome(in.nextInt())) System.out.println("true");
		else System.out.println("false");

	}

	/*
	Prüft, ob die angegebene Zahl größer als null ist.
	Zur Hilfe für andere Funktionen verwendet, weil Funktion redundant.
	 */
	public static boolean checkIfValid(int i) {
		if (i <= 0) {
			System.out.println("Eingabe muss größer als 0 sein!");
			return true;
		}
		return false;
	}

	/*
	Geht die Collatz Folge durch und startet bei n. Endet, wenn die Folge 4-2-1 erreicht wurde.
	 */
	public static void printCollatz(int n) {
		if (checkIfValid(n)) return;

		int count = 1;
		int current = n;

		System.out.print(current);

		while (current != 1) {
			if (current % 2 == 0)
				current = current / 2;
			else
				current = (current * 3) + 1;

			System.out.print(" " + current);
			count++;
		}

		System.out.println("\nLänge: " + count);
	}

	/*
	Gibt alle Zweierpotenzen aus, die kleiner oder gleich n sind.
	 */
	public static void printPowersOfTwoUpTo(int n) {
		if (checkIfValid(n)) return;

		int current = 1;

		System.out.print(current);
		current = current * 2;

		while (current <= n) {
			System.out.print(" " + current);
			current = current * 2;
		}

		System.out.print("\n");
	}


	/*
	Gibt ein auf dem Kopf stehendes Dreieck aus Sternchen mit der anfänglichen Breite
	von sideLength aus.
	 */
	public static void printTriangle(int sideLength) {
		if (checkIfValid(sideLength)) return;

		while (sideLength > 0) {
			int down = sideLength;
			while (down > 0) {
				System.out.print("*");
				down--;
			}
			System.out.print("\n");
			sideLength--;
		}
	}

	/*
	Gibt die Anzahl der Ziffern einer eingegebenen Zahl zurück.
	 */
	public static int calculateNumberOfDigits(int n) {
		if (n == 0) return 0;
		return Integer.toString(n).length();
	}

	/*
	Kehrt die Ziffernreihenfolge einer Zahl n um.
	 */
	public static int reverseNumber(int n) {
		String ausgeschrieben = Integer.toString(n);
		char[] zeichen = ausgeschrieben.toCharArray();

		int i = 0;
		char[] neueZeichen = new char[zeichen.length];
		while (i < zeichen.length) {
			neueZeichen[i] = zeichen[zeichen.length - 1 - i];
			i++;
		}

		ausgeschrieben = String.valueOf(neueZeichen);

		return Integer.parseInt(ausgeschrieben);
	}

	/*
	Gibt true zurück, wenn die eingegebene Integer Zahl ein Palindrom
	ist und false, wenn nicht.
	 */
	public static boolean isPalindrome(int n) {
		String ausgeschrieben = Integer.toString(n);
		char[] zeichen = ausgeschrieben.toCharArray();

		int i = 0;
		while (i < zeichen.length / 2) {
			if (zeichen[i] != zeichen[zeichen.length - 1 - i]) return false;
			i++;
		}

		return true;
	}
}