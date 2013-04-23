package ch.bfh.ti.progr2.serie2.atm;


import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
   A text-based simulation of an automatic teller machine.
 */
public class ATMSimulator
{
	public static void main(String[] args)
	{

		ATM theATM;
		try
		{  
			Bank theBank = new Bank();
			theBank.readCustomers("customers.txt");
			theATM = new ATM(theBank);
		}
		catch(IOException e)
		{  
			System.out.println("Error opening accounts file.");
			return;
		}

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		while (true)
		{
			ATMState state = theATM.getState();
			if (state == ATMState.START)
			{
				System.out.print("Enter customer number (0 to quit): ");
				int number = in.nextInt();
				if (number == 0) return;
				theATM.setCustomerNumber(number);            
			}
			else if (state == ATMState.PIN)
			{
				System.out.print("Enter PIN: ");
				int pin = in.nextInt();
				theATM.selectCustomer(pin);
			}
			else if (state == ATMState.ACCOUNT)
			{
				System.out.print("A=Checking, B=Savings, C=Quit: ");
				String command = in.next();
				if (command.equalsIgnoreCase("A"))
					theATM.selectAccount(AccountKind.CHECKING);
				else if (command.equalsIgnoreCase("B"))
					theATM.selectAccount(AccountKind.SAVINGS);
				else if (command.equalsIgnoreCase("C"))
					theATM.reset();
				else
					System.out.println("Illegal input!");                        
			}
			else if (state == ATMState.TRANSACT)
			{
				System.out.println("Balance=" + theATM.getBalance());
				System.out.print("A=Deposit, B=Withdrawal, C=Cancel: ");
				String command = in.next();
				if (command.equalsIgnoreCase("A"))
				{
					System.out.print("Amount: ");
					double amount = in.nextDouble();
					theATM.deposit(amount);
					theATM.back();
				}
				else if (command.equalsIgnoreCase("B"))
				{
					System.out.print("Amount: ");
					double amount = in.nextDouble();
					theATM.withdraw(amount);
					theATM.back();
				}
				else if (command.equalsIgnoreCase("C"))
					theATM.back();
				else
					System.out.println("Illegal input!");                                    
			}         
		}
	}
}

