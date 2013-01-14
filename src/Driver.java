import java.util.Random;

/**
 * Driver.java
 * 
 * A driver to test out the ConcurrentBucketHashMap.
 * 
 * @author Alex Casciani
 * @author Michael Yeaple
 * @author Peter Mikitsh
 * 
 */
public class Driver {

	ConcurrentBucketHashMap<String, String> map = new ConcurrentBucketHashMap<String, String>(3);
	
	/**
	 * Tasks interact with a ConcurrentBucketHashMap in order to see
	 * what kind of results can be produced 
	 */
	private class Task implements Runnable {
		private final String name;
		private final Random random = new Random();
		
		/** Default constructor
		 * 
		 * @param theName - The task name
		 */
		public Task(String theName) {
			name = theName;
		}
		
		@Override
		public void run() {
			
			for(int i = 1; i <= 5; i++) {
				// Put a value in the map.
				map.put(name + i, name + " " + i);
				
				// Randomly sleep.
				if ( random.nextBoolean() ) {
					try{
						Thread.sleep(500);
					} catch ( InterruptedException ie ) {
						System.err.println( "Error: " + ie.getMessage() );
					}
				}
				
				// Randomly remove the element we just put in.
				if ( random.nextBoolean() ){
					map.remove(name + i);
				}
			}
		}
	}
	
	private class SizeTask implements Runnable {
		private boolean killed;
		
		/** kill()
		 * 
		 * Kills the SizeTask thread.
		 */
		public void kill(){
			killed = true;
		}
		
		@Override
		public void run() {
			while ( !killed ){
				System.out.printf( "The size of the map is %s%n", map.size() );
				
				try{
					Thread.sleep(1000);
				} catch ( InterruptedException ie ) {
					System.err.println( "Error: " + ie.getMessage() );
				}
			}
		}
	}

	public static void main(String[] args) {
		Driver driver = new Driver();
		
		// Create our threads.
		SizeTask sz = driver.new SizeTask();
		Thread task1 = new Thread( driver.new Task( "Test" ) );
		Thread task2 = new Thread( driver.new Task( "Testable" ) );
		Thread task3 = new Thread( driver.new Task( "Tested" ) );
		Thread sizeTask = new Thread( sz );
		
		// Start our threads.
		sizeTask.start();
		task1.start();
		task2.start();
		task3.start();
		
		try{
			Thread.sleep(10000);
		} catch ( InterruptedException ie ) {
			System.err.println( "Error: " + ie.getMessage() );
		}
		
		// Kill the SizeTask thread.
		sz.kill();
	}

	
}
