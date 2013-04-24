package ch.bfh.ti.progr2.serie2.modelling;

import java.util.ArrayList;

/**
 * A certificate given to a student at the end of a term.
 */
public class Certificate {
	private Result[] results;
	private Student owner;
	private Term term;

	/**
	 * Create a new certificate.
	 * @param owner owner of the cert
	 * @param term term in which the certificate was given
	 * @param results results made during that term
	 */
	public Certificate(Student owner, Term term, Result[] results) {
		this.owner = owner;
		this.results = results;
		this.term = term;
	}

	/**
	 * Gets the owner of the certificate
	 * @return owner of the certificate
	 */
	public Student getOwner() {
		return owner;
	}

	/**
	 * Gets the results
	 * @return the results
	 */
	public Result[] getResults() {
		return results;
	}

	/**
	 * Gets the term
	 * @return the term
	 */
	public Term getTerm() {
		return term;
	}
}
