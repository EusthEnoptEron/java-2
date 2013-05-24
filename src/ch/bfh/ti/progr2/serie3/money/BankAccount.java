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
	private double balance;
	private Lock lock = new ReentrantLock();
	private Condition isNotNegative = lock.newCondition();
	private static int ID_SEED = 0;
	private final int id;

	private Bank bank;
	ExecutorService executor = new ThreadPoolExecutor(0, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

	private class Transaction implements Runnable {
		BankAccount from;
		BankAccount to;
		double amount;

		private Transaction(BankAccount to, double amount) {
			this.from = BankAccount.this;
			this.to = to;
			this.amount = amount;
		}

		@Override
		public void run() {

			// Array to store the necessary locks
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
					to.lock.unlock();
					if(from.isNotNegative.await(1, TimeUnit.SECONDS)) {
						from.lock.unlock();
						locks[0].lock();
						locks[1].lock();
					} else {
						throw new RuntimeException("Timeout!");
					}
				}

				// Do our tasks.
				from.withdraw(amount);
				to.deposit(amount);
			} catch (InterruptedException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			} catch (RuntimeException e) {
				System.out.printf("Couldn't transfer CHF%.2f from %d (%.2f) to %d (%.2f)\n", amount, id, from.getBalance(), to.id, to.getBalance());
			} finally {
				// Release both locks
				locks[0].unlock();
				locks[1].unlock();
			}


			System.out.printf("Transferred CHF%.2f from %d (%.2f) to %d (%.2f), with a total of CHF%.2f\n", amount, from.id, from.getBalance(), to.id, to.getBalance(), bank.getTotalBalance());
		}
	}


	/**
	 * Constructs a bank account with a zero balance.
	 */
	public BankAccount() {
		balance = 0;
		this.id = ID_SEED++;
	}

	public BankAccount(double balance) {
		this();
		this.balance = balance;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public int getId() {
		return id;
	}

	public Bank getBank() {
		return bank;
	}

	/**
	 * Deposits money into the bank account.
	 *
	 * @param amount the amount to deposit
	 */
	public synchronized void deposit(double amount) {
		balance += amount;
		isNotNegative.signalAll();
		try {
			Thread.sleep(BankTester.random.nextInt(30));
		} catch (InterruptedException e) {}
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
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} finally {
			lock.unlock();
		}
	}

	public void transfer(BankAccount to, double amount) {
		executor.submit(new Transaction(to, amount));
	}

	/**
	 * Gets the current balance of the bank account.
	 *
	 * @return the current balance
	 */
	public synchronized double getBalance() {
		return balance;
	}

	public Lock getLock() {
		return lock;
	}
}