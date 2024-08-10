package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    private TETile[][] world;
    private int width;
    private int height;
    private Map<String, String> coordinateSides;
    private List<String> validPointsForHallways;
    private List<String> validPointsForRooms;

    public World(int w, int h) {
        width = w;
        height = h;
        world = new TETile[w][h];
        createBlankWorld(w, h);
        coordinateSides = new HashMap<>();
        validPointsForHallways = new ArrayList<>();
        validPointsForRooms = new ArrayList<>();
    }

    private void createBlankWorld(int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y % 5 == 0 || x % 5 == 0) {
                    world[x][y] = Tileset.FLOWER;
                } else {
                    world[x][y] = Tileset.FLOWER;
                }
            }
        }
    }
    /**
     * Modifies 2D array world to generate a room
     *
     * @param w width of room
     * @param h height of room
     * @param fx start x coordinate on world
     * @param fy start y coordinate on world
     * @param side what side we are building on
     **/
    public void generateSpace(int w, int h, int fx, int fy, String side) {
        int[] startCoordinates = findNewCoordinates(w, h, fx, fy, side);
        int startX = startCoordinates[0];
        int startY = startCoordinates[1];
        if (side.equals("top") || side.equals("right")) {
            buildFromTopAndRight(w, h, startX, startY);
        } else if (side.equals("bottom") || side.equals("left")) {
            buildFromBottomAndLeft(w, h, startX, startY);
        }
        connect(fx, fy);
    }
    private void buildFromTopAndRight(int w, int h, int startX, int startY) {
        //make sure it doesn't start from beyond bottom or left end of the world (0,0)
        if (startX < 0 || startY < 2) {
            return;
        }
        //edge case: when we try to generate a room that is 2 or fewer pixels than right or top edge
        if (startX >= width - 2 || startY >= height - 2) {
            return;
        }
        int stopX = startX + w;
        int stopY = startY + h;
        if (stopX >= width) {
            stopX = width;
        }
        if (stopY >= height - 2) {
            stopY = height - 2;
        }
        TETile wall = Tileset.WALL;
        TETile floor = Tileset.FLOOR;
        //every room will only tear down at most 2 walls
        int wallsTornDown = 0;
        for (int x = startX; x < stopX; x++) {
            for (int y = startY; y < stopY; y++) {
                if (world[x][y].equals(floor)) {
                    //do nothing to continue over it
                    //continue;
                } else if (x == startX || x == stopX - 1) {
                    world[x][y] = wall;
                    //determineValidSite(w, h, x, y, startX, startY);
                    if (wallsTornDown < 2) {
                        wallsTornDown += connect(x, y);
                    }
                } else if (y == startY || y == stopY - 1) {
                    world[x][y] = wall;
                    //determineValidSite(w, h, x, y, startX, startY);
                    if (wallsTornDown < 2) {
                        wallsTornDown += connect(x, y);
                    }
                } else {
                    world[x][y] = floor;
                    removeValidSite(x, y);
                }
                if (world[x][y].equals(wall)) {
                    if (wallsTornDown < 2) {
                        wallsTornDown += connect(x, y);
                    }
                }
            }
        }
    }
    private void buildFromBottomAndLeft(int w, int h, int startX, int startY) {
        //make sure it doesn't start from beyond the top or right end of the world
        if (startX >= width || startY >= height - 2) {
            return;
        }
        //edge case: when we try to generate a room that is 2 or fewer pixels in the bottom or left side of world
        if (startX < 2 || startY < 2) {
            return;
        }
        int stopX = startX - w;
        int stopY = startY - h;
        if (stopX < 0) {
            stopX = -1;
        }
        if (stopY <= 2) {
            stopY = 1;
        }
        TETile wall = Tileset.WALL;
        TETile floor = Tileset.FLOOR;
        int wallsTornDown = 0;
        for (int x = startX; x > stopX; x--) {
            for (int y = startY; y > stopY; y--) {
                if (world[x][y].equals(floor)) {
                    //continue;
                } else if (x == startX || x == stopX + 1) {
                    world[x][y] = wall;
                    if (wallsTornDown < 2) {
                        wallsTornDown += connect(x, y);
                    }
                } else if (y == startY || y == stopY + 1) {
                    world[x][y] = wall;
                    if (wallsTornDown < 2) {
                        wallsTornDown += connect(x, y);
                    }
                } else {
                    world[x][y] = floor;
                    removeValidSite(x, y);
                }
                if (world[x][y].equals(wall)) {
                    if (wallsTornDown < 2) {
                        wallsTornDown += connect(x, y);
                    }
                }
            }
        }
    }

    private int[] findNewCoordinates(int w, int h, int startX, int startY, String side) {
        if (side.equals("top")) {
            return new int[]{startX - 1, startY};
        } else if (side.equals("right")) {
            return new int[]{startX, startY - 1};
        } else if (side.equals("bottom")) {
            return new int[]{startX + 1, startY};
        } else { //left
            return new int[]{startX, startY + 1};
        }
    }
    /**
     * will change a wall tile to a floor tile if a connection should be made
     *
     * @param x coordinate of tile
     * @param y coordinate of tile
     **/
    public int connect(int x, int y) {
        //breaking down walls building from the down side
        //if above and below this wall tile are floors then change to floor to connect
        //checks to make sure it won't go out of bounds
        if (y + 1 < height && y - 1 > 0) {
            if (world[x][y + 1].equals(Tileset.FLOOR) && world[x][y - 1].equals(Tileset.FLOOR)) {
                world[x][y] = Tileset.FLOOR;
                removeValidSite(x, y);
                return 1;
            }
        }
        //breaking down walls building from the left side
        //if left and right of this wall tile are floors then change to floor to connect
        //checks to make sure it won't go out of bounds
        if (x + 1 < width && x - 1 > 0) {
            if (world[x + 1][y].equals(Tileset.FLOOR) && world[x - 1][y].equals(Tileset.FLOOR)) {
                world[x][y] = Tileset.FLOOR;
                removeValidSite(x, y);
                return 1;
            }
        }
        return 0;
    }
    private void removeValidSite(int x, int y) {
        String coordinate = "" + x + "," + y;
        coordinateSides.remove(coordinate);
        validPointsForHallways.remove(coordinate);
        validPointsForRooms.remove(coordinate);
    }
    public TETile[][] getWorld() {
        return world;
    }
    public List<String> getValidPointsForHallways() {
        return validPointsForHallways;
    }
    public List<String> getValidPointsForRooms() {
        return validPointsForRooms;
    }
    public Map<String, String> getCoordinateSides() {
        return coordinateSides;
    }

    public static void main(String[] args) {
        World world = new World(80, 40);
        world.generateSpace(100, 100, 70, 30, "bottom");
        TERenderer ter = new TERenderer();
        ter.initialize(80, 40);
        ter.renderFrame(world.getWorld());
    }
}