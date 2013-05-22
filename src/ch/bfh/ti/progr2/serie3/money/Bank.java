package ch.bfh.ti.progr2.serie3.money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 22.05.13
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class Bank {
	// For test purposes
	public static Random random = new Random(5);

	private class Transaction implements Runnable {
		private final BankAccount from;
		private int fromIndex;

		private final BankAccount to;
		private int toIndex;

		private double amount;

		private Transaction(int fromIndex, int toIndex, double amount) {
			this.from = accounts.get(fromIndex);
			this.fromIndex = fromIndex;
			this.to = accounts.get(toIndex);
			this.toIndex = toIndex;

			this.amount = amount;
		}

		@Override
		public void run() {
			Lock lock1,
			     lock2;

			// Acquire locks in a certain order to prevent a deadlock
			lock1 = fromIndex < toIndex
					? from.getLock()
					: to.getLock();
			lock2 = fromIndex < toIndex
					? to.getLock()
					: from.getLock();
			try {
				// Lock!
				lock1.lock();
				lock2.lock();

				// Do our tasks.
				from.withdraw(amount);
				to.deposit(amount);
			}
			finally {
				// Release both locks
				lock1.unlock();
				lock2.unlock();
			}
			// We have to do this outside the synchronized block because the "getTotalBalance" method will try to acquire all locks
			System.out.printf("Transferred CHF%.2f from %d to %d, with a total of %.2f\n", amount, accounts.indexOf(from), accounts.indexOf(to), Bank.this.getTotalBalance());
		}
	}

	private ArrayList<BankAccount> accounts = new ArrayList<>();
	ExecutorService pool;
	public Bank() {
		pool = Executors.newFixedThreadPool(1);
	}

	public Bank(ArrayList<BankAccount> accounts) {
		this.accounts = accounts;
		preparePool();
	}

	public Bank(BankAccount[] accounts) {
		Collections.addAll(this.accounts, accounts);
		preparePool();
	}

	private void preparePool() {
		pool = Executors.newFixedThreadPool(accounts.size());
	}

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
					Thread.sleep(random.nextInt(30));
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

	public void addAccount(BankAccount account) {
		accounts.add(account);
	}

	public void makeTransaction(int from, int to, double amount) {
		pool.execute(new Transaction(from, to, amount));
	}

	public static void main(String[] args) {
		double MAX = 500;
		int transactionCount = 20;

		BankAccount[] accounts = new BankAccount[] {
			new BankAccount(random.nextDouble() * MAX),
			new BankAccount(random.nextDouble() * MAX),
			new BankAccount(random.nextDouble() * MAX),
			new BankAccount(random.nextDouble() * MAX),
			new BankAccount(random.nextDouble() * MAX),
			new BankAccount(random.nextDouble() * MAX),
			new BankAccount(random.nextDouble() * MAX)
		};

		Bank bank = new Bank(accounts);
		int from, to;
		System.out.printf("START: CHF%.2f\n\n", bank.getTotalBalance());

		for(int i = 0; i < transactionCount; i++) {
			from = random.nextInt(accounts.length);
			do {
				to = random.nextInt(accounts.length);
			} while (to == from);

			bank.makeTransaction(from, to, random.nextDouble() * 50);
		}
		bank.pool.shutdown();
	}
}
