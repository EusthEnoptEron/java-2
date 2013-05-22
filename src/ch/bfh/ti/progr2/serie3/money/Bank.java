package ch.bfh.ti.progr2.serie3.money;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 22.05.13
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class Bank {
	private class Transaction implements Runnable {
		private final BankAccount from;
		private final BankAccount to;
		private double amount;

		private Transaction(BankAccount from, BankAccount to, double amount) {
			this.from = from;
			this.to = to;
			this.amount = amount;
		}

		@Override
		public void run() {
			synchronized (from) {
				from.withdraw(amount);
				to.deposit(amount);

			}
		}
	}

	private ArrayList<BankAccount> accounts = new ArrayList<>();
	ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 60l, TimeUnit.SECONDS, null);
	public Bank() {
	}

	public Bank(ArrayList<BankAccount> accounts) {
		this.accounts = accounts;
	}

	public synchronized double getTotalBalance() {
		double amount = 0;
		for(BankAccount account: accounts) {
			amount += account.getBalance();
		}
		return amount;
	}

	public void addAccount(BankAccount account) {
		accounts.add(account);
	}

	public void makeTransaction(BankAccount from, BankAccount to, double amount) {
		pool.execute(new Transaction(from, to, amount));
	}

	public static void main(String[] args) {

	}
}
