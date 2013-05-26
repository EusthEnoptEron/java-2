package ch.bfh.ti.progr2.serie3.prime;

import java.util.ArrayList;

/**
 * A calculator with the purpose of calculating prime numbers.
 */
class PrimeCalculator extends Thread {
	// List of found numbers
	private ArrayList<Integer> list;

	// Up to which number to search
	private int max = 0;

	private boolean hasMax = false;

	/**
	 * Creates a new calculator
	 * @param name name of the calc (and the thread -> goes to Thread constructor)
	 */
	PrimeCalculator(String name) {
		super(name);
		this.list = new ArrayList<>();
	}

	/**
	 * Creates a new calculator
	 * @param name
	 * @param max
	 */
	PrimeCalculator(String name, int max) {
		this(name);
		this.max = max;
		hasMax = true;

	}


	/**
	 * Returns the results of this calculator.
	 * @return
	 */
	public Integer[] getResults() {
		return list.toArray(new Integer[list.size()]);
	}

	/**
	 * Returns the max number found.
	 * @return
	 */
	public int getMax() {
		if(list.size() == 0)
			return -1;
		else
			return list.get(list.size() - 1);
	}

	@Override
	public void run() {
		int i = 0;

		try {
			// Go on as long as the thread isn't interrupted and the max hasn't been reached.
			while(!isInterrupted() && (!hasMax || i <= max)) {
				if(isPrime(i)) {
					System.out.printf("%s: found %d\n", getName(), i);
					list.add(i);
				}
				i++;

				Thread.sleep((long)(Math.random() * 1000));
			}
		} catch (InterruptedException e) {
			System.out.printf("%s got interrupted\n", getName());
		}
	}

	private boolean isPrime(int no) {
		if(no < 2) return false;
		for(int i = 2; i < no; i++) {
			if( no % i == 0) return false;
		}
		return true;
	}


}