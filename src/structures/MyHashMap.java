package structures;

import java.util.Arrays;

// Very lightweight hash table.  Uses probing to resolve collisions
public class MyHashMap {
    // movesTable is the hash table for the move length values
    private byte[] movesTable;
    private int capacity;
    // cubeTable stores the cubes that map to a particular bin
    private long[] cubeTable;
	
    // Creates the hash table of the given capacity.  Capacity should 
    // be a sufficiently large prime number to minimize collisions
    public MyHashMap(int capacity) {
	this.capacity = capacity;
	movesTable = new byte[capacity];
	cubeTable = new long[capacity];
	// The following merely initializes the tables to empty
	Arrays.fill(movesTable, (byte)-1);
	Arrays.fill(cubeTable, (long)0);
    }
	
    // Associates the specified corner cube with a byte moves value.  Uses 
    // probing to resolve collisions
    public void put(long cube, byte moves) {
	int index = hash(cube);
	// if the slot is currently occupied, use linear probing to find
	// an empty slot
	while (movesTable[index] != (byte)-1) {
	    if (cube == cubeTable[index])
		break;
	    index = (index + 1) % capacity;
	}
	// write the values to the empty slot
	movesTable[index] = moves;
	cubeTable[index] = cube;
    }
	
    // returns the value associated with the specified corner cube.  If the specified
    // corner cube is not a key associated with a moves value, it will return -1
    public byte get(long cube) {
	int index = hash(cube);
	// use linear probing to see if the value is in the hash table
	while (movesTable[index] != (byte)-1) {
	    if (cube == cubeTable[index])
		break;
	    index = (index + 1) % capacity;
	}
	return movesTable[index];
    }
	
    // Look away!  It's my crappy hash function...
    private int hash(long cube) {
	cube = cube & 0b1111111111111111111111111111111111111111L;	
	// Mix the bits around a bit:
	cube = (cube >> 2) | (cube << 38);
	cube = cube * 37; //17
	cube = cube % (long) capacity;
	int hash = (int) cube;
	return Math.abs(hash);
    }
}
