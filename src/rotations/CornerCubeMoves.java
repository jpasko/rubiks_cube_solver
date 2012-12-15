package rotations;

// Class CubeMoves provides static methods to rotate corner cube faces
// Each method takes a corner cube input, and returns a corner cube 
// output.
public class CornerCubeMoves {
	// Corner cube state representation:
	// The state of the corner cube is abstractly stored as an 8-tuple where
	// each element is a concatenated 5-bit number; the least significant 2
	// bits store the orientation of the corner cubie occupying the corner 
	// encoded in the most significant 3 bits.
	//
	// The following represents the corner slot encoding:
	//
	//             White
	//				Face
	//          ------------
	//         | 0        1 |
	//   Blue  |   Orange   |
	//   Face  |    Face    |
	//         | 2        3 |
	//          ------------
	//
	//             White
	//				Face
	//          ------------
	//         | 4        5 |
	//  Green  |    Red     |
	//   Face  |    Face    |
	//         | 6        7 |
	//          ------------
	// 
	// The corner cubie encodings are:
	//
	//     	Orange/Blue/White:   0
	//     	Orange/White/Green:  1
	//      Orange/Yellow/Blue:  2
	//      Orange/Green/Yellow: 3
	//      Red/Green/White:     4
	//      Red/White/Blue:      5
	//      Red/Yellow/Green:    6
	//      Red/Blue/Yellow:     7
	//
	// The orientations of the corner cubies are described as such:
	// 		
	//    To find the orientation of a cubie, always index by the rotational
	//    position of its Orange or Red color relative to the Orange or Red
	//    face (whichever it belongs to).
	//
	// The orientations are encoded in the following manner:
	//
	//     	Orange or Red color is ON the Orange or Red face:        0
	//      Orange or Red color is CLOCKWISE ONE away from the face: 1
	//     	Orange or Red color is CLOCKWISE TWO away from the face: 2
	//
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//
	// numBits is the number of bits required to store the state of a single
	// corner.  Since there are 8 corners, 40 bits is required to store the
	// state of the corner cube.
	private static final int numBits = 5;
	
	// bit masks used to retrieve the states of the fixed corners
	private static final long corner0Mask = 0b11111L;
	private static final long corner1Mask = corner0Mask << numBits;
	private static final long corner2Mask = corner1Mask << numBits;
	private static final long corner3Mask = corner2Mask << numBits;
	private static final long corner4Mask = corner3Mask << numBits;
	private static final long corner5Mask = corner4Mask << numBits;
	private static final long corner6Mask = corner5Mask << numBits;
	private static final long corner7Mask = corner6Mask << numBits;
	private static final long orientMask0 = 0b11L;
	private static final long orientMask1 = orientMask0 << numBits;
	private static final long orientMask2 = orientMask1 << numBits;
	private static final long orientMask3 = orientMask2 << numBits;
	private static final long orientMask4 = orientMask3 << numBits;
	private static final long orientMask5 = orientMask4 << numBits;
	private static final long orientMask6 = orientMask5 << numBits;
	private static final long orientMask7 = orientMask6 << numBits;
	private static final long cubieMask0 = 0b11100L;
	private static final long cubieMask1 = cubieMask0 << numBits;
	private static final long cubieMask2 = cubieMask1 << numBits;
	private static final long cubieMask3 = cubieMask2 << numBits;
	private static final long cubieMask4 = cubieMask3 << numBits;
	private static final long cubieMask5 = cubieMask4 << numBits;
	private static final long cubieMask6 = cubieMask5 << numBits;
	private static final long cubieMask7 = cubieMask6 << numBits;
	private static final long moveCounterMask = 0b11111111L << 56;
	private static final long clearCounter =
	    corner0Mask | corner1Mask | corner2Mask |corner3Mask |
	    corner4Mask | corner5Mask | corner6Mask | corner7Mask;
	
	// bit clearing
	private static final long clearOrange =
	    corner4Mask | corner5Mask | corner6Mask | corner7Mask |
	    moveCounterMask;
	private static final long clearGreen =
	    corner0Mask | corner2Mask | corner5Mask | corner7Mask |
	    moveCounterMask;
	private static final long clearWhite =
	    corner2Mask | corner3Mask | corner6Mask | corner7Mask |
	    moveCounterMask;
	private static final long clearBlue =
	    corner1Mask | corner3Mask | corner4Mask | corner6Mask |
	    moveCounterMask;
	private static final long clearYellow =
	    corner0Mask | corner1Mask | corner4Mask | corner5Mask |
	    moveCounterMask;
	private static final long clearRed =
	    corner0Mask | corner1Mask | corner2Mask | corner3Mask |
	    moveCounterMask;
	
	// temporary storage for cube rotations
	private static long temp;
	
	// prints the cube for debugging purposes
	public static void printCornerCube(long cube) {
		System.out.print("C0: ");
		System.out.print(((cube & 0b11100) >> 2) + ", " +
				 (cube & 0b11));
		cube = cube >> numBits;
		System.out.print("; C1: ");
		System.out.print(((cube & 0b11100) >> 2) + ", " +
				 (cube & 0b11));
		cube = cube >> numBits;
		System.out.print("; C2: ");
		System.out.print(((cube & 0b11100) >> 2) + ", " +
				 (cube & 0b11));
		cube = cube >> numBits;
		System.out.print("; C3: ");
		System.out.print(((cube & 0b11100) >> 2) + ", " +
				 (cube & 0b11));
		cube = cube >> numBits;
		System.out.print("; C4: ");
		System.out.print(((cube & 0b11100) >> 2) + ", " +
				 (cube & 0b11));
		cube = cube >> numBits;
		System.out.print("; C5: ");
		System.out.print(((cube & 0b11100) >> 2) + ", " +
				 (cube & 0b11));
		cube = cube >> numBits;
		System.out.print("; C6: ");
		System.out.print(((cube & 0b11100) >> 2) + ", " +
				 (cube & 0b11));
		cube = cube >> numBits;		
		System.out.print("; C7: ");
		System.out.println(((cube & 0b11100) >> 2) + ", " +
				   (cube & 0b11));
	}
	
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// Face rotation methods...
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	// Generic rotation method for the corner cube, which includes a move
        // counter
	public static long rotateWithCounter(long cube, String rotationType) {
		if(rotationType.equals("orange90CW"))
			return rotateOrange90CW(cube);
		else if(rotationType.equals("orange90CCW"))
			return rotateOrange90CCW(cube);
		else if(rotationType.equals("orange180"))
			return rotateOrange180(cube);
		else if(rotationType.equals("red90CW"))
			return rotateRed90CW(cube);
		else if(rotationType.equals("red90CCW"))
			return rotateRed90CCW(cube);
		else if(rotationType.equals("red180"))
			return rotateRed180(cube);
		else if(rotationType.equals("green90CW"))
			return rotateGreen90CW(cube);
		else if(rotationType.equals("green90CCW"))
			return rotateGreen90CCW(cube);
		else if(rotationType.equals("green180"))
			return rotateGreen180(cube);
		else if(rotationType.equals("white90CW"))
			return rotateWhite90CW(cube);
		else if(rotationType.equals("white90CCW"))
			return rotateWhite90CCW(cube);
		else if(rotationType.equals("white180"))
			return rotateWhite180(cube);
		else if(rotationType.equals("blue90CW"))
			return rotateBlue90CW(cube);
		else if(rotationType.equals("blue90CCW"))
			return rotateBlue90CCW(cube);
		else if(rotationType.equals("blue180"))
			return rotateBlue180(cube);
		else if(rotationType.equals("yellow90CW"))
			return rotateYellow90CW(cube);
		else if(rotationType.equals("yellow90CCW"))
			return rotateYellow90CCW(cube);
		else if(rotationType.equals("yellow180"))
			return rotateYellow180(cube);
		else
			throw new IllegalArgumentException("Invalid rotation type");
	}
	
	// rotate as rotateWithCounter method, but clear the counter as well
	public static long rotate(long cube, String rotationType) {
		long rotatedCube = rotateWithCounter(cube, rotationType);
		// clear the counter
		return (rotatedCube & clearCounter);
	}
	
	// ORANGE
	public static long rotateOrange90CW(long cube) {
		temp = cube;
		// clear the bits to be changed
		cube = cube & clearOrange;
		// perform swaps; no need to change cubie orientations; this is
		// the orange face
		cube = cube | (corner2Mask & temp) >> 2*numBits; // 0 <- 2
		cube = cube | (corner0Mask & temp) << numBits; // 1 <- 0 
		cube = cube | (corner3Mask & temp) >> numBits; // 2 <- 3
		cube = cube | (corner1Mask & temp) << 2*numBits; // 3 <- 1
		// increment move counter
		cube = (cube & clearCounter) |
		    ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	public static long rotateOrange90CCW(long cube) { // tested
		temp = cube;
		cube = cube & clearOrange;
		cube = cube | (corner1Mask & temp) >> numBits; // 0 <- 1
		cube = cube | (corner3Mask & temp) >> 2*numBits; // 1 <- 3 
		cube = cube | (corner0Mask & temp) << 2*numBits; // 2 <- 0
		cube = cube | (corner2Mask & temp) << numBits; // 3 <- 2
		// increment move counter
		cube = (cube & clearCounter) |
		    ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}

	public static long rotateOrange180(long cube) { // tested
		temp = cube;
		cube = cube & clearOrange;
		cube = cube | (corner3Mask & temp) >> 3*numBits; // 0 <- 3
		cube = cube | (corner2Mask & temp) >> numBits; // 1 <- 2
		cube = cube | (corner1Mask & temp) << numBits; // 2 <- 1
		cube = cube | (corner0Mask & temp) << 3*numBits; // 3 <- 0
		// increment move counter
		cube = (cube & clearCounter) |
		    ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	// RED
	public static long rotateRed90CW(long cube) {
		temp = cube;
		cube = cube & clearRed;
		cube = cube | (corner6Mask & temp) >> 2*numBits; // 4 <- 6
		cube = cube | (corner4Mask & temp) << numBits; // 5 <- 4
		cube = cube | (corner7Mask & temp) >> numBits; // 6 <- 7
		cube = cube | (corner5Mask & temp) << 2*numBits; // 7 <- 5
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	public static long rotateRed90CCW(long cube) {
		temp = cube;
		cube = cube & clearRed;
		cube = cube | (corner5Mask & temp) >> numBits; // 4 <- 5
		cube = cube | (corner7Mask & temp) >> 2*numBits; // 5 <- 7 
		cube = cube | (corner4Mask & temp) << 2*numBits; // 6 <- 4
		cube = cube | (corner6Mask & temp) << numBits; // 7 <- 6
		// increment move counter
		cube = (cube & clearCounter) |
		    ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}

	public static long rotateRed180(long cube) {
		temp = cube;
		cube = cube & clearRed;
		cube = cube | (corner7Mask & temp) >> 3*numBits; // 4 <- 7
		cube = cube | (corner6Mask & temp) >> numBits; // 5 <- 6
		cube = cube | (corner5Mask & temp) << numBits; // 6 <- 5
		cube = cube | (corner4Mask & temp) << 3*numBits; // 7 <- 4
		// increment move counter
		cube = (cube & clearCounter) |
		    ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	// GREEN
	public static long rotateGreen90CW(long cube) { // tested
		temp = cube;
		cube = cube & clearGreen;
		cube = cube | (((corner3Mask & temp) >> 2*numBits) & cubieMask1); // 1 <- 3++
		cube = cube | (((((corner3Mask & temp & orientMask3) >> 3*numBits) + 1) % 3) << numBits);
		
		cube = cube | (((corner6Mask & temp) >> 3*numBits) & cubieMask3); // 3 <- 6--
		cube = cube | (((((corner6Mask & temp & orientMask6) >> 6*numBits) + 2) % 3) << 3*numBits);
		
		cube = cube | (((corner1Mask & temp) << 3*numBits) & cubieMask4); // 4 <- 1--
		cube = cube | (((((corner1Mask & temp & orientMask1) >> 1*numBits) + 2) % 3) << 4*numBits);
		
		cube = cube | (((corner4Mask & temp) << 2*numBits) & cubieMask6); // 6 <- 4++
		cube = cube | (((((corner4Mask & temp & orientMask4) >> 4*numBits) + 1) % 3) << 6*numBits);
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	public static long rotateGreen90CCW(long cube) {
		temp = cube;
		cube = cube & clearGreen;
		cube = cube | (((corner4Mask & temp) >> 3*numBits) & cubieMask1); // 1 <- 4++
		cube = cube | (((((corner4Mask & temp & orientMask4) >> 4*numBits) + 1) % 3) << numBits);
		
		cube = cube | (((corner1Mask & temp) << 2*numBits) & cubieMask3); // 3 <- 1--
		cube = cube | (((((corner1Mask & temp & orientMask1) >> numBits) + 2) % 3) << 3*numBits);
		
		cube = cube | (((corner6Mask & temp) >> 2*numBits) & cubieMask4); // 4 <- 6--
		cube = cube | (((((corner6Mask & temp & orientMask6) >> 6*numBits) + 2) % 3) << 4*numBits);
		
		cube = cube | (((corner3Mask & temp) << 3*numBits) & cubieMask6); // 6 <- 3++
		cube = cube | (((((corner3Mask & temp & orientMask3) >> 3*numBits) + 1) % 3) << 6*numBits);
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}

	public static long rotateGreen180(long cube) {
		temp = cube;
		cube = cube & clearGreen;
		cube = cube | (corner6Mask & temp) >> 5*numBits; // 1 <- 6
		cube = cube | (corner4Mask & temp) >> numBits; // 3 <- 4
		cube = cube | (corner3Mask & temp) << numBits; // 4 <- 3
		cube = cube | (corner1Mask & temp) << 5*numBits; // 6 <- 1
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	// WHITE
	public static long rotateWhite90CW(long cube) {
		temp = cube;
		cube = cube & clearWhite;
		cube = cube | (((corner1Mask & temp) >> numBits) & cubieMask0); // 0 < 1++
		cube = cube | (((((corner1Mask & temp & orientMask1) >> numBits) + 1) % 3));
		
		cube = cube | (((corner4Mask & temp) >> 3*numBits) & cubieMask1); // 1 <- 4--
		cube = cube | (((((corner4Mask & temp & orientMask4) >> 4*numBits) + 2) % 3) << numBits);
		
		cube = cube | (((corner5Mask & temp) >> numBits) & cubieMask4); // 4 <- 5++
		cube = cube | (((((corner5Mask & temp & orientMask5) >> 5*numBits) + 1) % 3) << 4*numBits);
		
		cube = cube | (((corner0Mask & temp) << 5*numBits) & cubieMask5); // 5 <- 0--
		cube = cube | (((((corner0Mask & temp & orientMask0)) + 2) % 3) << 5*numBits);
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	public static long rotateWhite90CCW(long cube) {
		temp = cube;
		cube = cube & clearWhite;
		cube = cube | (((corner5Mask & temp) >> 5*numBits) & cubieMask0); // 0 <- 5++
		cube = cube | (((((corner5Mask & temp & orientMask5) >> 5*numBits) + 1) % 3));
		
		cube = cube | (((corner0Mask & temp) << numBits) & cubieMask1); // 1 <- 0--
		cube = cube | (((((corner0Mask & temp & orientMask0)) + 2) % 3) << numBits);
		
		cube = cube | (((corner1Mask & temp) << 3*numBits) & cubieMask4); // 4 <- 1++
		cube = cube | (((((corner1Mask & temp & orientMask1) >> numBits) + 1) % 3) << 4*numBits);
		
		cube = cube | (((corner4Mask & temp) << numBits) & cubieMask5); // 5 <- 4--
		cube = cube | (((((corner4Mask & temp & orientMask4) >> 4*numBits) + 2) % 3) << 5*numBits);
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}

	public static long rotateWhite180(long cube) {
		temp = cube;
		cube = cube & clearWhite;
		cube = cube | (corner4Mask & temp) >> 4*numBits; // 0 <- 4
		cube = cube | (corner5Mask & temp) >> 4*numBits; // 1 <- 5
		cube = cube | (corner0Mask & temp) << 4*numBits; // 4 <- 0
		cube = cube | (corner1Mask & temp) << 4*numBits; // 5 <- 1
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}

	// BLUE
	public static long rotateBlue90CW(long cube) {
		temp = cube;
		cube = cube & clearBlue;
		cube = cube | (((corner5Mask & temp) >> 5*numBits) & cubieMask0); // 0 <- 5--
		cube = cube | (((((corner5Mask & temp & orientMask5) >> 5*numBits) + 2) % 3));
		
		cube = cube | (((corner0Mask & temp) << 2*numBits) & cubieMask2); // 2 <- 0++
		cube = cube | (((((corner0Mask & temp & orientMask0)) + 1) % 3) << 2*numBits);
		
		cube = cube | (((corner7Mask & temp) >> 2*numBits) & cubieMask5); // 5 <- 7++
		cube = cube | (((((corner7Mask & temp & orientMask7) >> 7*numBits) + 1) % 3) << 5*numBits);
		
		cube = cube | (((corner2Mask & temp) << 5*numBits) & cubieMask7); // 7 <- 2--
		cube = cube | (((((corner2Mask & temp & orientMask2) >> 2*numBits) + 2) % 3) << 7*numBits);
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	public static long rotateBlue90CCW(long cube) {
		temp = cube;
		cube = cube & clearBlue;
		cube = cube | (((corner2Mask & temp) >> 2*numBits) & cubieMask0); // 0 <- 2--
		cube = cube | (((((corner2Mask & temp & orientMask2) >> 2*numBits) + 2) % 3));
		
		cube = cube | (((corner7Mask & temp) >> 5*numBits) & cubieMask2); // 2 <- 7++
		cube = cube | (((((corner7Mask & temp & orientMask7) >> 7*numBits) + 1) % 3) << 2*numBits);
		
		cube = cube | (((corner0Mask & temp) << 5*numBits) & cubieMask5); // 5 <- 0++
		cube = cube | (((((corner0Mask & temp & orientMask0)) + 1) % 3) << 5*numBits);
		
		cube = cube | (((corner5Mask & temp) << 2*numBits) & cubieMask7); // 7 <- 5--
		cube = cube | (((((corner5Mask & temp & orientMask5) >> 5*numBits) + 2) % 3) << 7*numBits);
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}

	public static long rotateBlue180(long cube) {
		temp = cube;
		cube = cube & clearBlue;
		cube = cube | (corner7Mask & temp) >> 7*numBits; // 0 <- 7
		cube = cube | (corner5Mask & temp) >> 3*numBits; // 2 <- 5
		cube = cube | (corner2Mask & temp) << 3*numBits; // 5 <- 2
		cube = cube | (corner0Mask & temp) << 7*numBits; // 7 <- 0
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	// YELLOW
	public static long rotateYellow90CW(long cube) {
		temp = cube;
		cube = cube & clearYellow;
		cube = cube | (((corner7Mask & temp) >> 5*numBits) & cubieMask2); // 2 <- 7--
		cube = cube | (((((corner7Mask & temp & orientMask7) >> 7*numBits) + 2) % 3) << 2*numBits);
		
		cube = cube | (((corner2Mask & temp) << numBits) & cubieMask3); // 3 <- 2++
		cube = cube | (((((corner2Mask & temp & orientMask2) >> 2*numBits) + 1) % 3) << 3*numBits);
		
		cube = cube | (((corner3Mask & temp) << 3*numBits) & cubieMask6); // 6 <- 3--
		cube = cube | (((((corner3Mask & temp & orientMask3) >> 3*numBits) + 2) % 3) << 6*numBits);
		
		cube = cube | (((corner6Mask & temp) << numBits) & cubieMask7); // 7 <- 6++
		cube = cube | (((((corner6Mask & temp & orientMask6) >> 6*numBits) + 1) % 3) << 7*numBits);
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
	
	public static long rotateYellow90CCW(long cube) {
		temp = cube;
		cube = cube & clearYellow;
		cube = cube | (((corner3Mask & temp) >> numBits) & cubieMask2); // 2 <- 3--
		cube = cube | (((((corner3Mask & temp & orientMask3) >> 3*numBits) + 2) % 3) << 2*numBits);
		
		cube = cube | (((corner6Mask & temp) >> 3*numBits) & cubieMask3); // 3 <- 6++
		cube = cube | (((((corner6Mask & temp & orientMask6) >> 6*numBits) + 1) % 3) << 3*numBits);
		
		cube = cube | (((corner7Mask & temp) >> numBits) & cubieMask6); // 6 <- 7--
		cube = cube | (((((corner7Mask & temp & orientMask7) >> 7*numBits) + 2) % 3) << 6*numBits);
		
		cube = cube | (((corner2Mask & temp) << 5*numBits) & cubieMask7); // 7 <- 2++
		cube = cube | (((((corner2Mask & temp & orientMask2) >> 2*numBits) + 1) % 3) << 7*numBits);
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}

	public static long rotateYellow180(long cube) {
		temp = cube;
		cube = cube & clearYellow;
		cube = cube | (corner6Mask & temp) >> 4*numBits; // 2 <- 6
		cube = cube | (corner7Mask & temp) >> 4*numBits; // 3 <- 7
		cube = cube | (corner2Mask & temp) << 4*numBits; // 6 <- 2
		cube = cube | (corner3Mask & temp) << 4*numBits; // 7 <- 3
		// increment move counter
		cube = (cube & clearCounter) | ((cube & moveCounterMask) + (1L << 56));
		return cube;
	}
}
