package map;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

import game.territory.CityStateGovernment;
import game.territory.Government;
import game.territory.Realm;
import util.CDate;

public class World {

	public static Random random = new Random();
	public static final String IMAGE_URL = "/res/map_data/provinces.png";

	public static final CDate date = new CDate();

	protected static int[][] pixels;
	protected static int width, height;
	protected static Set<Integer> uniqueColors;
	protected static Map<Integer, Integer> colorToID;
	protected static Map<Integer, LinkedList<int[]>> IDtoPixels; // pls remove
	protected static int[][] pixelToID;

	protected static Territory[] territories;
	protected static List<Realm> realms;
	
	protected static Territory[] desert;
	protected static Territory[] ocean;
	
	public static final boolean DUMP_NEIGHBOURS = true;

	/*static {
		int count = 2;
		for (int c = 0; c < count; c++) {
			new Thread(() -> {
				while (true) {
					if (!colorQueue1.isEmpty()) {

						int original = colorQueue1.poll();
						int dst = colorQueue2.poll();

						for (int x = 0; x < width; x++) {
							for (int y = 0; y < height; y++) {
								if (pixels[y][x] == original) {
									GamePanel.myPicture.setRGB(x, y, dst);
								}
							}
						}
					} else {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}*/

	public World(String imageDst) throws IOException {
		loadMap(getClass().getResource(imageDst));
		
		populateWorld();
		determineNeighbours();
		
		new Thread(()->{
			polishWorld(0);
			Thread thread = new Thread(new ThreadedLoop());
			thread.start();
		}).start();
	}

	private static int[][] convertTo3DWithoutUsingGetRGB(BufferedImage image) {

		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		width = image.getWidth();
		height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[][] result = new int[height][width];
		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				result[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		} else {
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				result[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		}

		return result;
	}

	private static Set<Integer> getUniqueColors(int[][] pixels) {
		Set<Integer> out = new TreeSet<Integer>();
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[i].length; j++) {
				out.add(pixels[i][j]);

			}
		}
		return out;
	}

	public static void loadMap(URL imageURL) throws IOException {
		BufferedImage bimg = ImageIO.read(imageURL);

		pixels = convertTo3DWithoutUsingGetRGB(bimg);
		uniqueColors = getUniqueColors(pixels);
		System.out.println(uniqueColors.size());
		colorToID = new TreeMap<Integer, Integer>();
		IDtoPixels = new TreeMap<>();
		Iterator<Integer> it = uniqueColors.iterator();
		int i = 0;
		while (it.hasNext()) {
			colorToID.put(it.next(), i);
			i++;
		}

		pixelToID = new int[pixels.length][];
		//for (int j = 0; j < uniqueColors.size(); j++) {
		//	IDtoPixels.put(j, new LinkedList<int[]>());
		//}

		for (i = 0; i < pixels.length; i++) {
			pixelToID[i] = new int[pixels[i].length];
			for (int j = 0; j < pixels[i].length; j++) {
				pixelToID[i][j] = colorToID.get(pixels[i][j]);
		//		IDtoPixels.get(pixelToID[i][j]).add(new int[] { i, j });
			}
		}
	}

	public static int translateCoordsToID(double x, double y) {
		return pixelToID[(int) ((y + 0.5) * height)][(int) ((x + 0.5) * width * GamePanel.width_scale)];
	}

	public static Territory getTerritoryAt(double x, double y) {
		return territories[translateCoordsToID(x, y)];
	}

	public static Realm getTopRealm(double x, double y) {
		return getTerritoryAt(x, y).getTopLevelRealm();
	}

	public static Realm getChildRealm(Realm realm, double x, double y) {
		Territory t = getTerritoryAt(x, y);
		Realm r;
		for (r = realm; r.getParent() != t.getRealm(); r = r.getParent()) {
		}
		return r;
	}

	public static void populateWorld() {
		realms = new ArrayList<>();

		territories = new Territory[uniqueColors.size()];
		Iterator<Integer> uniqueColor = uniqueColors.iterator();
		for (int i = 0; i < territories.length; i++) {
			territories[i] = new Territory(i, uniqueColor.next());
			Government government = new CityStateGovernment();
			List<Territory> ts = new ArrayList<Territory>();
			ts.add(territories[i]);
			Realm realm = new Realm(new ArrayList<Realm>(), ts, government);
			realms.add(realm);
			territories[i].setRealm(realm);
		}
	}

	public static void determineNeighbours() {
		Territory t;
		int pixel;
		int pixel2;
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				pixel = pixels[y][x];
				t = territories[colorToID.get(pixel)];
				pixel2 = pixels[y + 1][x + 1];
				if (pixel2 != pixel) {
					boolean found = false;
					for (Territory t2 : t.getNeighbours()) {
						if (t2.fileColor == pixel2) {
							found = true;
							break;
						}
					}
					if (!found) {
						t.addNeighbour(territories[colorToID.get(pixel2)]);
					}
				}

				pixel2 = pixels[y + 1][x - 1];
				if (pixel2 != pixel) {
					boolean found = false;
					for (Territory t2 : t.getNeighbours()) {
						if (t2.fileColor == pixel2) {
							found = true;
							break;
						}
					}
					if (!found) {
						t.addNeighbour(territories[colorToID.get(pixel2)]);
					}
				}

				pixel2 = pixels[y - 1][x + 1];
				if (pixel2 != pixel) {
					boolean found = false;
					for (Territory t2 : t.getNeighbours()) {
						if (t2.fileColor == pixel2) {
							found = true;
							break;
						}
					}
					if (!found) {
						t.addNeighbour(territories[colorToID.get(pixel2)]);
					}
				}

				pixel2 = pixels[y - 1][x - 1];
				if (pixel2 != pixel) {
					boolean found = false;
					for (Territory t2 : t.getNeighbours()) {
						if (t2.fileColor == pixel2) {
							found = true;
							break;
						}
					}
					if (!found) {
						t.addNeighbour(territories[colorToID.get(pixel2)]);
					}
				}
			}
		}
		
		if (DUMP_NEIGHBOURS) {
			try {
				FileWriter fw = new FileWriter("neighbours.csv");
				for (Territory t2:territories) {
					fw.write(String.valueOf(t2.ID));
					for (Territory t3:t2.getNeighbours()) {
						fw.write(","+t3.ID);
					}
					fw.write(";");
				}
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void polishWorld(int runs) {
		for (int i = 0; i < runs; i++) {
			Territory t = territories[random.nextInt(territories.length)];
			Realm src = t.getRealm();
			Realm dst = t.getNeighbours().get(random.nextInt(t.getNeighbours().size())).getRealm();
			if (src != dst && src.getTotalTerritoryCount() <= dst.getTotalTerritoryCount()) {
				merge(t, src, dst);
			}
		}
	}

	public static void merge(Territory t, Realm src, Realm dst) {
		src.removeTerritory(t);
		dst.addTerritory(t);
		if (src.getTotalTerritoryCount() == 0) {
			realms.remove(src);
		}
		else {
			determineNeighbours(src);
		}
		determineNeighbours(dst);
		
		t.setRealm(dst);
		setTerritoryColor(t, dst.getTerritories().size() > 0 ? dst.getTerritories().get(0).fileColor : 0xffffffff);
	}

	private static void determineNeighbours(Realm src) {
		// not implemented yet - no Realm neighbours implemented
	}

	public static void setTerritoryColor(Territory t, int alpha, int r, int g, int b) {
		int argb = 0;
		argb += (((int) alpha & 0xff) << 24); // alpha
		argb += ((int) r & 0xff); // blue
		argb += (((int) g & 0xff) << 8); // green
		argb += (((int) b & 0xff) << 16); // red
		/*for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (pixels[y][x] == t.fileColor) {
					GamePanel.myPicture.setRGB(x, y, argb);
				}
			}
		}*/
		
		for (int[] pixel:t.pixels) {
			GamePanel.myPicture.setRGB(pixel[1], pixel[0], argb);
		}
	}

	public static void setTerritoryColor(Territory t, int argb) {
		
		 //for (int x = 0; x < width; x++) {
		//	 for (int y = 0; y < height; y++) {
		//		 if (pixels[y][x] == t.fileColor) { 
		//			 GamePanel.myPicture.setRGB(x, y, argb);
		//		}
		//	 }
		// }
		
		for (int[] pixel:t.pixels) {
			GamePanel.myPicture.setRGB(pixel[1], pixel[0], argb);
			//System.out.println("pixel");
		}
		 
		 
		//colorQueue1.add(t.fileColor);
		//colorQueue2.add(argb);
	}
	
	public static void loadTerritoryMeta() {
		desert = new Territory[] {territories[1288]};
		ocean = new Territory[] {};
	}
	
	public static void updateWorld() {
		int len = date.getNumberOfDaysInMonth();
		for (int i = date.getDay()-1;  i < territories.length; i+=len) {
			 territories[i].update();
		}
		
		date.addDay();
	}

}
