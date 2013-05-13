package ch.bfh.ti.progr2.serie3.prime;

class PrimeTester {
	public static final int NO_OF_THREADS = 5;
	private static final int PRIME_MAX = 60;


	public static void main(String[] args) {
		PrimeCalculator[] calcs = new PrimeCalculator[NO_OF_THREADS];

		// Create
		for(int i = 0; i < NO_OF_THREADS; i++) {
			calcs[i] = new PrimeCalculator("Calc_" + i);
			calcs[i].start();
		}
		PrimeCalculator mainCalc = new PrimeCalculator("Main", PRIME_MAX);
		System.out.println("STARTING MAIN");
		mainCalc.run();
		System.out.println("MAIN DONE");
		for(PrimeCalculator calc: calcs) {
			calc.interrupt();
		}

		System.out.println("--------------------");
		System.out.println("RESULT LISTING");
		System.out.println("--------------------");
		System.out.println("MAIN: " + mainCalc.getMax());
		for(PrimeCalculator calc: calcs) {
			System.out.println(calc.getName() + ": " + calc.getMax());
		}
	}
}