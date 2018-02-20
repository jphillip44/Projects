/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 3, Problem 3: PrioritySearchTree.java		   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A3sol;

public class PrioritySearchTree {
	private static class Node {
		private Data data;
		private Node left;
		private Node right;
		private Node parent;

		/**
		 * A constructor that builds a node that can be connected to other nodes
		 * at either the left or right side. This version accepts it's data as
		 * the object itself which allows for null to be included for data when
		 * the node is made.
		 * 
		 * @param data
		 *            is an object that stores the information of the node.
		 * @param parent
		 *            points to the node above the current node.
		 * @param left
		 *            points to the node to the left of the current node.
		 * @param right
		 *            points to the node to the right of the current node.
		 */
		private Node(Data data, Node parent, Node left, Node right) {
			if (data != null)
				this.data = new Data(data.x, data.y);
			this.parent = parent;
			this.left = left;
			this.right = right;
		}

		/**
		 * A constructor that builds a node that can be connected to other nodes
		 * at either the left or right side. This version accepts data points x
		 * and y which are passed to the data object. This version saves the
		 * object from generating a duplicate Node.
		 * 
		 * @param x
		 *            is the x information to be stored in data.
		 * @param y
		 *            is the y information to be stored in data.
		 * @param parent
		 *            points to the node above the current node.
		 * @param left
		 *            points to the node to the left of the current node.
		 * @param right
		 *            points to the node to the right of the current node.
		 */
		private Node(int x, int y, Node parent, Node left, Node right) {
			this.data = new Data(x, y);
			this.parent = parent;
			this.left = left;
			this.right = right;
		}

		/**
		 * Is a string representation of any node.
		 */
		public String toString() {
			return "(" + data + ")";
		}
	}

	private static class Data {
		public int x;
		public int y;

		/**
		 * Is a constructor to store the data points of the Node.
		 * 
		 * @param x
		 *            represents the x value of the node.
		 * @param y
		 *            represents the y value of the node.
		 */
		private Data(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Is a string representation of any data node. In the event of data
		 * being null, this method doesn't do anything as it already would
		 * return null as the value on it's own.
		 **/
		public String toString() {
			return x + "," + y;
		}
	}

	/**
	 * Does a recursive traversal of the tree and passes any node that has
	 * children to findChild();
	 * 
	 * @param n
	 *            the node being passed to the method.
	 * @return the node information retrieved by prioritySearch().
	 */
	public static Node prioritySearch(Node n) {

		if (n.left == null && n.right == null) {
			return n;
		}

		prioritySearch(n.left);
		prioritySearch(n.right);

		return findChild(n);
	}

	/**
	 * A recursive helper method that checks both the left and right nodes of
	 * the tree to determine the greater value. It also looks up the tree using
	 * checkParent() to make sure the selected value is not a duplicate in the
	 * tree. Whenever a node data is copied up the tree, it goes back to the
	 * previous node and finds a new promotion for that node. If it can't find
	 * one that does not exist higher up the tree, it will set the data of that
	 * node to null. This program does no work on the external nodes of the tree
	 * so they will always preserve their original data values.
	 * 
	 * @param n
	 *            the node being passed to the method.
	 * @return the node information retrieved by findChild();
	 */
	private static Node findChild(Node n) {

		if (n.left == null && n.right == null) {
			return n;
		}
		if ((n.right.data == null || n.left.data != null && (n.left.data.y >= n.right.data.y || !checkParent(n.right)))
				&& checkParent(n.left)) {
			n.data = n.left.data;
			findChild(n.left);
			return n;
		}
		if ((n.left.data == null || n.left.data.y < n.right.data.y || !checkParent(n.left)) && checkParent(n.right)) {
			n.data = n.right.data;
			findChild(n.right);
			return n;
		}

		n.data = null;
		return n;
	}

	/**
	 * A helper method that checks the value of the parent node to ensure that
	 * the top(n) node being promoted is of lesser value than the top(n) of the
	 * parent tree. This method takes advantage of the fact that coordinates are
	 * distinct and that nodes higher up the tree must be of greater value than
	 * it's immediate parent node. This method returns false if it finds a
	 * duplicate because we are specifically looking to avoid duplicates and
	 * simplifies the logic in findChild().
	 * 
	 * @param n
	 *            the node being passed to the method.
	 * @return if the node is free of any duplicates higher in the tree.
	 */
	private static boolean checkParent(Node n) {

		if (n.parent.data == null)
			return true;
		if (n.parent.data.y <= n.data.y)
			return false;
		return true;
	}

	/**
	 * Does a pre-order traversal of the tree recursively.
	 * 
	 * @param root
	 *            the node being passed to the tree.
	 */
	public static void prePrint(Node root) {
		System.out.print(root);
		if (root.left != null)
			prePrint(root.left);
		if (root.right != null)
			prePrint(root.right);
	}

	/**
	 * Does a in-order traversal of the tree recursively.
	 * 
	 * @param root
	 *            the node being passed to the tree.
	 */
	public static void inPrint(Node root) {
		if (root.left != null)
			inPrint(root.left);
		System.out.print(root);
		if (root.right != null)
			inPrint(root.right);
	}

	/**
	 * Does a post-order traversal of the tree recursively.
	 * 
	 * @param root
	 *            the node being passed to the tree.
	 */
	public static void postPrint(Node root) {
		if (root.left != null)
			postPrint(root.left);
		if (root.right != null)
			postPrint(root.right);
		System.out.print(root);
	}

	public static void main(String[] args) {
		Node root = new Node(null, null, null, null);
		Node root1 = new Node(null, root, null, null);
		root1.left = new Node(null, root1, null, null);
		root1.right = new Node(null, root1, null, null);
		root1.left.left = new Node(null, root1.left, null, null);
		root1.left.right = new Node(null, root1.left, null, null);
		root1.right.left = new Node(null, root1.right, null, null);
		root1.right.right = new Node(null, root1.right, null, null);
		root1.left.left.left = new Node(1, 1, root1.left.left, null, null);
		root1.left.left.right = new Node(2, 2, root1.left.left, null, null);
		root1.left.right.left = new Node(3, 3, root1.left.right, null, null);
		root1.left.right.right = new Node(4, 4, root1.left.right, null, null);
		root1.right.left.left = new Node(5, 5, root1.right.left, null, null);
		root1.right.left.right = new Node(6, 6, root1.right.left, null, null);
		root1.right.right.left = new Node(7, 7, root1.right.right, null, null);
		root1.right.right.right = new Node(8, 8, root1.right.right, null, null);

		Node root2 = new Node(null, root, null, null);
		root2.left = new Node(null, root2, null, null);
		root2.right = new Node(null, root2, null, null);
		root2.left.left = new Node(null, root2.left, null, null);
		root2.left.right = new Node(null, root2.left, null, null);
		root2.right.left = new Node(null, root2.right, null, null);
		root2.right.right = new Node(null, root2.right, null, null);
		root2.left.left.left = new Node(-8, 3, root2.left.left, null, null);
		root2.left.left.right = new Node(-7, 1, root2.left.left, null, null);
		root2.left.right.left = new Node(-6, 6, root2.left.right, null, null);
		root2.left.right.right = new Node(5, 4, root2.left.right, null, null);
		root2.right.left.left = new Node(-4, 6, root2.right.left, null, null);
		root2.right.left.right = new Node(-3, 9, root2.right.left, null, null);
		root2.right.right.left = new Node(-2, 1, root2.right.right, null, null);
		root2.right.right.right = new Node(-1, 7, root2.right.right, null, null);

		Node root3 = new Node(null, null, null, null);
		root3.left = new Node(null, root3, null, null);
		root3.right = new Node(null, root3, null, null);
		root3.left.left = new Node(null, root3.left, null, null);
		root3.left.right = new Node(null, root3.left, null, null);
		root3.right.left = new Node(null, root3.right, null, null);
		root3.right.right = new Node(null, root3.right, null, null);
		root3.left.left.left = new Node(null, root3.left.left, null, null);
		root3.left.left.right = new Node(null, root3.left.left, null, null);
		root3.left.right.left = new Node(null, root3.left.right, null, null);
		root3.left.right.right = new Node(null, root3.left.right, null, null);
		root3.right.left.left = new Node(null, root3.right.left, null, null);
		root3.right.left.right = new Node(null, root3.right.left, null, null);
		root3.right.right.left = new Node(null, root3.right.right, null, null);
		root3.right.right.right = new Node(null, root3.right.right, null, null);
		root3.left.left.left.left = new Node(-8, 8, root3.left.left.left, null, null);
		root3.left.left.left.right = new Node(-7, 7, root3.left.left.left, null, null);
		root3.left.left.right.left = new Node(-6, 6, root3.left.left.right, null, null);
		root3.left.left.right.right = new Node(-5, 5, root3.left.left.right, null, null);
		root3.left.right.left.left = new Node(-4, 4, root3.left.right.left, null, null);
		root3.left.right.left.right = new Node(-3, 3, root3.left.right.left, null, null);
		root3.left.right.right.left = new Node(-2, 2, root3.left.right.right, null, null);
		root3.left.right.right.right = new Node(-1, 1, root3.left.right.right, null, null);
		root3.right.left.left.left = new Node(1, -1, root3.right.left.left, null, null);
		root3.right.left.left.right = new Node(2, -2, root3.right.left.left, null, null);
		root3.right.left.right.left = new Node(3, -3, root3.right.left.right, null, null);
		root3.right.left.right.right = new Node(4, -4, root3.right.left.right, null, null);
		root3.right.right.left.left = new Node(5, -5, root3.right.right.left, null, null);
		root3.right.right.left.right = new Node(6, -6, root3.right.right.left, null, null);
		root3.right.right.right.left = new Node(7, -7, root3.right.right.right, null, null);
		root3.right.right.right.right = new Node(8, -8, root3.right.right.right, null, null);

		postPrint(root1);
		System.out.println("");
		prioritySearch(root1);
		postPrint(root1);
		System.out.println("\n");

		postPrint(root2);
		System.out.println("");
		prioritySearch(root2);
		postPrint(root2);
		System.out.println("\n");

		postPrint(root3);
		System.out.println("");
		prioritySearch(root3);
		postPrint(root3);
		System.out.println("");

	}
}
