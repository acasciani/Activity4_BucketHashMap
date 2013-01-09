
public class Driver {

	ConcurrentBucketHashMap<String, String> map = new ConcurrentBucketHashMap<String, String>(3);
	
	
	private class Task implements Runnable {
		private final String name;
		
		public Task(String theName) {
			name = theName;
		}
		
		@Override
		public void run() {
			for(int i = 1; i <= 5; i++) {
				map.put(name + i, name + " " + i);
			}
		}
	}
	
	private class TaskSize implements Runnable {
		@Override
		public void run() {
			System.out.printf("The size of the map is %s", map.size());
		}
	}

	public static void main(String[] args) {
		Driver driver = new Driver();
		
		Thread task1 = new Thread(driver.new Task("Test"));
		Thread task2 = new Thread(driver.new Task("Testing"));
		Thread task3 = new Thread(driver.new Task("Tested"));
		Thread sizeTask = new Thread(driver.new TaskSize());
		
		task1.start();
		task2.start();
		task3.start();
		sizeTask.start();
	}

	
}
