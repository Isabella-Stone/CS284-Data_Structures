//Isabella Stone
//I pledge my honor that I have abided by the Stevens Honor System.

package treap;

import java.util.Random;
import java.util.Stack;

/**
 * This class implements a 'treap' that is organized by BST 
 * and heap properties
 * @author Isabella Stone
 * @param <E>
 */
public class Treap<E extends Comparable<E>>{
	
	/**
	 * This class creates notes to fill the treap with
	 * @author Isabella Stone
	 * @param <E>
	 */
	private static class Node<E> {
		public E data; //key for the search
		public int priority; //random heap priority
		public Node<E> left;
		public Node<E> right;
		
		/**
		 * Creates the node object
		 * @param data data represents the key
		 * @param priority is the heap priority
		 */
		public Node(E data, int priority) {
			if (data ==null) {
				throw new IllegalArgumentException("cannot have null data");
			}
			this.data = data;
			this.priority = priority;
			this.left = null;
			this.right = null;
		}
		
		/**
		 * Performs a right tree rotation
		 * @return a reference to the root of the result
		 */
		Node<E> rotateRight() {
		    Node<E> greater = this.left;
	        this.left = greater.right;
	        greater.right = this;
			return greater;
		}
		
		/**
		 * Performs a left tree rotation
		 * @return a reference to the root of the result
		 */
		Node<E> rotateLeft() {
			Node<E> greater = this.right;
	        this.right = greater.left;
	        greater.left = this;
	        return greater;
		}
		
		/**
		 * Creates a string for node objects
		 */
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append("key=" + this.data + ", priority=" + this.priority);
			return s.toString();
		}
	}
	
	//treap
	private Random priorityGenerator;
	private Node<E> root;
	
	/**
	 * Creates a treap object
	 */
	public Treap() {
		this.root = null;
		this.priorityGenerator = new Random();
	}
	
	/**
	 * Creates a treap object
	 * @param seed helps generate a random priority
	 */
	public Treap(long seed) {
		this.root = null; 
		this.priorityGenerator = new Random(seed);
	}
	
	/**
	 * Calls add(E key, int priority) to add a new node to the treap
	 * @param key is used to make a BST insertion
	 * @return
	 */
	boolean add(E key) {
		if (this.find(this.root, key)) {
			return false;
		}
		int priority = priorityGenerator.nextInt();
		add(key, priority);
		return true;
	}
	
	/**
	 * Adds a new node to the treap
	 * @param key is used to make a BST insertion
	 * @param priority is used to reheap
	 * @return
	 */
	boolean add(E key, int priority) {
		//first insert based on key(letters) like a BST
		//initialize new node to insert, and stack
		Node<E> x = new Node<E>(key, priority);
		Stack<Node<E>> stack = new Stack<>();
		
		Node<E> current = root;
		if (root == null) {
			//if inserting into empty tree
			this.root = x;
			stack.push(root); //put root in stack 
			return true;
		}
		
		stack.push(current); 
		//find where to insert new node (x)
		while (!(current.right == null && current.left == null)) {
			//loop through while not at a leaf
			int i = key.compareTo(current.data);
			
			if (i < 0) {
				//needs to go to left
				if (current.left == null) {
					break;
				}
				else {
					current = current.left; 
				}
			}
			else {
				//needs to go right
				if (current.right == null) {
					break;
				}
				else {
					current = current.right;
				}
			}
			//put in stack
			stack.push(current);
		}
		
		//insert new node (x) :
		int i = key.compareTo(current.data);
		if (i < 0) {
			//needs to go to left
			current.left = x;
		}
		else {
			//needs to go right
			current.right = x;
		}
		
		//System.out.println(x.data + "; " + stack);
		this.reheap(stack, x);
		return true;
	}
	
	/**
	 * Reheaps the treap using priorities
	 * @param stack has each node in the path from the root until the spot where the new node was inserted
	 * @param node is the new node that was added 
	 */
	public void reheap(Stack<Node<E>> stack, Node<E> node) {
		//then use rotations to fix heap with priorities
		Node<E> grand;
		Node<E> parent = stack.peek(); 
		Node<E> child = node;
		//System.out.println("child: " + child + "; parent: " + parent + "\n");
		
		while (!stack.isEmpty()){
			parent = stack.peek();
			if (child.priority > parent.priority) {
				//if priority of child is greater--> need to rotate
				if (parent.left == child) {
					//if the child is the left child of the current, rotate right
					child = parent.rotateRight();
					if (root == parent) {
						root = child;
					}
				}
				else {
					//if the child is the right child
					child = parent.rotateLeft();
					if (root == parent) {
						root = child;
					}
				}
				
				//fix grandparent - reset grandparent left/right
				stack.pop(); //pop parent off to get to grandparent
				if (!stack.isEmpty()) {
					grand = stack.peek();
					if (grand.left == parent) {
						grand.left = child;
					}
					else {
						grand.right = child;	
					}
					parent = grand;
				}
			}
			else {
				break;
			}
		}
	}
	
	/**
	 * Deletes node with given key from the treap
	 * @param key of node to delete
	 * @return true if node with given key was deleted, false if node not found
	 */
	
	boolean delete(E key) {
		//then from node start swapping down till leave and undo pointers to it (to null)
		if (!this.find(key)) {
			return false;
		}
		
		//find node that needs to be deleted
		Node<E> del = this.getNode(this.root, key);
		
		//if deleting root need to make one rotation before finding parent
		if (this.root.data == key) {
			if (root.right == null) {
				root = root.rotateRight();
			}
			else if (root.left == null) {
				root = root.rotateLeft();
			}
			else {
				//if has 2 children
				if (root.right.priority > root.left.priority) {
					root = root.rotateLeft();
				}
				else {
					root = root.rotateRight();
				}
			}
			
		}
		
		Node<E> parent = this.getParent(this.root, key);
		//System.out.println(parent + "; " + del);
		
		while (true) {
			if (del.right == null && del.left == null) {
				break;
			}
			//System.out.println("while");
			//while not a leaf
			if (del.right == null) {
				//if has only a left child
				if (parent.right == del) {
					//System.out.println("1");
					parent.right = del.rotateRight();
					parent = parent.right;
				}
				else {
					//else parent.left == del
					//System.out.println("2");
					parent.left = del.rotateRight();
					parent = parent.left;
				}
			}
			else if (del.left == null) {
				//if has only a right child
				if (parent.right == del) {
					//System.out.println("3");
					parent.right = del.rotateLeft();
					parent = parent.right;
				}
				else {
					//else parent.left == del
					//System.out.println("4");
					parent.left = del.rotateLeft();
					parent = parent.left;
				}
				
			}
			else {
				//it has 2 children
				if (del.right.priority > del.left.priority) {
					if (parent.right == del) {
						//System.out.println("5");
						parent.right = del.rotateLeft();
						parent = parent.right;
					}
					else {
						//else parent.left == del
						//System.out.println("6");
						parent.left = del.rotateLeft();
						parent = parent.left;
					}
				}
				else {
					//if left priority is greater than right
					if (parent.right == del) {
						//System.out.println("7");
						parent.right = del.rotateRight();
						parent = parent.right;
					}
					else {
						//else parent.left == del
						//System.out.println("8");
						parent.left = del.rotateRight();
						parent = parent.left;
					}
				}
			}
		}
		
		//once its a leaf remove it
		if (parent.left == del) {
			parent.left = null;
		}
		else {
			parent.right = null;
		}
		return true;
	}
	
	/**
	 * Finds parent to assist in delete method
	 * @param root of tree
	 * @param key to find parent of
	 * @return parent node of key
	 */
	private Node<E> getParent(Node<E> root, E key) { 
		if (key.compareTo(root.data) < 0) {
			if (root.left.data == key) {
				return root;
			}
			return getParent(root.left, key);
		}
		else {
			//if (key.compareTo(root.data) > 0) 
			if (root.right.data == key) {
				return root;
			}
			return getParent(root.right, key);
		}
	}
	
	/**
	 * Returns node of the key to assist in delete method
	 * @param root of tree
	 * @param key to be found
	 * @return node of this key
	 */
	private Node<E> getNode(Node<E> root, E key) {
		if (root.data == key) {
			return root;
		}
		else if (key.compareTo(root.data) < 0){
			return getNode(root.left, key);
		} 
		else {
			return getNode(root.right, key);
		}
	}
	
	
	/**
	 * Determines if a node with given key is in the treap
	 * @param root is the root of the heap
	 * @param key is the key of the node we are looking for
	 * @return true if node with given key is found, false otherwise
	 */
	private boolean find(Node<E> root, E key) {
		//recursively check both sides 
		//first check root's children
		if (root == null) {
			//if root is null to begin with
			return false;
		}
		else if (root.data == key) {
			//if root has the key
			return true;
		}
		//then check left and right subtrees
		if (root.left != null && root.right == null) {
			//if right is null and left is not
			return find(root.left, key);
		}
		if (root.right != null && root.left == null) {
			//if left is null and right is not
			return find(root.right, key);
		}
		else if (root.right != null && root.left != null){
			//if both are not null
			return find(root.left, key) || find(root.right, key);
		}
 		
		//if never returned true
 		return false;
 		
	}
	
	/**
	 * Calls find(Node<E> root, E key) to determine if a node with given key is in the treap
	 * @param key is the key of the node we are looking for
	 * @return value from find(Node<E> root, E key)
	 */
	public boolean find(E key) {
		return this.find(this.root, key);
	}
	
	/**
	 * Creates a string for treap objects
	 * @param current node
	 * @param level of treap
	 * @return string of treap
	 */
	private String toString(Node<E> current, int level) {
		StringBuilder s = new StringBuilder();
		for (int i=0; i<level;i++) {
			s.append(" ");
		}
		if (current==null) {
			s.append("null\n");
		} else {
			s.append("(" + current.toString()+ ")\n");
			s.append(toString(current.left, level+1));
			s.append(toString(current.right,level+1));
		}
		return s.toString();
		
	}
	
	/**
	 * Calls toString(Node<E> current, int level) to make string for treap objects
	 */
	public String toString() {
		return toString(root,0);
	}
	
	/*
	public static void main(String[] args) {
		
		//examples trees / tests: 
		
		Treap<Integer>testTree = new Treap<Integer>();
		testTree.add(4, 19);
		testTree.add(2, 31);
		testTree.add(6, 70);
		testTree.add(1, 84);
		testTree.add(3, 12);
		testTree.add(5, 83);
		testTree.add(7, 26);
		System.out.println(testTree + "\n");
		//System.out.println(testTree.find(5));
		testTree.delete(5);
		System.out.println(testTree);
		
		
		
		Treap<Integer>testTree = new Treap<Integer>(6);
		testTree.add(4);//, 19);
		testTree.add(2);//, 31);
		testTree.add(6);//, 70);
		testTree.add(1);//, 84);
		testTree.add(0);//, 82); 
		testTree.add(-1);//, 3);
		testTree.add(3);//, 12);
		testTree.add(5);//, 83);
		testTree.add(7);//, 26);
		System.out.println(testTree + "\n");
		//System.out.println(testTree.delete(1));
		//testTree.delete(1);
		//System.out.println(testTree);
		
		
		
		Treap<String>testTree = new Treap<String>();
		testTree.add("a", 60);
		testTree.add("g", 80);
		testTree.add("x", 25);
		testTree.add("j", 65);
		testTree.add("u", 75);
		testTree.add("v", 21);
		testTree.add("z", 47);
		testTree.add("p", 99);
		testTree.add("r", 40);
		testTree.add("w", 32);
		testTree.delete("z");
		System.out.println(testTree);
		testTree.delete("p");
		System.out.println(testTree);
		
		
		
		Treap<String>testTree = new Treap<String>();
		testTree.add("a", 60);
		testTree.add("g", 80);
		testTree.add("j", 65);
		testTree.add("u", 75);
		testTree.add("r", 40);
		testTree.add("v", 21);
		testTree.add("z", 47);
		testTree.add("p", 99);
		testTree.add("x", 25);
		testTree.add("w", 32);
		System.out.println(testTree);
		testTree.add("i", 93);
		System.out.println(testTree);
		
	}
	*/
}
