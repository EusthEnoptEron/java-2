package ch.bfh.ti.progr2.serie3.ex5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Simon
 * Date: 24.05.13
 * Time: 09:37
 * To change this template use File | Settings | File Templates.
 */
public class SortTester {
	abstract static class Animal implements Comparable<Animal> {
		protected int numberOfTeeth = 0;

		@Override
		public int compareTo(Animal o) {
			return numberOfTeeth - o.numberOfTeeth;
		}
	}

	static class Bear extends Animal {
		public Bear() {
			this.numberOfTeeth = 20;
		}
		public Bear(int numberOfTeeth) {
			this.numberOfTeeth = numberOfTeeth;
		}
	}



	// "Bear has to be comparable with BEAR!"
	public static <T extends Comparable<T>> List<T> sort1(T[] array) {
		T[] arrayCopy = array.clone();
		Arrays.sort(arrayCopy);
		return Arrays.asList(arrayCopy);
	}
	// "Bear has to be comparable with bear or one of its superclasses!"
	public static <T extends Comparable<? super T>> List<T> sort2(T[] array) {
		T[] arrayCopy = array.clone();
		Arrays.sort(arrayCopy);
		return Arrays.asList(arrayCopy);
	}

	public static void main(String[] args) {
		Bear[] myList = new Bear[]{
			new Bear(4),
			new Bear(6),
			new Bear(2)
		};


//		List<Bear> nums = sort1(myList); //<- Doesn't compile
		List<Bear> nums2 = sort2(myList);
	}
}
