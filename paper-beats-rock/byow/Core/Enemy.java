package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy {
    private int x;
    private int y;
    private TETile[][] world;
    private Random seed;
    private int width;
    private int height;
    private Screen screen;

    public Enemy(TETile[][] world, Random seed, Screen screen) {
        this.screen = screen;
        this.world = world;
        this.seed = seed;
        width = world.length;
        height = world[0].length;
        int[] location = randomSpawn();
        x = location[0];
        y = location[1];
        world[location[0]][location[1]] = Tileset.TREE;
    }
    private int[] randomSpawn() {
        //choose random location
        int[] coordinates = new int[]{RandomUtils.uniform(seed, 0, width), RandomUtils.uniform(seed, 0, height)};
        while (!world[coordinates[0]][coordinates[1]].equals(Tileset.FLOOR)) {
            coordinates = new int[]{RandomUtils.uniform(seed, 0, width), RandomUtils.uniform(seed, 0, height)};
        }
        return coordinates;
    }
    public void move() {
        int choice = RandomUtils.uniform(seed, 0, 2);
        if (choice == 1) {
            int uod = RandomUtils.uniform(seed, 0, 2);
            if (uod == 1) {
                moveUp();
            } else {
                moveDown();
            }
        } else {
            int rol = RandomUtils.uniform(seed, 0, 2);
            if (rol == 1) {
                moveRight();
            } else {
                moveLeft();
            }
        }
    }
    public String getLocation() {
        return "" + x + "," + y;
    }
    public void remove() {
        world[x][y] = Tileset.AVATAR;
    }
    public int attack(List<String> list) {
        //returns number to decrement health by
        //activate the battle screen
        //0 = R, 1 = P, 2 = S
        int eRPS = RandomUtils.uniform(seed, 0, 3);
        String pRPS = "" + screen.battleScreen1();
        pRPS = pRPS.toUpperCase();
        list.add(pRPS);
        if (eRPS == 0 && pRPS.equals("R")) {
            screen.battleScreen2("ROCK", "ROCK");
            StdDraw.pause(1000);
            screen.tie();
            StdDraw.pause(1000);
            return attack(list);
        } else if (eRPS == 0 && pRPS.equals("P")) {
            screen.battleScreen2("PAPER", "ROCK");
            StdDraw.pause(1000);
            screen.win();
            StdDraw.pause(1000);
            return 0;
        } else if (eRPS == 0 && pRPS.equals("X")) {
            screen.battleScreen2("SCISSORS", "ROCK");
            StdDraw.pause(1000);
            screen.loss();
            StdDraw.pause(1000);
            return 1;
        } else if (eRPS == 1 && pRPS.equals("R")) {
            screen.battleScreen2("ROCK", "PAPER");
            StdDraw.pause(1000);
            screen.loss();
            StdDraw.pause(1000);
            return 1;
        } else if (eRPS == 1 && pRPS.equals("P")) {
            screen.battleScreen2("PAPER", "PAPER");
            StdDraw.pause(1000);
            screen.tie();
            StdDraw.pause(1000);
            return attack(list);
        } else if (eRPS == 1 && pRPS.equals("X")) {
            screen.battleScreen2("SCISSORS", "PAPER");
            StdDraw.pause(1000);
            screen.win();
            StdDraw.pause(1000);
            return 0;
        } else if (eRPS == 2 && pRPS.equals("R")) {
            screen.battleScreen2("ROCK", "SCISSORS");
            StdDraw.pause(1000);
            screen.win();
            StdDraw.pause(1000);
            return 0;
        } else if (eRPS == 2 && pRPS.equals("P")) {
            screen.battleScreen2("PAPER", "SCISSORS");
            StdDraw.pause(1000);
            screen.loss();
            StdDraw.pause(1000);
            return 1;
        } else { // if (eRPS == 2 && pRPS.equals("X"))
            screen.battleScreen2("SCISSORS", "SCISSORS");
            StdDraw.pause(1000);
            screen.tie();
            StdDraw.pause(1000);
            return attack(list);
        }
    }
    //0 = R, 1 = P, 2 = X
    public int attack(String pRPS, List<String> tie) {
        int eRPS = RandomUtils.uniform(seed, 0, 3);
        if (eRPS == 0 && pRPS.equals("R")) {
            tie.add("tie");
            return 0;
        } else if (eRPS == 0 && pRPS.equals("P")) {
            return 0;
        } else if (eRPS == 0 && pRPS.equals("X")) {
            return 1;
        } else if (eRPS == 1 && pRPS.equals("R")) {
            return 1;
        } else if (eRPS == 1 && pRPS.equals("P")) {
            tie.add("tie");
            return 0;
        } else if (eRPS == 1 && pRPS.equals("X")) {
            return 0;
        } else if (eRPS == 2 && pRPS.equals("R")) {
            return 0;
        } else if (eRPS == 2 && pRPS.equals("P")) {
            return 1;
        } else { // if (eRPS == 2 && pRPS.equals("X"))
            tie.add("tie");
            return 0;
        }
    }
    private void moveUp() {
        if (world[x][y + 1].equals(Tileset.FLOOR)) {
            world[x][y] = Tileset.FLOOR;
            y++;
            world[x][y] = Tileset.TREE;
        }
    }
    private void moveDown() {
        if (world[x][y - 1].equals(Tileset.FLOOR)) {
            world[x][y] = Tileset.FLOOR;
            y -= 1;
            world[x][y] = Tileset.TREE;
        }
    }
    private void moveLeft() {
        if (world[x - 1][y].equals(Tileset.FLOOR)) {
            world[x][y] = Tileset.FLOOR;
            x -= 1;
            world[x][y] = Tileset.TREE;
        }
    }
    private void moveRight() {
        if (world[x + 1][y].equals(Tileset.FLOOR)) {
            world[x][y] = Tileset.FLOOR;
            x++;
            world[x][y] = Tileset.TREE;
        }
    }
}
