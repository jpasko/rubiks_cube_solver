package structures;

// CubeTracer stores the move sequence that's been applied to the cube
public class CubeTracer {
    // Stores the moves as Strings in an array
    private String[] sequence;
	
    // index of the current move
    private int counter;
	
    // the length of the move sequence that will be stored
    // (typically God's Number, i.e. 20)
    private int length;
	
    // length must be greater than 0
    public CubeTracer(int length) {
	if(length < 1)
	    throw new IllegalArgumentException("Tracer length must be greater than 0");
	this.length = length;
	sequence = new String[length];
	counter = 0;
	sequence[0] = "The cube is already solved!";
    }
	
    // Adds a new move to the sequence
    public void writeMove(String move) {
	// store move
	sequence[counter] = move;
	// increment counter
	counter = (counter + 1) % length;
    }
	
    // Erases the last move that was stored
    public void eraseMove() {
	// decrement counter
	counter--;
	if(counter == -1)
	    counter = length - 1;
	// erase move
	sequence[counter] = null;
    }
	
    // returns the move sequence
    public String[] getSequence() {
	return sequence;
    }
	
    // resets the cube tracer
    public void reset() {
	counter = 0;
	for (int i = 0; i < length; i++) {
	    sequence[i] = null;
	}
	sequence[0] = "The cube is already solved!";
    }
}
