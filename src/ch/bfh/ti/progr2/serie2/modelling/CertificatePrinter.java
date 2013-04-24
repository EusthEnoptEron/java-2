package ch.bfh.ti.progr2.serie2.modelling;

/**
 * A printer that's used to make a textual representation of a student's results.
 */
public class CertificatePrinter {
	private static final int WIDTH = 80;
	private static final int ABBR_WIDTH = 20 -4;
	private static final int DESC_WIDTH = 40 -4;
	private static final int GRADE_WIDTH = 10 -4;
	private static final int ECTS_WIDTH = 10 -4;


	/**
	 * Prints a textual representation of the student's results
	 * @param certificate certificate to print
	 * @return a certificate
	 */
	public void print(Certificate certificate) {
		System.out.println(toString(certificate));
	}

	private String toString(Certificate certificate) {
		StringBuilder builder = new StringBuilder();
		Student student = certificate.getOwner();
		Term term = certificate.getTerm();
		Result[] results = certificate.getResults();


		// Build header
		builder.append(repeatString("#", WIDTH) + "\n");
		builder.append("# " + padRight( "Certificate for " + student.getName() + " (" + term.getSeason().name() + " "+ term.getYear() +")", WIDTH - 4) + " #\n");
		builder.append(repeatString("#", WIDTH) + "\n");

		builder.append(
				String.format("| %-" +ABBR_WIDTH + "s |  %-" +DESC_WIDTH + "s |  %-" +GRADE_WIDTH + "s |  %-" +ECTS_WIDTH + "s |\n",

						"Module",
						"Description",
						"Grade",
						"ECTS")
		);
		builder.append( repeatString("=", WIDTH) + "\n" );

		for(Result result: results) {
			builder.append(
					String.format("| %-" +ABBR_WIDTH + "s |  %-" +DESC_WIDTH + "s |  %-" +GRADE_WIDTH + "s |  %-" +ECTS_WIDTH + "s |\n",
							result.getModule().getAbbreviation(),
							result.getModule().getDescription(),
							result.getGrade().name(),
							result.hasPassed() ? result.getModule().getEcts() : 0)
			);
		}

		builder.append( repeatString("=", WIDTH) + "\n");
		builder.append( String.format("%"+ (WIDTH  - ECTS_WIDTH) +"s", student.getEcts(term)) );

		return builder.toString();
	}

	private String repeatString(String str, int n) {
		return new String(new char[n]).replace("\0", str);
	}

	private String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}
}
