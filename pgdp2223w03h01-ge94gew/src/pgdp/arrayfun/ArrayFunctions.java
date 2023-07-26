package pgdp.arrayfun;

import java.util.Arrays;

public class ArrayFunctions {

    protected ArrayFunctions() {
        throw new IllegalStateException("Don't create objects of type 'ArrayFunctions'!");
    }

    public static void main(String[] args) {
        //example call
        //System.out.println(sumOfSquares(new int[]{2147483647, 2147483647, 2147483647, 2147483647, -2147483648}));
        //System.out.println(Arrays.toString(zip(new int[]{1, 3}, new int[]{2, 4, 5, 6})));

        int[] a = new int[] {};
        rotate(a, 3);
        System.out.println(Arrays.toString(a));

    }

    /** Berechnet für das übergebene Array die Summe der Quadrate der Einträge.
     *  Gibt dabei einen Fehler aus und -1 zurück, wenn ein Overflow entsteht.
     *
     * @param array Ein beliebiges Integer-Array.
     * @return Die Summe der Quadrate, wenn diese in einen 'long' passt, -1 sonst.
     */
    public static long sumOfSquares(int[] array) {
        if (array.length == 0) return 0;

        long sum = 0;
        for (int a:array) {
            sum += (long) a * a;
            if (sum < 0) {
                System.out.println("Overflow!");
                return -1;
            }
        }

        return sum;
    }


    /** Methode, die zwei Arrays zu einem verbindet, indem sie abwechselnd Einträge des ersten und des zweiten Input-
     *  Arrays verwendet.
     *
     * @param a Ein beliebiges Integer-Array.
     * @param b Ein beliebiges Integer-Array.
     * @return 'a' und 'b' zusammengezipped.
     */
    public static int[] zip(int[] a, int[] b) {
        boolean firstStack = true;
        int[] result = new int[a.length + b.length];
        int ai = 0, bi = 0;
        for (int i = 0; i < a.length + b.length; i++) {
            if (firstStack) {                                   // wechsel zwischen beiden stacks
                if (ai < a.length) {
                    result[i] = a[ai];
                    ai++;
                }
                else {
                    result[i] = b[bi];
                    bi++;
                }
                firstStack = false;
            }
            else {
                if (bi < b.length) {
                    result[i] = b[bi];
                    bi++;
                }
                else {
                    result[i] = a[ai];
                    ai++;
                }
                firstStack = true;
            }
        }

        return result;
    }

    /** Methode, die eine beliebige Zahl an Arrays (dargestellt als Array von Arrays) zu einem einzigen Array verbindet,
     *  indem sie abwechselnd von jedem Array einen Eintrag nimmt, bis alle aufgebraucht sind.
     *
     * @param arrays Array von Integer-Arrays
     * @return Die Arrays in 'arrays' zusammengezipped
     */
    public static int[] zipMany(int[][] arrays) {
        int index = 0;              // index.
        int leng = 0;               // länge alle zusammen.
        int biggest = 0;            // länge des längsten arrays.

        for (int[] a:arrays) {      // länge alle zusammen berechnen.
            leng += a.length;
            if (a.length > biggest) biggest = a.length;
        }

        int[] result = new int[leng];           // Ergebnis Array erstellen

        for (int i = 0; i < biggest; i++) {     // Jeden Index der Ur-Arrays durchgehen
            for (int [] a:arrays) {             // Jedes Array in der Sammlung durchgehen
                if (i < a.length) {             // Wenn noch Elemente im Array, Werte übertragen
                    result[index] = a[i];
                    index++;                    // Index im Ergebnisarray hochzählen
                }
            }

        }

        return result;      // Rückgabe.
    }

    /** Behält aus dem übergebenen Array nur die Einträge, die innerhalb der übergebenen Grenzen liegen.
     *  Gibt das Ergebnis als neues Array zurück.
     *
     * @param array Ein beliebiges Integer-Array
     * @param min Ein beliebiger Integer
     * @param max Ein beliebiger Integer
     * @return Das gefilterte Array
     */
    public static int[] filter(int[] array,int min,int max) {

        int count = 0;
        for (int a:array) {                         // Zählt die Menge der in Frage kommenden Zahlen.
            if (a >= min && a <= max) count++;
        }

        int[] result = new int[count];              // Erstellt das Ergebnis Array.
        int index = 0;

        for (int a:array) {                         // Schreibt alle gültigen Werte ins Result Array
            if (a >= min && a <= max) {
                result[index] = a;
                index++;
            }
        }

        return result;
    }

    /** Rotiert das übergebene Array um die übergebene Anzahl an Schritten nach rechts.
     *  Das Array wird In-Place rotiert. Es gibt keine Rückgabe.
     *
     * @param array Ein beliebiges Integer-Array
     * @param amount Ein beliebiger Integer
     */

    /*
    Funktion, die alle werte eines Arrays um eine bestimmte anzahl nach links oder rechts verschiebt
    und dabei auch die rotation von herausfallenden elementen beachtet.
     */
    public static void rotate(int[] array, int amount) {
        int repeats;

        if (array.length == 0) return;
        if (amount >= 0) repeats = amount % array.length;                            // Wenn rotation positiv, normal rotieren.
        else repeats = array.length + amount % array.length;        // Wenn rotation negativ, modulo vom ende des arrays abziehen.

        for (int i = 0; i < repeats; i++) {
            int lastValue = array[array.length - 1];                // letzten Wert abspeichern
            for (int a = array.length - 2; a >= 0; a--) {           // alle elemente von hinten um eins verschieben
                array[a + 1] = array[a];
            }
            array[0]=lastValue;                                     // letzten wert von vorhin an den anfang setzen.
        }
    }

    /** Zählt die Anzahl an Vorkommen jeder Zahl im übergebenen Array, die in diesem mindestens einmal vorkommt.
     *  Die Rückgabe erfolgt über ein 2D-Array, bei dem jedes innere Array aus zwei Einträgen besteht: Einer Zahl,
     *  die im übergebenen Array vorkommt sowie der Anzahl an Vorkommen dieser.
     *  Für jede im übergebenen Array vorkommenden Zahl gibt es ein solches inneres Array.
     *  Diese tauchen im Rückgabewert in der gleichen Reihenfolge auf, in der die jeweils ersten Vorkommen der Zahlen
     *  im übergebenen Array auftauchen.
     *
     * @param array Ein beliebiges Integer-Array
     * @return Das Array mit den Vielfachheiten der einzelnen Zahlen, wiederum als Integer-Arrays mit zwei Einträgen dargestellt.
     */
    public static int[][] quantities(int[] array) {
        int [][] table = new int[0][2];

        for (int elem:array) {
            boolean foundRow = false;
            for (int[] row:table) {                     // Zeile mit der aktuellen Zahl wird gesucht
                if (row[0] == elem) {
                    row[1] = row[1] + 1;
                    foundRow = true;
                    break;                              // Wenn gefunden, abbruch und nächste Zahl
                }
            }

            if (!foundRow) {                                                            // Wenn nicht gefunden, dann wird die Zahl hinzugefügt
                int[][] copytable = new int[table.length + 1][2];
                System.arraycopy(table, 0, copytable, 0, table.length);         // Array wird kopiert.

                copytable[copytable.length - 1] = new int[] {elem, 1};                          // Zahl wird hinzugefügt.

                table = new int[copytable.length][2];
                System.arraycopy(copytable, 0, table, 0, copytable.length);     // Array wird zurückkopiert.
            }
        }

        return table;
    }
}
