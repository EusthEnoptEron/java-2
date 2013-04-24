package ch.bfh.ti.progr2.serie2.modelling;

/**
 * Enum for grades (A, B, C, D, E, FX, F)
 */
public enum Grade {
	A(true), B(true), C(true), D(true), E(true), FX(false), F(false);

	private boolean eligibleForEcts;
	private Grade(boolean getsEcts) {
		this.eligibleForEcts = getsEcts;
	}

	/**
	 * Returns whether or not this grade counts as passed.
	 * @return
	 */
	public boolean isEligibleForEcts() {
		return eligibleForEcts;
	}
}
