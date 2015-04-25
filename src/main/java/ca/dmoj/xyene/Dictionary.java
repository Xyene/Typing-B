package ca.dmoj.xyene;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dictionary container class.
 */
public class Dictionary {
    /**
     * An immutable container of all words known.
     */
    public static final List<String> WORDS = Collections.unmodifiableList(new ArrayList<String>() {{
        try {
            BufferedReader cin = new BufferedReader(
                    new InputStreamReader(Game.class.getClassLoader().getResourceAsStream("words.txt")));
            String line;
            while ((line = cin.readLine()) != null) {
                add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }});

    public static void init() {
        // Runs static block
    }

    /**
     * Fetches a random word.
     *
     * @return A random word in Dictionary#WORDS.
     */
    public static String getRandomWord() {
        return WORDS.get((int) (Math.random() * WORDS.size()));
    }
}
