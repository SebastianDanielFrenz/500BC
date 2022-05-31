package map;

public class ThreadedLoop implements Runnable {

	@Override
	public void run() {
		while (true) {
			long start = System.nanoTime();
			World.updateWorld();
			long end = System.nanoTime();
			double ms = (end - start) / 1000000.0;
			//System.out.println("Day took "+ms+" ms");
		}
	}

}
