package ch.bfh.ti.progr2.serie3.money;

import com.sun.javafx.collections.transformation.SortedList;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * A bank class that manages accounts.
 */
public class Bank {
	// Allowed inaccuracy of total balance
	private static final double EPSILON = 10e-2;
	public static volatile int counter = 0;

	private static Bank instance;

	// List of accounts sorted by ID
	private SortedMap<Integer, BankAccount> accounts = new TreeMap<>();

	// Balance used for debugging purposes
	private Double basisBalance = null;

	private Bank() {}

	/**
	 * Returns a Bank instance.
	 * @return
	 */
	public static Bank instance() {
		if(instance == null) {
			instance = new Bank();
		}
		return instance;
	}

	/**
	 * Add an account to the bank. You don't usually need to call this, since all accounts are automatically added to the bank.
	 * @param account
	 */
	public void registerAccount(BankAccount account) {
		accounts.put(account.getId(), account);
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
			// First, acquire locks for all accounts, sorted by the IDs (courtesy of SortedMap)
			for(BankAccount account: accounts.values()) {
				Lock lock = account.getLock();
				locks.add(lock);
				lock.lock();
			}

			// -> Now, the accounts can't be changed anymore.
			// Next, calculate the total balance
			for(BankAccount account: accounts.values()) {
				amount += account.getBalance();
			}
		} finally {
			// We're done, so release the locks
			for(Lock lock: locks) {
				lock.unlock();
			}
		}

		// Set this for coherency purposes
		if(basisBalance == null)
			basisBalance = amount;

		return amount;
	}

	/**
	 * Reports the result of a transaction and kills the application if anything's not in order.
	 * @param success transaction successful?
	 * @param from
	 * @param to
	 * @param amount
	 */
	public synchronized void report(boolean success, BankAccount from, BankAccount to, double amount) {
		synchronized (System.out) {
			if(success) {
				double balance = getTotalBalance();
				System.out.printf("%d) Transferred CHF%.2f from %d (%.2f) to %d (%.2f), with a total of CHF%.2f\n",  ++counter, amount, from.getId(), from.getBalance(), to.getId(), to.getBalance(), balance);

				// Verify total balance
				if(Math.abs(balance - basisBalance) > EPSILON) {
					System.out.println("ERROR: totals do not match!");
					System.exit(-1);
				}
			} else {
				System.out.printf("%d) FAIL: Couldn't transfer CHF%.2f from %d (%.2f) to %d (%.2f)\n", ++counter, amount, from.getId(), from.getBalance(), to.getId(), to.getBalance());
			}
		}
	}



	/**
	 * Shuts down all threads and waits for them to be terminated.
	 */
	public void shutdown() {
		for(BankAccount account: accounts.values()) {
			account.shutdown();
		}
	}

	public void printBalance() {
		System.out.println("---------------------------------");
		System.out.println("# BALANCE");
		System.out.println("---------------------------------");
		double total = 0;
		for(BankAccount account: accounts.values()) {
			total += account.getBalance();
			System.out.printf("Account #%d) CHF%.2f\n",  account.getId(), account.getBalance());
		}
		System.out.println("---------------------------------");
		System.out.printf("Total: CHF%.2f\n", total);
	}
}
