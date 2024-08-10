package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import javax.imageio.IIOException;
import java.awt.*;
import java.io.IOException;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    //added this myself
    private static final Screen screen = new Screen();
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static final Font menuFont = new Font("Monaco", Font.BOLD, 20);

    private static final Font TILEFONT =  new Font("Monaco", Font.BOLD, 14);

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 40);
        StdDraw.setFont(TILEFONT);
        screen.menu();

        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            Engine engine = new Engine();
            TETile[][] world = engine.interactWithInputString(args[1]);
            ter.chooseRender(engine.getLights(), world, engine.getPlayer());
            while (true) {
                engine.getScreen().HUD((double) engine.getPlayer().getHealth() / 5, engine.getLevel(), engine.getHover());
                StdDraw.pause(13);
            }
        } else {
            Engine engine = new Engine();
            engine.interactWithKeyboard();
        }
    }
}
