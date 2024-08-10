package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.Tileset;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class WorldTests {
    public static void main(String[] args) {
        int width = 6;
        int height = 6;
        int x = 31;
        int y = 7;
        String side = "top";
        World w = new World(80, 50);
        w.generateSpace(width, height, x, y, side);
        w.generateSpace(15, 4, 31, 11, "left");
        w.generateSpace(3, 15, 33, 7, "bottom");
        w.generateSpace(4, 7, 17, 10, "left");
        w.generateSpace(16, 3, 14, 9, "left");
        w.generateSpace(18, 4, 36, 11, "right");
        //w.getWorld()[19][21] = Tileset.FLOWER;
        //w.generateSpace(4, 20, 22, 23, "top");
        TERenderer ter = new TERenderer();
        ter.initialize(80, 50);
        ter.renderFrame(w.getWorld());
    }
}