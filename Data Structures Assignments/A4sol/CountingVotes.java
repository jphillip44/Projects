/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 4, Problem 3: Voting.java	 				   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A4sol;

import java.util.LinkedList;
import java.util.Map.*;
import java.util.TreeMap;

public class CountingVotes {

	public static int[] generateVotes(int k, int n) {
		int[] a = new int[n];
		int i = 0;
		double b = Math.random();

		for (i = 0; i < n; i++) {
			double c = Math.random() * k + b * (k << 23);
			a[i] = (int) c;
		}
		return a;
	}

	public static void sortVotes(int[] n) {
		TreeMap<Integer, Integer> a = new TreeMap<Integer, Integer>();
		for (int i = 0; i < n.length; i++) {
			Integer temp = a.get(n[i]);
			if (temp != null)
				a.put(n[i], temp + 1);
			else
				a.put(n[i], 1);
		}
		countVotes(a);
	}
	
	private static void countVotes(TreeMap<Integer, Integer> a){
		Entry<Integer, Integer> max = a.firstEntry();
		LinkedList<Integer> maxList = new LinkedList<Integer>();

		for (Entry<Integer, Integer> entry : a.entrySet()) {
			if (entry.getValue().compareTo(max.getValue()) > 0) {
				max = entry;
				while (maxList.size() != 0) {
					maxList.remove();
				}
				maxList.add(entry.getKey());
			} else if (entry.getValue().compareTo(max.getValue()) == 0) {
				maxList.add(entry.getKey());
			}

			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		findWinner(maxList, max);
	}
	
	private static void findWinner(LinkedList<Integer> maxList, Entry<Integer, Integer> max){
		if (maxList.size() > 1) {
			System.out.print("Winners are ");
			while (maxList.size() != 0) {
				if(maxList.size() > 2){
					System.out.print(maxList.remove() + ", ");
				}else if(maxList.size()==2){
					System.out.print(maxList.remove() + " ");
				}else{
					System.out.print("and " +maxList.remove());
				}
			}
		} else {
			System.out.print("Winner is " + maxList.remove());
		}
		System.out.println(" with " + max.getValue() + " votes!");
	}

	public static void main(String[] args) {
		sortVotes(generateVotes(10, 100));
	}
}
