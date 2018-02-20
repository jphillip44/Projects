/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 2, Problem 1: Coins.java			  		   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A2sol;

import java.util.Scanner;

public class Coins {
	private static int[] coins = { 25, 10, 5, 1 };
	private static int[] count = new int[coins.length];
	private static String[][] name = new String[][] { { "quarter", "dime", "nickel", "penny" },
			{ "quarters", "dimes", "nickels", "pennies" } };
	private static int j;

	/**
	 * A public method that makes a call to private method that calculates the
	 * number of ways change can be made from the given input.
	 * 
	 * @param money
	 *            The amount that needs to be converted to change.
	 */
	public static void ways(int money) {
		ways(money, 0, coins[0]);
	}

	/**
	 * A method that takes an input value of money. It makes sure money is
	 * greater than 0 and the index is not beyond the coins array. It then
	 * checks if the value of the coin is less than the amount needing to be
	 * changed. If it is not, it calls print() and outputs the current
	 * combination. If it is, it increases the count at that value, reduces
	 * money and calls itself again. If it is not, it increases i and wraps back
	 * around. When i reaches coins.length, the program then needs to clear the
	 * recursion stack. Now we testing for solutions without the last coin value
	 * so we need to remove it from count. This is also where j comes into to
	 * play. It is set so that we don't double count values. For example, if we
	 * are changing 15 cents, we have already counted 1 dime and 1 nickel so we
	 * don't want to count 1 nickel and 1 dime as an option as well. It then
	 * goes back into the previous pattern and continues to do so until we find
	 * the final combination which is all pennies. At that point, the recursion
	 * stack is clear and the method terminates.
	 * 
	 * @param money
	 *            the current value of coins being analyzed by the method.
	 * @param i
	 *            is our counter as to what coin we are currently analyzing.
	 * @param j
	 *            forces the i counter to run to next coin value once the
	 *            solutions for the previous coin value are found.
	 */
	private static void ways(int money, int i, int j) {

		if (money > 0 && coins.length != i) {
			if (money >= coins[i] && j >= coins[i]) {
				count[i]++;
				ways(money - coins[i], i, coins[i]);
				count[i]--;
				if (i < coins.length - 1)
					ways(money, i, coins[i + 1]);
				else
					ways(money, i, 0);
			} else {
				ways(money, i + 1, coins[i]);
			}
		} else if (money == 0) {
			print();
		}
	}

	/**
	 * A helper method that outputs all change combinations to the console.
	 */
	private static void print() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		sb.append(++j + ") ");
		for (int i = 0; i < coins.length; i++) {
			sb.append(!first && count[i] >= 1 ? ", " : "");
			int k = (count[i] == 1 ? 0 : 1);
			sb.append(count[i] > 0 ? count[i] + " " + name[k][i] : "");
			first = (first && count[i] < 1);
		}
		System.out.println(sb);
	}

	public static void main(String[] args) {
		System.out.println("Enter an amount in cents:");
		Scanner input = new Scanner(System.in);
		int value = input.nextInt();
		if (value > 0) {
			ways(value);
		} else {
			System.out.println("The entered amount should be a positive integer.");
		}
		input.close();
	}
}
