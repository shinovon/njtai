package njtai.d;

public class Task implements Runnable {
	
	private Runnable runnable;
	protected Task() {
	}
	
	public Task(Runnable runnable) {
		this.runnable = runnable;
	}

	public void run() {
		runnable.run();
	}
	
	public void stop() {
		
	}

}
