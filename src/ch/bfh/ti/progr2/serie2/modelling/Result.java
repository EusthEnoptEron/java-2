package ch.bfh.ti.progr2.serie2.modelling;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 22.04.13
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
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

	public boolean hasPassed() {
		return grade.isEligibleForEcts();
	}
}
