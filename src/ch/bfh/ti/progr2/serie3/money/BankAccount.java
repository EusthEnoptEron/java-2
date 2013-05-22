package ch.bfh.ti.progr2.serie3.money;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A bank account has a balance that can be changed by
 * deposits and withdrawals.
 */
public class BankAccount {
	private double balance;

	private class Worker extends Thread {
		BankAccount to;
		double amount;

		private Worker(BankAccount to, double amount) {
			this.to = to;
			this.amount = amount;
		}

		@Override
		public void run() {
			synchronized (BankAccount.this) {
				BankAccount.this.withdraw(amount);
				to.deposit(amount);
			}
		}
	}

	/**
	 * Constructs a bank account with a zero balance.
	 */
	public BankAccount() {
		balance = 0;
	}

	/**
	 * Deposits money into the bank account.
	 *
	 * @param amount the amount to deposit
	 */
	public synchronized void deposit(double amount) {
		balance += amount;
	}

	/**
	 * Withdraws money from the bank account.
	 *
	 * @param amount the amount to withdraw
	 */
	public synchronized void withdraw(double amount) {
		balance -= amount;
	}

	public void makeTransaction(BankAccount to, double amount) {
		(new Worker(to, amount)).run();
	}

	/**
	 * Gets the current balance of the bank account.
	 *
	 * @return the current balance
	 */
	public double getBalance() {
		return balance;
	}
}