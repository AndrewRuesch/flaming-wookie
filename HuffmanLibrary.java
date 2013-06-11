import java.io.File;
import java.io.FileOutputStream;
public class HuffmanLibrary {

	public static void Huffman_coder(String input_file, String output_file) throws CloneNotSupportedException, Exception{
	
		/*
		THE ALGORITHM
		
		1) read the file and convert it to a string of characters
		2) make a linked list of unique characters and keep track of frequency
		3) sort the linked list by increasing frequency
		4) take the list and build a Huffman tree based on the above frequencies
		5) traverse the Huffman tree to get the character codes
		6) make an array (String[]) of these codes, indexed such that the string code
			for a given character can be found in constant time
		7) reread the original ascii file, substituting the character codes for characters
			and concatinating the codes into one string as you go
		8) hand that string and the name of the target file (output_file) to BinaryFileWriter
		*/
		
		// 1)
		String inputFileString = readFileAsString(input_file);
		
		// 2)
		HLinkedList unsortedList = createHList(inputFileString);
		
		// 3)
		HLinkedList sortedList = getSortedLinkedList(unsortedList);
		
		// 4)
		HTree tree = createHuffmanTree(sortedList);
		
		// 5)		
		java.util.ArrayList codeValues = updateCodeValues(tree);
		
		// 6)
		String[] codeStringArray = toArray(tree, codeValues);
		
		// 7)
		String codeString = readFileAsCodeString(inputFileString, codeStringArray);
		
		// 8)
		createBinaryFile(codeString, output_file);
		
		//method to write final code string to text file
		toFile(codeString, "huffman_encoding.txt");
		
			
	}

	//read in file and concat to one string
	public static String readFileAsString(String fileName) throws Exception{
		java.io.File file = new java.io.File(fileName);   //input file specified at method invocation
		java.util.Scanner input = new java.util.Scanner(file);
		String fileString = "";  //file as one string
		
		while (input.hasNext()) {
			fileString += input.nextLine();
			fileString += String.valueOf((char)10); //add a new line character; adds a exta newLine at the end
		}
		
		//remove the extra new line at end
		fileString = fileString.substring(0, fileString.length() - 1);
		
		input.close();
		
		return fileString;
	}
	
	//take string from readFileAsString() and make a linked list 
	//of unique characters
	public static HLinkedList createHList(String fileContents) {
		HLinkedList list = new HLinkedList();
		
		char c = (char)0;
		for (int i = 0; i < fileContents.length(); i++) {
			c = fileContents.charAt(i);
			if (!list.contains(c))
				list.insertIntoPosition(new HTreeNode(c), i);
			else {
				//find the node in the list that contains character c, 
				//and update its frequency
				(list.find(new HTreeNode(c))).frequency++; 
			}
		}
		
		return list;	
	}
	
	public static HLinkedList getSortedLinkedList(HLinkedList list) {
		HTreeNode current = list.head;
		
		while (sorted(list)) {
			current = list.head;
			//iterate over length of list
			for (int i = 0; i < list.size - 1 ; i++) {
				if (current.frequency > (current.next).frequency) {
					
					char tempChar = current.character;
					int tempFreq = current.frequency;
					
					current.character = (current.next).character; 
					current.frequency = (current.next).frequency;
					
					(current.next).character = tempChar;
					(current.next).frequency = tempFreq;
				}

				//move current to point to the next
				current = current.next;
			}
		}
		return list;
	}

	private static boolean sorted(HLinkedList list) { 
		HTreeNode current = list.head;	
		while (current.next != null) {
			if (current.frequency > (current.next).frequency)
				return true;
			current = current.next;
		}
		return false;
	}
	
	
	
	//make sure to throw CloneNotSupportedException in whatever method calls this method!
	//take sorted linked list and build a Huffman tree
	public static HTree createHuffmanTree(HLinkedList newList) throws CloneNotSupportedException {
		//make a copy of list, otherwise the list in the class that 
		//made the method call will be nackered
		HLinkedList list = (HLinkedList)newList.clone();
		HTree HuffmanTree = new HTree();
		//assume list is in correct order 
		HTreeNode listHead = null;
		HTreeNode first = null;
		HTreeNode second = null;
		
		//continue the following process until the list has only one node
		while (list.size > 1) {
			listHead = list.head;  
					
			first = listHead;
			second = first.next;
			
			HTreeNode combined = new HTreeNode(first.frequency + second.frequency);
			
			combined.left = first;
			combined.right = second;
			
			list.insertIntoPosition(combined, list.size);
			
			list.size -= 2;
			list.head = ((list.head).next).next;
		}
		
		HuffmanTree.root = list.head;
		
		return HuffmanTree;		
	}
	
	//traverse the tree to build a codes, then add the codes
	//to an arrayList and return it; huffmanCoder() will take this 
	//arrayList and generate a string of codes from the original 
	//ascii text
	//changed method header from public void updateCodeValues
	public static java.util.ArrayList updateCodeValues (HTree tree) {
		//inorder traversal receives a refernce to this list, so 
		//the list is modified in place; call inorder traversal 
		//and then return the list
		java.util.ArrayList arrayList = new java.util.ArrayList();
		inorderTraversal(tree.root, "", arrayList);
		return arrayList;
	}
	
	public static void inorderTraversal(HTreeNode root, String path, java.util.List arrayList) {
		if (root.left != null) {
			path += "0";
			inorderTraversal(root.left, path, arrayList);
		}
		if (root.character != (char)0) { //not null
			arrayList.add(path);
		}
		path = path.substring(0, path.length() - 1);
		if (root.right != null) {
			path +=  "1";
			inorderTraversal(root.right, path, arrayList);
		}
	}
	
	//make a new linked list with the nodes in-order;
	//this is necessary to implement toArray() correctly
	public static HLinkedList getInOrderList(HTree tree) {
		HLinkedList inorder = new HLinkedList();
		printInorder(tree.root, inorder);
		return inorder;
	}
	
	public static void printInorder(HTreeNode root, HLinkedList inorder) {
		if (root.left != null)
			printInorder(root.left, inorder);
		if (root.character != (char)0)  //not null
			inorder.insertIntoPosition(root, inorder.size); //append node
		if (root.right != null)
			printInorder(root.right, inorder);
	}
	
	//take the linked sorted linkedlist and the ArrayList of codes, 
	//and construct a new array combining the information form the two;
	//the array will have 127 spaces to accomodate the characters in the ascii table;
	//the characters from the nodes, and their associated codes, will be indexed in
	//the array by the int value of the character, ie position 97 in the array will
	//represent char a, and will contain the code for a. to get the code for L for example, 
	//simply look in array[(int)'L']. this means that accessing the code
	//corresponding to a given character will take constant time; this will be helpful
	//when converting the ascii source text to a string of character codes
	public static String[] toArray (HTree tree, java.util.ArrayList arrayList) {
		HLinkedList list = getInOrderList(tree);
		String[] codes = new String[127];
		HTreeNode current = list.head;
		String codeString = "";
		
		for (int i = 0; i < list.size; i++) {
			codeString = String.valueOf(arrayList.get(i));
			codes[(int)current.character] = codeString; //this works because the items in list and arrayList
																	  //are in corresponding order
			current = current.next;
		}
		
		return codes;
	}
	
	//read in file, substitute code values for chars, and concat to one string
	public static String readFileAsCodeString(String inputFileString, String[] codeStringArray) throws Exception{
		String fileCodeString = "";
				
		for (int i = 0; i < inputFileString.length(); i++) {
			int n = (int)inputFileString.charAt(i);
			String codeForC = codeStringArray[n];
			fileCodeString += codeForC;
		}		
		
		return fileCodeString;
	}
	
	//to write code string to text file
	public static void toFile(String s, String output_file) throws Exception{
		java.io.File file = new java.io.File(output_file);
		java.io.PrintWriter output = new java.io.PrintWriter(file);
		output.print(s);
		output.close();
	}
	
	//methods from BinaryFileWriter
	public static void writeBinaryFile(String fileName,byte[] b) throws Exception{
		FileOutputStream f = new FileOutputStream (new File(fileName));
		f.write(b);
		f.close();
	}
	
	public static void createBinaryFile(String seq,String fileName) throws Exception{
		writeBinaryFile(fileName,toByteSequence(seq));
	}
	
	public static byte[] toByteSequence(String data) throws Exception{
	int j=-1;
	int byteArrSize = data.length()/8;
	if (data.length()%8 != 0)
		byteArrSize++;
	byte[] byteSeq = new byte[byteArrSize];
	for(int i=0;i< data.length();i++){
		if (i%8 == 0){
			j++;
			byteSeq[j] = 0x00;
		}
		byte tmp ;
		if (data.charAt(i) == '1'){
			tmp = 0x01;
		}
		else if (data.charAt(i) == '0') {
			tmp = 0x00;
		}
		else
			throw new Exception ("error in format");
		byteSeq[j] = (byte) (tmp | ( byteSeq[j] << 1));
	}
	
		return byteSeq;
	}
}
