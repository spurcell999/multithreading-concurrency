package mutexes.barSimulation;

import java.util.concurrent.CountDownLatch;

public class BarSimulationElicitRaceCondition
{
	private static final int numberOfThreads = 2;

	public static void main(String[] args)
	{
		for (int i = 1; i <= numberOfThreads; i++)
		{
			String name = "Thread" + i;
			new Thread(new BarPatron(name), name).start();
		}
	}

	private static class BarPatron implements Runnable
	{
		private final String name;

		private static final CountDownLatch cl = new CountDownLatch(
				numberOfThreads);

		// Must be volatile as shared between threads and modified
		private static volatile boolean roundBeingBought = false;

		public BarPatron(String name)
		{
			this.name = name;
		}

		public void run()
		{
			enterBarAndOrderDrinks();
		}

		private void enterBarAndOrderDrinks()
		{
			// Don't want to be seen as a cheapskate - if no drinks being
			// bought take the initiative and buy them!
			if (!roundBeingBought) // 1
			{
				// To show the race condition
				// TODO: Debug only - remove this!!
				try
				{
					cl.countDown();
					cl.await();
				}
				catch (InterruptedException e)
				{
					// Can ignore this
				}

				roundBeingBought = true; // 2
				buyDrinks();
			}
			else
			{
				waitForDrink();
			}
		}

		private void buyDrinks()
		{
			System.out.println(name + " is buying the drinks.");
		}

		private void waitForDrink()
		{
			System.out.println(name + " is waiting for a drink.");
		}
	}
}
