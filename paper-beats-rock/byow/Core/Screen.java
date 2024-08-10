package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import javax.xml.stream.events.Characters;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Screen {

    private int screenWidth;
    private int screenHeight;
    private Font defaultFont;
    private Color backgroundColor;
    private Color defaultTextColor;
    private final ArrayList<Character> DIGITS = digitsList();
    private final Font HUDFONT = new Font("Monaco", Font.BOLD, 20);

    public Screen(int screenWidth, int screenHeight, Font defaultFont, Color backgroundColor, Color defaultTextColor) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.defaultFont = defaultFont;
        this.backgroundColor = backgroundColor;
        this.defaultTextColor = defaultTextColor;
        initializeScreen();
    }

    public Screen() {
        this.screenWidth = 80;
        this.screenHeight = 40;
        this.defaultFont = new Font("Monaco", Font.BOLD, 30);;
        this.backgroundColor = Color.BLACK;
        this.defaultTextColor = Color.WHITE;
        initializeScreen();
        //StdDraw.enableDoubleBuffering();
    }

    private void initializeScreen() {
        StdDraw.setCanvasSize(screenWidth * 16, screenHeight * 16);
        StdDraw.clear(backgroundColor);
        StdDraw.setXscale(0, screenWidth);
        StdDraw.setYscale(0, screenHeight);
        StdDraw.enableDoubleBuffering();
    }

    private ArrayList<Character> digitsList() {
        ArrayList<Character> dl = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dl.add(Character.forDigit(i, 10));
        }
        return dl;
    }

    public void insertCenteredText(String text) {
        insertCenteredText(text, defaultFont, defaultTextColor);
    }

    public void insertCenteredText(String text, Font font, Color textColor) {
        insertText(text, font, textColor, screenWidth / 2, screenHeight / 2);
    }

    public void insertText(String text, int x, int y) {
        insertText(text, defaultFont, defaultTextColor, x, y);
    }

    public void insertUpperRightText(String text) {
        insertUpperRightText(text, defaultFont, defaultTextColor);
    }

    public void insertUpperRightText(String text, Font font, Color textColor) {
        StdDraw.setPenColor(textColor);
        StdDraw.setFont(font);
        StdDraw.textRight(screenWidth, screenHeight - 1, text);
        StdDraw.show();
    }

    public void insertUpperLeftText(String text) {
        insertUpperLeftText(text, defaultFont, defaultTextColor);
    }

    public void insertUpperLeftText(String text, Font font, Color textColor) {
        StdDraw.setPenColor(textColor);
        StdDraw.setFont(font);
        StdDraw.textLeft(0, screenHeight - 1, text);
        StdDraw.show();
    }

    public void insertLowerLeftText(String text) {
        insertLowerLeftText(text, defaultFont, defaultTextColor);
    }

    public void insertLowerLeftText(String text, Font font, Color textColor) {
        StdDraw.setPenColor(textColor);
        StdDraw.setFont(font);
        StdDraw.textLeft(0, 0, text);
        StdDraw.show();
    }

    public void insertLowerRightText(String text) {
        insertLowerRightText(text, defaultFont, defaultTextColor);
    }

    public void insertLowerRightText(String text, Font font, Color textColor) {
        StdDraw.setPenColor(textColor);
        StdDraw.setFont(font);
        StdDraw.textRight(screenWidth, 0, text);
        StdDraw.show();
    }

    public void insertText(String text, Font font, Color textColor, int x, int y) {
        StdDraw.setPenColor(textColor);
        StdDraw.setFont(font);
        StdDraw.text(x, y, text);
        StdDraw.show();
    }

    public void menu() {
        StdDraw.clear(backgroundColor);
        Font menuFont = new Font("Monaco", Font.BOLD, 20);
        insertText("CS61B: THE GAME", screenWidth / 2, screenHeight - 10);
        insertText("New Game (N)", menuFont, Color.white, screenWidth / 2, screenHeight - 15);
        insertText("Load Game (L)", menuFont, Color.white, screenWidth / 2, screenHeight - 20);
        insertText("Quit (Q)", menuFont, Color.white, screenWidth / 2, screenHeight - 25);
    }

    public void newWorldScreen(int stageNumber) {
        StdDraw.clear(backgroundColor);
        insertText("Congrats!", screenWidth / 2, 22);
        insertText("You beat level " + stageNumber + "!", screenWidth / 2, 18);
    }

    private void seedScreen(String seed) {
        StdDraw.clear(backgroundColor);
        insertText("Insert Seed:", screenWidth / 2, 22);
        insertText(seed, screenWidth / 2, 18);
    }

    public String liveSeedScreen() {
        seedScreen("");
        String seed = "";
        char lastChar;
        boolean inputtedSeed = false;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                lastChar = StdDraw.nextKeyTyped();
                if ((lastChar == 'S' || lastChar == 's') && inputtedSeed) {
                    return seed;
                } else if (DIGITS.contains(lastChar)){
                    seed += lastChar;
                    seedScreen(seed);
                    inputtedSeed = true;
                }
            }
        }
    }
    public char battleScreen1() {
        StdDraw.clear(backgroundColor);
        insertText("BATTLE TIME", screenWidth / 2, 24);
        insertText("CHOOSE:", screenWidth / 2, 20);
        insertText("ROCK (R)\t\t\tPAPER (P)\t\t\tSCISSORS (X)", screenWidth / 2, 16);
        StdDraw.pause(1000);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                String s = "" + c;
                s = s.toUpperCase();
                if (s.equals("R") || s.equals("P") || s.equals("X")) {
                    return c;
                }
            }
        }
    }
    public void battleScreen2(String playerChoice, String enemyChoice) {
        StdDraw.clear(backgroundColor);
        insertText("YOU CHOSE: " + playerChoice, screenWidth / 2, 22);
        StdDraw.pause(1000);
        insertText("ENEMY CHOSE: " + enemyChoice, screenWidth / 2, 18);
        StdDraw.show();
    }
    public void tie() {
        StdDraw.clear(backgroundColor);
        insertText("TIE", screenWidth / 2, 22);
        StdDraw.show(1000);
    }
    public void win() {
        StdDraw.clear(backgroundColor);
        insertText("YOU WIN :)", screenWidth / 2, 22);
        StdDraw.show(1000);
    }
    public void loss() {
        StdDraw.clear(backgroundColor);
        insertText("YOU LOST :(", screenWidth / 2, 22);
        insertText("-1 HP", screenWidth / 2, 18);
        StdDraw.show(1000);
    }

    public void gameOver() {
        StdDraw.clear(backgroundColor);
        StdDraw.setFont(defaultFont);
        insertText("GAME OVER", screenWidth / 2, 24);
        insertText("Play Again? (P)\t\t\tQuit (:Q)", screenWidth / 2, 20);
        StdDraw.show();
    }

    /** HUD creates and displays a screen that displays the
     * HP, current level, name of hovered tile, and the controls (displayed below)
     * Note for nathan bby <3: the two bars (above and below) probably take up two tiles each
     *
     * @param healthPercentage - ratio of HP user has left
     *                         (throws IllegalArgumentException if healthPercentage < 0 or healthPercentage > 1)
     * @param currentLevel - level number to be displayed in top center
     * @param tileName - tile name ot be displayed on top right
     */
    public void HUD(double healthPercentage, int currentLevel, String tileName) {
        if (healthPercentage < 0 || healthPercentage > 1) {
            throw new IllegalArgumentException("first argument must be between 0 and 1 inclusive");
        }
        blackBars();
        whiteBorders();
        HUDText(healthPercentage, currentLevel, tileName);
        StdDraw.show();
    }


    private void HUDText(double healthPercentage, int currentLevel, String tileName) {
        StdDraw.setFont(HUDFONT);
        healthBar(healthPercentage);
        //level text
        StdDraw.text(screenWidth / 2, screenHeight - 1.1, "Level " + currentLevel);
        //tile text
        StdDraw.textRight(screenWidth - 1, screenHeight - 1.1, tileName);
        //restart text
        StdDraw.textLeft(1, 0.9, "Restart (R)");
        //toggle lights text
        StdDraw.textRight(screenWidth - 1, 0.9, "Toggle Lights (L)");
        //save and quit text
        StdDraw.text(screenWidth / 2, 0.9, "Save and Quit (:Q)");
    }

    private void filledRectangleLeft(double x, double y, double width, double height) {
        StdDraw.filledRectangle(x + (width / 2), y, (width / 2), height);
    }

    private void rectangleLeft(double x, double y, double width, double height) {
        StdDraw.rectangle(x + (width / 2), y, (width / 2), height);
    }

    private void healthBar(double healthPercent) {
        StdDraw.textLeft(0.5, screenHeight - 1.1, "HP");
        //Hp bar
        StdDraw.setPenColor(Color.RED);
        filledRectangleLeft(2.5, screenHeight - 0.95, 8 * healthPercent, .55);
        //border of hp bar
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setPenRadius(0.004);
        rectangleLeft(2.5, screenHeight - 0.95, 8, .55);
    }

    private void whiteBorders() {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setPenRadius(0.005);
        StdDraw.line(0, screenHeight - 2, screenWidth, screenHeight - 2);
        StdDraw.line(0, 2, screenWidth, 2);
    }

    private void blackBars() {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(screenWidth / 2, screenHeight - 1, screenWidth / 2, 1);
        StdDraw.filledRectangle(screenWidth / 2, 1, screenWidth / 2, 1);
        StdDraw.show();
    }

    public static void main(String[] args) {
        Screen scr = new Screen();
        StdDraw.clear(Color.MAGENTA);
        scr.gameOver();
    }
}