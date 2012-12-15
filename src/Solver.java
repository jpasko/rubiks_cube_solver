import heuristics.*;
import input.*;
import search.*;
import structures.*;

// This is the main client class of the RubiksCubeSolver project.  It's 
// responsible for orchestrating the entire operation, from obtaining
// the cube state from the console, converting it to a compact form, 
// computing the heuristic function, passing the compact cube to the IDA*
// search, utilizing static cube rotation methods, and printing the 
// solution when found
public class Solver {	
    public static void main(String args[]) {
	System.out.print("This application finds the shortest solution");
	System.out.println("for your Rubik's Cube.");
	System.out.println("Please wait while data is being initialized.");
	CornerHeuristic h = new CornerHeuristic();
	MyHashMap heuristic = h.getHeuristic();
	CubeEntry c = new CubeEntry();
	CubeTracer tracer = new CubeTracer(IDAStar.GODS_NUMBER);
	do {
	    do {
		c.enterCubeState();
		c.printCube();
	    } while(!c.isCubeOK());
	    c.createCompactCube();
	    long cornerCube = c.getCornerCube();
	    long edgeCube = c.getEdgeCube();
	    boolean foundSolution =
		IDAStar.findSolution(cornerCube, edgeCube, heuristic, tracer);
	    if (foundSolution) {
		System.out.println("Solution:");
		String[] solution = tracer.getSequence();
		for (String move : solution)
		    if (move != null)
			System.out.println(move);
	    } else
		System.out.println("No solution found.");
	    tracer.reset();
	} while(c.solveAnotherCube());
    }
}
