package mutexes.barSimulationCounting.arbiter.hashtable;

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

	public void roundBought(String name)
	{
		int numBought = roundsBought.get(name);

		roundsBought.put(name, numBought + 1);

		roundsSoFar.getAndIncrement();
	}

	public int getNumBought(String name)
	{
		return roundsBought.get(name);
	}

	public int getNumRoundsSoFar()
	{
		return roundsSoFar.get();
	}
}
