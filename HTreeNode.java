//node adt for huffman tree and associated huffman linked list

public class HTreeNode {

	//data fields
	char character = (char)0;
	int frequency = 1;
	HTreeNode next = null;
	HTreeNode left = null;
	HTreeNode right = null;
	String code = "";
	
	//constructor
	public HTreeNode() {
	}
	
	public HTreeNode(char c) {
		character = c;
	}
	
	public HTreeNode(int freq) {
		frequency = freq;
	}
	
	public HTreeNode(char c, int freq) {
		character = c;
		frequency = freq;
	}
		
	public HTreeNode(char ch, int f, HTreeNode n, HTreeNode l, HTreeNode r, String c) { 
		character = ch;
		frequency = f;
		next = n;
		left = l;
		right = r;
		code = c;
	}	
}
