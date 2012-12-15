package structures;

// Circular queue implemented using an array
public class MyArrayQueue implements MyQueue {
    private int head;
    private int tail;
    private long[] queue;
    private int capacity;
    private int numElements;
	
    public MyArrayQueue(int capacity) {
	queue = new long[capacity];
	head = 0;
	tail = 0;
	numElements = 0;
	this.capacity = capacity;
    }
	
    public MyArrayQueue() {
	queue = new long[10];
	head = 0;
	tail = 0;
	numElements = 0;
	this.capacity = 10;
    }
	
    public boolean isEmpty() {
	return numElements == 0;
    }
	
    public long remove() {
	if (isEmpty()) {
	    throw new IllegalArgumentException("Queue is empty!");
	}
	numElements--;
	long element = queue[head];
	head = (head + 1) % capacity;
	return element;
    }
	
    public void add(long element) {
	if (isEmpty())
	    queue[head] = element;
	else {
	    tail = (tail + 1) % capacity;
	    queue[tail] = element;
	}
	if (numElements < capacity)
	    numElements++;
    }
}
