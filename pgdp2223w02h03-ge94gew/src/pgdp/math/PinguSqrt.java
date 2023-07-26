package pgdp.math;

import java.util.Scanner;

public class PinguSqrt {

	/*
	Berechnet mit dem Verfahren des schriftlichen Wurzelziehens die Quadratwurzel einer
	beliebigen double-Zahl.
	 */
	public static void sqrt(double n) {
		if (n < 0) {											// Prüft, ob die Zahl negativ ist.
			System.out.println("Keine negativen Wurzeln!");
			return;
		}

		System.out.println("Wurzel aus " + String.valueOf(n));
		System.out.println();

		String aktuellesErgebnis = "0";
		int indexStartOfDeciaml = 1;

		String nAlsString = String.format("%.4f", n).replace(',', '.');
		String vorKomma = nAlsString.substring(0, nAlsString.indexOf('.'));
		String nachKomma = nAlsString.substring(nAlsString.indexOf('.') + 1);

		if (vorKomma.length() % 2 != 0) {
			vorKomma = "0" + vorKomma;
		}

		String comb = vorKomma + nachKomma;

		long pastDigits = 0;
		for (int a = 0; a < comb.length() - 1; a = a + 2) {
			String currentTwo = String.valueOf(pastDigits) + comb.substring(a, a + 2);
			long cTwo = Long.parseLong(currentTwo);

			System.out.println(cTwo);
			System.out.println("--------");

			int count = 0;
			for (long sub = Long.parseLong(String.valueOf(Long.parseLong(aktuellesErgebnis) * 2) + "1"); sub <= cTwo; sub = sub + 2) {
				cTwo = cTwo - sub;
				count++;
				System.out.println("-" + sub);
			}

			System.out.println("--------");
			System.out.println("Rest: " + cTwo);
			System.out.println("neue Ergebnis Ziffer: " + count);
			System.out.println();

			pastDigits = cTwo;
			aktuellesErgebnis = aktuellesErgebnis + String.valueOf(count);

			if (a == vorKomma.length()) {
				indexStartOfDeciaml = aktuellesErgebnis.length() - 1;
			}
		}

		aktuellesErgebnis = aktuellesErgebnis.substring(1, indexStartOfDeciaml) + "." + aktuellesErgebnis.substring(indexStartOfDeciaml, indexStartOfDeciaml + 2);
		double dVersion = Double.parseDouble(aktuellesErgebnis);
		System.out.println("Ergebnis: " + dVersion);
	}

	public static void main(String[] args) {
		// test your implementation here
		sqrt(1049.767485);
		sqrt(4);
	}

}
