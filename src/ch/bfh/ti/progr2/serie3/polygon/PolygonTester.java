package ch.bfh.ti.progr2.serie3.polygon;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 23.05.13
 * Time: 21:51
 * To change this template use File | Settings | File Templates.
 */
public class PolygonTester {
	// Test entry point
	public static void main(String[] args) {

		// Make a 4x4 rect
		Polygon rect = new Polygon(
			new int[] {0, 4, 4, 0},
			new int[] {0, 0, 3, 3},
		4);

		Polygon parallelogram = new Polygon(
			new int[] {1, 5, 4, 0},
			new int[] {0, 0, 3, 3},
		4);

		Polygon trapezoid = new Polygon(
			new int[] {3, 5, 7, 0},
			new int[] {4, 4, 0, 0},
		4);

		printTest(12.0, rect.getArea());
		printTest(12.0, parallelogram.getArea());

		// h = 4
		// b1 = 5 - 3 = 2
		// b2 = 7 - 0 = 7
		// A = h/2*(b1+b2)
		printTest(4/2.0 * (2+7), trapezoid.getArea());
	}
	private static void printTest(double expected, double area) {
		System.out.printf("Expected: %.2f, got: %.2f\n", expected, area);
	}
}
