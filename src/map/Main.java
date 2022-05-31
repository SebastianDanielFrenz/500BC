package map;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) throws IOException {
		
		try {
			GamePanel.myPicture = ImageIO.read(new Runnable() {public void run() {}}.getClass().getResource(World.IMAGE_URL));
		} catch (IOException e) {
			e.printStackTrace();
		}

		World world = new World(World.IMAGE_URL);
		World.date.set(-500, 1, 1);
		GamePanel panel = new GamePanel(world);
		//thread.start();
		long lastFrame = 0;
		double delay = 1 / 120.0;
		while (true) {
			long start = System.nanoTime();
			panel.repaint();
			long end = System.nanoTime();
			panel.gameLoop();
			double duration = (end - start) / 1000000000.0;
			if (delay - duration > 0) {
				try {
					Thread.sleep((long) ((delay - duration) * 1000), 0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// System.out.println(1 / (((double) end - start) / 1000000000));
		}

	}

}
