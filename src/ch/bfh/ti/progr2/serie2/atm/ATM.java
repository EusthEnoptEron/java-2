package ch.bfh.ti.progr2.serie2.atm;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

enum ATMState {START, PIN, ACCOUNT, TRANSACT};
enum AccountKind {CHECKING, SAVINGS}

public class ATM 
{  
	private ATMState state;
	private int customerNumber;
	private Customer currentCustomer;
	private BankAccount currentAccount;
	private AccountKind accountKind;
	private Bank theBank;
	private Logger logger;

	// Max size in MB
	private static final int MAX_SIZE = 10;
	private static final int LOG_FILES = 5;

	/**
   Constructs an ATM for a given bank.
   @param aBank the bank to which this ATM connects
	 */    
	public ATM(Bank aBank)
	{
		theBank = aBank;
		reset();

		// Set up logger
		logger = Logger.getLogger(ATMSimulator.class .getName());
		try {
			// %t (win7) = C:\Users\USER\AppData\Local\Temp\
			// %g = log-file index
			FileHandler fileHandler = new FileHandler("%ttransactions.%g.xml",MAX_SIZE * 1024, LOG_FILES);
			logger.setUseParentHandlers(false);
			logger.addHandler(fileHandler);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
   Resets the ATM to the initial state.
	 */
	public void reset()
	{
		customerNumber = -1;
		currentAccount = null;
		state = ATMState.START;             
	}

	/** 
   Sets the current customer number 
   and sets state to PIN. 
   (Precondition: state is START)
   @param number the customer number.
	 */
	public void setCustomerNumber(int number) 
	{
		assert state == ATMState.START;
		customerNumber = number;
		state = ATMState.PIN;
	}

	/** 
   Finds customer in bank.
   If found sets state to ACCOUNT, else to START.
   (Precondition: state is PIN)
   @param pin the PIN of the current customer
	 */
	public void selectCustomer(int pin)
	{  
		assert state == ATMState.PIN;
		currentCustomer 
		= theBank.findCustomer(customerNumber, pin);
		if (currentCustomer == null) 
			state = ATMState.START;
		else 
			state = ATMState.ACCOUNT;
	}

	/** 
   Sets current account to checking or savings. Sets 
   state to TRANSACT. 
   (Precondition: state is ACCOUNT or TRANSACT)
   @param account one of CHECKING or SAVINGS
	 */
	public void selectAccount(AccountKind account)
	{
		assert state == ATMState.ACCOUNT || state == ATMState.TRANSACT;
		if (account == AccountKind.CHECKING)
			currentAccount = currentCustomer.getCheckingAccount();
		else
			currentAccount = currentCustomer.getSavingsAccount();
		accountKind = account;
		state = ATMState.TRANSACT;
	}

	/** 
   Withdraws amount from current account. 
   (Precondition: state is TRANSACT)
   @param value the amount to withdraw
	 */
	public void withdraw(double value)
	{  
		assert state == ATMState.TRANSACT;
		logger.info(String.format("Customer %d withdrew %f from a %s account", customerNumber, value, accountKind.name().toLowerCase()));
		currentAccount.withdraw(value);
	}

	/** 
   Deposits amount to current account. 
   (Precondition: state is TRANSACT)
   @param value the amount to deposit
	 */
	public void deposit(double value)
	{  
		assert state == ATMState.TRANSACT;
		logger.info(String.format("Customer %d deposited %f from a %s account", customerNumber, value, accountKind.name().toLowerCase()));
		currentAccount.deposit(value);
	}

	/** 
   Gets the balance of the current account. 
   (Precondition: state is TRANSACT)
   @return the balance
	 */
	public double getBalance()
	{  
		assert state == ATMState.TRANSACT;
		return currentAccount.getBalance();
	}

	/**
   Moves back to the previous state.
	 */
	public void back()
	{
		if (state == ATMState.TRANSACT)
			state = ATMState.ACCOUNT;
		else if (state == ATMState.ACCOUNT)
			state = ATMState.PIN;
		else if (state == ATMState.PIN)
			state = ATMState.START;
	}

	/**
   Gets the current state of this ATM.
   @return the current state
	 */
	public ATMState getState()
	{
		return state;
	}
}
