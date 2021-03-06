/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 1, Problem 1: ArraySqueeze.java			   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/


package A1;

/**
 * The purpose of this class is to squeeze an array of ints.
 * 
 * The main method runs some tests.
 * 
 * @author andy
 * 
 */

public class ArraySqueeze {

	/**
	 * squeeze() takes an array of ints. On completion the array contains the
	 * same numbers, but wherever the array had two or more consecutive
	 * duplicate numbers, they are replaced by one copy of the number. Hence,
	 * after squeeze() is done, no two consecutive numbers in the array are the
	 * same.
	 * 
	 * Any unused elements at the end of the array are set to -1.
	 * 
	 * For example, if the input array is [ 4 , 1 , 1 , 3 , 3 , 3 , 1 , 1 ], it
	 * reads [ 4 , 1 , 3 , 1 , -1 , -1 , -1 , -1 ] after squeeze() completes.
	 * 
	 * @param ints
	 *            the input array.
	 */

	public static void squeeze(int[] ints) {

		/*
		 * j loops through the array and compares it to i to locate
		 * none-duplicate values of the array. i is incremented every time it
		 * does not match j and i is inserted into the new i. This ensures i is
		 * always unique. The second loop fills the array with the value -1.
		 */
		int i = 0;
		for (int j = 0; j < ints.length; j++) {
			if (ints[i] != ints[j]) {
				ints[++i] = ints[j];
			}
		}
		while (i++ < ints.length - 1) {
			ints[i] = -1;
		}

	}

	/**
	 * main() runs test cases on your squeeze() method. Prints summary
	 * information on basic operations and halts with an error (and a stack
	 * trace) if any of the tests fail.
	 */

	public static void main(String[] args) {
		String result;

		System.out.println("Let's squeeze arrays!\n");

		int[] test1 = { 3, 7, 7, 7, 4, 5, 5, 2, 0, 8, 8, 8, 8, 5 };
		System.out.println("squeezing " + TestHelper.stringInts(test1) + ":");
		squeeze(test1);
		result = TestHelper.stringInts(test1);
		System.out.println(result + "\n");
		TestHelper.verify(result.equals("[ 3 , 7 , 4 , 5 , 2 , 0 , 8 , 5 , -1 , -1 , -1 , -1 , -1 , -1 ]"),
				"BAD SQEEZE!!!  No cookie.");

		int[] test2 = { 6, 6, 6, 6, 6, 3, 6, 3, 6, 3, 3, 3, 3, 3, 3 };
		System.out.println("squeezing " + TestHelper.stringInts(test2) + ":");
		squeeze(test2);
		result = TestHelper.stringInts(test2);
		System.out.println(result + "\n");
		TestHelper.verify(result.equals("[ 6 , 3 , 6 , 3 , 6 , 3 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ]"),
				"BAD SQEEZE!!!  No cookie.");

		int[] test3 = { 4, 4, 4, 4, 4 };
		System.out.println("squeezing " + TestHelper.stringInts(test3) + ":");
		squeeze(test3);
		result = TestHelper.stringInts(test3);
		System.out.println(result + "\n");
		TestHelper.verify(result.equals("[ 4 , -1 , -1 , -1 , -1 ]"), "BAD SQEEZE!!!  No cookie.");

		int[] test4 = { 0, 1, 2, 3, 4, 5, 6 };
		System.out.println("squeezing " + TestHelper.stringInts(test4) + ":");
		squeeze(test4);
		result = TestHelper.stringInts(test4);
		System.out.println(result + "\n");
		TestHelper.verify(result.equals("[ 0 , 1 , 2 , 3 , 4 , 5 , 6 ]"), "BAD SQEEZE!!!  No cookie.");

		System.out.println("\nAdditional tests done by the student or TA:\n");

		// Insert your additional test cases here.

		// Making sure it doesn't think alternating numbers are duplicates.

		int[] test10 = { 1, 0, 1, 0, 1, 0 };
		System.out.println("squeezing " + TestHelper.stringInts(test10) + ":");
		squeeze(test10);
		result = TestHelper.stringInts(test10);
		System.out.println(result + "\n");
		TestHelper.verify(result.equals("[ 1 , 0 , 1 , 0 , 1 , 0 ]"), "BAD SQEEZE!!!  No cookie.");

		// Testing for negative numbers.

		int[] test11 = { -4, -4, -2, -2 };
		System.out.println("squeezing " + TestHelper.stringInts(test11) + ":");
		squeeze(test11);
		result = TestHelper.stringInts(test11);
		System.out.println(result + "\n");
		TestHelper.verify(result.equals("[ -4 , -2 , -1 , -1 ]"), "BAD SQEEZE!!!  No cookie.");

		// Testing very short duplicate numbers.

		int[] test12 = { 4, 4, 1, 4, 4 };
		System.out.println("squeezing " + TestHelper.stringInts(test12) + ":");
		squeeze(test12);
		result = TestHelper.stringInts(test12);
		System.out.println(result + "\n");
		TestHelper.verify(result.equals("[ 4 , 1 , 4 , -1 , -1 ]"), "BAD SQEEZE!!!  No cookie.");

		// Testing for negative and positive of the same value.

		int[] test13 = { 15, 15, -15, -15 };
		System.out.println("squeezing " + TestHelper.stringInts(test13) + ":");
		squeeze(test13);
		result = TestHelper.stringInts(test13);
		System.out.println(result + "\n");
		TestHelper.verify(result.equals("[ 15 , -15 , -1 , -1 ]"), "BAD SQEEZE!!!  No cookie.");
	}
}
