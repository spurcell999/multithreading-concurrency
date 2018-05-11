package liveness.fileAndNetwork;

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
		
		// Taking the mutexes in the opposite order as Task1 does - BAD!
		synchronized(networkCon) {
			synchronized (file) {
				networkCon.access();
				file.access();
			}
		}
	}
}
