package signalling.waitnotify;

import java.util.LinkedList;
import java.util.Queue;

// For discussion only: this is not intended to be run
public class ProducerConsumerRaceCondition
{
	final Queue<Object> queue = new LinkedList<>(); // Mock work as Object
	
	void producer(Object work) {
		synchronized(queue) {
			queue.offer(work);
			queue.notify();
		}
	}
	
	Object consumer() {
		Object work;
		
		while (queue.isEmpty()) {
			// Race condition - we will wait [possibly forever] if the producer
			// adds an item when consumer thread is here
			synchronized(queue) {
				try {
					queue.wait();
				} catch (InterruptedException e) {
					// What to do here?
				}
			}
		}
		// Race condition - we will get null if another consumer thread removes work
		// while this consumer thread is here
		work = queue.poll();
		return work;
	}
}
