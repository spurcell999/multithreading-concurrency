package liveness.fileAndNetworkSingleMutex;

public class Driver
{
	public static void main(String[] args)
	{
		MyFile file = new MyFile();
		MyNetworkCon networkCon = new MyNetworkCon();
		
		Object mutex = new Object();
		
		Task1 task1 = new Task1(file, networkCon, mutex);
		Task2 task2 = new Task2(file, networkCon, mutex);
		
		Thread thread1 = new Thread(task1, "Task 1");
		Thread thread2 = new Thread(task2, "Task 2");
		
		thread1.start();
		thread2.start();
	}
}
