/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 1, Problem 2: ArrayLongestPlateau.java	   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A1;

/**
 * The purpose of this class is to find the longest plateau of an array of ints.
 * 
 * The main method runs some tests.
 * 
 * @author andy
 * 
 */

public class ArrayLongestPlateau {

	/**
	 * longestPlateau() returns the longest plateau of an array of ints.
	 * 
	 * @return an array int[3] of the form {value, start, len} representing the
	 *         longest plateau of ints[] as a length len contiguous subarray
	 *         starting at index start with common element values value.
	 * 
	 *         For example, on the input array [2, 3, 3, 3, 3, 6, 6, 1, 1, 1],
	 *         it returns [6, 5, 2], indicating the longest plateau of this
	 *         array is the subarray [6, 6]; it starts at index 5 and has length
	 *         2.
	 * 
	 * @param ints
	 *            the input array.
	 */

	/*
	 * The longestPlateau method starts in a state where we assume we are on a
	 * peak. It then check if ints[i] is higher, lower or the same than ints[i -
	 * 1] If ints[i] is higher, we know that the previous flat was not a peak so
	 * the counters restart. If it is the same, we increment currLen and check
	 * again. If it is lower, we know we have ended a peak. We also know the
	 * next flat is not a peak. We then check if currLen is longer than len and
	 * if so, we update our global values. The loop continues and no values
	 * update until ints[i] > ints[i - 1]. At the end of the loop, it does the
	 * same check as at the end of a peak. If we weren't in a peak, it'll check
	 * anyways but currLen will not be longer than len. len, value and start and
	 * then returned.
	 */
	public static int[] longestPlateau(int[] ints) {

		// Initialize variables for both the current peak and to store the
		// longest one
		int currValue = 0, currStart = 0, currLen = 0;
		int value = 0, start = 0, len = 0;

		// Checks if we are in a peak or not
		boolean peak = true;
		for (int i = 0; i < ints.length; i++) {
			// Checks for the start of a peak and sets variables accordingly
			if (i == 0 || ints[i] > ints[i - 1]) {
				currStart = i;
				currValue = ints[i];
				currLen = 1;
				peak = true;
				// Increments the length of the peak for as long as the current
				// element matches the previous one
			} else if (ints[i] == ints[i - 1] && peak) {
				currLen++;
				// Resets peak in the event that the upcoming flat can not be a
				// peak.
			} else {
				peak = false;
			}
			// If the flat ends, checks if it's a peak and if so checks if it's
			// the longest peak.
			if ((i == ints.length - 1 || !peak) && currLen > len) {
				len = currLen;
				value = currValue;
				start = currStart;
			}
		}

		int[] result = { value, start, len };
		return result;
	}

	/**
	 * main() runs test cases on your longestPlateau() method. Prints summary
	 * information on basic operations and halts with an error (and a stack
	 * trace) if any of the tests fail.
	 */

	public static void main(String[] args) {
		String result;

		System.out.println("Let's find longest plateaus of arrays!\n");

		int[] test1 = { 4, 1, 1, 6, 6, 6, 6, 1, 1 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test1) + ":");
		result = TestHelper.stringInts(longestPlateau(test1));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 6 , 3 , 4 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		int[] test2 = { 3, 3, 1, 2, 4, 2, 1, 1, 1, 1 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test2) + ":");
		result = TestHelper.stringInts(longestPlateau(test2));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 3 , 0 , 2 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		int[] test3 = { 3, 3, 1, 2, 4, 0, 1, 1, 1, 1 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test3) + ":");
		result = TestHelper.stringInts(longestPlateau(test3));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 1 , 6 , 4 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		int[] test4 = { 3, 3, 3, 4, 1, 2, 4, 4, 0, 1 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test4) + ":");
		result = TestHelper.stringInts(longestPlateau(test4));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 4 , 6 , 2 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		int[] test5 = { 7, 7, 7, 7, 9, 8, 2, 5, 5, 5, 0, 1 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test5) + ":");
		result = TestHelper.stringInts(longestPlateau(test5));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 5 , 7 , 3 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		int[] test6 = { 4 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test6) + ":");
		result = TestHelper.stringInts(longestPlateau(test6));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 4 , 0 , 1 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		int[] test7 = { 4, 4, 4, 5, 5, 5, 6, 6 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test7) + ":");
		result = TestHelper.stringInts(longestPlateau(test7));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 6 , 6 , 2 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		System.out.println("\nAdditional tests done by the student or TA:\n");

		// Insert your additional test cases here.

		// Testing for long flat at the end that isn't a plateau .

		int[] test10 = { 4, 1, 1, 1, 1, 1, 1, 1, 1 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test10) + ":");
		result = TestHelper.stringInts(longestPlateau(test10));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 4 , 0 , 1 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		// Testing to ensure negative numbers do not confuse the test.

		int[] test11 = { -5, 1, 1, 1, -8, 6, 6, 1, 1 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test11) + ":");
		result = TestHelper.stringInts(longestPlateau(test11));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 1 , 1 , 3 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		// Testing for long flat at the start that isn't a plateau.

		int[] test12 = { 1, 1, 1, 1, 1, 1, 1, 1, 6 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test12) + ":");
		result = TestHelper.stringInts(longestPlateau(test12));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 6 , 8 , 1 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		// Testing for double Plateau on the boundaries.

		int[] test13 = { 4, 3, 2, 1, 0, 1, 2, 3, 4 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test13) + ":");
		result = TestHelper.stringInts(longestPlateau(test13));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 4 , 0 , 1 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		// Testing for a single length Plateau in the middle.

		int[] test14 = { 1, 2, 3, 4, 5, 4, 3, 2, 1 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test14) + ":");
		result = TestHelper.stringInts(longestPlateau(test14));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 5 , 4 , 1 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		// Testing for 0s in the list and multiple Plateaus.

		int[] test15 = { 0, 1, 0, 1, 0, 1, 0, 1, 0 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test15) + ":");
		result = TestHelper.stringInts(longestPlateau(test15));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 1 , 1 , 1 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		// Similar to test 14 but with negative numbers.

		int[] test16 = { -4, -3, -2, -1, 0, -1, -2, -3, -4 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test16) + ":");
		result = TestHelper.stringInts(longestPlateau(test16));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ 0 , 4 , 1 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

		// Testing for single unit negative Plateau.

		int[] test17 = { -4 };
		System.out.println("longest plateau of " + TestHelper.stringInts(test17) + ":");
		result = TestHelper.stringInts(longestPlateau(test17));
		System.out.println("[ value , start , len ] = " + result + " \n");
		TestHelper.verify(result.equals("[ -4 , 0 , 1 ]"), "Wrong: that's not the longest plateau!!!  No chocolate.");

	}
}
