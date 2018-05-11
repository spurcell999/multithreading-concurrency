package mutexes.barSimulationCounting.arbiter.fixInvariants;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

public class BarSimulationArbiter
{
	private final Hashtable<String, Integer> roundsBought = new Hashtable<>();
	private final AtomicInteger roundsSoFar = new AtomicInteger();

	public void register(String name)
	{
		roundsBought.put(name, 0);
	}

	public synchronized void roundBought(String name)
	{
		int numBought = roundsBought.get(name);

		roundsBought.put(name, numBought + 1);

		roundsSoFar.getAndIncrement();
	}

	public synchronized int[] getNumBoughtAndRoundsSoFar(String name)
	{
		return new int[] { roundsBought.get(name), roundsSoFar.get() };
	}
}
