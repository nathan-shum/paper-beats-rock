package byow.Core;

import byow.TileEngine.TETile;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

import static com.google.common.truth.Truth.assertThat;

public class Tests {

    @Test
    public void testingSave() throws IOException {
        Engine e = new Engine();
        String randomString = generateRandomString(100000);
        for (int i = 0; i < 100000; i++) {
            e.save(randomString, 1);
            assertThat(e.load(1)).isEqualTo(randomString);
        }
    }

    private static String generateRandomString(int length) {
        Random random = new Random();
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    @Test

    public void agTest() throws IOException{

        Engine e1 = new Engine();

        Engine e2 = new Engine();

        TETile[][] result = e1.interactWithInputString("n2838278388919144292ss");

        e2.interactWithInputString("n2838278388919144292ss:q");

        TETile[][] result2 = e2.interactWithInputString("l");

        assertThat(result2).isEqualTo(result);

    }
}

