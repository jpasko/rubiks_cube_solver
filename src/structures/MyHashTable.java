package structures;

import java.util.Arrays;

// Lightweight array-based hash table for storing heuristic moves and denoting
// which cube states are discovered.
// This implementation uses separate chaining to resolve collisions, at the cost
// of lookup performance (depends on the number of collisions) and some space
// increases due to linked list overhead
public class MyHashTable {
    private ListNode[] hashtable; // table of ListNode references
    private int capacity;
	
    // Creates the hash table of the given capacity.  Capacity should 
    // be a sufficiently large prime number to minimize collisions
    public MyHashTable(int capacity) {
	this.capacity = capacity;
	// in separate chaining, the hashtable is actually an array of linked list
	// nodes
	this.hashtable = new ListNode[capacity];
	// explicitly shows that all bins are empty in the hashtable:
	Arrays.fill(hashtable, null);
    }
	
    // returns true if the moves hash table includes the moves for the specified
    // corner cube; false otherwise
    public boolean containsCube(long cube) {
	boolean foundMoves = false;
	ListNode curr = hashtable[hash(cube)];
	while(curr != null) {
	    if(curr.getCube() == cube) {
		foundMoves = (curr.getMoves() != Byte.MIN_VALUE);
		break;
	    }
	    curr = curr.nextNode();
	}
	return foundMoves;
    }
	
    // returns true iff the specified corner cube has been discovered already.
    public boolean isDiscovered(long cube) {
	boolean discovered = false;
	ListNode curr = hashtable[hash(cube)];
	while(curr != null) {
	    if(curr.getCube() == cube) {
		discovered = curr.getIsDiscovered();
		break;
	    }
	    curr = curr.nextNode();
	}
	return discovered;
    }
	
    // denote that the corner cube state has been discovered
    public void discover(long cube) {
	if (hashtable[hash(cube)] == null) // nothing is hashed here yet
	    hashtable[hash(cube)] = new ListNode(cube, Byte.MIN_VALUE, true);
	else { // something hashed here; search linked list to see if it's cube
	    ListNode curr = hashtable[hash(cube)];
	    ListNode end = curr;
	    while(curr != null) {
		if(curr.getCube() == cube)
		    return; // no need to proceed; cube is in table, hence discovered
		end = curr;
		curr = curr.nextNode();
	    }
	    // We searched and searched but alas, no matching cube was found.
	    // Let us now add ours...
	    end.setNext(new ListNode(cube, Byte.MIN_VALUE, true));
	}
    }
	
    // associates the specified corner cube with a byte moves value, performing
    // an overwrite if it's already there
    public void put(long cube, byte moves) {
	if (hashtable[hash(cube)] == null) // nothing is hashed here yet
	    hashtable[hash(cube)] = new ListNode(cube, moves, true);
	else { // something hashed here; search linked list to see if it's cube
	    ListNode curr = hashtable[hash(cube)];
	    ListNode end = curr;
	    while(curr != null) {
		if(curr.getCube() == cube) {
		    curr.setMoves(moves);  // it's already here, so overwrite
		    return;
		}
		end = curr;
		curr = curr.nextNode();
	    }
	    // It's not been hashed yet; do it now by adding another node
	    end.setNext(new ListNode(cube, moves, true));
	}
    }
	
    // returns the value associated with the specified corner cube.  If the
    // specified corner cube is not a key associated with a moves value, it
    // will return 0
    public byte get(long cube) {
	byte moves = 0;
	ListNode curr = hashtable[hash(cube)];
	while(curr != null) {
	    if(curr.getCube() == cube) {
		moves = curr.getMoves();
		break;
	    }
	    curr = curr.nextNode();
	}
	if (moves == Byte.MIN_VALUE)
	    moves = 0;
	return moves;
    }
	
    // Look away!  It's my crappy hashing function...
    private int hash(long cube) {
	cube = cube & 0b1111111111111111111111111111111111111111L;	
	cube = (cube >> 2) | (cube << 38);
	cube = cube | (cube << 40);
	cube = cube ^ Long.rotateLeft(cube, 27);
	cube = cube ^ Long.rotateRight(cube, 7);
	cube = cube % (long) capacity;
	int hash = (int) cube;
	return Math.abs(hash);
    }
}
