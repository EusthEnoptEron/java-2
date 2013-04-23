package ch.bfh.ti.progr2.serie2.modelling;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 22.04.13
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
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
