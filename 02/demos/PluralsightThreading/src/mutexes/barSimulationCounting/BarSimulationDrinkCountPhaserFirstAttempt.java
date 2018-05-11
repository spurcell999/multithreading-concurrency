package mutexes.barSimulationCounting;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BarSimulationDrinkCountPhaserFirstAttempt
{
	private static final int numberOfThreads = 2;

	public static void main(String[] args)
	{
		for (int i = 1; i <= numberOfThreads; i++)
		{
			String name = "Thread " + i;
			new Thread(new BarPatron(name), name).start();
		}
	}

	private static class BarPatron implements Runnable
	{
		private static final int numBarVisits = 50;

		private final String name;

		private static final AtomicBoolean roundBeingBought = new AtomicBoolean();

		private final AtomicInteger numRoundsBought = new AtomicInteger();
		
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
			}

			int percentage = numRoundsBought.get() * 100 / numBarVisits;

			System.out.println(name + ": bought " + numRoundsBought
					+ " rounds (" + percentage + "%).");
		}

		private void enterBarAndOrderDrinks(int visit)
		{
			roundBeingBought.set(false);

			// Wait until both get into the bar
			phaser.arriveAndAwaitAdvance();

			// Don't want to be seen as a cheapskate - if no drinks being
			// bought take the initiative and buy them!
			if (roundBeingBought.compareAndSet(false, true))
			{
				buyDrinks(visit);
				numRoundsBought.getAndIncrement();
			}
			else
			{
				waitForDrink(visit);
			}
		}

		private void buyDrinks(int visit)
		{
			System.out.println("Bar visit " + visit + ": " + name
					+ " is buying the drinks.");
		}

		private void waitForDrink(int visit)
		{
			System.out.println("Bar visit " + visit + ": " + name
					+ " is waiting for a drink.");
		}
	}
}
