package ch.bfh.ti.progr2.serie3.prime2;

import java.util.ArrayList;
import java.util.concurrent.*;

class PrimeTester {
	public static final int NO_OF_THREADS = 10;
	private static final int PRIME_MAX = 9973;

	static ArrayList<Integer> mainResults = new ArrayList<>();
	static ArrayList<Integer> subResults = new ArrayList<>();



	public static void main(String[] args) {
		long timeMain = 0,
			 timeSub = 0;

		// STEP 1: Calculate in main method
		PrimeCalculator mainCalc = new PrimeCalculator(0, PRIME_MAX+1);
		TimeWatch watch = TimeWatch.start();
		mainResults = mainCalc.calculate();
		timeMain = watch.time();

		// STEP 2: Calculate in threads
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<ArrayList<Integer>> service = new ExecutorCompletionService<ArrayList<Integer>>(executor);

		int countPerThread = PRIME_MAX / NO_OF_THREADS;

		Future<ArrayList<Integer>>[] results = new Future[NO_OF_THREADS];

		watch.reset();
		// Create
		for(int i = 0; i < NO_OF_THREADS; i++) {

			int start = i * countPerThread;

			// The last task is a special case: calculate all remaining numbers (maybe the numbers wouldn't add up)
			if(i + 1 == NO_OF_THREADS) {
				results[i] = service.submit(new PrimeCalculator(start, PRIME_MAX - start+1));
			} else {
				results[i] = service.submit(new PrimeCalculator(start, countPerThread));
			}
		}

		// We're fetching the futures manually to keep the order
		for(Future<ArrayList<Integer>> result: results) {
			try {
//				System.out.println(result.get());
				subResults.addAll(result.get());
			} catch (InterruptedException|ExecutionException e) {
				e.printStackTrace();
			}
		}
		timeSub = watch.time();
		executor.shutdown();

		System.out.println("RESULT LISTING");
		System.out.println("----------------------------------");
		System.out.printf( "| Main method took %d ms\n", timeMain );
		System.out.printf( "| Threads took %d ms\n", timeSub);
		System.out.println("| -------------------------------");
		System.out.printf("| WINNER: %s (%.2f%%)\n", timeMain < timeSub
														? "main"
														: "threads", ((double)Math.min(timeMain, timeSub)) / Math.max(timeMain, timeSub) * 100 );
		System.out.println("| -------------------------------");


		if(mainResults.equals(subResults)) {
			System.out.println("| results are EQUAL");
		} else {
			System.out.println("| results are DIFFERENT");
		}

		for(Integer integer: subResults) {
			System.out.print(integer + ", ");
		}
	}


}

class TimeWatch {

	long starts;

	public static TimeWatch start() {
		return new TimeWatch();
	}

	private TimeWatch() {
		reset();
	}

	public TimeWatch reset() {
		starts = System.currentTimeMillis();
		return this;
	}

	public long time() {
		long ends = System.currentTimeMillis();
		return ends - starts;
	}

	public long time(TimeUnit unit) {
		return unit.convert(time(), TimeUnit.MILLISECONDS);
	}

}
