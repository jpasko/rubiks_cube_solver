package structures;

import java.util.Arrays;

// Class MyDiscoveryTable implements some of the functionality 
// of the SET ADT.  In particular, it maintains knowledge of 
// which corner cubes have been discovered and which have not, and
// allows the client to mark cube states as discovered
public class MyDiscoveryTable {
    /*
     * DESIGN:
     * Uses a large number of discovery tables with different hashes.
     * 
     * When checking if a cube is mapped already (isDiscovered), only
     * return true if it is discovered in all discovery tables.  If it
     * is not discovered in all tables, and you'd like to note its discovery,
     * then set the flag in all discovery tables
     */
    // these are the discoveryTables
    private boolean[] discoveryTable0;
    private boolean[] discoveryTable1;
    private boolean[] discoveryTable2;
    private boolean[] discoveryTable3;
    private boolean[] discoveryTable4;
    private boolean[] discoveryTable5;
    private int capacity;
	
    // Creates the hash table of the given capacity.  Capacity should 
    // be a sufficiently large prime number to minimize collisions
    public MyDiscoveryTable(int capacity) {
	this.capacity = capacity;
	discoveryTable0 = new boolean[capacity];
	discoveryTable1 = new boolean[capacity];
	discoveryTable2 = new boolean[capacity];
	discoveryTable3 = new boolean[capacity];
	discoveryTable4 = new boolean[capacity];
	discoveryTable5 = new boolean[capacity];
	// The following merely initializes the tables to empty
	Arrays.fill(discoveryTable0, false);
	Arrays.fill(discoveryTable1, false);
	Arrays.fill(discoveryTable2, false);
	Arrays.fill(discoveryTable3, false);
	Arrays.fill(discoveryTable4, false);
	Arrays.fill(discoveryTable5, false);
    }
	
    // returns true if the specified corner cube has been discovered already in
    // all tables; else false
    public boolean isDiscovered(long cube) {
	return (discoveryTable0[hash0(cube)] & discoveryTable1[hash1(cube)] &
		discoveryTable2[hash2(cube)] & discoveryTable3[hash3(cube)] &
		discoveryTable4[hash4(cube)] & discoveryTable5[hash5(cube)]);
    }
	
    // updates the discovery table to denote that the corner cube state has been
    // discovered
    public void discover(long cube) {
	discoveryTable0[hash0(cube)] = true;
	discoveryTable1[hash1(cube)] = true;
	discoveryTable2[hash2(cube)] = true;
	discoveryTable3[hash3(cube)] = true;
	discoveryTable4[hash4(cube)] = true;
	discoveryTable5[hash5(cube)] = true;
    }
	
    // hash functions follow...
    private int hash0(long cube) {
	cube = cube & 0b1111111111111111111111111111111111111111L;	
	// Mix the bits around a bit:
	cube = (cube >> 2) | (cube << 38);
	cube = cube * 17;
	cube = cube % (long) capacity;
	int hash = (int) cube;
	return Math.abs(hash);
    }
	
    private int hash1(long cube) {
	cube = cube & 0b1111111111111111111111111111111111111111L;	
	// Mix the bits around a bit:
	cube = (cube >> 2) | (cube << 38);
	cube = cube * 23;
	//cube = cube ^ Long.rotateRight(cube, 1);
	cube = cube % (long) capacity;
	int hash = (int) cube;
	return Math.abs(hash);
    }
	
    private int hash2(long cube) {
	cube = cube & 0b1111111111111111111111111111111111111111L;	
	// Mix the bits around a bit:
	cube = (cube >> 2) | (cube << 38);
	cube = cube ^ Long.rotateLeft(cube, 7);
	cube = cube % (long) capacity;
	int hash = (int) cube;
	return Math.abs(hash);
    }
	
    private int hash3(long cube) {
	cube = cube & 0b1111111111111111111111111111111111111111L;	
	// Mix the bits around a bit:
	cube = (cube >> 2) | (cube << 38);
	cube = cube ^ Long.rotateRight(cube, 7);
	cube = cube % (long) capacity;
	int hash = (int) cube;
	return Math.abs(hash);
    }

    private int hash4(long cube) {
	cube = cube & 0b1111111111111111111111111111111111111111L;	
	// Mix the bits around a bit:
	cube = (cube >> 2) | (cube << 38);
	cube = cube * 7;
	cube = cube % (long) capacity;
	int hash = (int) cube;
	return Math.abs(hash);
    }
	
    private int hash5(long cube) {
	cube = cube & 0b1111111111111111111111111111111111111111L;	
	// Mix the bits around a bit:
	cube = (cube >> 2) | (cube << 38);
	cube = cube * 3;
	cube = cube ^ Long.rotateRight(cube, 2);
	cube = cube % (long) capacity;
	int hash = (int) cube;
	return Math.abs(hash);
    }
}
