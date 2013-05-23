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
			new int[]{0, 4, 4, 0},
			new int[]{0, 0, 4, 4},
		4);

		printTest(16.0, rect.getArea());

	}
	private static void printTest(double expected, double area) {
		System.out.printf("Expected: %.2f, got: %.2f\n", expected, area);
	}
}
