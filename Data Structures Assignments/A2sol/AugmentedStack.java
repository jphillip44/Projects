/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 2, Problem 3: AugmentedStack.java			   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A2sol;

import java.util.LinkedList;

public class AugmentedStack <E extends Comparable<? super E>>{
private LinkedList<E> stack;
	private LinkedList<E> minStack;
	private int size;
	private int minSize;

	/**
	 * 
	 */
	public AugmentedStack() {
		this.minStack = new LinkedList<E>();
		this.stack = new LinkedList<E>();
		this.size = 0;
		this.minSize = 0;
	}

	/**
	 * Checks if the size of minSize is 0 or if the new element is smaller or
	 * equal to the previous minimum element. If so it adds the element to
	 * minStack and increments minSize. It also adds the new element to stack
	 * and increments stack.
	 * 
	 * @param e
	 *            is the element being added to the top of the stack.
	 */
	public void push(E e) {
		if (minSize == 0 || (minStack.getFirst()).compareTo(e) >= 0) {
			minStack.addFirst(e);
			minSize++;
		}
		stack.addFirst(e);
		size++;
	}

	/**
	 * Checks if the stack is empty. If so, returns null. Otherwise it checks if
	 * the element being removed from the stack is the minimum value, if so it
	 * removes the element from minStack and decreases minSize. It then
	 * decreases size and removes the element from stack.
	 * 
	 * @return the element being removed from the stack.
	 */
	public E pop() {
		if (isEmpty()){
			return null;
		}
		if ((minStack.getFirst()).compareTo(stack.getFirst()) == 0) {
			minStack.removeFirst();
			minSize--;
		}
		size--;
		return stack.removeFirst();
	}

	/**
	 * Checks if the stack is empty. If so, returns null. Otherwise, it returns
	 * the element at the top of minStack which is also the smallest element in
	 * stack.
	 * 
	 * @return the smallest value in stack.
	 */
	public E getMin() {
		if (isEmpty()){
			return null;
		}
		return minStack.getFirst();
	}

	/**
	 * Returns the boolean value of whether the size is 0.
	 * 
	 * @return true if size is 0 and false otherwise.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Checks if the stack is empty. If so, returns null. Otherwise it returns
	 * the element at the top of the stack.
	 * 
	 * @return the element at the top of the stack.
	 */
	public E top() {
		if (isEmpty()) {
			return null;
		}
		return stack.getFirst();
	}

	public static void main(String[] args) {
		AugmentedStack<Double> AS = new AugmentedStack<Double>();
		AugmentedStack<String> AS1 = new AugmentedStack<String>();

		System.out.println(AS.isEmpty());					//true
		System.out.println(AS1.isEmpty());					//true
		System.out.println(AS.pop());						//null
		System.out.println(AS1.pop());						//null
		System.out.println(AS.top());						//null
		System.out.println(AS1.top());						//null
		System.out.println(AS.getMin());					//null
		System.out.println(AS1.getMin());					//null

		AS.push(4.0);
		AS.push(3.5);
		AS.push(3.6);
		AS.push(-1.0);
		AS.push(-1.0);
		AS1.push("ahello");
		AS1.push("aWorld");
		AS1.push("afoo");
		AS1.push("aBar");

		System.out.println(AS.getMin());					//-1.0
		System.out.println(AS1.getMin());					//aBar
		System.out.println(AS.isEmpty());					//false
		System.out.println(AS1.isEmpty());					//false
		System.out.println(AS.pop());						//-1.0
		System.out.println(AS.getMin());					//-1.0
		System.out.println(AS.pop());						//-1.0
		System.out.println(AS1.pop());						//aBar
		System.out.println(AS.top());						//3.6
		System.out.println(AS1.top());						//afoo
		System.out.println(AS.getMin());					//3.5
		System.out.println(AS1.getMin());					//aWord

	}
}
