package ch.bfh.ti.progr2.serie2.modelling;

import java.util.ArrayList;

/**
 * A student who can have a list of results.
 */
public class Student {
	private String name;
	private int id;

	ArrayList<Result> results = new ArrayList<>();

	/**
	 * Creates a new student.
	 * @param id unique ID of the student (no check is made - this is a responsibility of the DB)
	 * @param name name of the student
	 */
	public Student(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Adds a result the student has made.
	 * @param module module he absolved
	 * @param grade grade he got
	 */
	public void addResult(Module module, Grade grade) {
		addResult(new Result(module, grade));
	}

	/**
	 * Adds a result the student has made.
	 * @param result the result he made
	 */
	public void addResult(Result result) {
		results.add(result);
	}

	/**
	 * Returns the number of ECTS this student has acquired.
	 * @return
	 */
	public int getEcts() {
		int ects = 0;
		// Loop through his results and add ECTS if he passed
		for(Result set: results) {
			if(set.hasPassed())
				ects += set.getModule().getEcts();
		}
		return ects;
	}

	/**
	 * Returns the list of modules this student has visited.
	 * @return
	 */
	public Module[] getModules() {
		Module[] modules = new Module[results.size()];
		int i = 0;
		for(Result set: results) {
			modules[i++] = set.getModule();
		}
		return modules;
	}

	public Result[] getResults() {
		return results.toArray(new Result[0]);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
