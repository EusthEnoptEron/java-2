package ch.bfh.ti.progr2.serie3.money;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A bank account has a balance that can be changed by
 * deposits and withdrawals.
 */
public class BankAccount {
	// Seconds to wait for the condition to be fulfilled
	private static final int TIMEOUT = 3;
	private static final int THREADS_PER_ACCOUNT = 15;


	// For identifying and ordering
	private static int ID_SEED = 0;
	private final int id;

	// Balance
	private double balance;

	// For thread-safety
	private Lock lock = new ReentrantLock();
	private Condition isNotNegative = lock.newCondition();

	// Our thread provider
	ExecutorService executor = Executors.newFixedThreadPool(THREADS_PER_ACCOUNT);

	/**
	 * Represents a transaction made from this account to another one
	 */
	private class Transaction implements Runnable {
		BankAccount from;
		BankAccount to;
		double amount;

		/**
		 * Creates a new transaction.
		 * @param to where the money goes
		 * @param amount how much money gets transferred
		 */
		private Transaction(BankAccount to, double amount) {
			this.from = BankAccount.this;
			this.to = to;
			this.amount = amount;
		}


		@Override
		public void run() {
			// Array to store the necessary locks. We need 2
			Lock[] locks = new Lock[2];

			// Acquire locks in a certain order to prevent a deadlock
			if(from.id < to.id) {
				locks[0] = from.lock;
				locks[1] = to.lock;
			} else {
				locks[0] = to.lock;
				locks[1] = from.lock;
			}

			// Lock!
			locks[0].lock();
			locks[1].lock();

			try {
				while (from.balance < amount) {
					// There's too less money on this account
					// => abandon locks and wait for more money

					to.lock.unlock();

					if(from.isNotNegative.await(TIMEOUT, TimeUnit.SECONDS)) {
						// Okay, we got money: abandon the from lock temporarily
						// and acquire it again in the right order.
						from.lock.unlock();
						locks[0].lock();
						locks[1].lock();
					} else {
						from.lock.unlock();
						// Timeout elapsed and nobody deposited any money
						// => exit
						throw new RuntimeException("Timeout!");
					}
				}

				// Do our tasks.
				from.withdraw(amount);
				to.deposit(amount);

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				Bank.instance().report(false, from, to, amount);
				return;
			} finally {
				// Release both locks
				try {
					locks[0].unlock();
					locks[1].unlock();
				} catch(IllegalMonitorStateException e) { }
			}

			Bank.instance().report(true, from, to, amount);
		}
	}


	/**
	 * Constructs a bank account with a zero balance.
	 */
	public BankAccount() {
		balance = 0;
		this.id = ID_SEED++;
		Bank.instance().registerAccount(this);
	}

	public BankAccount(double balance) {
		this();
		this.balance = balance;
	}

	/**
	 * Gets the unique ID of this account.
	 * @return
	 */
	public int getId() {
		return id;
	}


	/**
	 * Deposits money into the bank account.
	 *
	 * @param amount the amount to deposit
	 */
	public synchronized void deposit(double amount) {
		balance += amount;
		// Notify the guys that are in the waiting queue
		isNotNegative.signalAll();
	}

	/**
	 * Withdraws money from the bank account.
	 *
	 * @param amount the amount to withdraw
	 */
	public void withdraw(double amount) {
		lock.lock();

		try {
			while (balance < amount)
				isNotNegative.await();

			balance -= amount;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void transfer(BankAccount to, double amount) {
		executor.execute(new Transaction(to, amount));
	}

	/**
	 * Gets the current balance of the bank account.
	 *
	 * @return the current balance
	 */
	public double getBalance() {
		return balance;
	}

	public Lock getLock() {
		return lock;
	}

	/**
	 * Shuts down the executor
	 */
	public void shutdown() {
		executor.shutdown();
		try {
			executor.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}