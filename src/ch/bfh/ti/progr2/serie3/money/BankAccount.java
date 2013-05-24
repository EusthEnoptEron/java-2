package ch.bfh.ti.progr2.serie3.money;

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
		// Array to store the necessary locks
		Lock[] locks = new Lock[2];

		// Acquire locks in a certain order to prevent a deadlock
		if(this.id < to.id) {
			locks[0] = this.lock;
			locks[1] = to.lock;
		} else {
			locks[0] = to.lock;
			locks[1] = this.lock;
		}

		// Lock!
		locks[0].lock();
		locks[1].lock();

		try {
			while (balance < amount) {
				to.lock.unlock();
				isNotNegative.await();

				this.lock.unlock();
				locks[0].lock();
				locks[1].lock();
			}

			// Do our tasks.
			this.withdraw(amount);
			to.deposit(amount);
		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} finally {
			// Release both locks
			locks[0].unlock();
			locks[1].unlock();
		}
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