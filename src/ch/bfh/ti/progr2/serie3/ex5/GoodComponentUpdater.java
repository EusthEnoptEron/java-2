package ch.bfh.ti.progr2.serie3.ex5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class GoodComponentUpdater extends JFrame {
	private JButton button = new JButton();
	private JProgressBar bar = new JProgressBar();
	public GoodComponentUpdater(String title) {
		super(title);
		button = new JButton("Start");
		setLayout(new FlowLayout());
		add(button);
		add(bar);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setEnabled(false); // Button is accessed
				InfoThread t = new InfoThread();
				t.start();
			}});
	}

	// Program Entry Point
// -------------------
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new BadComponentUpdater("Bad Component Updater");
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	// Inner class: InfoThread
// -----------------------
	class InfoThread extends Thread {
		int value, currentValue;

		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					Thread.currentThread().sleep(500);
					// Generate a random value
					value = (int)(Math.random() * 100);

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							currentValue = bar.getValue(); // Progress bar is accessed
							// Display a value only if it is a new one.
							if(currentValue != value) {
								bar.setValue(value); // Progress bar is accessed
							}
						}
					});

				} catch (Exception ex) { ex.printStackTrace(); }
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					button.setEnabled(true); // Button is accessed
				}
			});
		}
	}
}