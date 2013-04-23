package ch.bfh.ti.progr2.serie2.modelling;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 13/04/23
 * Time: 17:18
 * To change this template use File | Settings | File Templates.
 */
public class ModellingTest {
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

		student.addResult(modules[0], Grade.A);
		student.addResult(modules[1], Grade.B);
		student.addResult(modules[2], Grade.C);
		student.addResult(modules[3], Grade.D);
		student.addResult(modules[4], Grade.E);
		student.addResult(modules[5], Grade.FX);
		student.addResult(modules[6], Grade.F);

		System.out.print(printer.getCertificate(student));
	}
}
