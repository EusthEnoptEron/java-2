package ch.bfh.ti.progr2.serie2.modelling;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 22.04.13
 * Time: 09:38
 * To change this template use File | Settings | File Templates.
 */
public class Module {
	private String abbreviation;
	private String description;
	private int ects;

	/**
	 * Creates a new module definition.
	 * @param abbreviation
	 * @param description
	 * @param ects
	 */
	public Module(String abbreviation, String description, int ects) {
		this.abbreviation = abbreviation;
		this.description = description;
		this.ects = ects;
	}

	/**
	 * Gets the abbreviation of this module
	 * @return
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Gets the description of this module
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the number of ECTS credited for passing this module.
	 * @return
	 */
	public int getEcts() {
		return ects;
	}
}
