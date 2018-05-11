package liveness.fileAndNetworkSingleMutex;

public class Task1 implements Runnable
{
	private final MyFile file;
	private final MyNetworkCon networkCon;
	private final Object mutex;
	
	public Task1(MyFile file, MyNetworkCon networkCon, Object mutex)
	{
		this.file = file;
		this.networkCon = networkCon;
		this.mutex = mutex;
	}
	
	public void run()
	{
		System.out.println("Task1 is about to acquire the mutex");
		
		synchronized(mutex) {
			file.access();
			networkCon.access();
		}
	}
}
