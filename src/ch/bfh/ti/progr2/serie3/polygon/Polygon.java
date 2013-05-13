package ch.bfh.ti.progr2.serie3.polygon;

/**
 * A polygon with getArea functionality.
 */
public class Polygon extends java.awt.Polygon {
	public Polygon() {
		super();
	}
	public Polygon(int[] xpoints, int[] ypoints, int npoints) {
		super(xpoints, ypoints, npoints);
	}

	/**
	 * Returns the area of this polygon.
	 * @return area of polygon
	 */
	public double getArea() {
		return getArea(0);
	}

	/**
	 * Returns the area of a subpolygon. Increases `offset` in order to "remove" one point after another.
	 * @param offset index where to start
	 * @return
	 */
	private double getArea(int offset) {
		// Should we stop calculating?
		if(npoints - offset <= 2)
			return 0;

		// Declare variables
		int x1 = xpoints[offset],
			y1 = ypoints[offset],
			x2 = xpoints[offset+1],
			y2 = ypoints[offset+1],
			x3 = xpoints[npoints-1],
			y3 = ypoints[npoints-1];

		// Calculate
		return Math.abs(x1 * y2 + x2 * y3 + x3 * y1 - y1 * x2 - y2 * x3 - y3 * x1 ) / 2.0
				+ getArea(offset+1);
	}

	public static void main(String[] args) {
		Polygon myPoly = new Polygon(new int[]{0, 2, 4, 4, 2, 0}, new int[]{0, 0, 0, 2, 2, 2}, 6);
		System.out.println(myPoly.getArea());
	}
}
