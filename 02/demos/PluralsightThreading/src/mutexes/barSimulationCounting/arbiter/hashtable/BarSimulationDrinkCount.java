package mutexes.barSimulationCounting.arbiter.hashtable;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;

public class BarSimulationDrinkCount
{
	private static final int numberOfThreads = 2;

	private static BarSimulationArbiter arbiter = new BarSimulationArbiter();

	
	public static void main(String[] args)
	{
		Thread t = null;

		for (int i = 1; i <= numberOfThreads; i++)
		{
			String name = "Thread" + i;
			
			arbiter.register(name);
			
			t = new Thread(new BarPatron(name), name);
			t.start();
		}

		// Since they exit the bar together, calling isAlive on just one is safe
		while (t.isAlive())
		{
			displayStatistics();

			try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
				// Safe to ignore
			}
		}

		System.out.println("Final results:");

		displayStatistics();
	}

	private static void displayStatistics()
	{
		for (int i = 1; i <= numberOfThreads; i++)
		{
			String name = "Thread" + i;

			int roundsBought = arbiter.getNumBought(name);
			int roundsSoFar = arbiter.getNumRoundsSoFar();

			if (roundsBought > 0)
			{
				int percentage = roundsBought * 100 / roundsSoFar;

				System.out.println("After " + roundsSoFar + " rounds, " + name
						+ " has bought " + roundsBought + " rounds ("
						+ percentage + "%)");
			}
		}
	}

	private static class BarPatron implements Runnable
	{
		private static final int numBarVisits = 50;

		private final String name;
		
		private static final AtomicBoolean roundBeingBought = new AtomicBoolean();

		private static final Phaser phaser = new Phaser(numberOfThreads);

		public BarPatron(String name)
		{
			this.name = name;
		}

		public void run()
		{
			for (int visit = 1; visit <= numBarVisits; visit++)
			{
				enterBarAndOrderDrinks(visit);

				try
				{
					if (visit % 10 == 0)
					{
						Thread.sleep(10);
					}
				}
				catch (InterruptedException e)
				{
					// Safe to ignore
				}
			}
		}

		private void enterBarAndOrderDrinks(int visit)
		{
			roundBeingBought.set(false);;

			// Wait until both get into the bar
			phaser.arriveAndAwaitAdvance();

			// Don't want to be seen as a cheapskate - if no drinks being
			// bought take the initiative and buy them!
			if (roundBeingBought.compareAndSet(false, true))
			{
				buyDrinks(visit);
				arbiter.roundBought(name);
			}
			else
			{
				waitForDrink(visit);
			}

			// Leave the bar together, don't want to reset roundBeingBought
			// before the other thread waits for the drink
			phaser.arriveAndAwaitAdvance();
		}

		private void buyDrinks(int visit)
		{
			// System.out.println("Bar visit " + visit + ": " + name
			// + " is buying the drinks.");
		}

		private void waitForDrink(int visit)
		{
			// System.out.println("Bar visit " + visit + ": " + name
			// + " is waiting for a drink.");
		}
	}
}
