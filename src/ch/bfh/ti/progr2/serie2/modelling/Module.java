package ch.bfh.ti.progr2.serie2.modelling;

/**
 * Representation of a module.
 */
public class Module {
	private String abbreviation;
	private String description;
	private int ects;

	/**
	 * Creates a new module definition.
	 * @param abbreviation short name
	 * @param description long name
	 * @param ects number of ECTS awarded for this module
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
