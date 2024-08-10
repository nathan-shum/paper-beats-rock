package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Goal {
    private int x;
    private int y;
    private TETile[][] world;
    private Random seed;
    private int width;
    private int height;

    public Goal(TETile[][] world, Random seed) {
        this.world = world;
        this.seed = seed;
        width = world.length;
        height = world[0].length;
        int[] location = randomSpawn();
        x = location[0];
        y = location[1];
        world[x][y] = Tileset.LOCKED_DOOR;
    }

    private int[] randomSpawn() {
        //choose random location
        int[] coordinates = new int[]{RandomUtils.uniform(seed, 5, width - 5), RandomUtils.uniform(seed, 5, height - 5)};
        boolean isFloor = world[coordinates[0]][coordinates[1]].equals(Tileset.FLOOR);
        boolean nextToWall = checkWalls(coordinates[0], coordinates[1]);
        while (!isFloor || nextToWall) {
            coordinates = new int[]{RandomUtils.uniform(seed, 0, width), RandomUtils.uniform(seed, 0, height)};
            isFloor = world[coordinates[0]][coordinates[1]].equals(Tileset.FLOOR);
            nextToWall = checkWalls(coordinates[0], coordinates[1]);
        }
        return coordinates;
    }
    private boolean checkWalls(int x, int y) {
        if (x + 1 < width) {
            if (world[x + 1][y].equals(Tileset.WALL)) {
                return true;
            }
        }
        if (x - 1 >= 0) {
            if (world[x - 1][y].equals(Tileset.WALL)) {
                return true;
            }
        }
        if (y - 1 >= 0) {
            if (world[x][y - 1].equals(Tileset.WALL)) {
                return true;
            }
        }
        if (y + 1 < height) {
            if (world[x][y + 1].equals(Tileset.WALL)) {
                return true;
            }
        }
        if (x + 1 < width && y + 1 < height) {
            if (world[x + 1][y + 1].equals(Tileset.WALL)) {
                return true;
            }
        }
        if (x - 1 >= 0 && y + 1 < height) {
            if (world[x - 1][y + 1].equals(Tileset.WALL)) {
                return true;
            }
        }
        if (x - 1 >= 0 && y - 1 >= 0) {
            if (world[x - 1][y - 1].equals(Tileset.WALL)) {
                return true;
            }
        }
        if (x + 1 < width && y - 1 >= 0) {
            if (world[x + 1][y - 1].equals(Tileset.WALL)) {
                return true;
            }
        }
        return false;
    }
    public String getLocation() {
        return "" + x + "," + y;
    }
}
