package signalling.waitnotify;

import java.util.LinkedList;
import java.util.Queue;

// For discussion only: this is not intended to be run
public class ProducerConsumerFirstAttempt
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
		
		synchronized(queue) {
			try {
				queue.wait();
			} catch (InterruptedException e) {
				// What to do here?
			}
			
			work = queue.poll();
		}
		
		return work;
	}
}
