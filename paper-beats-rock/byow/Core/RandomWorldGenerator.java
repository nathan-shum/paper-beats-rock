package byow.Core;

import java.util.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


public class RandomWorldGenerator {
    private World world;
    private Random seed;
    private int width;
    private int height;
    private Map<String, String> coordinateSides;
    private List<String> validPointsForHallways;
    private List<String> validPointsForRooms;
    private List<String> previouslyChosenCoordinates;

    /**
     * @param seed
     * @param width width of the world
     * @param height height of the world
     */
    public RandomWorldGenerator(Random seed, int width, int height) {
        world = new World(width, height);
        this.seed = seed;
        this.width = width;
        this.height = height;
        coordinateSides = world.getCoordinateSides();
        validPointsForHallways = world.getValidPointsForHallways();
        validPointsForRooms = world.getValidPointsForRooms();
        previouslyChosenCoordinates = new ArrayList<>();
    }

    /**
     * generate the random rooms and hallways according to seed
     * modify world
     */
    public void generateWorld() {

        //generates first room
        int spawnWidth = RandomUtils.uniform(seed, 5, 10);
        int spawnHeight = RandomUtils.uniform(seed, 5, 10);
        int spawnX = RandomUtils.uniform(seed, 30, 40);
        int spawnY = RandomUtils.uniform(seed, 10, 25);
        //default to top for first room
        world.generateSpace(spawnWidth, spawnHeight, spawnX, spawnY, "top");
        //reset the spawn room's spawn point so the wall isn't broken down
        world.getWorld()[spawnX][spawnY] = Tileset.WALL;
        Map <String, String> firstSites = findValidCoordinates(spawnWidth, spawnHeight, spawnX, spawnY, "top");
        addNewSites(firstSites, spawnWidth, spawnHeight);
        spawnX += 2;
        validPointsForHallways.add("" + spawnX + "," + spawnY);
        coordinateSides.put("" + spawnX + "," + spawnY, "bottom");

        int numRooms = RandomUtils.uniform(seed, 13, 16);
        int numHalls = RandomUtils.uniform(seed, 13, 16);
        while (numRooms > 0 && numHalls > 0) {
            if (validPointsForHallways.size() > 0) {
                for (int i = 0; i < 1; i++) {
                    int genWidth;
                    int genHeight;
                    String strSpawnCoords = randomCoordinate("hallway");
                    String side = coordinateSides.remove(strSpawnCoords);
                    if (side == null) {
                        break;
                    }
                    if (side.equals("top") || side.equals("bottom")) {
                        genWidth = RandomUtils.uniform(seed, 3, 4);
                        genHeight = RandomUtils.uniform(seed, 13, 18);
                    } else {
                        genWidth = RandomUtils.uniform(seed, 13, 22);
                        genHeight = RandomUtils.uniform(seed, 3, 4);
                    }
                    int[] SpawnCoordinates = stringConverter(strSpawnCoords);
                    world.generateSpace(genWidth, genHeight, SpawnCoordinates[0], SpawnCoordinates[1], side);
                    Map <String, String> newSites = findValidCoordinates(genWidth, genHeight, SpawnCoordinates[0], SpawnCoordinates[1], side);
                    addNewSites(newSites, genWidth, genHeight);
                }
            }
            numHalls--;
            //generate 1 room
            for (int i = 0; i < 1; i++) {
                if (validPointsForRooms.size() > 0) {
                    int genWidth = RandomUtils.uniform(seed, 5, 10);
                    int genHeight = RandomUtils.uniform(seed, 5, 10);
                    String strSpawnCoords = randomCoordinate("room");
                    String side = coordinateSides.remove(strSpawnCoords);
                    if (side == null) {
                        break;
                    }
                    int[] SpawnCoordinates = stringConverter(strSpawnCoords);
                    world.generateSpace(genWidth, genHeight, SpawnCoordinates[0], SpawnCoordinates[1], side);
                    Map<String, String> newSites = findValidCoordinates(genWidth, genHeight, SpawnCoordinates[0], SpawnCoordinates[1], side);
                    addNewSites(newSites, genWidth, genHeight);
                }
            }
            numRooms--;
        }
    }
    public TETile[][] getWorld() {
        return world.getWorld();
    }
    /**
     * chooses random valid point to grow
     * removes it from list
     * @param space is the type of space you're building
     * @return a String "x,y"
     */
    private String randomCoordinate(String space) {
        if (space.equals("hallway")) {
            int index =  RandomUtils.uniform(seed, 0, validPointsForHallways.size());
            String retCoordinate = validPointsForHallways.remove(index);
            previouslyChosenCoordinates.add(retCoordinate);
            return retCoordinate;
        } else {
            int index =  RandomUtils.uniform(seed, 0, validPointsForRooms.size());
            String retCoordinate = validPointsForRooms.remove(index);
            previouslyChosenCoordinates.add(retCoordinate);
            return retCoordinate;
        }
    }

    /**
     * parses the coordinate pair "x,y" and returns it in an array form
     * @param coordString
     * @return [x, y]
     */
    private int[] stringConverter(String coordString) {
        String[] temp = coordString.split(",");
        int x = Integer.parseInt(temp[0]);
        int y = Integer.parseInt(temp[1]);
        return new int[]{x, y};
    }
    private Map<String, String> findValidCoordinates(int width, int height, int startX, int startY, String side) {
        Map<String, String> retMap = new HashMap<>();
        int upper = startY + height - 1;
        int lowest = startY - height + 1;
        int rightest = startX + width - 1;
        int leftest = startX - width + 1;
        if (side.equals("top")) {
            //top coordinate
            //this one can change
            int x = startX;
            if (validate(x, upper)) {
                retMap.put("" + x + "," + upper, "top");
            }
            //left coordinate
            int y = upper - 1;
            int xx = startX - 1;
            if (validate(xx, y)) {
                retMap.put("" + xx + "," + y, "left");
            }
            //right
            int xxx = rightest - 1;
            if (validate(xxx, y)) {
                retMap.put("" + xxx + "," + y, "right");
            }
        } else if (side.equals("left")) {
            //top
            int x = leftest + 1;
            int y = startY + 1;
            if (validate(x, y)) {
                retMap.put("" + x + "," + y, "top");
            }
            //left=
            if (validate(leftest, startY)) {
                retMap.put("" + leftest + "," + startY, "left");
            }
            //bottom
            int yy = lowest + 1;
            if (validate(x, yy)) {
                retMap.put("" + x + "," + yy, "bottom");
            }
        } else if (side.equals("right")) {
            //top
            int x = rightest - 1;
            int y = upper - 1;
            if (validate(x, y)) {
                retMap.put("" + x + "," + y, "top");
            }
            //right
            if (validate(rightest, startY)) {
                retMap.put("" + rightest + "," + startY, "right");
            }
            //bottom
            int yy = startY - 1;
            if (validate(x, yy)) {
                retMap.put("" + x + "," + yy, "bottom");
            }
        } else if (side.equals("bottom")) {
            //left
            int y = lowest + 1;
            int x = leftest + 1;
            if (validate(x, y)) {
                retMap.put("" + x + "," + y, "left");
            }
            //bot
            if (validate(startX, lowest)) {
                retMap.put("" + startX + "," + lowest, "bottom");
            }
            //right
            int xx = startX + 1;
            if (validate(xx, y)) {
                retMap.put("" + xx + "," + y, "right");
            }
        }
        return retMap;
    }
    private boolean validate(int x, int y) {
        if (x < 2) {
            return false;
        } else if (y < 2) {
            return false;
        } else if (x >= width - 3) {
            return false;
        } else if (y >= height - 3) {
            return false;
        }
        return true;
    }
    private void addNewSites(Map<String, String> newSites, int width, int length) {
        for (String site : newSites.keySet()) {
            if (!previouslyChosenCoordinates.contains(site)) {
                //if it is a hallway add it to points for rooms
                if (width < 5 || length < 5) {
                    validPointsForRooms.add(site);
                }
                coordinateSides.put(site, newSites.get(site));
                validPointsForHallways.add(site);
            }
        }
    }
}