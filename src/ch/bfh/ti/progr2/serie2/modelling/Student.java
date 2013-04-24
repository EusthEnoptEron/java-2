package ch.bfh.ti.progr2.serie2.modelling;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * A student who can have a list of results.
 */
public class Student {
	private String name;
	private int id;

	Hashtable<Term, ArrayList<Result> > results;

	/**
	 * Creates a new student.
	 * @param id unique ID of the student (no check is made - this is a responsibility of the DB)
	 * @param name name of the student
	 */
	public Student(int id, String name) {
		results = new Hashtable<>();
		this.id = id;
		this.name = name;
	}

	/**
	 * Adds a result the student has made.
	 * @param module module he absolved
	 * @param grade grade he got
	 */
	public void addResult(Term term, Module module, Grade grade) {
		addResult(term, new Result(module, grade));
	}

	/**
	 * Adds a result the student has made.
	 * @param result the result he made
	 */
	public void addResult(Term term, Result result) {
		if(!results.containsKey(term)) {
			results.put(term, new ArrayList<Result>());
		}
		results.get(term).add(result);
	}

	/**
	 * Returns the number of ECTS this student has acquired in total.
	 * @return
	 */
	public int getEcts() {
		int ects = 0;
		// Loop through all terms and collect the ECTS
		for(Term term: results.keySet().toArray(new Term[0])) {
			ects += getEcts(term);
		}
		return ects;
	}

	/**
	 * Returns the number of ECTS this student has acquired during a certain term.
	 * @param term
	 * @return
	 */
	public int getEcts(Term term) {
		ArrayList<Result> results = this.results.get(term);
		int ects = 0;

		if(results != null) {
			for(Result set: results) {
				if(set.hasPassed())
					ects += set.getModule().getEcts();
			}
		}

		return ects;
	}

	/**
	 * Returns the list of modules this student has attended during a term.
	 * @return
	 */
	public Module[] getModules(Term term) {
		Result[] results = getResults(term);
		Module[] modules = new Module[results.length];

		int i = 0;
		for(Result set: results) {
			modules[i++] = set.getModule();
		}
		return modules;
	}

	/**
	 * Returns the achievements of the student during a term.
	 * @param term
	 * @return
	 */
	public Result[] getResults(Term term) {
		ArrayList<Result> results = this.results.get(term);
		if(term == null) {
			return new Result[0];
		} else {
			// Convert to array
			return results.toArray(new Result[results.size()]);
		}
	}

	/**
	 * Returns the certificate the student gets for a term
	 * @param term
	 * @return the certificate the student gets for a term
	 */
	public Certificate getCertificate(Term term) {
		return new Certificate(this, term, getResults(term));
	}

	/**
	 * Returns the student id
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the student
	 * @return
	 */
	public String getName() {
		return name;
	}

}
