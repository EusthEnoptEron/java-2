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

	/**
	 * Constructs a bank account with a zero balance.
	 */
	public BankAccount() {
		balance = 0;
	}

	public BankAccount(double balance) {
		this.balance = balance;
	}

	/**
	 * Deposits money into the bank account.
	 *
	 * @param amount the amount to deposit
	 */
	public synchronized void deposit(double amount) {
		balance += amount;
		try {
			Thread.sleep(Bank.random.nextInt(30));
		} catch (InterruptedException e) {}
	}

	/**
	 * Withdraws money from the bank account.
	 *
	 * @param amount the amount to withdraw
	 */
	public synchronized void withdraw(double amount) {
		balance -= amount;
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
}