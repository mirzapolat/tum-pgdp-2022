package pgdp.datatypes.geometry;

public class Rational {
    public int numerator;
    public int denominator;

    public Rational(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        if (denominator == 0) System.out.println("A Rational-Object with 'denominator' zero was created. Do NOT use!");
    }

    public void multiply(Rational other) {
        this.numerator = this.numerator * other.numerator;
        this.denominator = this.denominator * other.denominator;
    }

    public double toDouble() {
        return (double) this.numerator / (double) this.denominator;
    }

    public String toString() {
        return numerator + "/" + denominator;
    }
}
