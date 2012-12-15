package structures;

// Linked list node for use in separate chaining with MyHashTable
public class ListNode {
    private ListNode next;
    private long cube;   // the corner cube, which is the key
    private byte moves;  // number of moves, which is a value
    private boolean isDiscovered; // flag to denote whether key is already seen
	
    public ListNode(long cube, byte moves, boolean isDiscovered) {
	this.cube = cube;
	this.moves = moves;
	this.isDiscovered = isDiscovered;
	this.next = null;
    }
	
    public void setNext(ListNode nextNode) {
	this.next = nextNode;
    }
	
    public void setMoves(byte moves) {
	this.moves = moves;
    }
	
    public void discover() {
	this.isDiscovered = true;
    }
	
    public ListNode nextNode() {
	return next;
    }
	
    public long getCube() {
	return cube;
    }
	
    public byte getMoves() {
	return moves;
    }
	
    public boolean getIsDiscovered() {
	return isDiscovered;
    }
}
