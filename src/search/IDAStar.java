package search;

import rotations.*;
import structures.*;

// class IDAStar implements the IDA* search algorithm as a static
// method for use in solving Rubik's Cube
public class IDAStar {
    // God's Number: All possible states of the Rubik's Cube can be solved in 20
    // moves or fewer.  This will help cut down the search space, when coupled
    // with the heuristic
    public static final int GODS_NUMBER = 20;
	
    // corner cube goal state
    private static final long cornerGoal =
	0b1110011000101001000001100010000010000000L;
			
    // edge cube goal state
    private static final long edgeGoal =
	0b101101010010010100000111001100010100100000110001000001000000L;
	
    // String array of rotation types
    private static String[] rotationTypes =
    {"orange90CW", "orange90CCW", "orange180", "green90CW", "green90CCW",
     "green180", "red90CW", "red90CCW", "red180", "blue90CW", "blue90CCW",
     "blue180", "white90CW", "white90CCW", "white180", "yellow90CW",
     "yellow90CCW", "yellow180"};
	
    private static long numNodes;
	
    // The public method used to solve the cube via IDA*.  Input arguments are
    // the corner and edge cube compact forms, as well as tracer, which
    // references a CubeTracer object used to track and store moves applied to
    // the cube.  After the algorithm completes, tracer holds the solution if it
    // were found.  Returns true if the solution were found; false otherwise
    public static boolean findSolution(long cornerCube, long edgeCube, 
				       MyHashMap heuristic, CubeTracer tracer) {
	// first, search to the depth given by the heuristic at the starting
	// point:
	int costLimit = (int)heuristic.get(cornerCube);
	// perform at most God's Number of iterative deepenings:
	for(int i = 0; i < GODS_NUMBER; i++) {
	    System.out.println("Search depth: " + costLimit);
	    // set the number of nodes expanded to 0
	    numNodes = 0;
	    // update the cost limit (i.e. the iterative depth) at each search
	    costLimit =
		findSolutionHelper(cornerCube, edgeCube, costLimit, 0, tracer,
				   heuristic);
	    // print the number of nodes expanded while searching to the current
	    // depth
	    System.out.println("Number of nodes expanded: " + numNodes);
	    // cost_limit will be set to -1 if a solution is found at that depth
	    // (which must be optimal)
	    if(costLimit < 0) {
		return true; // solution was found, and is stored in tracer
	    }
	}
	return false; // no solution was found after searching to depth 20
    }
	
    // returns the new cost_limit if the solution is not found at the current
    // depth, otherwise return -1, indicating that a solution was found and is
    // contained within the CubeTracer object
    private static int findSolutionHelper(long cornerCube, long edgeCube,
					  int costLimit, int startCost,
					  CubeTracer tracer,
					  MyHashMap heuristic) {
	// increment the number of nodes
	numNodes++;
	// Indicate if we've found a solution (stored in the CubeTracer object)
	if (cornerCube == cornerGoal && edgeCube == edgeGoal)
	    return -1;
	// Get the minimum estimate of the solution length from the heuristic
	// at the starting point
	int minCost = startCost + (int)heuristic.get(cornerCube);
	// If this estimate exceeds the cost limit, we won't find the solution
	// along this path.  Return the value of minCost (our estimate for how
	// long the solution is along this path)
	if (minCost > costLimit)
	    return minCost;
	// set the new cost limit to God's Number
        int nextCostLimit = GODS_NUMBER;
        // for all neighbors 1 move away:
        for (String type : rotationTypes) {
	    // compute the next corner cube
	    long nextCornerCube = CornerCubeMoves.rotate(cornerCube, type);
	    // compute the next edge cube
	    long nextEdgeCube = EdgeCubeMoves.rotate(edgeCube, type);
	    // store the move type in the tracer
	    tracer.writeMove(type);
	    // make a recursive call to perform the search on the neighbor,
	    // noting that startCost is incremented
	    int newCostLimit = findSolutionHelper(nextCornerCube, nextEdgeCube,
						  costLimit, (startCost + 1),
						  tracer, heuristic);
	    // if a solution is found on one of these subtrees, return
	    // immediately
	    if(newCostLimit < 0)
		return -1;
	    // the only way this will be executed is we didn't find a solution
	    // along this path find the minimum estimated solution length of all
	    // subtrees, and set as the cost limit for the next iteration
	    nextCostLimit = Math.min(nextCostLimit, newCostLimit);
            // erase the last move since we're backtracking (won't be erased
	    // upon finding a solution)
            tracer.eraseMove();
        }
        // nextCostLimit is min(f(node) for all nodes reached at costLimit)
        return nextCostLimit; 
    }
}
