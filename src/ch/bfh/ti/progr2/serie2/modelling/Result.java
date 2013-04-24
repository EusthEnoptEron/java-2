package ch.bfh.ti.progr2.serie2.modelling;

/**
 * Represents a result (a module and a grade)
 */
public class Result {
	private Module module;
	private Grade grade;

	/**
	 * Creates a new set consisting of a module and a grade
	 * @param module
	 * @param grade
	 */
	public Result(Module module, Grade grade) {
		this.grade = grade;
		this.module = module;
	}

	/**
	 * Gets the module linked with this set.
	 * @return module linked with this set
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * Gets the grade linked with this set
	 * @return grade linked with this set
	 */
	public Grade getGrade() {
		return grade;
	}

	/**
	 * Returns whether or not the student has passed this module
	 * @return whether or not the student has passed this module
	 */
	public boolean hasPassed() {
		return grade.isEligibleForEcts();
	}
}
