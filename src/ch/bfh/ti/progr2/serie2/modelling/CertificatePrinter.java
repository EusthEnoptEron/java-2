package ch.bfh.ti.progr2.serie2.modelling;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 13/04/23
 * Time: 17:21
 * To change this template use File | Settings | File Templates.
 */
public class CertificatePrinter {
	private static final int WIDTH = 80;
	private static final int ABBR_WIDTH = 20 -4;
	private static final int DESC_WIDTH = 40 -4;
	private static final int GRADE_WIDTH = 10 -4;
	private static final int ECTS_WIDTH = 10 -4;


	public String getCertificate(Student student) {
		StringBuilder builder = new StringBuilder();

		// Build header
		builder.append(repeatString("#", WIDTH) + "\n");
		builder.append("# " + padRight( "Certificate for " + student.getName(), WIDTH - 4) + " #\n");
		builder.append(repeatString("#", WIDTH) + "\n");

		builder.append(
			String.format("| %-" +ABBR_WIDTH + "s |  %-" +DESC_WIDTH + "s |  %-" +GRADE_WIDTH + "s |  %-" +ECTS_WIDTH + "s |\n",

					"Module",
					"Description",
					"Grade",
					"ECTS")
		);
		builder.append( repeatString("=", WIDTH) + "\n" );

		for(Result result: student.getResults()) {
			builder.append(
					String.format("| %-" +ABBR_WIDTH + "s |  %-" +DESC_WIDTH + "s |  %-" +GRADE_WIDTH + "s |  %-" +ECTS_WIDTH + "s |\n",
					result.getModule().getAbbreviation(),
					result.getModule().getDescription(),
					result.getGrade().name(),
					result.hasPassed() ? result.getModule().getEcts() : 0)
			);
		}

		builder.append( repeatString("=", WIDTH) + "\n");
		builder.append( String.format("%"+ (WIDTH  - ECTS_WIDTH) +"s", student.getEcts()) );

		return builder.toString();
	}

	private String repeatString(String str, int n) {
		return new String(new char[n]).replace("\0", str);
	}

	private String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}
}
