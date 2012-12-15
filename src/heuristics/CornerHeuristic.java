package heuristics;

import rotations.*;
import structures.*;

// Computes the corner cube heuristic for use in the A* search of the state
// space of the full 3x3x3 Rubik's Cube
public class CornerHeuristic {
    // Performs a breadth-first enumeration of the entire state space of the
    // corner cube, mapping each state to the number of moves required to first
    // reach it
    //
    // Mask to remove the cube state encoding when mapping move lengths
    private final long moveCounterMask = 0b11111111L << 56;
    // Mask to remove the counter
    private final long cubeStateMask =
	0b1111111111111111111111111111111111111111L;
	
    // String array of rotation types
    private String[] rotationTypes = {"orange90CW", "orange90CCW", "orange180",
				      "green90CW", "green90CCW",
				      "green180", "red90CW", "red90CCW", "red180",
				      "blue90CW", "blue90CCW", "blue180", 
				      "white90CW", "white90CCW", "white180",
				      "yellow90CW", "yellow90CCW", "yellow180"};
	
    // goal (solved) state of the corner cube, described in accordance to the
    // details listed in CornerCubeMoves.java
    private long goal = 0b1110011000101001000001100010000010000000L;
	
    // counter to see how many states were discovered
    private int states = 0;

    // Queue for the breadth-first search
    private MyQueue q = new MyArrayQueue(89000000);
	
    // Custom hash table, which associates the corner cube with the number of
    // moves it was first reached in
    private MyHashMap h = new MyHashMap(180000000);
	
    // Another hash table, which does its best to avoid collisions and works as
    // a lightweight Set ADT, to denote which corner cube states have been
    // discovered.
    // Try some primes, such as: 1073676287, 776531419
    private MyDiscoveryTable d = new MyDiscoveryTable(500000000);
	
    // Constructor enumerates the corner cube states and fills the hash table
    public CornerHeuristic() {
	System.out.println("Generating corner cube heuristic...");
	// queue the goal state
	q.add(goal);
	// mark it as discovered
	d.discover(goal);
	// Now, fire up BFS
	while (!q.isEmpty()) {
	    // remove a cube state from the queue
	    long cube = q.remove();
	    // determine all its neighbors; queue if undiscovered
	    for (String type : rotationTypes) {
		// perform the rotation, using the method that tracks move
		// numbers
		long rotatedCube =
		    CornerCubeMoves.rotateWithCounter(cube, type);
		// queue if the rotated cube hasn't been queued already
		if (!d.isDiscovered((rotatedCube & cubeStateMask))) {
		    q.add(rotatedCube); 
		    // note that you've queued the cube:
		    d.discover((rotatedCube & cubeStateMask));
		}
	    }
	    // map the corner cube state to the move counter (taking care to
	    // mask off the appropriate values)
	    h.put((cube & cubeStateMask),
		  (byte)((cube & moveCounterMask) >> 56));
	    states++;
	}
	System.out.println("States discovered: " + states);
	// System.out.println("Collisions detected: " + h.getCollisions());
    }
	
    // Returns the hashtable which stores the heuristic
    public MyHashMap getHeuristic() {
	return h;
    }
}
