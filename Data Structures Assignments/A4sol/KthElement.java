/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 3, Problem 2: KthElement.java	  		   	   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A4sol;

import java.util.Scanner;

public class KthElement {

	private static void smallSearch(int len, int a[], int b[], int aStart, int bStart) {
		int mid, kth = 0;

		if (len == 1) {
			kth = (a[aStart] < b[bStart] ? a[aStart] : b[bStart]);
			System.out.println(kth);
			return;
		}

		mid = (len % 2 == 0 ? len / 2 - 1 : len / 2);

		if (a[aStart + mid] < b[bStart + mid]) {
			aStart = aStart + (len / 2);
		} else {
			bStart = bStart + (len / 2);
		}

		smallSearch(mid + 1, a, b, aStart, bStart);
	}

	private static void largeSearch(int len, int a[], int b[], int aStart, int bStart) {
		int mid, kth = 0;

		if (len == 1) {
			kth = (a[aStart] > b[bStart] ? a[aStart] : b[bStart]);
			System.out.println(kth);
			return;
		}

		mid = (len % 2 == 0 ? len / 2 : (len / 2) + 1);

		if (a[aStart + len / 2] < b[bStart + len / 2]) {
			aStart = aStart + (len / 2);
		} else {
			bStart = bStart + (len / 2);
		}

		largeSearch(mid, a, b, aStart, bStart);
	}

	public static void findK(int value, int a[], int b[]) {

		int j = (a.length + b.length - value + 1);
		if (value <= a.length) {
			smallSearch(value, a, b, 0, 0);
		} else if (value > a.length) {
			largeSearch(j, a, b, a.length - j, b.length - j);
		}
	}

	public static void main(String[] args) {

		int a[], b[];
		a = new int[100];
		b = new int[100];

		for (int k = 0, i = 1; i < 200; i = i + 2, k++) {
			a[k] = i;
		}
		for (int k = 0, j = 2; j < 201; j = j + 2, k++) {
			b[k] = j;
		}

		for (int k = 0, i = 1; i < 200; i = i + 2, k++) {
			System.out.print(a[k] + ":");
			findK(i, a, b);
		}
		for (int k = 0, j = 2; j < 201; j = j + 2, k++) {
			System.out.print(b[k] + ":");
			findK(j, a, b);
		}

		int c[] = { 3, 5, 9, 15, 27, 33, 35, 41, 57, 65, 77, 78, 79, 80, 81, 83, 85, 87, 89, 90, 91, 94, 99, 105, 106,
				108, 109, 115, 122, 130 };
		int d[] = { 2, 16, 18, 42, 44, 46, 48, 50, 52, 54, 55, 56, 59, 60, 61, 62, 63, 64, 66, 67, 93, 95, 97, 110, 111,
				114, 117, 118, 121, 125 };
		int CuD = c.length + d.length;

		Scanner input = new Scanner(System.in);
		int value;
		do {
			System.out.print("Enter a number between 1 and " + CuD + " (or -1 to exit): ");
			value = input.nextInt();
			if (value == -1) {
				System.out.println("Good bye!");
			} else if (value <= 0 || value > CuD)
				System.out.println("The entered amount should be between 1 and " + CuD + "!");
			else
				findK(value, c, d);
		} while (value != -1);
		input.close();
	}

}
