package ch.bfh.ti.progr2.serie3.money;

import com.sun.javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * A bank class that manages accounts.
 */
public class Bank {
	private volatile ArrayList<Transaction> transactions = new ArrayList<>();

	// Runnable class that is used for transactions
	private class Transaction implements Runnable {
		// Store descriptive variables
		private final BankAccount from;
		private int fromIndex;

		private final BankAccount to;
		private int toIndex;

		// Amount of money to transfer
		private double amount;

		private Transaction(int fromIndex, int toIndex, double amount) {
			this.from = accounts.get(fromIndex);
			this.fromIndex = fromIndex;
			this.to = accounts.get(toIndex);
			this.toIndex = toIndex;

			this.amount = amount;
		}

		// Run the job
		@Override
		public void run() {
			from.transfer(to, amount);
			// We have to do this outside the synchronized block because the "getTotalBalance" method will try to acquire all locks
			System.out.printf("Transferred CHF%.2f from %d (%.2f) to %d (%.2f), with a total of CHF%.2f\n", amount, fromIndex, from.getBalance(), toIndex, to.getBalance(), Bank.this.getTotalBalance());
			transactions.remove(this);
		}
	}

	private ArrayList<BankAccount> accounts = new ArrayList<>();
	ExecutorService pool;

	/**
	 * Creates a new bank instance from a list of accounts.
	 * @param accounts list of accounts
	 */
	public Bank(Collection<BankAccount> accounts) {
		for(BankAccount account: accounts) {
			this.accounts.add(account.getId(), account);
		}
		preparePool();
	}

	/**
	 * Creates a new bank instance from a list of accounts.
	 * @param accounts list of accounts
	 */
	public Bank(BankAccount[] accounts) {
		for(BankAccount account: accounts) {
			this.accounts.add(account.getId(), account);
		}
		preparePool();
	}

	// Prepare thread pool
	private void preparePool() {
		pool = Executors.newFixedThreadPool(accounts.size());
	}

	/**
	 * Returns the total balance over all accounts in this bank instance.
	 * In order to stay atomic, this will create locks for all accounts.
	 * @return
	 */
	public double getTotalBalance() {
		ArrayList<Lock> locks = new ArrayList<>();
		double amount = 0;
		try {
			// First, acquire locks for all accounts!
			for(BankAccount account: accounts) {
				Lock lock = account.getLock();
				locks.add(lock);
				lock.lock();
			}

			// Next, calculate the total balance
			for(BankAccount account: accounts) {
				amount += account.getBalance();

				// Sleep for a random time to make things harder
				try {
					Thread.sleep(BankTester.random.nextInt(30));
				} catch (InterruptedException e) {}
			}
		} finally {
			// We're done, so release the locks
			for(Lock lock: locks) {
				lock.unlock();
			}
		}

		return amount;
	}


	/**
	 * Makes a asynchronous transaction.
	 * @param from account number in this bank  from which the transaction goes
	 * @param to account number in this bank  to which the transaction goes
	 * @param amount amount of money to transfer
	 */
	public void makeTransaction(int from, int to, double amount) {
		Transaction transaction = new Transaction(from, to, amount);
		transactions.add(transaction);
		pool.execute(transaction);
	}

	/**
	 * Shuts down the thread pool contained by this bank instance.
	 */
	public void shutdown() {
		pool.shutdown();
		try {
			boolean terminatedNormally = pool.awaitTermination(5, TimeUnit.SECONDS);
			if(!terminatedNormally) {
				System.out.println("Couldn't terminate. Please see the report below for details.");
				System.out.println("------------------------------------------------------------");
				for(Transaction transaction: transactions) {
					System.out.printf("Couldn't transfer CHF%.2f from %d (%.2f) -> %d (%.2f).\n",
							transaction.amount,
							transaction.fromIndex,
							transaction.from.getBalance(),
							transaction.toIndex,
							transaction.to.getBalance());
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

	}

}
