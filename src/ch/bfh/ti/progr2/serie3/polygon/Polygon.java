package ch.bfh.ti.progr2.serie3.polygon;

/**
 * A polygon with included getArea functionality.
 */
public class Polygon extends java.awt.Polygon {
	public Polygon() {
		super();
	}
	public Polygon(int[] xpoints, int[] ypoints, int npoints) {
		super(xpoints, ypoints, npoints);
	}

	/**
	 * Returns the area of this polygon. Beware that this does *not* cover irregular polygons, which would require an
	 * entirely different approach.
	 * @return area of polygon
	 */
	public double getArea() {
		return getArea(this.npoints);
	}

	/**
	 * Returns the area of a subpolygon. Decrements `npoints` in order to shrink the task step by step.
	 * This deliberately overwrites the instance variable, since it represents our new length in the current step.
	 * @param npoints Number of vertices to consider
	 * @return
	 */
	private double getArea(int npoints) {
		// Should we stop calculating?
		if(npoints <= 2)
			return 0;

		// Declare variables
		int x1 = xpoints[npoints-1],
		    y1 = ypoints[npoints-1],
		    x2 = xpoints[npoints-2],
		    y2 = ypoints[npoints-2],
		    x3 = xpoints[0],
		    y3 = ypoints[0];

		// Calculate
		return Math.abs(x1 * y2 + x2 * y3 + x3 * y1 - y1 * x2 - y2 * x3 - y3 * x1 ) / 2.0
		       + getArea(npoints-1);
	}

	public static void main(String[] args) {
		Polygon myPoly = new Polygon(new int[]{0, 2, 4, 4, 2, 0}, new int[]{0, 0, 0, 2, 2, 2}, 6);
		System.out.println(myPoly.getArea());
	}
}
