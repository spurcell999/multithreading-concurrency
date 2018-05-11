package liveness.fileAndNetworkSameOrder;

public class Task2 implements Runnable
{
	private final MyFile file;
	private final MyNetworkCon networkCon;
	
	public Task2(MyFile file, MyNetworkCon networkCon)
	{
		this.file = file;
		this.networkCon = networkCon;
	}
	
	public void run()
	{
		System.out.println("Task2 is about to acquire the mutexes");
		
		// Now taking the mutexes in the same order as Task1
		synchronized(file) {
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// This is a demo, so can ignore it
			}
			
			synchronized (networkCon) {
				networkCon.access();
				file.access();
			}
		}
	}
}
