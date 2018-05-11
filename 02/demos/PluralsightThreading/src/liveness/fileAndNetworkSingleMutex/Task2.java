package liveness.fileAndNetworkSingleMutex;

public class Task2 implements Runnable
{
	private final MyFile file;
	private final MyNetworkCon networkCon;
	private final Object mutex;
	
	public Task2(MyFile file, MyNetworkCon networkCon, Object mutex)
	{
		this.file = file;
		this.networkCon = networkCon;
		this.mutex = mutex;
	}
	
	public void run()
	{
		System.out.println("Task2 is about to acquire the mutex");
		
		synchronized(mutex) {
			networkCon.access();
			file.access();
		}
	}
}
