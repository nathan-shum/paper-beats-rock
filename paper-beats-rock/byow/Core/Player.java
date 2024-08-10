package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class Player {
    /**Add instance variables to the field*/
    private int health;
    private int x;
    private int y;
    private TETile[][] world;
    private Random seed;
    private int width;
    private int height;

    public Player(TETile[][] world, Random seed, int playerHealth) {
        health = playerHealth;
        this.world = world;
        this.seed = seed;
        width = world.length;
        height = world[0].length;
        int[] location = randomSpawn();
        x = location[0];
        y = location[1];
        world[location[0]][location[1]] = Tileset.AVATAR;
    }

    public Player(TETile[][] world, Random seed) {
        health = 5;
        this.world = world;
        this.seed = seed;
        width = world.length;
        height = world[0].length;
        int[] location = randomSpawn();
        x = location[0];
        y = location[1];
        world[location[0]][location[1]] = Tileset.AVATAR;
    }

    private int[] randomSpawn() {
        //choose random location
        int[] coordinates = new int[]{RandomUtils.uniform(seed, 0, width), RandomUtils.uniform(seed, 0, height)};
        while (!world[coordinates[0]][coordinates[1]].equals(Tileset.FLOOR)) {
            coordinates = new int[]{RandomUtils.uniform(seed, 0, width), RandomUtils.uniform(seed, 0, height)};
        }
        return coordinates;
    }
    public void moveUp() {
        if (world[x][y + 1].equals(Tileset.FLOOR) || world[x][y + 1].equals(Tileset.LOCKED_DOOR) || world[x][y + 1].equals(Tileset.TREE)) {
            world[x][y] = Tileset.FLOOR;
            y++;
            world[x][y] = Tileset.AVATAR;
        }
    }
    public void moveDown() {
        if (world[x][y - 1].equals(Tileset.FLOOR) || world[x][y - 1].equals(Tileset.LOCKED_DOOR) || world[x][y - 1].equals(Tileset.TREE)) {
            world[x][y] = Tileset.FLOOR;
            y -= 1;
            world[x][y] = Tileset.AVATAR;
        }
    }
    public void moveLeft() {
        if (world[x - 1][y].equals(Tileset.FLOOR) || world[x - 1][y].equals(Tileset.LOCKED_DOOR) || world[x - 1][y].equals(Tileset.TREE)) {
            world[x][y] = Tileset.FLOOR;
            x -= 1;
            world[x][y] = Tileset.AVATAR;
        }
    }
    public void moveRight() {
        if (world[x + 1][y].equals(Tileset.FLOOR) || world[x + 1][y].equals(Tileset.LOCKED_DOOR) || world[x + 1][y].equals(Tileset.TREE)) {
            world[x][y] = Tileset.FLOOR;
            x++;
            world[x][y] = Tileset.AVATAR;
        }
    }
    public String getLocation() {
        return "" + x + "," + y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getHealth() {
        return health;
    }
    public void loseHealth(int num) {
        health -= num;
    }

}
