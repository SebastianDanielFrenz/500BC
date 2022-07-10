package map;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

import game.territory.CityStateGovernment;
import game.territory.Government;
import game.territory.Realm;
import game.territory.terrain.Terrain;
import util.CDate;

public class World {

    public static Random random = new Random();
    public static final String IMAGE_URL = "/res/map_data/provinces.png";

    public static CDate date = new CDate();

    protected static int[][] pixels;
    protected static int width, height;
    protected static Set<Integer> uniqueColors;
    protected static Map<Integer, Integer> colorToID;
    // protected static Map<Integer, LinkedList<int[]>> IDtoPixels; // pls remove
    protected static int[][] pixelToID;

    protected static Territory[] territories;
    protected static List<Realm> realms;

    public static final boolean DUMP_NEIGHBOURS = false;
    public static final boolean VIEW_BIOME = false;
    public static final boolean VIEW_UNIQUE_COLORS = false;
    public static final boolean REFACTOR_MAP = false;
    public static final boolean COLOR_BASED_TERRAIN_LOADING = false;

    public static Set<Territory> DEBUG_LIST = new TreeSet<Territory>((x, y) -> x.ID == y.ID ? 0 : x.ID > y.ID ? 1 : -1);

    public static final Scanner scanner = new Scanner(System.in);

    public static CDate beginDate;

    /*
     * static { int count = 2; for (int c = 0; c < count; c++) { new Thread(() -> {
     * while (true) { if (!colorQueue1.isEmpty()) {
     *
     * int original = colorQueue1.poll(); int dst = colorQueue2.poll();
     *
     * for (int x = 0; x < width; x++) { for (int y = 0; y < height; y++) { if
     * (pixels[y][x] == original) { GamePanel.myPicture.setRGB(x, y, dst); } } } }
     * else { try { Thread.sleep(200); } catch (InterruptedException e) {
     * e.printStackTrace(); } } } }).start(); } }
     */

    public World(String imageDst) throws IOException {
        loadMap(getClass().getResource(imageDst));

        populateWorld();
        loadBiomes();
        if (VIEW_BIOME) {
            loadBiomesDebug();
        } else if (VIEW_UNIQUE_COLORS) {
            loadBiomesDebug();

            Thread thread = new Thread(new ThreadedLoop());
            thread.start();
        } else {
            determineNeighbours();

            new Thread(() -> {
                polishWorld(0);
                Thread thread = new Thread(new ThreadedLoop());
                thread.start();
            }).start();
        }
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
        // IDtoPixels = new TreeMap<>();
        Iterator<Integer> it = uniqueColors.iterator();
        int i = 0;
        while (it.hasNext()) {
            colorToID.put(it.next(), i);
            i++;
        }

        pixelToID = new int[pixels.length][];

        territories = new Territory[uniqueColors.size()];
        Iterator<Integer> uniqueColor = uniqueColors.iterator();
        for (int j = 0; j < uniqueColors.size(); j++) {
            // IDtoPixels.put(j, new LinkedList<int[]>());
            territories[j] = new Territory(j, uniqueColor.next());
        }

        try {
            FileReader fr = new FileReader("pixels.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String[] split;

            Territory t;
            int len;
            while (line != null && !line.isEmpty()) {
                split = line.split(" = ");
                t = territories[Integer.parseInt(split[0])];
                len = Integer.parseInt(split[1]);
                t.pixelsX = new short[len];
                t.pixelsY = new short[len];
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Territory t = territories[0];
        for (i = 0; i < pixels.length; i++) {
            pixelToID[i] = new int[pixels[i].length];
            for (int j = 0; j < pixels[i].length; j++) {
                pixelToID[i][j] = colorToID.get(pixels[i][j]);
                // IDtoPixels.get(pixelToID[i][j]).add(new int[] { i, j });
                if (REFACTOR_MAP) {
                    territories[pixelToID[i][j]].addPixelWithoutPixelFile((short) i, (short) j);
                } else {
                    territories[pixelToID[i][j]].addPixel((short) i, (short) j);
                }
            }
        }

        if (REFACTOR_MAP) {
            FileWriter fw = new FileWriter("pixels.txt");
            for (Territory t2 : territories) {
                fw.write(String.valueOf(t2.ID) + " = " + String.valueOf(t2.pixelCount));
            }
            fw.close();
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

        Iterator<Integer> uniqueColor = uniqueColors.iterator();
        for (int i = 0; i < territories.length; i++) {

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
                for (Territory t2 : territories) {
                    fw.write(String.valueOf(t2.ID));
                    for (Territory t3 : t2.getNeighbours()) {
                        fw.write("," + t3.ID);
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
        } else {
            determineNeighbours(src);
        }
        determineNeighbours(dst);

        t.setRealm(dst);
        // setTerritoryColor(t, dst.getTerritories().size() > 0 ?
        // dst.getTerritories().get(0).fileColor : 0xffffffff);
    }

    private static void determineNeighbours(Realm src) {
        // not implemented yet - no Realm neighbours implemented
    }

    public static void setTerritoryColor(Territory t, int alpha, int r, int g, int b) {
        /*
         * for (int x = 0; x < width; x++) { for (int y = 0; y < height; y++) { if
         * (pixels[y][x] == t.fileColor) { GamePanel.myPicture.setRGB(x, y, argb); } } }
         */

        /*
         * for (int[] pixel : IDtoPixels.get(t.ID)) {
         * GamePanel.myPicture.setRGB(pixel[1], pixel[0], argb); }
         */

        setTerritoryColor(t, generateColorCode(alpha, r, g, b));
    }

    public static void setTerritoryColor(Territory t, int argb) {

        // for (int x = 0; x < width; x++) {
        // for (int y = 0; y < height; y++) {
        // if (pixels[y][x] == t.fileColor) {
        // GamePanel.myPicture.setRGB(x, y, argb);
        // }
        // }
        // }

        // for (int[] pixel : IDtoPixels.get(t.ID)) {
        for (int i = 0; i < t.pixelCount; i++) {
            GamePanel.myPicture.setRGB(t.pixelsY[i], t.pixelsX[i], argb);
            // System.out.println("pixel");
        }

        // colorQueue1.add(t.fileColor);
        // colorQueue2.add(argb);
    }

    public static int generateColorCode(int alpha, int r, int g, int b) {
        int argb = 0;
        argb += (((int) alpha & 0xff) << 24); // alpha
        argb += ((int) b & 0xff); // blue
        argb += (((int) g & 0xff) << 8); // green
        argb += (((int) r & 0xff) << 16); // red
        return argb;
    }

    public static void updateWorld() {
        int len = date.getNumberOfDaysInMonth();
        //for (int i = date.getDay() - 1; i < territories.length; i += len) {
        for (int i = 0; i < territories.length; i++) {
            territories[i].update();
        }
        for (int i = 0; i < realms.size(); i++) {
            realms.get(i).update();
        }

        date.addDay();
    }

    public static void dumpDebugList() {
        try {
            System.out.println("Biome: ");
            String biome = scanner.nextLine();
            FileWriter fw = new FileWriter("biomes.txt", true);
            for (Territory t : DEBUG_LIST) {
                fw.write(t.ID + " = " + biome + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBiomes() {
        try {
            FileReader fr = new FileReader("biomes.txt");
            BufferedReader br = new BufferedReader(fr);

            String line;
            Terrain terrain;
            while (true) {
                line = br.readLine();
                if (line == null || line.equals("")) {
                    break;
                }
                String[] s = line.split(" = ");
                int ID = Integer.parseInt(s[0]);
                String terrain_name = s[1];

                terrain = Terrain.getTerrain(terrain_name);
                territories[ID].setTerrain(terrain);
                // if (terrain == null) {
                // throw new RuntimeException("Terrain for " + ID + " was null!");
                // }
                // System.out.println(terrain.name);
            }

            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBiomesDebug() {
        realms.clear();
        for (int i = 0; i < Terrain.registry.size(); i++) {
            Realm realm = new Realm(new ArrayList<Realm>(), new ArrayList<Territory>(), new CityStateGovernment());
            realm.setName(Terrain.registry_names.get(i));
            realm.setColor(Terrain.registry.get(i).color);
            realms.add(realm);
        }

        /*
         * for (int i = 0; i < territories.length; i++) {
         * territories[i].setTerrain(Terrain.PLAINS); }
         */

        Terrain t;
        Random r = new Random();
        final int max = 20;
        for (int i = 0; i < territories.length; i++) {
            int index = Terrain.getIndex(territories[i].getTerrain());
            if (index != -1) {
                realms.get(index).addTerritory(territories[i]);
                t = territories[i].getTerrain();
            } else {
                realms.get(2).addTerritory(territories[i]);
                t = Terrain.registry.get(2);
            }
            World.setTerritoryColor(territories[i], 0xff, t.r > 128 ? t.r - r.nextInt(max) : t.r + r.nextInt(max),
                    t.g > 128 ? t.g - r.nextInt(max) : t.g + r.nextInt(max),
                    t.b > 128 ? t.b - r.nextInt(max) : t.b + r.nextInt(max));
        }

    }

    public static void runDebugMethodRecolor() {
        Terrain t;
        Random r = new Random();
        final int max = 20;
        for (int i = 0; i < territories.length; i++) {
            int index = Terrain.getIndex(territories[i].getTerrain());
            if (index != -1) {
                t = territories[i].getTerrain();
            } else {
                t = Terrain.registry.get(2);
            }
            World.setTerritoryColor(territories[i], 0xff, t.r > 128 ? t.r - r.nextInt(max) : t.r + r.nextInt(max),
                    t.g > 128 ? t.g - r.nextInt(max) : t.g + r.nextInt(max),
                    t.b > 128 ? t.b - r.nextInt(max) : t.b + r.nextInt(max));
        }
    }

    public static void runDebugMethodDumpPixels() {
        try {
            FileWriter fw = new FileWriter("pixels.txt", true);
            for (Territory t : territories) {
                fw.write(t.ID + " = " + t.pixelCount + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runDebugMethod() {

    }

    public static void dumpTerrainByColor() {
        try {
            FileWriter fw = new FileWriter("color2Terrain.txt");
            for (int i = 0; i < territories.length; i++) {
                fw.write(territories[i].fileColor + " = " + territories[i].getTerrain());
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBiomeByColor() {

    }

}
