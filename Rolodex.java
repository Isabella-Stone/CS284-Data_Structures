//Isabella Stone; Section B
//I pledge my honor that I have abided by the Stevens Honor System.

package rolodex;

import java.util.ArrayList;

public class Rolodex {
	private Entry cursor;
	private final Entry[] index;

	// Constructor
	Rolodex() {	
		index = new Entry[26];
		
		for (int i = 0; i < 26; i++) {
			index[i] = new Separator(null, null, (char) (i+65));
		}
		
		for (int i = 0; i < 26; i++) {
			if (i == 0 ) {
				index[i].next = index[i+1];
				index[i].prev = index[25];
			}
			else if (i == 25) {
				index[i].next = index[0];
				index[i].prev = index[i-1];
			}
			else {
				index[i].next = index[i+1];
				index[i].prev = index[i-1];
			}		
		}
	}


	public Boolean contains(String name) {
		//start at separator of letter name starts with
		int letter = ((int) name.charAt(0)) - 65;
		Entry current = index[letter].next; //start at first card at letter separator
		
		//loop until hits next letter separator
		//special case for if letter is z
		if (letter == 25) {
			while (current != index[0].next) {
				if (current.getName().equals(name)) {
					return true;
				}
				current = current.next;
			}
		}
		//rest of letters
		else {
			while (current != index[letter + 1].next) {
				if (current.getName().equals(name)) {
					return true;
				}
				current = current.next;
			}
		}
		//if not found return false
		return false;
	}
	
	public int size() {
		int size = 0;
		Entry current = index[0].next;//start at first card
		while (current.next != index[0].next) {
			//only count cards, not separators
			if(current.isSeparator() == false) {
				size++;
			}
			current = current.next;
		}
		return size;
	}

	public ArrayList<String> lookup(String name) {
		if (this.contains(name) == false) {
			throw new IllegalArgumentException("lookup: name not found");
		}
		
		ArrayList<String> nums = new ArrayList<String>();
		int letter = ((int) name.charAt(0)) - 65;
		Entry current = index[letter];
		//special case for if letter is z
		if (letter == 25) {
			while (current.next != index[0].next) {
				if(current.getName().equals(name)) {
					nums.add(((Card)current).getCell());
				}
				current = current.next;
			}			
		}
		//for rest of letters
		else {
			while (current.next != index[letter + 1].next) {
				if(current.getName().equals(name)) {
					nums.add(((Card)current).getCell());
				}
				current = current.next;
			}
		}
		return nums;
	}

	
	public void addCard(String name, String cell) {
		int letter = (int)name.charAt(0) - 65;
		Entry current = index[letter];
		
		//see if card exists first
		while (current.next.isSeparator() == false) {
			if (name.equals(current.getName()) && cell.equals(((Card) current).getCell())) {
				throw new IllegalArgumentException("addCard: duplicate entry");
			}
			current = current.next;
		}
		
		current = index[letter];
		//if DNE then add in alphabetically
		if (current.next.isSeparator()) {
			current.next = new Card(current.prev, current.next, name, cell);
		}
		else {
			while (current.next.getName().compareTo(name) < 0) {
				current = current.next;
			}
			current.next = new Card(current.prev, current.next, name, cell);
		}
		
	}

	public void removeCard(String name, String cell) {		
		//if name DNE
		if (this.contains(name)==false) {
			throw new IllegalArgumentException("removeCard: name does not exist");
		}
		//if specific cell DNE
		if (! this.lookup(name).contains(cell)) {
			throw new IllegalArgumentException("removeCard: cell for that name does not exist");
		}
		
		//start at separator of letter name starts with
		int letter = ((int) name.charAt(0)) - 65;
		Entry current = index[letter]; //start at first card at letter separator
		
		//while not at next letter cards yet
		if (letter == 25) {
			while (current.next != index[0].next) {
				//if the next card is the one that needs to be removed
				if (current.next.getName().equals(name) && ((Card) current.next).getCell().equals(cell)) {
					break;
				}
				current = current.next;
			}
		}
		else {
			while (current.next != index[letter + 1].next) {
				//if the next card is the one that needs to be removed
				if (current.next.getName().equals(name) && ((Card) current.next).getCell().equals(cell)) {
					break;
				}
				current = current.next;
			}
		}
		
		current.next = current.next.next;

	}
	
	public void removeAllCards(String name) {
		if (this.contains(name)==false) {
			throw new IllegalArgumentException("removeAllCards: name does not exist");
		}
		//every time it finds card with name, call removeCard
		int letter = ((int) name.charAt(0)) - 65;
		Entry current = index[letter]; //start at first card at letter separator
		
		if (letter == 25) {
			//while not at next letter cards yet
			while (current.next != index[0].next) {
				//if its a card for proper name remove it
				if(current.getName().equals(name)) {
					this.removeCard(name, ((Card)current).getCell());
				}
				current = current.next;
			}
		}
		//rest of letters
		else {
			//while not at next letter cards yet
			while (current.next != index[letter + 1].next) {
				//if its a card for proper name remove it
				if(current.getName().equals(name)) {
					this.removeCard(name, ((Card)current).getCell());
				}
				current = current.next;
			}
		}
	}

	public String toString() {
		Entry current = index[0];

		StringBuilder b = new StringBuilder();
		while (current.next!=index[0]) {
			b.append(current.toString()+"\n");
			current=current.next;
		}
		b.append(current.toString()+"\n");		
		return b.toString();
	}
	
	// Cursor operations

	public void initializeCursor() {
		cursor = index[0];
	}

	public void nextSeparator() {
		
		Entry current = cursor.next; 
		while (!current.isSeparator()) {
			current = current.next;
		}
		cursor = current;
	}

	public void nextEntry() {
		Entry current = cursor;
		current = current.next;
		cursor = current;
	}

	public String currentEntryToString() {
		if (cursor.isSeparator()) {
			return "Separator " + cursor.getName();
		}
		else {
			return "Name: " + cursor.getName() + ", Cell: " + ((Card)cursor).getCell();
		}

	}

	/*
	public static void main(String[] args) {

		Rolodex r = new Rolodex();


		System.out.println(r);

		r.addCard("Chloe", "123");
		r.addCard("Chad", "23");
		r.addCard("Cris", "3");
		r.addCard("Cris", "4");
		r.addCard("Cris", "5");
		r.addCard("Zin", "5");
		r.addCard("Zin", "66");
		r.addCard("Abby", "5");
		//		r.addCard("Cris", "4");
		r.addCard("Maddie", "23");

		System.out.println(r);

		System.out.println(r.contains("Albert"));

		r.removeAllCards("Cris");
		r.removeAllCards("Zin");
		System.out.println(r);

		r.removeAllCards("Chad");
		r.removeAllCards("Chloe");

		r.addCard("Chloe", "123");
		r.addCard("Chad", "23");
		r.addCard("Cris", "3");
		r.addCard("Cris", "4");

		System.out.println(r);


	}
	*/
}