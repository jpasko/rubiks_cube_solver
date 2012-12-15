package rotations;

// Similarly to class CornerCubeMoves, this class provides static
// methods to rotate the edge cubies
public class EdgeCubeMoves {
    //
    //  Edge cubie position encodings:
    //
    //                  -----0------
    //  	       |            |
    //        	       1   White    2
    //        	       |    Face    |
    //        	       |            |
    //         	    	-----3------
    //   ----(1)-----    ----(3)-----    ----(2)-----    ----(0)-----
    //  |            |  |            |  |	     |  |	     |
    //  4    Blue    5 (5)   Orange  6 (6)  Green    7 (7)   Red    (4)
    //  |    Face    |  |    Face    |  |    Face    |  |    Face    |
    //  |      	     |  |            |  |            |  |            |
    //   -----8------    -----9------    -----10-----    -----11-----
    //   		 ----(9)-----
    //  	    	|            |
    //        	       (8)  Yellow  (10)
    //                  |    Face    |
    //        	  	|            |
    //         	    	 ----(11)----
    //
    //
    // Edge position (slot) FACE colors:
    //  0:  White
    //  1:  White
    //  2:  White
    //  3:  White
    //  4:  Blue
    //  5:  Blue
    //  6:  Orange
    //  7:  Green
    //  8:  Blue
    //  9:  Orange
    // 10:  Green
    // 11:  Red
    //
    // Edge cubie identification encodings (the first color is the MAJOR color):
    //  0:  White/Red
    //  1:  White/Blue
    //  2:  White/Green
    //  3:  White/Orange
    //  4:  Blue/Red
    //  5:  Blue/Orange
    //  6:  Orange/Green
    //  7:  Green/Red
    //  8:  Blue/Yellow
    //  9:  Orange/Yellow
    // 10:  Green/Yellow
    // 11:  Red/Yellow
    //
    //
    //  Edge cubie orientation:
    //   0: The MAJOR color of the edge cubie is aligned with the FACE color 
    //      of the edge position
    //   1: Else
    //
    //  Conjecture:
    //   x -> (y)  OR  (x) ->  y   <-->  FLIP orientation
    //   x ->  y   OR  (x) -> (y)  <-->  KEEP orientation
    //
    // END OF ENCODING EXPLANATION
    //
    // Temp variable for use in rotations
    private static long temp;
	
    // Number of bits to store the cubie ID and orientation in one slot
    private static final int numBits = 5;
	
    // Flips only the LSB when XOR'd with the target
    private static final long flip = 0b1L;
	
    // Masks:
    // Corner masks:
    private static final long corner0Mask = 0b11111L;
    private static final long corner1Mask = corner0Mask << numBits;
    private static final long corner2Mask = corner1Mask << numBits;
    private static final long corner3Mask = corner2Mask << numBits;
    private static final long corner4Mask = corner3Mask << numBits;
    private static final long corner5Mask = corner4Mask << numBits;
    private static final long corner6Mask = corner5Mask << numBits;
    private static final long corner7Mask = corner6Mask << numBits;
    private static final long corner8Mask = corner7Mask << numBits;
    private static final long corner9Mask = corner8Mask << numBits;
    private static final long corner10Mask = corner9Mask << numBits;
    private static final long corner11Mask = corner10Mask << numBits;
    // Clearing masks
    private static final long clearWhite = corner4Mask | corner5Mask | corner6Mask |
	corner7Mask | corner8Mask | corner9Mask | corner10Mask | corner11Mask;
    private static final long clearBlue = corner0Mask | corner2Mask | corner3Mask |
	corner6Mask | corner7Mask | corner9Mask | corner10Mask | corner11Mask;
    private static final long clearOrange = corner0Mask | corner1Mask | corner2Mask |
	corner4Mask | corner7Mask | corner8Mask | corner10Mask | corner11Mask;
    private static final long clearGreen = corner0Mask | corner1Mask | corner3Mask |
	corner4Mask | corner5Mask | corner8Mask | corner9Mask | corner11Mask;
    private static final long clearRed = corner1Mask | corner2Mask | corner3Mask |
	corner5Mask | corner6Mask | corner8Mask | corner9Mask | corner10Mask;
    private static final long clearYellow = corner0Mask | corner1Mask | corner2Mask |
	corner3Mask | corner4Mask | corner5Mask | corner6Mask | corner7Mask;
	
    // prints the cube for debugging purposes
    public static void printEdgeCube(long cube) {
	System.out.print("E0: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;
	System.out.print("; E1: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;
	System.out.print("; E2: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;
	System.out.print("; E3: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;
	System.out.print("; E4: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;
	System.out.print("; E5: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;
	System.out.print("; E6: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;		
	System.out.print("; E7: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;		
	System.out.print("; E8: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;		
	System.out.print("; E9: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;		
	System.out.print("; E10: ");
	System.out.print(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
	cube = cube >> numBits;		
	System.out.print("; E11: ");
	System.out.println(((cube & 0b11110) >> 1) + ", " + (cube & 0b1));
    }
	
    // Static rotation methods follow:
    // Generic rotation method for the edge cube
    public static long rotate(long cube, String rotationType) {
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
	
    // WHITE
    public static long rotateWhite90CW(long cube) {
	temp = cube;
	// clear the positions that change
	cube = cube & clearWhite;
	// Orientations don't change, so just swap positions
	cube = cube | ((corner1Mask & temp) >> numBits);  // 0 <- 1
	cube = cube | ((corner3Mask & temp) >> 2*numBits);// 1 <- 3
	cube = cube | ((corner0Mask & temp) << 2*numBits);// 2 <- 0
	cube = cube | ((corner2Mask & temp) << numBits);  // 3 <- 2
	return cube;
    }
	
    public static long rotateWhite90CCW(long cube) {
	temp = cube;
	// clear the positions that change
	cube = cube & clearWhite;
	// Orientations don't change, so just swap positions
	cube = cube | ((corner2Mask & temp) >> 2*numBits);// 0 <- 2
	cube = cube | ((corner0Mask & temp) << numBits);  // 1 <- 0
	cube = cube | ((corner3Mask & temp) >> numBits);  // 2 <- 3
	cube = cube | ((corner1Mask & temp) << 2*numBits);// 3 <- 1
	return cube;
    }
	
    public static long rotateWhite180(long cube) {
	return rotateWhite90CW(rotateWhite90CW(cube));
    }
	
    // BLUE
    public static long rotateBlue90CW(long cube) {
	temp = cube;
	cube = cube & clearBlue;
	cube = cube | (((corner4Mask & temp) >> 4*numBits) ^ flip) << numBits; // 1 <- 4 (flip)
	cube = cube | ((corner8Mask & temp) >> 4*numBits);                     // 4 <- 8
	cube = cube | (((corner1Mask & temp) >> numBits) ^ flip) << 5*numBits; // 5 <- 1 (flip)
	cube = cube | ((corner5Mask & temp) << 3*numBits);                     // 8 <- 5
	return cube;
    }
	
    public static long rotateBlue90CCW(long cube) {
	temp = cube;
	cube = cube & clearBlue;
	cube = cube | (((corner5Mask & temp) >> 5*numBits) ^ flip) << numBits; // 1 <- 5 (flip)
	cube = cube | (((corner1Mask & temp) >> numBits) ^ flip) << 4*numBits; // 4 <- 1 (flip)
	cube = cube | ((corner8Mask & temp) >> 3*numBits); // 5 <- 8
	cube = cube | ((corner4Mask & temp) << 4*numBits); // 8 <- 4
	return cube;
    }
	
    public static long rotateBlue180(long cube) {
	return rotateBlue90CW(rotateBlue90CW(cube));
    }
	
    // ORANGE
    public static long rotateOrange90CW(long cube) {
	temp = cube;
	cube = cube & clearOrange;
	cube = cube | ((corner5Mask & temp) >> 2*numBits); // 3 <- 5
	cube = cube | (((corner9Mask & temp) >> 9*numBits) ^ flip) << 5*numBits; // 5 <- 9 (flip)
	cube = cube | (((corner3Mask & temp) >> 3*numBits) ^ flip) << 6*numBits; // 6 <- 3 (flip)
	cube = cube | ((corner6Mask & temp) << 3*numBits); // 9 <- 6
	return cube;
    }
	
    public static long rotateOrange90CCW(long cube) {
	temp = cube;
	cube = cube & clearOrange;
	cube = cube | (((corner6Mask & temp) >> 6*numBits) ^ flip) << 3*numBits; // 3 <- 6 (flip)
	cube = cube | ((corner3Mask & temp) << 2*numBits); // 5 <- 3
	cube = cube | ((corner9Mask & temp) >> 3*numBits); // 6 <- 9
	cube = cube | (((corner5Mask & temp) >> 5*numBits) ^ flip) << 9*numBits; // 9 <- 5 (flip)
	return cube;
    }
	
    public static long rotateOrange180(long cube) {
	return rotateOrange90CW(rotateOrange90CW(cube));
    }
	
    // GREEN
    public static long rotateGreen90CW(long cube) {
	temp = cube;
	cube = cube & clearGreen;
	cube = cube | ((corner6Mask & temp) >> 4*numBits); // 2 <- 6
	cube = cube | (((corner10Mask & temp) >> 10*numBits) ^ flip) << 6*numBits; // 6 <- 10 (flip)
	cube = cube | (((corner2Mask & temp) >> 2*numBits) ^ flip) << 7*numBits; // 7 <- 2 (flip)
	cube = cube | ((corner7Mask & temp) << 3*numBits); // 10 <- 7
	return cube;
    }
	
    public static long rotateGreen90CCW(long cube) {
	temp = cube;
	cube = cube & clearGreen;
	cube = cube | (((corner7Mask & temp) >> 7*numBits) ^ flip) << 2*numBits; // 2 <- 7 (flip)
	cube = cube | ((corner2Mask & temp) << 4*numBits); // 6 <- 2
	cube = cube | ((corner10Mask & temp) >> 3*numBits); // 7 <- 10
	cube = cube | (((corner6Mask & temp) >> 6*numBits) ^ flip) << 10*numBits; // 10 <- 6 (flip)
	return cube;
    }
	
    public static long rotateGreen180(long cube) {
	return rotateGreen90CW(rotateGreen90CW(cube));
    }
	
    // RED
    public static long rotateRed90CW(long cube) {
	temp = cube;
	cube = cube & clearRed;
	cube = cube | ((corner7Mask & temp) >> 7*numBits); // 0 <- 7
	cube = cube | ((corner0Mask & temp) << 4*numBits); // 4 <- 0
	cube = cube | (((corner11Mask & temp) >> 11*numBits) ^ flip) << 7*numBits; // 7 <- 11 (flip)
	cube = cube | (((corner4Mask & temp) >> 4*numBits) ^ flip) << 11*numBits; // 11 <- 4 (flip)
	return cube;
    }
	
    public static long rotateRed90CCW(long cube) {
	temp = cube;
	cube = cube & clearRed;
	cube = cube | ((corner4Mask & temp) >> 4*numBits); // 0 <- 4
	cube = cube | (((corner11Mask & temp) >> 11*numBits) ^ flip) << 4*numBits; // 4 <- 11 (flip)
	cube = cube | ((corner0Mask & temp) << 7*numBits); // 7 <- 0
	cube = cube | (((corner7Mask & temp) >> 7*numBits) ^ flip) << 11*numBits; // 11 <- 7 (flip)
	return cube;
    }
	
    public static long rotateRed180(long cube) {
	return rotateRed90CW(rotateRed90CW(cube));
    }
	
    // YELLOW
    public static long rotateYellow90CW(long cube) {
	temp = cube;
	// clear the positions that change
	cube = cube & clearYellow;
	// Orientations don't change, so just swap positions
	cube = cube | ((corner11Mask & temp) >> 3*numBits); // 8 <- 11
	cube = cube | ((corner8Mask & temp) << numBits);// 9 <- 8
	cube = cube | ((corner9Mask & temp) << numBits);// 10 <- 9
	cube = cube | ((corner10Mask & temp) << numBits);// 11 <- 10
	return cube;
    }
	
    public static long rotateYellow90CCW(long cube) {
	temp = cube;
	// clear the positions that change
	cube = cube & clearYellow;
	// Orientations don't change, so just swap positions
	cube = cube | ((corner9Mask & temp) >> numBits); // 8 <- 9
	cube = cube | ((corner10Mask & temp) >> numBits);// 9 <- 10
	cube = cube | ((corner11Mask & temp) >> numBits);// 10 <- 11
	cube = cube | ((corner8Mask & temp) << 3*numBits);// 11 <- 8
	return cube;
    }
	
    public static long rotateYellow180(long cube) {
	return rotateYellow90CW(rotateYellow90CW(cube));
    }
}
