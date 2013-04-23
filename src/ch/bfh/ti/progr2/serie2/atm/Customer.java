package ch.bfh.ti.progr2.serie2.atm;

/**
   A bank customer with a checking and a savings account.
*/
public class Customer
{  
   private int customerNumber;
   private int pin;
   private BankAccount checkingAccount;
   private BankAccount savingsAccount;

	/**
	 * Constructs a customer with a given number and PIN.
	 * @param number the customer number
	 * @param pin the personal identification number
	 * @param checkingAccount
	 * @param savingsAccount
	 */
   public Customer(int number, int pin, BankAccount checkingAccount, BankAccount savingsAccount)
   {  
      this.customerNumber = number;
      this.pin = pin;
      this.checkingAccount = checkingAccount;
      this.savingsAccount = savingsAccount;
   }
   
   /** 
      Tests if this customer matches a customer number 
      and PIN.
      @param aNumber a customer number
      @param aPin a personal identification number
      @return true if the customer number and PIN match
   */
   public boolean match(int aNumber, int aPin)
   {  
      return customerNumber == aNumber && pin == aPin;
   }
   
   /** 
      Gets the checking account of this customer.
      @return the checking account
   */
   public BankAccount getCheckingAccount()
   {  
      return checkingAccount;
   }
   
   /** 
      Gets the savings account of this customer.
      @return the checking account
   */
   public BankAccount getSavingsAccount()
   {  
      return savingsAccount;
   }
}
