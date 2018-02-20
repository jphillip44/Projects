/***********************************************************
 * EECS2011: Fundamentals of Data Structures,  Fall 2016   * 
 * Assignment 4, Problem 2: CountRange.java				   *
 * Student Name:   Joshua, Phillip						   *
 * Student cse account:  jep4444						   *
 * Student ID number:  207961907					 	   *
 ***********************************************************/

package A4sol;

public class CountRange<K extends Comparable<? super K>, V> {
	private Node<K, V> root;

	public static class Entry<K, V> {
		private K key;
		private V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public void setKey(K key) {
			this.key = key;
		}
	}

	private static class Node<K, V> {
		private Entry<K, V> entry;
		private Node<K, V> left;
		private Node<K, V> right;
		private int size;

		public Node(Entry<K, V> entry, Node<K, V> left, Node<K, V> right, int size) {
			this.entry = entry;
			this.left = left;
			this.right = right;
			this.size = size;
		}

		public Node<K, V> getLeft() {
			return left;
		}

		public void setLeft(Node<K, V> left) {
			this.left = left;
		}

		public Node<K, V> getRight() {
			return right;
		}

		public void setRight(Node<K, V> right) {
			this.right = right;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public Entry<K, V> getEntry() {
			return entry;
		}
	}

	public CountRange() {
		root = null;
	}

	public void add(K key, V value) {
		root = addNode(root, new Entry<K, V>(key, value));
	}

	private Node<K, V> addNode(Node<K, V> root, Entry<K, V> entry) {
		if (root == null) {
			return new Node<K, V>(entry, null, null, 1);
		} else if (root.getEntry().getKey().compareTo(entry.getKey()) > 0) {
			root.setLeft(addNode(root.getLeft(), entry));
		} else if (root.getEntry().getKey().compareTo(entry.getKey()) < 0) {
			root.setRight(addNode(root.getRight(), entry));
		} 
		addSize(root);
		return root;
	}

	private Node<K, V> findLargest(Node<K, V> root) {
		subtractSize(root);
		if (root.getRight() == null) {
			return root;
		} else {
			return findLargest(root.getRight());
		}
	}

	private Node<K, V> removeLargest(Node<K, V> root) {
		if (root.getRight() == null) {
			return root.getLeft();
		} else {
			root.setRight(removeLargest(root.getRight()));
			return root;
		}
	}

	public void remove(K key) {
		root = removeNode(root, key);
	}

	private Node<K, V> removeNode(Node<K, V> root, K key) {
		if (getSize(key) == -1) {
			return root;
		}
		subtractSize(root);
		if (root.getEntry().getKey().compareTo(key) == 0) {
			if (root.getLeft() == null) {
				return root.getRight();
			} else if (root.getRight() == null) {
				return root.getLeft();
			} else {
				Node<K,V> temp = findLargest(root.getLeft());
				root.getEntry().setKey(temp.getEntry().getKey());
				root.getEntry().setValue(temp.getEntry().getValue());
				root.setLeft(removeLargest(root.getLeft()));
			}
		} else if (root.getEntry().getKey().compareTo(key) > 0) {
			root.setLeft(removeNode(root.getLeft(), key));
		} else {
			root.setRight(removeNode(root.getRight(), key));
		}
		return root;
	}

	public V getValue(K key) {
		return getValue(root, key);
	}

	private V getValue(Node<K, V> root, K key) {
		if (root == null) {
			return null;
		} else if (root.getEntry().getKey().compareTo(key) == 0) {
			return root.getEntry().getValue();
		} else if (root.getEntry().getKey().compareTo(key) > 0) {
			return getValue(root.getLeft(), key);
		} else {
			return getValue(root.getRight(), key);
		}
	}

	public void setValue(K key, V value) {
		setValue(root, key, value);
	}

	private void setValue(Node<K, V> root, K key, V value) {
		if (root == null) {
			return;
		} else if (root.getEntry().getKey().compareTo(key) == 0) {
			root.getEntry().setValue(value);
		} else if (root.getEntry().getKey().compareTo(key) > 0) {
			setValue(root.getLeft(), key, value);
		} else {
			setValue(root.getRight(), key, value);
		}
	}

	public int getSize(K key) {
		return getSize(root, key);
	}

	private int getSize(Node<K, V> root, K key) {
		if (root == null) {
			return -1;
		} else if (root.getEntry().getKey().compareTo(key) == 0) {
			return root.getSize();
		} else if (root.getEntry().getKey().compareTo(key) > 0) {
			return getSize(root.getLeft(), key);
		} else {
			return getSize(root.getRight(), key);
		}
	}

	private void addSize(Node<K, V> root) {
		root.setSize(root.getSize() + 1);
	}

	private void subtractSize(Node<K, V> root) {
		root.setSize(root.getSize() - 1);
	}

	public int countRange(K start, K end) {
		if (start.compareTo(end) > 0) {
			return -1;
		}
		Node<K, V> subRoot = subRoot(root, start, end);
		int size = 0;
		if (start != subRoot.getEntry().getKey()) {
			size += countLeft(subRoot.getLeft(), start, 0);
		}
		if (end != subRoot.getEntry().getKey()) {
			size += countRight(subRoot.getRight(), end, 0);
		}
		return ++size;
	}

	private int countLeft(Node<K, V> root, K start, int size) {
		if (root == null) {
			return size;
		}
		if (root.getEntry().getKey().compareTo(start) > 0) {
			size += (root.getRight() != null ? root.getRight().getSize() + 1 : 1);
			return countLeft(root.getLeft(), start, size);
		} else if (root.getEntry().getKey().compareTo(start) < 0) {
			return countLeft(root.getRight(), start, size);
		} else {
			size += (root.getRight() != null ? root.getRight().getSize() + 1 : 1);
			return size;
		}

	}

	private int countRight(Node<K, V> root, K end, int size) {
		if (root == null) {
			return size;
		}
		if (root.getEntry().getKey().compareTo(end) < 0) {
			size += (root.getLeft() != null ? root.getLeft().getSize() + 1 : 1);
			return countRight(root.getRight(), end, size);
		} else if (root.getEntry().getKey().compareTo(end) > 0) {
			return countRight(root.getLeft(), end, size);
		} else {
			size += (root.getLeft() != null ? root.getLeft().getSize() + 1 : 1);
			return size;
		}
	}

	private Node<K, V> subRoot(Node<K, V> root, K start, K end) {
		if (root.getEntry().getKey().compareTo(start) < 0) {
			return subRoot(root.getRight(), start, end);
		} else if (root.getEntry().getKey().compareTo(end) > 0) {
			return subRoot(root.getLeft(), start, end);
		} else {
			return root;
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		infixPrint(root, sb);
		return sb.toString().trim();
	}

	private void infixPrint(Node<K, V> root, StringBuffer sb) {
		if (root != null) {
			infixPrint(root.left, sb);
			sb.append("(" + root.getEntry().getKey() + "," + root.getEntry().getValue() + ")");
			infixPrint(root.right, sb);
		}
	}

	public static void main(String[] args) {
		CountRange<Integer, Integer> test = new CountRange<Integer, Integer>();

		test.add(8, 8);
		test.add(4, 4);
		test.add(2, 2);
		test.add(3, 3);
		test.add(1, 1);
		test.add(6, 6);
		test.add(5, 5);
		test.add(7, 7);
		test.add(12, 12);
		test.add(10, 10);
		test.add(9, 9);
		test.add(11, 11);
		test.add(14, 14);
		test.add(13, 13);
		test.add(15, 15);

		System.out.println(test);	
		for(int i = 1; i < 16; i++)
			System.out.println(i+": "+test.countRange(i, 15));
		
		for(int i = 1; i < 16; i++)
			System.out.println(i+": "+test.countRange(1, i));
		
		for(int i = 1, j = 15; i <= j; i++, j--)
			System.out.println(i+": "+test.countRange(i, j));
		
		System.out.println("---");
		
		System.out.println(test.countRange(1, 15));
		test.remove(12);
		System.out.println(test);
		System.out.println(test.countRange(1, 15));
		test.remove(4);
		System.out.println(test);
		System.out.println(test.countRange(1, 15));
		test.remove(6);
		System.out.println(test);
		System.out.println(test.countRange(1, 15));
		test.remove(6);
		System.out.println(test);
		System.out.println(test.countRange(1, 15));

		System.out.println("---");
		
		for(int i = 1; i < 16; i++)
			System.out.println(i+": "+test.countRange(i, 15));
		
		for(int i = 1; i < 16; i++)
			System.out.println(i+": "+test.countRange(1, i));
		
		for(int i = 1, j = 15; i <= j; i++, j--)
			System.out.println(i+": "+test.countRange(i, j));
		

	}

}
