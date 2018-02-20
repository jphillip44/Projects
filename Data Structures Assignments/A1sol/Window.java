/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 1, Problem 3: Window.java			  		   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A1;

public class Window {
	protected double left, right, bottom, top;

	@SuppressWarnings("serial")
	public class InvalidWindowException extends Exception {
		/**
		 * An exception that alerts the user of an illegal Window size.
		 * 
		 * @param name
		 *            a string that allows for custom user text.
		 */
		public InvalidWindowException(String name) {
			super(name);
		}

		/**
		 * An exception that alerts the user of an illegal Window size.
		 */
		public InvalidWindowException() {
			super("Window size is illegal!");
		}
	}

	/**
	 * Construct a window with a left, right, bottom and top boundary.
	 * 
	 * @param left
	 *            is the leftmost boundary of the window.
	 * @param right
	 *            is the rightmost boundary of the window.
	 * @param bottom
	 *            is the lower boundary of the window.
	 * @param top
	 *            is the upper boundary of the window.
	 * @throws InvalidWindowException
	 *             when the boundaries don't make a proper window
	 */
	public Window(double left, double right, double bottom, double top) throws InvalidWindowException {
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;

		if (left >= right)
			throw new InvalidWindowException("Right must be greater than left!");
		if (bottom >= top)
			throw new InvalidWindowException("Top must be greater than bottom!");
	}

	/**
	 * @return the value of the window's leftmost boundary.
	 */
	public double getLeft() {
		return left;
	}

	/**
	 * @return the value of the window's rightmost boundary.
	 */
	public double getRight() {
		return right;
	}

	/**
	 * @return the value of the window's lower boundary.
	 */
	public double getBottom() {
		return bottom;
	}

	/**
	 * @return the value of the window's upper boundary.
	 */
	public double getTop() {
		return top;
	}

	/**
	 * Sets the left boundary of the window.
	 * 
	 * @param left
	 *            the name of window's leftmost boundary.
	 * @throws InvalidWindowException
	 *             if the leftmost boundary is to the right of the rightmost
	 *             boundary.
	 */
	public void setLeft(double left) throws InvalidWindowException {
		if (left >= right)
			throw new InvalidWindowException("Right must be greater than left!");
		this.left = left;
	}

	/**
	 * Sets the right boundary of the window.
	 * 
	 * @param right
	 *            the name of window's rightmost boundary.
	 * @throws InvalidWindowException
	 *             if the rightmost boundary is to the left of the leftmost
	 *             boundary.
	 */
	public void setRight(double right) throws InvalidWindowException {
		if (left >= right)
			throw new InvalidWindowException("Right must be greater than left!");
		this.right = right;
	}

	/**
	 * Sets the lower boundary of the window
	 * 
	 * @param bottom
	 *            the name of the window's lower boundary.
	 * @throws InvalidWindowException
	 *             if the lower boundary is above the upper boundary.
	 */
	public void setBottom(double bottom) throws InvalidWindowException {
		if (bottom >= top)
			throw new InvalidWindowException("Top must be greater than bottom!");
		this.bottom = bottom;
	}

	/**
	 * Sets the upper boundary of the window
	 * 
	 * @param top
	 *            the name of the window's upper boundary.
	 * @throws InvalidWindowException
	 *             if the upper boundary is below the lower boundary.
	 */
	public void setTop(double top) throws InvalidWindowException {
		if (bottom >= top)
			throw new InvalidWindowException("Top must be greater than bottom!");
		this.top = top;
	}

	/**
	 * Checks if this window encloses another window w. It checks if the left
	 * and bottom boundaries are a lesser value than w's boundaries. It checks
	 * if the right and top boundaries are a greater value than w's boundaries.
	 * 
	 * @param w
	 *            is the name of the window being compared to this window.
	 * @return true if all four checks are true and false if any single one is
	 *         not.
	 */
	public Boolean encloses(Window w) {
		return (this.getLeft() <= w.getLeft() && this.getRight() >= w.getRight() && this.getBottom() <= w.getBottom()
				&& this.getTop() >= w
						.getTop()); /*
									 * { return true; } else { return false; }
									 */
	}

	/**
	 * Checks if this window overlaps with another window w. It checks if the
	 * left and bottom boundaries are greater than or equal to w's right and top
	 * boundaries. It checks if the right and top boundaries are less than or
	 * equal to w's left and bottom boundaries.
	 * 
	 * @param w
	 *            is the name of the window being compared to this window.
	 * @return false if any of the four checks are true and true if none of them
	 *         are.
	 */
	public Boolean overlaps(Window w) {
		return !(this.getLeft() >= w.getRight() || this.getRight() <= w.getLeft() || this.getBottom() >= w.getTop()
				|| this.getTop() <= w.getBottom());/*
													 * { return false; } else {
													 * return true; }
													 */
	}

	/**
	 * Compares all window pairs, from an array, of type (i,j) and checks if
	 * they overlap.
	 * 
	 * @param windows
	 *            is the name of the array of Windows being passed to the
	 *            method.
	 * @return the number of window pairs that overlap.
	 * @throws InvalidWindowException
	 *             if the window w is of invalid specifications.
	 */
	public static int overlapCount(Window[] windows) throws InvalidWindowException {
		Window w;
		int count = 0;
		for (int i = 0; i < windows.length; i++) {
			w = new Window(windows[i].left, windows[i].right, windows[i].bottom, windows[i].top);
			for (int j = i + 1; j < windows.length; j++) {
				if (w.overlaps(windows[j])) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Compares all window pairs, from an array, of type (i,j) and (j,i) and
	 * checks if one encloses the other.
	 * 
	 * @param windows
	 *            is the name of the array of Windows being passed to the method
	 * @return the number of window pairs where one encloses the other.
	 * @throws InvalidWindowException
	 *             if the window w is of invalid specifications.
	 */
	public static int enclosureCount(Window[] windows) throws InvalidWindowException {
		Window w;
		int count = 0;
		for (int i = 0; i < windows.length; i++) {
			w = new Window(windows[i].left, windows[i].right, windows[i].bottom, windows[i].top);
			for (int j = 0; j < windows.length; j++) {
				if (i != j && w.encloses(windows[j])) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Takes a window array and runs both enclosureCount and overlapCount on
	 * them.
	 * 
	 * @param windows
	 *            is the name of the array of Windows being passed to the
	 *            method.
	 * @throws InvalidWindowException
	 *             if the window w is of invalid specifications.
	 */
	public static void test(Window[] windows) throws InvalidWindowException {
		System.out.println(overlapCount(windows) + " overlapping windows");
		System.out.println(enclosureCount(windows) + " enclosing windows");
	}

	/**
	 * @return A string representation of the Window class.
	 */
	@Override
	public String toString() {
		return "Window [left=" + getLeft() + ", right=" + getRight() + ", bottom=" + getBottom() + ", top=" + getTop()
				+ "]";
	}

	/**
	 * Main() method tests a bunch to different cases ensure that the program is
	 * operating correctly.
	 * 
	 * @throws InvalidWindowException
	 *             if the window w is of invalid specifications.
	 */
	public static void main(String[] args) throws InvalidWindowException {
		/*
		 * Test 1 is 4 adjacent windows touching. None of the windows overlap or
		 * enclose another so both should be 0.
		 */
		System.out.println(" Test 1\n ------");
		Window[] test1 = new Window[4];
		test1[0] = new Window(-2, 0, 0, 2);
		test1[1] = new Window(0, 2, 0, 2);
		test1[2] = new Window(-2, 0, -2, 0);
		test1[3] = new Window(0, 2, -2, 0);
		test(test1);

		/*
		 * Test 2 is the same as Test 1 but with an additional window in the
		 * middle. Middle window overlaps with all 4 outer windows. No overlap
		 * among outer windows. There is no window enclosed in this method.
		 */
		System.out.println("\n Test 2\n ------");
		Window[] test2 = new Window[5];
		test2[0] = new Window(-2, 0, 0, 2);
		test2[1] = new Window(0, 2, 0, 2);
		test2[2] = new Window(-2, 0, -2, 0);
		test2[3] = new Window(0, 2, -2, 0);
		test2[4] = new Window(-1, 1, -1, 1);
		test(test2);

		/*
		 * Test 3 is similar to Test 2 but the window now encloses the other
		 * windows. Same logic as two but the outer window now encloses all 4
		 * inner windows.
		 */
		System.out.println("\n Test 3\n ------");
		Window[] test3 = new Window[5];
		test3[0] = new Window(-2, 0, 0, 2);
		test3[1] = new Window(0, 2, 0, 2);
		test3[2] = new Window(-2, 0, -2, 0);
		test3[3] = new Window(0, 2, -2, 0);
		test3[4] = new Window(-3, 3, -3, 3);
		test(test3);

		/*
		 * Test 4 is 4 windows overlapping at the corner. All windows overlap
		 * with all other windows. This will give a Σ(n-1) relationship which
		 * means we have 6 overlapping windows. No windows are enclosed.
		 */
		System.out.println("\n Test 4\n ------");
		Window[] test4 = new Window[4];
		test4[0] = new Window(-1, 1, 0, 2);
		test4[1] = new Window(0, 2, 0, 2);
		test4[2] = new Window(-1, 1, -1, 1);
		test4[3] = new Window(0, 2, -1, 1);
		test(test4);

		/*
		 * Test 5 is similar to Test 4 but with an additional window in the
		 * middle. Σ(n-1) relationship so we have 10 overlapping windows. This
		 * time, all four outer windows enclose the inner window.
		 */
		System.out.println("\n Test 5\n ------");
		Window[] test5 = new Window[5];
		test5[0] = new Window(-1, 1, 0, 2);
		test5[1] = new Window(0, 2, 0, 2);
		test5[2] = new Window(-1, 1, -1, 1);
		test5[3] = new Window(0, 2, -1, 1);
		test5[4] = new Window(0, 1, 0, 1);
		test(test5);

		/*
		 * Test 6 is similar to Test 5 but with an additional window with the
		 * same boundaries as the 4 outside windows. Σ(n-1) relationship so we
		 * have 15 overlapping windows. New window encloses all 5 prior windows
		 * plus the existing 4 windows enclosed from Test 5.
		 */
		System.out.println("\n Test 6\n ------");
		Window[] test6 = new Window[6];
		test6[0] = new Window(-1, 1, 0, 2);
		test6[1] = new Window(0, 2, 0, 2);
		test6[2] = new Window(-1, 1, -1, 1);
		test6[3] = new Window(0, 2, -1, 1);
		test6[4] = new Window(0, 1, 0, 1);
		test6[5] = new Window(-1, 2, -1, 2);
		test(test6);

		/*
		 * Test 7 is similar to Test 6 but with the outer window outside all
		 * other windows. The Math for Test 7 is the same as 6.
		 */
		System.out.println("\n Test 7\n ------");
		Window[] test7 = new Window[6];
		test7[0] = new Window(-1, 1, 0, 2);
		test7[1] = new Window(0, 2, 0, 2);
		test7[2] = new Window(-1, 1, -1, 1);
		test7[3] = new Window(0, 2, -1, 1);
		test7[4] = new Window(0, 1, 0, 1);
		test7[5] = new Window(-2, 3, -2, 3);
		test(test7);

		/*
		 * Test 8 is a window inside an window inside a window. All 3 windows
		 * overlap. Outermost window encloses both the middle and inner window
		 * and the middle window incloses the inner window.
		 */
		System.out.println("\n Test 8\n ------");
		Window[] test8 = new Window[3];
		test8[0] = new Window(-1, 1, -1, 1);
		test8[1] = new Window(-2, 2, -2, 2);
		test8[2] = new Window(-3, 3, -3, 3);
		test(test8);

		/*
		 * Test 9 is Test 8 reversed, this is to show that enclose works in both
		 * directions. Math is the same as Test 8.
		 */

		System.out.println("\n Test 9\n ------");
		Window[] test9 = new Window[3];
		test9[2] = new Window(-1, 1, -1, 1);
		test9[1] = new Window(-2, 2, -2, 2);
		test9[0] = new Window(-3, 3, -3, 3);
		test(test9);

		/*
		 * Test 10 is 4 adjacent windows not touching. The math is the same as
		 * Test 1.
		 */

		System.out.println("\n Test 10\n -------");
		Window[] test10 = new Window[4];
		test10[0] = new Window(-3, -1, 1, 3);
		test10[1] = new Window(1, 3, 1, 3);
		test10[2] = new Window(-3, -1, -3, -1);
		test10[3] = new Window(1, 3, -3, -1);
		test(test10);

		/*
		 * Test 11 is the same as Test 10 but with an additional window in the
		 * middle. Math is the same as Test 2.
		 */

		System.out.println("\n Test 11\n -------");
		Window[] test11 = new Window[5];
		test11[0] = new Window(-3, -1, 1, 3);
		test11[1] = new Window(1, 3, 1, 3);
		test11[2] = new Window(-3, -1, -3, -1);
		test11[3] = new Window(1, 3, -3, -1);
		test11[4] = new Window(-2, 2, -2, 2);
		test(test11);

		/*
		 * Test 12 is similar to Test 11 but the additional window encloses all
		 * windows. The math is the same as Test 2.
		 */
		System.out.println("\n Test 12\n -------");
		Window[] test12 = new Window[5];
		test12[0] = new Window(-3, -1, 1, 3);
		test12[1] = new Window(1, 3, 1, 3);
		test12[2] = new Window(-3, -1, -3, -1);
		test12[3] = new Window(1, 3, -3, -1);
		test12[4] = new Window(-4, 4, -4, 4);
		test(test12);

		/*
		 * Test 13 is a window inside a window inside a window with a common
		 * corner boundary. The math is the same as Test 8.
		 */
		System.out.println("\n Test 13\n -------");
		Window[] test13 = new Window[3];
		test13[0] = new Window(0, 1, 0, 1);
		test13[1] = new Window(0, 2, 0, 2);
		test13[2] = new Window(0, 3, 0, 3);
		test(test13);

		/*
		 * Test 14 is two identical boxes. Both windows overlap. Both windows
		 * enclose each other. This is the only such case where both windows can
		 * do so.
		 */
		System.out.println("\n Test 14\n -------");
		Window[] test14 = new Window[2];
		test14[0] = new Window(-1, 1, -1, 1);
		test14[1] = new Window(-1, 1, -1, 1);
		test(test14);

		/*
		 * Test 15 is two boxes that overlap that only have a single common
		 * boundary(all other test had at least 2). In this case, both windows
		 * overlap but neither enclose the other.
		 */
		System.out.println("\n Test 15\n -------");
		Window[] test15 = new Window[2];
		test15[0] = new Window(-1, 1, -1, 2);
		test15[1] = new Window(0, 2, 0, 1);
		test(test15);

		/*
		 * Test 16 is three identical boxes. All three boxes overlap. All the
		 * windows enclose each other so we should see 6 overlaps.
		 */
		System.out.println("\n Test 16\n -------");
		Window[] test16 = new Window[3];
		test16[0] = new Window(-1, 1, -1, 1);
		test16[1] = new Window(-1, 1, -1, 1);
		test16[2] = new Window(-1, 1, -1, 1);
		test(test16);

		/*
		 * Here I am going to catch all the possible places the program can
		 * throw an exception.
		 */
		System.out.println("\n Exception tests \n ---------------");
		try {
			new Window(0, 0, 0, 0);
		} catch (InvalidWindowException i) {
			System.out.println("Exception test 1: " + i);
		}

		try {
			new Window(0, 1, 0, 0);
		} catch (InvalidWindowException i) {
			System.out.println("Exception test 2: " + i);
		}

		Window e = new Window(-1, 1, -1, 1);

		try {
			e.setLeft(1);
		} catch (InvalidWindowException i) {
			System.out.println("Exception test 3: " + i);
		}

		try {
			e.setRight(-1);
		} catch (InvalidWindowException i) {
			System.out.println("Exception test 4: " + i);
		}

		try {
			e.setBottom(1);
		} catch (InvalidWindowException i) {
			System.out.println("Exception test 5: " + i);
		}
		try {
			e.setTop(-1);
		} catch (InvalidWindowException i) {
			System.out.println("Exception test 6: " + i);
		}

		/*
		 * Here we can see the method was not changed but we still want to show
		 * that the set methods work.
		 */
		System.out.println(e);
		e.setLeft(-2);
		e.setRight(2);
		e.setBottom(-2);
		e.setTop(2);
		System.out.println(e);
	}
}
