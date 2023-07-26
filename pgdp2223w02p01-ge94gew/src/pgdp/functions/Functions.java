package pgdp.functions;

public class Functions {

    public static int square(int n) {
        int square = n*n;
        return square;
    }

    public static int sumOfSquares(int a, int b) {
        int aSquared = square(a);
        int bSquared = square(b);
        int sum = aSquared + bSquared;

        return sum;
    }

    public static int cube(int n) {                         // Gibt das hoch drei einer Funktion zurück.
        return n * n * n;
    }

    public static int average(int a, int b, int c) {        // Gibt den Durchschnitt der drei Zahlen zurück, solange das ERgebnis eine ganze Zahl ist.
        return (a + b + c) / 3;
    }

    public static boolean isPythagoreanTriple(int a, int b, int c) {    // Prüft, ob die angegebenen Zahlen a, b und c ein Pythagoreisches Tripel bilden.
        // TODO: Benutze in dieser Methode keine arithmetischen Operatoren (i.e. +, -, *, /, % etc.)!

        return sumOfSquares(a, b) == square(c);
    }

}
