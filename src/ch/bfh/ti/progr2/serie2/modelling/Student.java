package ch.bfh.ti.progr2.serie2.modelling;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 22.04.13
 * Time: 09:39
 * To change this template use File | Settings | File Templates.
 */
public class Student {
	private String firstName;
	private String lastName;
	private int id;

	ArrayList<ModuleGradeSet> results = new ArrayList<>();

	/**
	 * Creates a new student.
	 * @param id unique ID of the student.
	 * @param firstName First name of the student
	 * @param lastName Last name of the student
	 */
	public Student(int id, String firstName, String lastName) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	/**
	 * Adds a result the student has made.
	 * @param module module he absolved
	 * @param grade grade he got
	 */
	public void addResult(Module module, Grade grade) {
		addResult(new ModuleGradeSet(module, grade));
	}

	/**
	 * Adds a result the student has made.
	 * @param result the result he made
	 */
	public void addResult(ModuleGradeSet result) {
		results.add(result);
	}

	/**
	 * Returns the number of ECTS this student has acquired.
	 * @return
	 */
	public int getEcts() {
		int ects = 0;
		// Loop through his results and add ECTS if he passed
		for(ModuleGradeSet set: results) {
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
		for(ModuleGradeSet set: results) {
			modules[i++] = set.getModule();
		}
		return modules;
	}

	public ModuleGradeSet[] getResults() {
		return results.toArray(new ModuleGradeSet[0]);
	}

	public int getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}
}
