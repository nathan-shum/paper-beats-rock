package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.*;

import static java.lang.Long.parseLong;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int THIRTEEN = 13;
    public static final int TWOHUNDRED = 200;
    public static final int ONETHOUSAND = 1000;

    private WorldAssembler currentWorld;
    private Player player;
    private Goal goal;
    private Random seed;
    private Screen screen = new Screen();
    private int level = 1;
    private boolean lightsOn = false;

    /**
     * @source <a href="https://www.digitalocean.com/community/tutorials/java-create-new-file">...</a>
     */
    private String parseSave() {
        File saveFile = new File("byow/Core/saved.txt");
        In scanner = new In(saveFile);
        StringBuilder parsedBuilder = new StringBuilder();
        while (scanner.hasNextChar()) {
            parsedBuilder.append(scanner.readChar());
        }
        String parsedString = parsedBuilder.toString();
        if (parsedString.length() == 0) {
            return "EMPTY";
        }
        return parsedString;
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        boolean pressedColon = false;
        InputSource inputSource = new KeyboardInputSource();
        screen.menu();
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            String saveString = "";
            if (c == 'N' || c == 'n') {
                String genSeed = screen.liveSeedScreen();
                seed = new Random(parseLong(genSeed));
                cpg();
                ter.renderArea(currentWorld.getWorld(), player.getX(), player.getY());
                saveString += "N" + genSeed + "S";
                level = 1;
                moveSaveQuit(seed, saveString);
                break;
            } else if (c == 'L' || c == 'l') {
                String inputString = load(0);
                if (inputString.equals("EMPTY")) {
                    String genSeed = screen.liveSeedScreen();
                    seed = new Random(parseLong(genSeed));
                    cpg();
                    ter.renderArea(currentWorld.getWorld(), player.getX(), player.getY());
                    saveString += "N" + genSeed + "S";
                    level = 1;
                    moveSaveQuit(seed, saveString);
                } else {
                    interactWithInputString(inputString);
                    if (lightsOn) {
                        ter.renderFrame(currentWorld.getWorld());
                    } else {
                        ter.renderArea(currentWorld.getWorld(), player.getX(), player.getY());
                    }
                    moveSaveQuit(seed, inputString);
                }
            } else if (c == 'Q' || c == 'q') {
                return;
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        String saveString = "";
        input = input.toUpperCase();
        InputSource inputSource = new StringInputDevice(input);
        String seedBuilder = "";
        String firstChar = "" + inputSource.getNextKey();
        if (firstChar.equals("N")) {
            saveString += "N";
            while (inputSource.possibleNextInput()) {
                String currChar = "" + inputSource.getNextKey();
                if (currChar.equals("S")) {
                    seed = new Random(parseLong(seedBuilder));
                    cpg();
                    saveString += "S";
                    break;
                } else {
                    seedBuilder += currChar;
                    saveString += currChar;
                }
            }
        } else if (firstChar.equals("L")) {
            String inputString = load(0);
            if (inputString.equals("EMPTY")) {
                String genSeed = screen.liveSeedScreen();
                seed = new Random(parseLong(genSeed));
                cpg();
            } else {
                interactWithInputString(inputString);
            }
        } else if (firstChar.equals("Q")) {
            return null;
        }
        boolean pressedColon = false;
        while (inputSource.possibleNextInput()) {
            String currChar = "" + inputSource.getNextKey();
            currChar = currChar.toUpperCase();
            if (currChar.equals(":")) {
                pressedColon = true;
            } else if (pressedColon && currChar.equals("Q")) {
                save(saveString, 0);
                break;
            }
            if (currChar.equals("R")) {
                currentWorld.encounteredEnemy("R");
                saveString += currChar;
            } else if (currChar.equals("P")) {
                currentWorld.encounteredEnemy("P");
                saveString += currChar;
            } else if (currChar.equals("X")) {
                currentWorld.encounteredEnemy("X");
                saveString += currChar;
            } else if (currChar.equals("W")) {
                currentWorld.moveAllEnemies();
                player.moveUp();
                saveString += currChar;
            } else if (currChar.equals("A")) {
                currentWorld.moveAllEnemies();
                player.moveLeft();
                saveString += currChar;
            } else if (currChar.equals("S")) {
                currentWorld.moveAllEnemies();
                player.moveDown();
                saveString += currChar;
            } else if (currChar.equals("D")) {
                currentWorld.moveAllEnemies();
                player.moveRight();
                saveString += currChar;
            } else if (currChar.equals("L")) {
                lightsOn = !lightsOn;
                saveString += currChar;
            }
            if (currentWorld != null && currentWorld.reachedGoal()) {
                currentWorld = new WorldAssembler(seed, WIDTH, HEIGHT, screen, player.getHealth());
                player = currentWorld.getPlayer();
                goal = currentWorld.getGoal();
                level++;
            }
        }
        return currentWorld.getWorld();
    }
    private void moveSaveQuit(Random genSeed, String saveString) {
        InputSource inputSource = new KeyboardInputSource();
        boolean pressedColon = false;
        while (true) {
            screen.HUD((double) player.getHealth() / 5, level, getHover());
            StdDraw.pause(THIRTEEN);
            List<String> rpsDecisions = new ArrayList<>();
            if (currentWorld.encounteredEnemy(rpsDecisions)) {
                for (String rps : rpsDecisions) {
                    saveString += rps;
                }
                if (player.getHealth() == 0) {
                    endGame();
                    return;
                } else {
                    renderWorldandHUD();
                }
            } else if (StdDraw.hasNextKeyTyped()) {
                char c = inputSource.getNextKey();
                if (c == 'W') {
                    pressedColon = false;
                    currentWorld.moveAllEnemies();
                    player.moveUp();
                    renderWorldandHUD();
                    saveString += "W";
                } else if (c == 'A') {
                    pressedColon = false;
                    currentWorld.moveAllEnemies();
                    player.moveLeft();
                    renderWorldandHUD();
                    saveString += "A";
                } else if (c == 'S') {
                    pressedColon = false;
                    currentWorld.moveAllEnemies();
                    player.moveDown();
                    renderWorldandHUD();
                    saveString += "S";
                } else if (c == 'D') {
                    pressedColon = false;
                    currentWorld.moveAllEnemies();
                    player.moveRight();
                    renderWorldandHUD();
                    saveString += "D";
                } else if (c == ':') {
                    pressedColon = true;
                } else if (pressedColon && c == 'Q') {
                    save(saveString, 0);
                    break;
                } else if (c == 'L') {
                    pressedColon = false;
                    lightsOn = !lightsOn;
                    renderWorldandHUD();
                    saveString += "L";
                } else if (c == 'R') {
                    String newSeed = screen.liveSeedScreen();
                    seed = new Random(parseLong(newSeed));
                    cpg();
                    ter.renderArea(currentWorld.getWorld(), player.getX(), player.getY());
                    screen.HUD((double) player.getHealth() / 5, level, getHover());
                    String newSaveString = "N" + newSeed + "S";
                    level = 1;
                    lightsOn = false;
                    moveSaveQuit(seed, newSaveString);
                } else {
                    pressedColon = false;
                }
                if (currentWorld != null && currentWorld.reachedGoal()) {
                    StdDraw.pause(TWOHUNDRED);
                    screen.newWorldScreen(level);
                    StdDraw.pause(ONETHOUSAND);
                    currentWorld = new WorldAssembler(seed, WIDTH, HEIGHT, screen, player.getHealth());
                    player = currentWorld.getPlayer();
                    goal = currentWorld.getGoal();
                    renderWorldandHUD();
                    level++;
                    moveSaveQuit(genSeed, saveString);
                }
            }
        }
    }
    private void endGame() {
        InputSource inputSource = new KeyboardInputSource();
        screen.gameOver();
        boolean pressedColon = false;
        while (inputSource.possibleNextInput()) {
            char startOver = inputSource.getNextKey();
            String s = "" + startOver;
            s = s.toUpperCase();
            if (s.equals("P")) {
                String newSeed = screen.liveSeedScreen();
                seed = new Random(parseLong(newSeed));
                cpg();
                ter.renderArea(currentWorld.getWorld(), player.getX(), player.getY());
                screen.HUD((double) player.getHealth() / 5, level, getHover());
                save("EMPTY", 0);
                String newSaveString = "N" + newSeed + "S";
                level = 1;
                lightsOn = false;
                moveSaveQuit(seed, newSaveString);
            }
            if (s.equals(":")) {
                pressedColon = true;
            } else if (pressedColon && s.equals("Q")) {
                save("EMPTY", 0);
                break;
            } else {
                pressedColon = false;
            }
        }
    }

    /** Creates a file in the save-files directory with fileData inside
     *
     *
     * @param fileData - the user's directional inputs,
     * @param saveSlot - what slot you want to insert the save into
     *                 choice from slot 0, 1, and 2
     * @throws IOException - if it can't find the directory (don't worry about this)
     * @source <a href="https://www.digitalocean.com/community/tutorials/java-create-new-file">...</a>
     */
    public void save(String fileData, int saveSlot) {
        String fileSeparator = System.getProperty("file.separator");
        String path = "byow" + fileSeparator + "core" + fileSeparator + "saved.txt";
        File file = new File(path);
        //creates file
        try {
            if (file.createNewFile()) {
                System.out.println("saved.txt File Created in Project root directory");
            } else {
                System.out.println("File saved.txt has been overriden");
            }
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(fileData.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.out.println("IOExcepetion when calling save");
        }
    }

    /**
     *
     * @param saveSlot - slot where file is stored
     * @return save file data
     */
    public String load(int saveSlot) {
        return parseSave();
    }

    public String getHover() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (y >= HEIGHT || y < 0 || x < 0 || x >= WIDTH) {
            return "nothing?";
        }
        TETile temp = currentWorld.getWorld()[x][y];
        if (!lightsOn) {
            if (x < player.getX() + 4 && x > player.getX() - 4 && y < player.getY() + 4 && y > player.getY() - 4) {
                if (temp.equals(Tileset.FLOOR)) {
                    return "floor";
                } else if (temp.equals(Tileset.WALL)) {
                    return "wall";
                } else if (temp.equals(Tileset.LOCKED_DOOR)) {
                    return "goal";
                } else if (temp.equals(Tileset.TREE)) {
                    return "enemy";
                } else if (temp.equals(Tileset.AVATAR)) {
                    return "avatar";
                }
            } else {
                return "nothing?";
            }
        } else {
            if (temp.equals(Tileset.FLOOR)) {
                return "floor";
            } else if (temp.equals(Tileset.WALL)) {
                return "wall";
            } else if (temp.equals(Tileset.LOCKED_DOOR)) {
                return "goal";
            } else if (temp.equals(Tileset.TREE)) {
                return "enemy";
            } else if (temp.equals(Tileset.AVATAR)) {
                return "avatar";
            }
        }
        return "nothing";
    }
    public boolean getLights() {
        return lightsOn;
    }
    public Player getPlayer() {
        return player;
    }

    public Screen getScreen() {
        return screen;
    }
    public int getLevel() {
        return level;
    }
    private void cpg() {
        currentWorld = new WorldAssembler(seed, WIDTH, HEIGHT, screen);
        player = currentWorld.getPlayer();
        goal = currentWorld.getGoal();
    }
    private void renderWorldandHUD() {
        ter.chooseRender(lightsOn, currentWorld.getWorld(), player);
        screen.HUD((double) player.getHealth() / 5, level, getHover());
    }
}
