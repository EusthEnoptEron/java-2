package ch.bfh.ti.progr2.serie3.ex5;

import javafx.util.Pair;

class MyInteger {
	private int value;
	public int get() { return value; }
	// This is OK. Integer assignments are atomic.
	public void set(int v) { value = v; }
}
class MyIncrement {
	private int value;
	// Needs to be synchronized
	public synchronized void inc() { value++; }
}

/**
 * We have to synchronize on "p" because p might be changed.
 */
class MySwapper {
	public static <T, S> Pair<S, T> swap(Pair<T, S> p) {
		T first;
		S second;
		synchronized (p) {
			first = p.getKey();
			second = p.getValue();
		}
		Pair<S, T> p2 = new Pair<>(second, first);
		return p2;
	}
}

