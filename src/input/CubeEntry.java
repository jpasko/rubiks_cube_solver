package input;

import java.util.Scanner;

// class CubeEntry is responsible for obtaining the state of
// the user's Rubik's Cube, and converting it to the compact
// form used by the search and associated heuristic.
public class CubeEntry {
    // private char array to hold the cube's state.  Cube state
    // is implemented as a mapping of every cube face onto a one
    // dimensional array in the following manner, where the given
    // numbers represent array indices:
    //
    //                    0  1  2
    //                    3  4  5
    //			  6  7  8
    //
    //         9  10 11   12 13 14   15 16 17   18 19 20
    //         21 22 23   24 25 26   27 28 29   30 31 32
    //	       33 34 35   36 37 38   39 40 41   42 43 44
    //
    // 		          45 46 47
    //                    48 49 50
    // 			  51 52 53
    //
    // In terms of cube colors, the cube is oriented as such:
    //
    //                       W
    //                    B  O  G  R
    //                       Y
    private char[] cube = new char[54];
	
    // Compact (primitive) cube state encodings for both the corner
    // and edge cubes
    private long cornerCube;
    private long edgeCube;
	
    // mapping of cubie positions to array positions
    private int[] orangeFace = {12, 13, 14, 24, 25, 26, 36, 37, 38};
    private int[] greenFace  = {15, 16, 17, 27, 28, 29, 39, 40, 41};
    private int[] redFace    = {18, 19, 20, 30, 31, 32, 42, 43, 44};
    private int[] blueFace   = {9, 10, 11, 21, 22, 23, 33, 34, 35};
    private int[] whiteFace  = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    private int[] yellowFace = {45, 46, 47, 48, 49, 50, 51, 52, 53};
	
    // 0: 12/11/6  (corner #: cubie faces)
    // 1: 14/8/15
    // 2: 36/45/35
    // 3: 38/39/47
    // 4: 18/17/2
    // 5: 20/0/9
    // 6: 42/53/41
    // 7: 44/33/51
    // Serialized sets of three indices corresponding to the cubie face
    // colors in the char cube array
    private int[] getCornerColors = {12, 11, 6, 14, 8, 15, 36, 45, 35, 38,
				     39, 47, 18, 17, 2, 20, 0, 9, 42, 53,
				     41, 44, 33, 51};
	
    // 0 : 1/19  (corner #: cubie faces)
    // 1 : 3/10
    // 2 : 5/16
    // 3 : 7/13
    // 4 : 21/32
    // 5 : 23/24
    // 6 : 26/27
    // 7 : 29/30
    // 8 : 34/48
    // 9 : 37/46
    // 10: 40/50
    // 11: 43/52
    // same as getColors, but for the edge cube
    private int[] getEdgeColors = {1, 19, 3, 10, 5, 16, 7, 13, 21, 32, 23,
				   24, 26, 27, 29, 30, 34, 48, 37, 46, 40,
				   50, 43, 52};
	
    // Scanner object to get user input from console
    private Scanner sc;
	
    // Constructor creates the Scanner object, taking input stream from the
    // console
    public CubeEntry() {
	sc = new Scanner(System.in);
    }
	
    public void enterCubeState() {
	int i;     // counter to keep track of cubie entries
	System.out.println("Orient the cube such that the ORANGE face points upwards.");
	System.out.println("Now, if necessary, rotate the cube so that the WHITE face");
	System.out.println("points away from you.");
	System.out.println("Using the characters \'B\', \'G\', \'R\', \'O\', \'Y\', ");
	System.out.println("and \'W\' to denote, respectively, BLUE, GREEN, RED, ");
	System.out.println("ORANGE, YELLOW, and WHITE, enter the colors of the cubies");
	System.out.println("on the ORANGE face from left to right, top to bottom,");
	System.out.println("pressing enter after submitting each cubie color.");
	i = 0;
	while (i < 9) {
	    String temp = sc.next();
	    temp = temp.toUpperCase();
	    cube[orangeFace[i]] = temp.charAt(0);
	    i++;
	}
	System.out.println("Now, rotate the cube so the GREEN face points upwards and");
	System.out.println("repeat the entry procedure.");
	i = 0;
	while (i < 9) {
	    String temp = sc.next();
	    temp = temp.toUpperCase();
	    cube[greenFace[i]] = temp.charAt(0);
	    i++;
	}
	System.out.println("Now, rotate the cube so the RED face points upwards and");
	System.out.println("repeat the entry procedure.");
	i = 0;
	while (i < 9) {
	    String temp = sc.next();
	    temp = temp.toUpperCase();
	    cube[redFace[i]] = temp.charAt(0);
	    i++;
	}
	System.out.println("Now, rotate the cube so the BLUE face points upwards and");
	System.out.println("repeat the entry procedure.");
	i = 0;
	while (i < 9) {
	    String temp = sc.next();
	    temp = temp.toUpperCase();
	    cube[blueFace[i]] = temp.charAt(0);
	    i++;
	}
	System.out.println("Rotate once more so that the ORANGE face again is on");
	System.out.println("top.  Now rotate so that the WHITE face points upwards,");
	System.out.println("and repeat the entry procedure");
	i = 0;
	while (i < 9) {
	    String temp = sc.next();
	    temp = temp.toUpperCase();
	    cube[whiteFace[i]] = temp.charAt(0);
	    i++;
	}
	System.out.println("Finally, rotate so that the YELLOW face points upwards,");
	System.out.println("and repeat the entry procedure");
	i = 0;
	while (i < 9) {
	    String temp = sc.next();
	    temp = temp.toUpperCase();
	    cube[yellowFace[i]] = temp.charAt(0);
	    i++;
	}
		
	// Finally, enforce the face coloring
	cube[25] = 'O';
	cube[28] = 'G';
	cube[31] = 'R';
	cube[22] = 'B';
	cube[4]  = 'W';
	cube[49] = 'Y';
    }
	
    // Convert the array representation of the cube to a string, with proper
    // formatting, for convenient printing
    private String cubeToString() {
	StringBuffer result = new StringBuffer();
	for (int i = 0; i < cube.length; i++) {
	    result.append(cube[i]);
	}
	return result.toString();
    }
	
    // Prints the current state of the cube out to the console
    public void printCube() {
	String cubeString = cubeToString();
	System.out.println("    " + cubeString.substring(0, 3));
	System.out.println("    " + cubeString.substring(3, 6));
	System.out.println("    " + cubeString.substring(6, 9));
	System.out.println(cubeString.substring(9, 12) + " "
			   + cubeString.substring(12, 15)
			   + " " +    cubeString.substring(15, 18)
			   + " " + cubeString.substring(18, 21));
	System.out.println(cubeString.substring(21, 24) + " "
			   + cubeString.substring(24, 27)
			   + " " +    cubeString.substring(27, 30)
			   + " " + cubeString.substring(30, 33));
	System.out.println(cubeString.substring(33, 36) + " "
			   + cubeString.substring(36, 39)
			   + " " +    cubeString.substring(39, 42)
			   + " " + cubeString.substring(42, 45));
	System.out.println("    " + cubeString.substring(45, 48));
	System.out.println("    " + cubeString.substring(48, 51));
	System.out.println("    " + cubeString.substring(51));
    }
	
    // prompts the user to review the printed cube, and signal whether it is
    // accurate
    public boolean isCubeOK() {
	System.out.print("Is the cube correctly entered? (y/n):");
	return sc.next().toUpperCase().equals("Y");
    }
	
    // asks the user whether they'd like to solve another instance of the cube
    // (returning true if they enter yes)
    public boolean solveAnotherCube() {
	System.out.print("Would you like to solve another scrambled cube? (y/n):");
	return sc.next().toUpperCase().equals("Y");
    }
	
    // computes the cornerCube and edgeCube representations of the entered cube
    // state MUST be called after a cube has successfully been entered for
    // correct operation
    public void createCompactCube() {
	computeCornerCube();
	computeEdgeCube();
    }
	
    // returns the corner cube (WARNING: don't call before a call to
    // createCompactCube has been made)
    public long getCornerCube() {
	return this.cornerCube;
    }
	
    // returns the edge cube (WARNING: don't call before a call to
    // createCompactCube has been made)
    public long getEdgeCube() {
	return this.edgeCube;
    }
	
    private void computeCornerCube() {
	cornerCube = 0L;
	char[] colors = new char[3];
	for(int i = 0; i < 8; i++) {
	    // get the three colors for the cubie in corner position i
	    for(int j = 0; j < 3; j++)
		colors[j] = cube[getCornerColors[3*i + j]];
	    cornerCube = cornerCube | (cornerIDandOrientation(colors) << 5*i);
	}
	// for reference:
	// 0: 12/11/6  (corner #: cubie faces)
	// 1: 14/8/15
	// 2: 36/45/35
	// 3: 38/49/47
	// 4: 18/17/2
	// 5: 20/0/9
	// 6: 42/53/41
	// 7: 44/33/51
    }
	
    // returns a long containing the corner ID as bits marked with 'x' and
    // the corner orientation with bits marked as 'y' in the returned value
    // 0bxxxyyL
    private long cornerIDandOrientation(char[] colors) {
	if(colors.length != 3)
	    throw new IllegalArgumentException("Invalid input array length (corner cube)");
	// The corner cubie encodings are:
	// 	Orange/Blue/White:   0
	//     	Orange/White/Green:  1
	//      Orange/Yellow/Blue:  2
	//      Orange/Green/Yellow: 3
	//      Red/Green/White:     4
	//      Red/White/Blue:      5
	//      Red/Yellow/Green:    6
	//      Red/Blue/Yellow:     7
	String c = new String(colors);
	if(c.equals("OBW")) { // corner cubie 0
	    return 0b00000L;
	} else if(c.equals("WOB")) {
	    return 0b00001L;
	} else if(c.equals("BWO")) {
	    return 0b00010L;
	} else if(c.equals("OWG")) { // corner cubie 1
	    return 0b00100L;
	} else if(c.equals("GOW")) {
	    return 0b00101L;
	} else if(c.equals("WGO")) {
	    return 0b00110L;
	} else if(c.equals("OYB")) { // corner cubie 2
	    return 0b01000L;
	} else if(c.equals("BOY")) {
	    return 0b01001L;
	} else if(c.equals("YBO")) {
	    return 0b01010L;
	} else if(c.equals("OGY")) { // corner cubie 3
	    return 0b01100L;
	} else if(c.equals("YOG")) {
	    return 0b01101L;
	} else if(c.equals("GYO")) {
	    return 0b01110L;
	} else if(c.equals("RGW")) { //corner cubie 4
	    return 0b10000L;
	} else if(c.equals("WRG")) {
	    return 0b10001L;
	} else if(c.equals("GWR")) {
	    return 0b10010L;
	} else if(c.equals("RWB")) { // corner cubie 5
	    return 0b10100L;
	} else if(c.equals("BRW")) {
	    return 0b10101L;
	} else if(c.equals("WBR")) {
	    return 0b10110L;
	} else if(c.equals("RYG")) { // corner cubie 6
	    return 0b11000L;
	} else if(c.equals("GRY")) {
	    return 0b11001L;
	} else if(c.equals("YGR")) {
	    return 0b11010L;
	} else if(c.equals("RBY")) { // corner cubie 7
	    return 0b11100L;
	} else if(c.equals("YRB")) {
	    return 0b11101L;
	} else if(c.equals("BYR")) {
	    return 0b11110L;
	} else {
	    throw new IllegalArgumentException("[corner cubie] Invalid permutation or input detected!");
	}
    }
	
    // returns a long containing the edge ID and orientation in the same way
    // as above, with bits as such: 0bxxxxyL
    private long edgeIDandOrientation(char[] colors) {
	if(colors.length != 2)
	    throw new IllegalArgumentException("Invalid input array length (edge cube)");
	// Edge cubie identification encodings
	// (the first color is the MAJOR color):
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
	String c = new String(colors);
	// edge cubie 0
	if(c.equals("WR")) {
	    return 0b00000L;
	} else if(c.equals("RW")) {
	    return 0b00001L;
	    // edge cubie 1
	} else if(c.equals("WB")) {
	    return 0b00010L;
	} else if(c.equals("BW")) {
	    return 0b00011L;
	    // edge cubie 2
	} else if(c.equals("WG")) {
	    return 0b00100L;
	} else if(c.equals("GW")) {
	    return 0b00101L;
	    // edge cubie 3
	} else if(c.equals("WO")) {
	    return 0b00110L;
	} else if(c.equals("OW")) {
	    return 0b00111L;
	    // edge cubie 4
	} else if(c.equals("BR")) {
	    return 0b01000L;
	} else if(c.equals("RB")) {
	    return 0b01001L;
	    // edge cubie 5
	} else if(c.equals("BO")) {
	    return 0b01010L;
	} else if(c.equals("OB")) {
	    return 0b01011L;
	    // edge cubie 6
	} else if(c.equals("OG")) {
	    return 0b01100L;
	} else if(c.equals("GO")) {
	    return 0b01101L;
	    // edge cubie 7
	} else if(c.equals("GR")) {
	    return 0b01110L;
	} else if(c.equals("RG")) {
	    return 0b01111L;
	    // edge cubie 8
	} else if(c.equals("BY")) {
 	    return 0b10000L;
	} else if(c.equals("YB")) {
	    return 0b10001L;
	    // edge cubie 9
	} else if(c.equals("OY")) {
	    return 0b10010L;
	} else if(c.equals("YO")) {
	    return 0b10011L;
	    // edge cubie 10
	} else if(c.equals("GY")) {
	    return 0b10100L;
	} else if(c.equals("YG")) {
	    return 0b10101L;
	    // edge cubie 11
	} else if(c.equals("RY")) {
	    return 0b10110L;
	} else if(c.equals("YR")) {
	    return 0b10111L;
	} else {
	    throw new IllegalArgumentException("[edge cubie] Invalid permutation or input detected!");
	}
    }
	
    private void computeEdgeCube() {
	edgeCube = 0L;
	char[] colors = new char[2];
	for(int i = 0; i < 12; i++) {
	    // get the two colors for the cubie in edge position i
	    for(int j = 0; j < 2; j++)
		colors[j] = cube[getEdgeColors[2*i + j]];
	    edgeCube = edgeCube | (edgeIDandOrientation(colors) << 5*i);
	}
	// for reference:
	// 0 : 1/19  (corner #: cubie faces)
	// 1 : 3/10
	// 2 : 5/16
	// 3 : 7/13
	// 4 : 21/32
	// 5 : 23/24
	// 6 : 26/27
	// 7 : 29/30
	// 8 : 34/48
	// 9 : 37/46
	// 10: 40/50
	// 11: 43/52
    }
}
