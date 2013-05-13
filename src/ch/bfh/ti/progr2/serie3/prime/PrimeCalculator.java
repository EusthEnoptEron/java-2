package ch.bfh.ti.progr2.serie3.prime;

import java.util.ArrayList;

class PrimeCalculator extends Thread {
	private ArrayList<Integer> list;
	private int max = -1;
	private boolean hasMax = false;

	PrimeCalculator(String name) {
		super(name);
		this.list = new ArrayList<>();
	}

	PrimeCalculator(String name, int max) {
		this(name);
		this.max = max;
		hasMax = true;

	}

	public Integer[] getResults() {
		return list.toArray(new Integer[0]);
	}

	public int getMax() {
		return list.get(list.size() - 1);
	}

	@Override
	public void run() {
		int i = 0;

		try {
			while( (hasMax && i < max)
					|| (!hasMax  && !isInterrupted()) ) {
				// System.out.printf("%s: is %d a prime number?", getName(), i);
				if(isPrime(i)) {
					list.add(i);
					// System.out.println(" Yes!");
				} else {
					// System.out.println(" No!");
				}
				// if(!hasMax)
				Thread.currentThread().sleep((long)(Math.random() * 1000));
				i++;

				// if(hasMax) {
				Thread[] threads = new Thread[PrimeTester.NO_OF_THREADS + 1];
				getThreadGroup().enumerate(threads);
				System.out.println("----- " + getName() + " -----");
				for(Thread t: threads) {
					System.out.println(t.getName() + ": " + t.getState());
				}
				// }
			}
		} catch (InterruptedException e) {
			// System.out.printf("%s got interrupted\n", getName());
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