package structures;

// Interface for a lightweight queue of longs.
public interface MyQueue {
	// returns true if the queue contains no elements; false otherwise
	boolean isEmpty();
	
	// removes the head of the queue
	long remove();
	
	// adds the element to the end of the queue
	void add(long cube);
}
