package ch.bfh.ti.progr2.serie2.modelling;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Representation of a school term (Winter / Summer)
 */
public class Term {
	private static ArrayList<Term> instances = new ArrayList<>();

	enum Season {SUMMER, WINTER}

	private Season season;
	private int year;

	private Term(Season season, int year) {
		this.season = season;
		this.year = year;
	}

	/**
	 * Returns the requested term.
	 * @param season SUMMER/WINTER
	 * @param year year of the term
	 * @return
	 */
	public static Term getInstance(Season season, int year) {
		// Check for existing instances
		for(Term term: instances) {
			if(term.season == season && term.year == year) {
				return term;
			}
		}

		// None found -> create a new one
		Term term = new Term(season, year);
		instances.add(term);

		return term;
	}

	/**
	 * Returns the season of this term.
	 * @return
	 */
	public Season getSeason() {
		return season;
	}

	/**
	 * Returns the year of this term.
	 * @return
	 */
	public int getYear() {
		return year;
	}
}
