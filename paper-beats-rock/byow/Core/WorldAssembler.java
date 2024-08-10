package byow.Core;

import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldAssembler {
    private TETile[][] world;
    private Player player;
    private Goal goal;
    private ArrayList<Enemy> enemies;
    public WorldAssembler(Random seed, int width, int height, Screen screen, int playerHealth) {
        RandomWorldGenerator newWorld = new RandomWorldGenerator(seed, width, height);
        newWorld.generateWorld();
        world = newWorld.getWorld();
        player = new Player(newWorld.getWorld(), seed, playerHealth);
        goal = new Goal(newWorld.getWorld(), seed);
        int numEnemies = RandomUtils.uniform(seed, 8, 15);
        enemies = new ArrayList<>();
        for (int i = 0; i < numEnemies; i++) {
            Enemy enemy = new Enemy(world, seed, screen);
            enemies.add(enemy);
        }
    }
    public WorldAssembler(Random seed, int width, int height, Screen screen) {
        RandomWorldGenerator newWorld = new RandomWorldGenerator(seed, width, height);
        newWorld.generateWorld();
        world = newWorld.getWorld();
        player = new Player(newWorld.getWorld(), seed);
        goal = new Goal(newWorld.getWorld(), seed);
        int numEnemies = RandomUtils.uniform(seed, 8, 15);
        enemies = new ArrayList<>();
        for (int i = 0; i < numEnemies; i++) {
            Enemy enemy = new Enemy(world, seed, screen);
            enemies.add(enemy);
        }
    }
    public TETile[][] getWorld() {
        return world;
    }
    public Player getPlayer() {
        return player;
    }
    public Goal getGoal() {
        return goal;
    }
    public boolean reachedGoal() {
        if (player.getLocation().equals(goal.getLocation())) {
            return true;
        }
        return false;
    }
    public boolean encounteredEnemy(List<String> list) {
        ArrayList<Enemy> enims = new ArrayList<>();
        enims.addAll(enemies);
        for (Enemy enemy : enims) {
            if (enemy.getLocation().equals(player.getLocation())) {
                int healthLoss = enemy.attack(list);
                enemy.remove();
                enemies.remove(enemy);
                player.loseHealth(healthLoss);
                return true;
            }
        }
        return false;
    }
    public boolean encounteredEnemy(String input) {
        ArrayList<Enemy> enims = new ArrayList<>();
        enims.addAll(enemies);
        for (Enemy enemy : enims) {
            if (enemy.getLocation().equals(player.getLocation())) {
                List<String> tie = new ArrayList<>();
                int healthLoss = enemy.attack(input, tie);
                if (tie.size() < 1) {
                    enemies.remove(enemy);
                }
                player.loseHealth(healthLoss);
                return true;
            }
        }
        return false;
    }
    public void moveAllEnemies() {
        ArrayList<Enemy> enims = new ArrayList<>();
        enims.addAll(enemies);
        for (Enemy enemy : enims) {
            enemy.move();
        }
    }
}
