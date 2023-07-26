package pgdp.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static pgdp.PinguLib.*;
import static pgdp.array.Array.print;
import static pgdp.array.Array.minAndMax;
import static pgdp.array.Array.invert;
import static pgdp.array.Array.intersect;
import static pgdp.array.Array.linearize;
import static pgdp.array.Array.bubbleSort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ArrayTest {

	/*
	Test für die Funktion:
	print();
	 */
	@Test
	void testPrintNormal() {
		setup();
		print(new int[] {1, 2, 3, 4});
		assertEquals("{1, 2, 3, 4}", getConsoleOutput());
		reset();
	}
	@Test
	void testPrintEmpty() {
		setup();
		print(new int[] {});
		assertEquals("{}", getConsoleOutput());
		reset();
	}
	@Test
	void testPrintNegative() {
		setup();
		print(new int[] {-1, -2, -3, -4});
		assertEquals("{-1, -2, -3, -4}", getConsoleOutput());
		reset();
	}
	@Test
	void testPrintBigNumbers() {
		setup();
		print(new int[] {25346, 4000000, 64375, 9483, 374});
		assertEquals("{25346, 4000000, 64375, 9483, 374}", getConsoleOutput());
		reset();
	}

	/*
	Test für die Funktion:
	minAndMax();
	 */
	@Test
	void testMinAndMaxNormal() {
		setup();
		minAndMax(new int[] {5, 0, 28, 19, 2, 10});
		assertEquals("Minimum = 0, Maximum = 28", getConsoleOutput());
		reset();
	}
	@Test
	void testMinAndMaxNegative() {
		setup();
		minAndMax(new int[] {15, -3, -28, 15, 25});
		assertEquals("Minimum = -28, Maximum = 25", getConsoleOutput());
		reset();
	}
	@Test
	void testMinAndMaxEmpty() {
		setup();
		minAndMax(new int[] {});
		assertEquals("", getConsoleOutput());
		reset();
	}
	@Test
	void testMinAndMaxOnlyOne() {
		setup();
		minAndMax(new int[] {1});
		assertEquals("Minimum = 1, Maximum = 1", getConsoleOutput());
		reset();
	}
	@Test
	void testMinAndMaxBigNumbers() {
		setup();
		minAndMax(new int[] {1000000, 2000000, 3000000, 4000000});
		assertEquals("Minimum = 1000000, Maximum = 4000000", getConsoleOutput());
		reset();
	}

	/*
	Test für die Funktion:
	invert();
	 */
	@Test
	void testInvertNormal() {
		setup();
		int[] a = new int[] {1, 2, 3};
		invert(a);
		assertEquals("[3, 2, 1]", Arrays.toString(a));
		reset();
	}
	@Test
	void testInvertEmpty() {
		setup();
		int[] a = new int[] {};
		invert(a);
		assertEquals("[]", Arrays.toString(a));
		reset();
	}
	@Test
	void testInvertNegative() {
		setup();
		int[] a = new int[] {-2, -3, 5, 6};
		invert(a);
		assertEquals("[6, 5, -3, -2]", Arrays.toString(a));
		reset();
	}
	@Test
	void testInvertBigNumbers() {
		setup();
		int[] a = new int[] {2000000, 3000000, 4000000, 1000000};
		invert(a);
		assertEquals("[1000000, 4000000, 3000000, 2000000]", Arrays.toString(a));
		reset();
	}

	/*
	Test für Funktion:
	intersect();
	 */
	@Test
	void testIntersect() {
		// Normal Numbers
		assertEquals("[0, 1, 2, 3, 4, 5]",
				Arrays.toString(intersect(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 6)));
		// Länge länger als Array
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0]",
				Arrays.toString(intersect(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 12)));
		// Länge 0
		assertEquals("[]",
				Arrays.toString(intersect(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 0)));
		// Länge negativ
		assertEquals("[]",
				Arrays.toString(intersect(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, -2)));
		// Array leer
		assertEquals("[0, 0, 0, 0, 0]",
				Arrays.toString(intersect(new int[] {}, 5)));
		// Sehr große Zahlen
		assertEquals("[]",
				Arrays.toString(intersect(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, -1000000)));
	}

	/*
	Test für Funktion:
	linearize();
	 */
	@Test
	void testLinearized() {
		assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]",
				Arrays.toString(linearize(new int[][] {{1, 2, 3, 4}, {5, 6, 7}, {8, 9}, {10}, {11, 12}})));
		assertEquals("[]",
				Arrays.toString(linearize(new int[][] {})));
		assertEquals("[]",
				Arrays.toString(linearize(new int[][] {{}, {}})));
		assertEquals("[5, -23, 19, 4]",
				Arrays.toString(linearize(new int[][] {{5, -23}, {19, 4}})));
	}

	/*
	Test für Funktion:
	BubbleSort
	 */
	@Test
	void testBubbleSort() {
		int[] a = new int[] {5, 1, 4, 2, 3};
		bubbleSort(a);
		assertEquals("[1, 2, 3, 4, 5]", Arrays.toString(a));

		a = new int[] {};
		bubbleSort(a);
		assertEquals("[]", Arrays.toString(a));

		a = new int[] {0, 0, 2, 0, 0};
		bubbleSort(a);
		assertEquals("[0, 0, 0, 0, 2]", Arrays.toString(a));

		a = new int[] {18, -5, 3, 2, -46};
		bubbleSort(a);
		assertEquals("[-46, -5, 2, 3, 18]", Arrays.toString(a));

		a = new int[] {7, 7, -7};
		bubbleSort(a);
		assertEquals("[-7, 7, 7]", Arrays.toString(a));
	}

}
