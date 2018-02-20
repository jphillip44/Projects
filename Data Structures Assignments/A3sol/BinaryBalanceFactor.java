/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 3, Problem 2: BinaryBalanceFactor.java	   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A3sol;

public class BinaryBalanceFactor {

	public static class Node {
		private int data;
		private Node left;
		private Node right;

		/**
		 * A constructor that builds a node that can be connected to other nodes
		 * at either the left or right side.
		 * 
		 * @param data
		 *            stores the data inside the node.
		 * @param left
		 *            points to the node to the left of the current node.
		 * @param right
		 *            points to the node to the right of the current node.
		 */
		public Node(int data, Node left, Node right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}

	}

	/**
	 * This method generates the balance factor by doing a full tree traversal
	 * checking the height of its left and right nodes and using the post order
	 * property to pass the greater value to the parent node + 1. The parent
	 * node can use this information from both it's child nodes to calculate the
	 * balance factor(by subtracting right from left). It does this all the way
	 * up the tree so no node ever has to calculate the height with any
	 * information other than what is returned by the children nodes. When we
	 * print out the nodes, a number greater than 0 is an unbalanced tree while
	 * 0 is balanced.
	 * 
	 * @param n
	 *            the node being checked for it's balance factor.
	 * @return the balance factor at a given node.
	 */
	public static int balanceFactor(Node n) {
		int leftHeight, rightHeight;

		if (n == null) {
			return 0;
		} else if (n.left == null && n.right == null) {
			System.out.print("(" + n.data + ":" + 0 + ")");
			return 1;
		}

		leftHeight = balanceFactor(n.left);
		rightHeight = balanceFactor(n.right);

		int height = (leftHeight > rightHeight ? leftHeight - rightHeight : rightHeight - leftHeight);
		System.out.print("(" + n.data + ":" + height + ")");
		return (leftHeight > rightHeight ? leftHeight : rightHeight) + 1;
	}

	public static void main(String[] args) {
		Node root = new Node(0, null, null);
		root.left = new Node(1, null, null);
		root.right = new Node(2, null, null);
		root.left.left = new Node(3, null, null);
		root.left.right = new Node(4, null, null);
		balanceFactor(root);
		System.out.println("");
		root.right.left = new Node(5, null, null);
		root.right.right = new Node(6, null, null);
		balanceFactor(root);
		System.out.println("");
		root.left.left.right = new Node(8, null, null);
		root.left.left.right.left = new Node(17, null, null);
		root.left.left.right.right = new Node(18, null, null);
		balanceFactor(root);
		System.out.println("");
		root.right.right = null;
		root.right.left = null;
		balanceFactor(root);
		System.out.println("");
		root.right = null;
		balanceFactor(root);
		System.out.println("");
		root.left.left.right.right.left = new Node(37, null, null);
		balanceFactor(root);
	}
}
