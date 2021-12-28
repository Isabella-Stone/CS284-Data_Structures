package huffman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
//import java.util.Stack;
//import treap.Treap.Node;

/*
 * Instructions: 
 * First: Read through the assignment specification, make sure you understand what the assignment is asking for.
 * Second: There are number of "TODO" instructions within this code, make sure to complete all of them fully.
 * Third: Test you code.
 */


// TODO: Name and Pledge

// Pledge: I pledge my honor that I have abided by the Stevens Honor System.
// Name: Isabella Stone


/**
 * HW6 CS284 Spring 2021
 * Implements a Huffman encoding tree.
 * The included code has been commented for the student's benefit, feel free to read through it.
 */
public class HuffmanTree {

	// ******************** Start of Stub Code ******************** //
	// ************************************************************ //
    /** Node<E> is an inner class and it is abstract.
     * There will be two kinds
     * of Node, one for leaves and one for internal nodes. */
    abstract static class Node implements Comparable<Node>{
        /** The frequency of all the items below this node */
        protected int frequency;
        
        public Node(int freq) {
        	this.frequency = freq;
        }
        
		/** Needed for the Minimum Heap used later in this stub. */
		public int compareTo(Node other) {
			return this.frequency - other.frequency;
		}
    }
    /** Leaves of a Huffman tree contain the data items */
    protected static class LeafNode extends Node {
        // Data Fields
        /** The data in the node */
        protected char data;
        /** Constructor to create a leaf node (i.e. no children) */
        public LeafNode(char data, int freq) {
            super(freq);
            this.data = data;
        }
        /** toString method */
        public String toString() {
            return "[value= "+this.data + ",freq= "+frequency+"]";
        }
    }
    /** Internal nodes contain no data,
     * just references to left and right subtrees */
    protected static class InternalNode extends Node {
        /** A reference to the left child */
        protected Node left;
        /** A reference to the right child */
        protected Node right;

        /** Constructor to create an internal node */
        public InternalNode(Node leftC, Node rightC) {
            super(leftC.frequency + rightC.frequency);
            left = leftC; right = rightC;
        }
        public String toString() {
            return "(freq= "+frequency+")";
        }
    }
	
	// Enough space to encode all "extended ascii" values
	// This size is probably overkill (since many of the values are not "printable" in the usual sense)
	private static final int codex_size = 256;
	
	/* Data Fields for Huffman Tree */
	private Node root;
	
	public HuffmanTree(String s) {
		root = buildHuffmanTree(s);
	}
	
	/**
	 * Returns the frequencies of all characters in s.
	 * @param s
	 * @return
	 */
	public static int[] frequency(String s) {
		int[] freq = new int[codex_size];
		for (char c: s.toCharArray()) {
			freq[c]++;
		}
		return freq;
	}
	
	/**
	 * Builds the actual Huffman tree for that particular string.
	 * @param s
	 * @return
	 */
	private static Node buildHuffmanTree(String s) {
		int[] freq = frequency(s);
		
		// Create a minimum heap for creating the Huffman Tree
		// Note to students: You probably won't know what this data structure
		// is yet, and that is okay.
		PriorityQueue<Node> min_heap = new PriorityQueue<Node>();
		
		// Go through and create all the nodes we need
		// as in, all the nodes that actually appear in our string (have a frequency greater then 0)
		for(int i = 0; i < codex_size; i++) {
			if (freq[i] > 0) {
				// Add a new node (for that character) to the min_heap, notice we have to cast our int i into a char.
				min_heap.add(new LeafNode((char) i, freq[i]));
			}
		}
		
		// Edge case (string was empty)
		if (min_heap.isEmpty()) {
			throw new NullPointerException("Cannot encode an empty String");
		}
		
		// Now to create the actual Huffman Tree 
		// NOTE: this algorithm is a bit beyond what we cover in cs284, 
		// you'll see this in depth in cs385
		
		// Merge smallest subtrees together
		while (min_heap.size() > 1) {
			Node left = min_heap.poll();
			Node right = min_heap.poll();
			Node merged_tree = new InternalNode(left, right);
			min_heap.add(merged_tree);
		}
		
		// Return our structured Huffman Tree
		return min_heap.poll();
	}
	
	// ******************** End of Stub Code ******************** //
	// ********************************************************** //
	
	/**
	 * Public toString function
	 */
	public String toString() {
		// TODO Complete toString method (see assignment specification)
		// HINT: Might need helper method for preOrderTraversal
		return toString(this.root,0);
    }
	
	/**
	 * Private toString helper
	 * @param root is the current root of the tree
	 * @param level of the tree
	 * @return string representation of a HuffmanTree object
	 */
	private String toString(Node root, int level) {
		StringBuilder s = new StringBuilder();
		for (int i=0; i<level;i++) {
			s.append(" ");
		}
		if (root==null) {
			s.append("null\n");
		} 
		//else if it's an internal node
		else if (root instanceof InternalNode){
		    //if its a leaf
			s.append(((InternalNode)root).toString() + "\n");
			s.append(toString(((InternalNode)root).left, level + 1) + "\n");
			s.append(toString(((InternalNode)root).right, level + 1));
		}
		//else it's a leaf node
		else {
			s.append(((LeafNode)root).toString());
		}
		return s.toString();
	}
	
	/**
	 * Turns boolean bits into a binary string
	 * @param encoding is the Boolean array to transform
	 * @return a binary representation of the boolean bits
	 */
	public static String bitsToString(Boolean[] encoding) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < encoding.length; i++) {
			if (encoding[i] == true) {
				//true corresponds to 1
				s.append("1");
			}
			else {
				//false corresponds to 0
				s.append("0");
			}
		}
		return s.toString();
	}
	
	/**
	 * Decodes a Boolean array into a word
	 * @param coding the Boolean array to be decoded
	 * @return the String that corresponds to the Boolean array
	 */
	public String decode(Boolean[] coding) {
		if (coding.length == 0) {
			throw new IllegalArgumentException("Cannot use empty array");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(bitsToString(coding));
		
		String result = "";
		Node current = root;
		
		while (!(sb.isEmpty())) {
			if (current instanceof LeafNode) {
				//if at leaf node, add char
				result += ((LeafNode)current).data;
				current = root;
			}
			//check to go left or right
			if (sb.charAt(0) == '0') {
				if (((InternalNode)current).left == null) {
					//if can't go left
					throw new IllegalArgumentException("Encoding is not valid");
				}
				current = ((InternalNode)current).left;
				//remove from sb
				sb.deleteCharAt(0);
				//System.out.println(sb);
				if (sb.isEmpty()) {
					break;
				}
			}
			else {
				//will run if (str.charAt(0) == '1')
				if (((InternalNode)current).right == null) {
					//if can't go right
					throw new IllegalArgumentException("Encoding is not valid");
				}
				current = ((InternalNode)current).right;
				//remove from sb
				sb.deleteCharAt(0);
				//System.out.println(sb);
				if (sb.isEmpty()) {
					break;
				}
			}
		}
		if (current instanceof LeafNode) {
			result += ((LeafNode)current).data;
		}
		if (sb.isEmpty() && result.isEmpty()) {
			//if never found one but went through whole thing
			throw new IllegalArgumentException("Encoding is not valid");
		}
		return result;
	}
	
	/**
	 * Encodes a String into a Boolean array
	 * @param inputText is the String to be encoded
	 * @return a Boolean array representation of the String
	 */
	public Boolean[] encode(String inputText) { 
		if (inputText.isEmpty()) {
			throw new IllegalArgumentException("Cannot use empty string");
		}
		//create an ArrayList for the answer/result
		ArrayList<Boolean> result = new ArrayList<>();
		
		//loop through chars in inputText
		for (Character car : inputText.toCharArray()) {
			ArrayList<Boolean> temp = new ArrayList<>();
			if (!(successOfRoute(root, car, temp))) {
				//if a letter from inputText is not found
				throw new IllegalArgumentException("Contains letter not found in HuffmanTree");
			}
			result.addAll(temp);
		}
		
		//return final result as a Boolean array
		return (Boolean[]) result.toArray(new Boolean[result.size()]);
	}
	
	/**
	 * Determines is the letter you are looking for has been found 
	 * @param root of current tree / sub tree
	 * @param car is the character that it checks to be found or not
	 * @param route is the ArrayList of Boolean that tells how to get to the car,
	 * where false is left, and true is right
	 * @return is the route is successful
	 */
	public boolean successOfRoute(Node root, Character car, ArrayList<Boolean> route) {
		//left = false = 0
		//right = true = 1
		if (root instanceof LeafNode) {
			//if at a LeafNode / letter
			if (((LeafNode)root).data == car) {
				//if you found the right letter return true
				return true;
			}
			else {
				//found the wrong letter so return false
				return false;
			}
		}
		else if (successOfRoute(((InternalNode)root).right, car, route)) {
			//add a 0 to go left
			route.add(0, true);
			return true;
		}
		else if (successOfRoute(((InternalNode)root).left, car, route)) {
			//add a 0 to go right
			route.add(0, false);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Encodes a String but does not search for the same letter twice
	 * @param inputText is the String to encode
	 * @return a Boolean array of the encoded String, where false corresponds to 0
	 * and true corresponds to 1
	 */
	public Boolean[] efficientEncode(String inputText) {
		// TODO Complete efficientEncode method
		// NOTE: Should only go through the tree once.
		
		if (inputText.isEmpty()) {
			throw new IllegalArgumentException("Cannot use empty string");
		}
		//map to store chars and their values
		Map<Character, ArrayList<Boolean>> values = new HashMap<Character, ArrayList<Boolean>>();
		
		//create an ArrayList for the answer/result
		ArrayList<Boolean> result = new ArrayList<>();
		
		//loop through chars in inputText
		for (Character car : inputText.toCharArray()) {
			if (values.containsKey(car)) {
				//System.out.println("hash"); //make sure it uses hash map
				//if car is already in the map 
				result.addAll(values.get(car));
			}
			else {
				ArrayList<Boolean> temp = new ArrayList<>();
				if (!(successOfRoute(root, car, temp))) {
					//if a letter from inputText is not found
					throw new IllegalArgumentException("Contains letter not found in HuffmanTree");
				}
				result.addAll(temp);
				//add into map (values) 
				values.put(car, temp);
			}
		}
		
		//return final result as a Boolean array
		return (Boolean[]) result.toArray(new Boolean[result.size()]);
	}
	
	
	/*
	public static void main(String[] args) {
		// Code to see if stuff works...
		
		
		String s = "This string is used as a basis for setting up the Huffman tree";
		HuffmanTree t = new HuffmanTree(s); // Creates specific Huffman Tree for "s"
		// Now you can use encode, decode, and toString to interact with your specific Huffman Tree
		System.out.println(t);
		System.out.println(bitsToString(t.encode("hinge")));
		//01111110111111101001100 - hinge result
		System.out.println(bitsToString(t.efficientEncode("hhh")));
		
		
		
		//decode test
		String s = "This string is used as a basis for setting up the Huffman tree";
				// Create specific Huffman Tree for "s"
				HuffmanTree t = new HuffmanTree(s);
				Boolean [] c = {true, true , false , true , true , false , true , false ,
				false , true , true , true , true , true , true , true ,
				false , true , true , true , true , false , true , true , false, false};
				System.out.println(t.decode(c)); //ignite
		
		 
	}
	*/
}
