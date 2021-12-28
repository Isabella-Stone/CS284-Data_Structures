/*
 * I pledge my honor that I have abided by the Stevens Honor System.
 * Isabella Stone
 */

import java.lang.reflect.Array;

public class BinaryNumber {
	
	private int data[];
	private int length;

	public BinaryNumber(int length) {
		if (length < 0) {
			throw new IllegalArgumentException("Improper length.");
		}
		this.data = new int[length];
		this.length = length;
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
	}
	
	public BinaryNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (Character.getNumericValue(str.charAt(i)) != 1 && Character.getNumericValue(str.charAt(i)) != 0) {
				throw new IllegalArgumentException("Must enter binary");
			}
		}
		this.length = str.length();
		this.data = new int[str.length()];
		for (int i = 0; i < str.length(); i++) {
			data[i] = Character.getNumericValue(str.charAt(i));
		}
	}
	
	public int getLength() {
		return this.length;
	}
	
	public int getDigit(int index) {
		if (index < 0 || index >= this.length) {
			throw new IllegalArgumentException("Index out of bounds.");
		}
		return data[index];
	}
	
	public int[] getInnerArray() {
		return this.data;
	}
	
	public static int[] bwor(BinaryNumber bn1, BinaryNumber bn2) {
		if (bn1.length != bn2.length) {
			throw new IllegalStateException("Bn1 and bn2 must be same length.");
		}
		int bwor[] = new int[bn1.getLength()];
		
		if(bn1.getLength() == bn2.getLength() ) {
			
			for (int i = 0; i < bn1.getLength(); i++) {
				
				if (bn1.getDigit(i)==0 && bn2.getDigit(i)==0) {
					bwor[i] = 0;
				}
				else {
					bwor[i] = 1;
				}

			}
		}
		
		return bwor;
	}	
	
	public static int[] bwand(BinaryNumber bn1, BinaryNumber bn2) {
		if (bn1.length != bn2.length) {
			throw new IllegalStateException("Bn1 and bn2 must be same length.");
		}
		int bwand[] = new int[bn1.getLength()];
		
		if(bn1.getLength() == bn2.getLength() ) {
			
			for (int i = 0; i < bn1.getLength(); i++) {
				
				if (bn1.getDigit(i)==1 && bn2.getDigit(i)==1) {
					bwand[i] = 1;
				}
				else {
					bwand[i] = 0;
				}

			}
		}
		
		return bwand;
	}
	
	public void bitShift(int direction, int amount) {
		
		if (direction != -1 && direction != 1) {
			throw new IllegalArgumentException("Invalid value for direction.");
		}
		if (amount < 0) {
			throw new IllegalArgumentException("Amount must be nonnegative.");
		}
		if (direction == 1) {
			//make new array of proper length
			int arr[] = new int[length-amount];
			//if shift right, only keep items < length-amount
			for (int i = 0; i < length-amount; i++) {
				arr[i] = data[i];
			}
			this.length = arr.length;
			this.data = arr;
		}
		
		else if (direction == -1) {
			//make new array of proper length
			int arr[] = new int[length+amount];
			//if shift left, add 'amount' zeroes
			for (int i = 0; i < length+amount; i++) {
				//first fill arr with all of data
				if (i < length) {
					arr[i] = data[i];
				}
				//once all of data is added, add zeroes
				else {
					arr[i] = 0;
				}
			}
			this.length = arr.length;
			this.data = arr;
		}
		
	}
	
	public void add(BinaryNumber aBinaryNumber) {
		
		//prepend 
		if (this.length < aBinaryNumber.length) {
			int amount = aBinaryNumber.length - this.length;
			int zeroes[] = new int[this.length+amount]; //new array with proper length to add zeroes
			//add zeroes to beginning
			for (int i = 0; i < amount; i++) {
				zeroes[i] =  0;	
			}
			//fill rest of zeroes[] 
			for (int i = amount; i < zeroes.length; i++) {
				zeroes[i] = this.data[i-amount];	
			}
			this.length = zeroes.length;
			this.data = zeroes;
		}
		else if (aBinaryNumber.length < this.length) {
			int amount = this.length - aBinaryNumber.length;
			int zeroes[] = new int[aBinaryNumber.length+amount];
			//add zeroes to beginning
			for (int i = 0; i < amount; i++) {
				zeroes[i] =  0;	
			}
			//fill rest of zeroes[] 
			for (int i = amount; i < zeroes.length; i++) {
				zeroes[i] = aBinaryNumber.data[i-amount];	
			}	
			
			aBinaryNumber.length = zeroes.length;
			aBinaryNumber.data = zeroes;
		}
		
		int sum[] = new int[length]; //array to hold answer
		int carry = 0; //initialize carry to 0 (no carry at beginning)
		
		//add rules
		for (int i = this.length-1; i >= 0; i--) {
			if (carry == 0) {
				//if you have a 0 and 0
				if (this.data[i] == 0 && aBinaryNumber.data[i] == 0) {
					sum[i] = 0;
					carry = 0;
				}
				//if you have a 0 and 1
				else if ((this.data[i] == 0 && aBinaryNumber.data[i] == 1) || (this.data[i] == 1 && aBinaryNumber.data[i] == 0)) {
					sum[i] = 1;
					carry = 0;
				}
				//if you have a 1 and 1
				else if (this.data[i] == 1 && aBinaryNumber.data[i] == 1) {
					sum[i] = 0;
					carry = 1;
				}
			}
			//else if carry == 1
			else {
				//if you have a 0 and 0
				if (this.data[i] == 0 && aBinaryNumber.data[i] == 0) {
					sum[i] = 1;
					carry = 0;
				}
				//if you have a 0 and 1
				else if ((this.data[i] == 0 && aBinaryNumber.data[i] == 1) || (this.data[i] == 1 && aBinaryNumber.data[i] == 0)) {
					sum[i] = 0;
					carry = 1;
				}
				//if you have a 1 and 1
				else if (this.data[i] == 1 && aBinaryNumber.data[i] == 1) {
					sum[i] = 1;
					carry = 1;
				}
			}	
		}
		
		this.length = sum.length;
		this.data = sum;
			
		//if carry still == 1 at end, add 1 to beginning of receiving array after shifting 
		if (carry == 1) {
			int sum2[] = new int[this.length + 1];
			sum2[0] = 1;
			for (int i = 1; i < this.length + 1; i++) {
				sum2[i] = sum[i-1];
			}
			
			this.length = sum2.length;
			this.data = sum2;
		}
		
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < this.length; i++) {
			s.append(this.data[i]);			
		}
		return s.toString();
	}
	
	public int toDecimal() {
		int x = 0;
		for (int i = 0; i < Array.getLength(data); i++)  {
			x += data[(this.length-1)-i] * Math.pow(2, i);
		}
		return x;
	} 
	
	public static void main (String[] args) {
		
		//CONSTRUCTOR TEST*************************************
		/*
		BinaryNumber x = new BinaryNumber(9);
		for (int i = 0; i < x.length; i++) {
			System.out.print(x.data[i]);
		}
		*/
		
		
		//TO DECIMAL TEST*************************************
		/*
		BinaryNumber num = new BinaryNumber("0010011100011");
		System.out.println(num);
		System.out.println(num.toDecimal());
		*/
		
		
		//BIT SHIFT TEST*************************************
		/*
		BinaryNumber num1 = new BinaryNumber("1011");
		num1.bitShift(-1, 2); //shift left
		System.out.println("left:" + num1); 
		
		BinaryNumber num2 = new BinaryNumber("1011");
		num2.bitShift(1, 2); //shift right
		System.out.println("right:" + num2); 
		*/
		
		
		//BWOR BWAND TEST*************************************
		/*
		System.out.println("bwor/bwand test: ");
		BinaryNumber b1 = new BinaryNumber("100101");
		BinaryNumber b2 = new BinaryNumber("111001");
		
		int[] x;
		x = BinaryNumber.bwor(b1, b2);
		for (int i = 0; i < x.length; i++) {
			System.out.print(x[i]);
		}
		System.out.println(); //print new line
		x = BinaryNumber.bwand(b1, b2);
		for (int i = 0; i < x.length; i++) {
			System.out.print(x[i]);
		}
		*/
		
		
		//BINARY ADD TEST*************************************
		/*
		BinaryNumber aa = new BinaryNumber("10101");
		BinaryNumber bb = new BinaryNumber("11110110");
		
		aa.add(bb);
		System.out.println("Answer:" + aa); 
		*/
	}
	
	
}
