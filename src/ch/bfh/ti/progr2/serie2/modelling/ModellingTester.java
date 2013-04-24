package ch.bfh.ti.progr2.serie2.modelling;

/**
 * Short tester class that prints out a certificate.
 */
public class ModellingTester {
	private static Module[] modules = new Module[]{
		new Module("PROGR1", "Introduction to Java Programming", 6),
		new Module("PROGR2", "Java Concepts andMethods", 6),
		new Module("LINALG", "Linear algebra", 4),
		new Module("ANALY", "Analysis", 4),
		new Module("PHYS1", "Physic : introduction", 4),
		new Module("PHYS2", "Physic : advanced topics", 4),
		new Module("OPSYS", "Operating systems", 4),
		new Module("CRYPTO", "Applied Cryptography", 2),
		new Module("NETD", "Networks design", 4),
	};

	public static void main(String[] args) {

		CertificatePrinter printer = new CertificatePrinter();
		Student student = new Student(0, "Hans Muster");

		Term summer = Term.getInstance(Term.Season.SUMMER, 2012);
		Term winter = Term.getInstance(Term.Season.WINTER, 2012);


		student.addResult(summer, modules[0], Grade.A);
		student.addResult(winter, modules[1], Grade.B);
		student.addResult(summer, modules[2], Grade.C);
		student.addResult(winter, modules[3], Grade.D);
		student.addResult(summer, modules[4], Grade.E);
		student.addResult(winter, modules[5], Grade.FX);
		student.addResult(summer, modules[6], Grade.F);
		student.addResult(winter, modules[7], Grade.A);
		student.addResult(summer, modules[8], Grade.B);

		printer.print(student.getCertificate(summer));
		printer.print(student.getCertificate(winter));

	}
}
