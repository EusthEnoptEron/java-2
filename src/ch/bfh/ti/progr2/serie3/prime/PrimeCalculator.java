package ch.bfh.ti.progr2.serie3.prime;

import java.util.ArrayList;

class PrimeCalculator extends Thread {
	private ArrayList<Integer> list;
	private int max = 0;
	private boolean hasMax = false;
	private boolean takeBreaks = true;

	PrimeCalculator(String name) {
		super(name);
		this.list = new ArrayList<>();
	}

	PrimeCalculator(String name, int max) {
		this(name);
		this.max = max;
		hasMax = true;

	}

	public void setTakeBreaks(boolean takeBreaks) {
		this.takeBreaks = takeBreaks;
	}

	public Integer[] getResults() {
		return list.toArray(new Integer[list.size()]);
	}

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
			while(!isInterrupted() && (!hasMax || i < max)) {
				if(isPrime(i)) {
					System.out.printf("%s: found %d\n", getName(), i);
					list.add(i);
				}
				i++;

				if(takeBreaks)
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