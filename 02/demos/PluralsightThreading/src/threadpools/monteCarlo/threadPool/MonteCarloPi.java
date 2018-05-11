package threadpools.monteCarlo.threadPool;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class MonteCarloPi
{
	public static void main(String[] args)
	{
		int numPoints;
		int numTasks;
		int numWorkers;
		
		try (Scanner scanner = new Scanner(System.in))
		{
			System.out.println("How many points?");

			numPoints = scanner.nextInt();
			
			System.out.println("How many tasks?");
			
			numTasks = scanner.nextInt();
			
			System.out.println("How many workers?");
			
			numWorkers = scanner.nextInt();
		}
		
		// Yes we might do less points due to rounding
		int pointsPerTask = numPoints / numTasks;
		
		if (pointsPerTask <= 0) {
			throw new IllegalArgumentException("Too few points for the number of tasks");
		}

		int totalPoints = pointsPerTask * numTasks;
		
		PIResult results[] = new PIResult[numTasks];
		PITask tasks[] = new PITask[numTasks];
		
		for (int i=0; i<numTasks; i++) {
			results[i] = new PIResult();
			tasks[i] = new PITask(pointsPerTask, results[i]);
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(numWorkers);
		
		// With a small number of threads this is unnecessary
		((ThreadPoolExecutor) executor).prestartAllCoreThreads();
		
		@SuppressWarnings("unchecked")
		Future<PIResult> futures[] = new Future[numTasks];
		
		long nowBefore = System.currentTimeMillis();
		
		for (int i=0; i<numTasks; i++)
		{
			futures[i] = executor.submit(tasks[i], results[i]);
		}

		for (int i=0; i<numTasks; i++) {
			try
			{
				futures[i].get();
			}
			catch (InterruptedException e)
			{
				// Can ignore
			}
			catch (ExecutionException e)
			{
				// Can ignore
			}
		}

		long timeTaken = System.currentTimeMillis() - nowBefore;

		int totalInCircle = 0;
		
		for (int i=0; i<numTasks; i++) {
			totalInCircle += results[i].getResult();
		}
		
		double pi = (double) 4 * totalInCircle / totalPoints;
		
		System.out.println("Monte Carlo approximation of pi: " + pi);

		System.out.println("Time taken: " + timeTaken + " ms");
		
		executor.shutdownNow();
	}
}
