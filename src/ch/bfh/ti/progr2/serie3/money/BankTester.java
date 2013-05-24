package ch.bfh.ti.progr2.serie3.money;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 13/05/22
 * Time: 23:45
 * To change this template use File | Settings | File Templates.
 */
public class BankTester {
	private static final double MAX_BALANCE = 50;
	private static final double TRANSACTION_RANGE = 50;
	private static final int TRANSACTION_COUNT = 20;
	// For test purposes
	public static Random random = new Random(5);

	public static void main(String[] args) {

		BankAccount[] accounts = new BankAccount[] {
				new BankAccount(random.nextDouble() * MAX_BALANCE),
				new BankAccount(random.nextDouble() * MAX_BALANCE),
				new BankAccount(random.nextDouble() * MAX_BALANCE),
				new BankAccount(random.nextDouble() * MAX_BALANCE),
				new BankAccount(random.nextDouble() * MAX_BALANCE),
				new BankAccount(random.nextDouble() * MAX_BALANCE),
				new BankAccount(random.nextDouble() * MAX_BALANCE)
		};

		Bank bank = new Bank(accounts);
		BankAccount from, to;
		System.out.printf("START: CHF%.2f\n\n", bank.getTotalBalance());

		for(int i = 0; i < TRANSACTION_COUNT; i++) {
			from = accounts[random.nextInt(accounts.length)];
			do {
				to = accounts[random.nextInt(accounts.length)];
			} while (to == from);

			from.transfer(to, random.nextDouble() * TRANSACTION_RANGE);
		}
	}
}
