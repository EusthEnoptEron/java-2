package ch.bfh.ti.progr2.serie3.money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * A bank class that manages accounts.
 */
public class Bank {
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
			// Array to store the necessary locks
			Lock[] locks = new Lock[2];

			// Acquire locks in a certain order to prevent a deadlock
			if(fromIndex < toIndex) {
				locks[0] = from.getLock();
				locks[1] = to.getLock();
			} else {
				locks[0] = to.getLock();
				locks[1] = from.getLock();
			}

			try {
				// Lock!
				locks[0].lock();
				locks[1].lock();

				// Do our tasks.
				from.withdraw(amount);
				to.deposit(amount);
			}
			finally {
				// Release both locks
				locks[0].unlock();
				locks[1].unlock();
			}
			// We have to do this outside the synchronized block because the "getTotalBalance" method will try to acquire all locks
			System.out.printf("Transferred CHF%.2f from %d to %d, with a total of %.2f\n", amount, accounts.indexOf(from), accounts.indexOf(to), Bank.this.getTotalBalance());
		}
	}

	private ArrayList<BankAccount> accounts = new ArrayList<>();
	ExecutorService pool;

	/**
	 * Creates a new bank instance from a list of accounts.
	 * @param accounts list of accounts
	 */
	public Bank(ArrayList<BankAccount> accounts) {
		this.accounts = accounts;
		preparePool();
	}

	/**
	 * Creates a new bank instance from a list of accounts.
	 * @param accounts list of accounts
	 */
	public Bank(BankAccount[] accounts) {
		Collections.addAll(this.accounts, accounts);
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
		pool.execute(new Transaction(from, to, amount));
	}

	/**
	 * Shuts down the thread pool contained by this bank instance.
	 */
	public void shutdown() {
		pool.shutdown();
	}

}
