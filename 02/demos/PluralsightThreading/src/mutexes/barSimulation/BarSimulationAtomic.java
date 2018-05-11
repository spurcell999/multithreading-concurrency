package mutexes.barSimulation;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class BarSimulationAtomic
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

		private static final AtomicBoolean roundBeingBought = new AtomicBoolean();

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
			// To prove no race condition
			// TODO: Debug only - remove this!!
			try
			{
				cl.countDown();
				cl.await();
			}
			catch (InterruptedException e)
			{
			}
			
			if (roundBeingBought.compareAndSet(false, true))
			{
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
