package liveness.fileAndNetwork;

public class Task1 implements Runnable
{
	private final MyFile file;
	private final MyNetworkCon networkCon;
	
	public Task1(MyFile file, MyNetworkCon networkCon)
	{
		this.file = file;
		this.networkCon = networkCon;
	}
	
	public void run()
	{
		System.out.println("Task1 is about to acquire the mutexes");
		
		synchronized(file) {
			synchronized (networkCon) {
				file.access();
				networkCon.access();
			}
		}
	}
}
