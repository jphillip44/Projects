/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 3, Problem 1: CardShuffle.java			   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A3sol;

public class CardShuffle<E> {
	/*
	 * The arrays suit and num are not used in the shuffle pattern and thus do
	 * not violate the rule of using additional lists to perform the shuffle
	 * operation.
	 */
	private static char[] suit = { 'H', 'C', 'S', 'D' };
	private static char[] num = { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'X', 'J', 'Q', 'K' };

	private static Node mid;
	private static Node head;
	private static Node curr;
	private static int size;

	public static class Node {
		private String data;
		private Node next;

		/**
		 * A constructor to build a Node that can be connected to another node.
		 * 
		 * @param data
		 *            stores the data inside the node.
		 * @param next
		 *            points to the next node in the list.
		 */
		public Node(String data, Node next) {
			this.data = data;
			this.next = null;
		}

		/**
		 * Generate a string representation of the list.
		 */
		public String toString() {
			Node n = head.next;
			if (n.data == null) {
				return "[]";
			}
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			toString(n, sb);
			return sb.toString();
		}

		/**
		 * A private recursive helper method that helps generate the list
		 * output.
		 * 
		 * @param n
		 *            is the node being passed to the method.
		 * @param sb
		 *            is a StringBuilder that continues to build as the method
		 *            goes.
		 */
		private static void toString(Node n, StringBuilder sb) {
			if (n.next != null) {
				sb.append(n.data + ", ");
				toString(n.next, sb);
				return;
			}
			sb.append(n.data + "]");
			return;
		}
	}

	/**
	 * Loops halfway through the list size to find the middle of the list.
	 */
	private static void findMid() {
		mid = head;
		for (int i = 0; i < size / 2; i++) {
			mid = mid.next;
		}
	}

	/**
	 * A method that takes a list, splits it in half by index, and shuffles it
	 * by interleaving the Nodes. It works by finding the midpoint and then
	 * moving the pointer to right after the head. It continues to increment the
	 * mid pointer and inserts each successive second half node two positions
	 * after the previous insert.
	 * 
	 * @param head
	 *            is a pointer to the head of the list.
	 * @param tail
	 *            is a pointer to the end of the list.
	 */
	public static void shuffle(Node head, Node tail) {
		Node start = head;
		findMid();
		mid = mid.next;
		Node temp = mid;

		while (temp != tail) {
			temp = mid;
			mid = mid.next;
			temp.next = start.next;
			start.next = temp;
			start = temp.next;
		}
		tail.next = null;
	}

	/**
	 * A method that uses two arrays in a loop to generate an ordered list that
	 * represents a deck of cards.
	 */
	public static void makeList() {
		head = new Node(null, null);
		curr = head;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				curr.next = new Node("" + suit[i] + num[j], null);
				curr = curr.next;
				size++;
			}
		}
	}

	public static void main(String[] args) {
		makeList();
		System.out.println("Not Shuffled:  " + head);
		shuffle(head.next, curr);
		System.out.println("Shuffled Once: " + head);
		shuffle(head.next, curr);
		System.out.println("Shuffled Twice:" + head);
		shuffle(head.next, curr);
	}
}
