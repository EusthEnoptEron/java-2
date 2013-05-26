package ch.bfh.ti.progr2.serie3.prime;

class PrimeTester {
	public static final int NO_OF_THREADS = 5;
	private static final int PRIME_MAX = 60;


	public static void main(String[] args) {
		// CREATE CALCULATORS
		PrimeCalculator[] calcs = new PrimeCalculator[NO_OF_THREADS];
		PrimeCalculator mainCalc = new PrimeCalculator("Main", PRIME_MAX);

		for(int i = 0; i < NO_OF_THREADS; i++) {
			// Create & start thread
			calcs[i] = new PrimeCalculator("Calc_" + i);
			calcs[i].start();
		}

		// START CALCULATION
		//---------------------------------
		System.out.println("STARTING MAIN");
		mainCalc.run();
		System.out.println("MAIN DONE");

		// Main done -> interrupt others.
		for(PrimeCalculator calc: calcs) {
			calc.interrupt();
		}

		// Wait for them to finish
		for(PrimeCalculator calc: calcs) {
			try {
				calc.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// List results
		System.out.println("--------------------");
		System.out.println("RESULT LISTING");
		System.out.println("--------------------");
		System.out.println("MAIN: " + mainCalc.getMax());
		for(PrimeCalculator calc: calcs) {
			System.out.println(calc.getName() + ": " + calc.getMax());
		}
	}
}