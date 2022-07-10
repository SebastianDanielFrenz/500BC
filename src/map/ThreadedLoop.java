package map;

public class ThreadedLoop implements Runnable {

    @Override
    public void run() {
        if (World.VIEW_UNIQUE_COLORS) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Starting!");
            for (int i = 0; i < World.territories.length; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
                World.setTerritoryColor(World.territories[i], 255, 0, 0, 0);
            }
        } else {
            while (true) {
                long start = System.nanoTime();
                World.updateWorld();
                long end = System.nanoTime();
                double sec = (end - start) / 1000000000.0;
                System.out.println(Math.round(1 / sec) + "days/sec");
            }
        }
    }

}
