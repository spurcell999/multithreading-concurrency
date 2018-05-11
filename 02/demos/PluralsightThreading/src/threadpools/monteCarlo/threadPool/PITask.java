package threadpools.monteCarlo.threadPool;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PITask implements Runnable
{
	private int numPoints;
	private PIResult result;
	
	public PITask(int numPoints, PIResult result)
	{
		this.numPoints = numPoints;
		this.result = result;
	}
	
	private boolean isInCircle(double x, double y)
	{
		return x * x + y * y <= 1;
	}

	public void run()
	{
		// Gotcha, needs to be here, not in the constructor, otherwise
		// it will be that of the main thread
		Random rand = ThreadLocalRandom.current();
		int numInCircle = 0;

		for (int i = 0; i < numPoints; i++)
		{
			if (isInCircle(rand.nextDouble(), rand.nextDouble()))
			{
				numInCircle++;
			}
		}

		result.setResult(numInCircle);
	}
}
