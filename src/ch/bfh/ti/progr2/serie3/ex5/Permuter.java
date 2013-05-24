package ch.bfh.ti.progr2.serie3.ex5;

public class Permuter {
	public static void main(String args[]) {
		permuteString("", "String");
	}

	/**
	 * Finds all permutations of the endingString and prints them appended to beginningString
	 */
	public static void permuteString(String beginningString, String endingString) {
		// Can we shuffle the endingString, anyway?
		if (endingString.length() <= 1)
			System.out.println(beginningString + endingString);
		else {
			// There are still letters to shuffle!
			// => Loop through them
			for (int i = 0; i < endingString.length(); i++) {
				try {
					// Extract the current letter from the endingString
					String newString = endingString.substring(0, i) + endingString.substring(i + 1);

					// Append the extracted letter to beginningString and look for more permutations
					permuteString(beginningString + endingString.charAt(i), newString);

					// EXAMPLE:
					// IF
					//  beginningString = "St"
					//  endingString    = "ring"
					//  i = 1 (second iteration)
					// THEN
					//  1) extract "i" -> endingString = "rng"
					//  2) permuteString("Sti", "rng")

				} catch (StringIndexOutOfBoundsException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
}