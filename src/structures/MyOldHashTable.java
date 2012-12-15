package structures;

import java.util.Arrays;

// Lightweight array-based hash table for storing heuristic moves and denoting
// which cube states are discovered
// Does nothing to handle collisions!!!
public class MyOldHashTable {
    private byte[] movesTable;
    private boolean[] discoveryTable;
    private int capacity;
	
    // Creates the hash table of the given capacity.  Capacity should 
    // be a sufficiently large prime number to prevent my crappy hash function
    // from colliding on any of the ~89 million corner cube states
    public MyOldHashTable(int capacity) {
	this.capacity = capacity;
	movesTable = new byte[capacity];
	discoveryTable = new boolean[capacity];
	// The following merely initializes the tables to empty
	Arrays.fill(movesTable, Byte.MIN_VALUE);
	Arrays.fill(discoveryTable, false);
    }
	
    // returns true if the moves hash table includes the moves for the specified corner
    // cube; false otherwise
    public boolean containsCube(long cube) {
	return movesTable[hash(cube)] != Byte.MIN_VALUE;
    }
	
    // returns true if the specified corner cube has been discovered already; else false
    public boolean isDiscovered(long cube) {
	return discoveryTable[hash(cube)];
    }
	
    // updates the discovery table to denote that the corner cube state has been discovered
    public void discover(long cube) {
	discoveryTable[hash(cube)] = true;
    }
	
    // associates the specified corner cube with a byte moves value
    public void put(long cube, byte moves) {
	movesTable[hash(cube)] = moves;
    }
	
    // returns the value associated with the specified corner cube.  If the specified
    // corner cube is not a key associated with a moves value, it will return
    // Byte.MIN_VALUE
    public byte get(long cube) {
	return movesTable[hash(cube)];
    }
	
    // Look away!  It's my crappy hash function...
    private int hash(long cube) {
	cube = cube & 0b1111111111111111111111111111111111111111L;	
	//		// Mix the bits around a bit:
	//		cube = (cube >> 2) | (cube << 38); //
	//		cube = cube * 23;                  // 17 -- 84118006
	//		// make the hash positive (2s comp - need leading zeros)
	//		cube = cube % (long)capacity;
	//		int hash =  (int) cube;
	//		return Math.abs(hash);
	//		int hash = (int) ((cube * 1234567L) >> (64 - 30));
	//		cube = cube | (cube << 40);
	//		cube = cube ^ Long.rotateLeft(cube, 7);
	cube = cube ^ Long.rotateRight(cube, 1);
	cube = cube % (long) capacity; //
	int hash = (int) cube;         //
	return Math.abs(hash);
    }
}
