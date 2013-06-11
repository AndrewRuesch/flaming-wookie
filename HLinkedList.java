//Huffman linked list ADT

public class HLinkedList implements Cloneable {
	
	//data fields
	HTreeNode head = null;
	HTreeNode tail = null;
	int size = 0;
	
	public HTreeNode getHeadNode() {
		return head;
	}

	public int getSize() {
		return size;
	}
	
	public void insertIntoPosition(HTreeNode node, int position) {
		if (head == null && tail == null)
			addFirst(node);
		else if (position >= size)
			addLast(node);
		else if (position <= 0)
			addFront(node);
		else 
			add(node, position);
	}	
	
	private void addFirst(HTreeNode n) {
		head = n;
		tail = n;
		size++;
	}
	
	private void addLast(HTreeNode n) {
		tail.next = n;
		n.next = null;
		tail = n;
		size++;
	}
	
	private void addFront(HTreeNode n) {
		HTreeNode temp = head;
		head = n;
		n.next = temp;
		size++;
	}
	
	private void add(HTreeNode n, int position) {
		HTreeNode current = head;
		for (int i = 0; i < position - 2; i++) { //current is now one b4 node at position
			current = current.next;
		}
		HTreeNode temp = current.next;
		current.next = n;
		n.next = temp;
		size++;
	}	
	
	public HTreeNode getHead() {
		if (head != null)
			return findAndRemove(head);
		else {
			System.out.println("tried to invoked getHead on null");
			return null;
		}
		
	}
	
	//find node that has same character as node (first instance of),
	//return it [its reference] and remove it from the list
	public HTreeNode findAndRemove(HTreeNode node) {
		HTreeNode current = head;
		for (int i = 0; i < size; i++) {
			//if node = head or tail
			if (current.character == node.character) {
				//remove and return first
				if (head == current) {
					head = current.next;
					size--;
					return current;
				}
				//remove and return last
				else if (tail == current) { //current is now one b4 node at position
					current.next = null;
					HTreeNode temp = tail;
					tail = current;
					size--;
					return temp;
				}
			}
			//else if node is found in the middle
			else if ((current.next).character == node.character) {
				HTreeNode temp = current.next; 
				current.next = (current.next).next;
				size--;
				return temp;
			}
			else {
				//no match, so go to the next node in the list					
				current = current.next;
			}
		}
		System.out.println("Cannot return node: node not found in the list.");
		return null; //node not found
	}
	
	//use the character data field to find match
	public HTreeNode find(HTreeNode node) { 
		HTreeNode current = head;
		for (int i = 0; i < size; i++) {
			if (current.character == node.character)
				return current; // return reference to 
			current = current.next;
		}
		return null;
	}
	
	//to see if node with a particular character is already in the list
	public boolean contains(char character) {
		HTreeNode current = head;
		for (int i = 0; i < size; i++) {
			if (current.character == character)
				return true;
			current = current.next;
		}
		return false;
	}
	
	public String toString() {
		String str = "";
		HTreeNode current = head;
		for (int i = 0; i < size - 1; i++) {
			if (size == 1)
				str += Character.toString(current.character);
			else
				str += Character.toString(current.character) + ", ";
			current = current.next;
		}
		str += Character.toString(current.character); //so there's no coma after last
		return str;
	}
	
	
	//optional iterator, incase choose to make
	//head and tail private
	public class HLinkedListIterator {
		
		private HTreeNode current = head;
		private int iteratorIndex = 0;
		
		boolean hasNext() {
			if (iteratorIndex < size) {
				iteratorIndex++;
				return true;
			}
			return false;
		}
		
		//iteratorIndex is now 1 too large, so return node
		//at position iteratorIndex - 1
		HTreeNode next() {
			HTreeNode current = head;
			for (int i = 0; i < iteratorIndex - 1; i++) {
				current = current.next;
			}
			return current;
		}		
	
	}
	
	//override protected clone method
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
