package threadpools.components;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureDemonstration
{
	public static void main(String[] args) {
		Callable<Integer> callable = new Callable<Integer>() {
			public Integer call() {
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					System.out.println("Task has been interrupted");
				}
				
				return 10;
			}
		};
		
		FutureTask<Integer> task1 = new FutureTask<Integer>(callable);
		
		Thread t = new Thread(task1);
		t.start();
		
		System.out.println("Starting task1");
		System.out.println("Task1 is done: " + task1.isDone());
		
		int result = 0;
		
		try {
			result = task1.get();
		} catch (InterruptedException e) {
			System.out.println("Get interrupted");
		} catch (ExecutionException e) {
			System.out.println("Execution exception");
		} catch (CancellationException e) {
			System.out.println("Cancellation exception");
		}
		
		System.out.println("Result of task1 is " + result);
		System.out.println("Task1 is done: " + task1.isDone());
		System.out.println("Task1 is cancelled: " + task1.isCancelled());
		
		FutureTask<Integer> task2 = new FutureTask<Integer>(callable);
		
		Thread t2 = new Thread(task2);
		t2.start();

		System.out.println("Starting task2");
		System.out.println("Task2 is cancelled: " + task2.isCancelled());
	
		// The task should start before we cancel
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) { /* Ignore */ }
		
		task2.cancel(true); // What is different if we pass false?
	
		// Try get now
		try {
			result = task2.get();
		} catch (InterruptedException e) {
			System.out.println("Get interrupted");
		} catch (ExecutionException e) {
			System.out.println("Execution exception");
		} catch (CancellationException e) {
			System.out.println("Cancellation exception");
		}
		
		System.out.println("Task2 is done: " + task2.isDone());
		System.out.println("Task2 is cancelled: " + task2.isCancelled());
	}
}
