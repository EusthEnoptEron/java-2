package ch.bfh.ti.progr2.serie3.prime2;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * A calculator with the purpose of calculating prime numbers.
 */
class PrimeCalculator implements Callable<ArrayList<Integer>> {
	private int start,
	            count;

	/**
	 * Calculates a new calculator
	 * @param start from where
	 * @param count how many
	 */
	PrimeCalculator(int start, int count) {
		this.start = start;
		this.count = count;
	}


	private boolean isPrime(int no) {
		if(no < 2) return false;
		for(int i = 2; i < no; i++) {
			if( no % i == 0) return false;
		}
		// Commenting this out would make the difference even clearer
//		try {
//			Thread.sleep(1);
//		} catch (InterruptedException e) {
//			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//		}
		return true;
	}


	@Override
	public ArrayList<Integer> call() throws Exception {
		return calculate();
	}

	/**
	 * Calculates the prime numbers this calculator has been set to calculate.
	 * @return a list of results
	 */
	public ArrayList<Integer> calculate() {
		ArrayList<Integer> results = new ArrayList<>();

		for(int i = 0; i < count; i++) {
			//Calc actual number
			int j = i + start;

			if(isPrime(j)) {
				results.add(j);
			}
		}

		return results;
	}
}