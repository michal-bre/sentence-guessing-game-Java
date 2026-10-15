package game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Loads the sentences file and keeps the sentences in a map by difficulty level.
 * Key: 1 = easy (up to 8 chars), 2 = medium (9-15 chars), 3 = hard (16-20 chars).
 */
public class SentenceRepository {

    public static final int EASY = 1;
    public static final int MEDIUM = 2;
    public static final int HARD = 3;

    private final Map<Integer, List<String>> sentencesByLevel = new HashMap<>();
    private final Random random = new Random();

    public SentenceRepository() {
        sentencesByLevel.put(EASY, new ArrayList<>());
        sentencesByLevel.put(MEDIUM, new ArrayList<>());
        sentencesByLevel.put(HARD, new ArrayList<>());
    }

    /**
     * Reads the file line by line and puts every sentence in the list of its level.
     */
    public void load(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String sentence = line.trim();
                if (sentence.isEmpty()) {
                    continue;
                }
                int level = levelOf(sentence);
                if (level == 0) {
                    System.out.println("Skipping sentence longer than 20 chars: " + sentence);
                    continue;
                }
                sentencesByLevel.get(level).add(sentence);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Sentences file not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading sentences file: " + e.getMessage());
        }
    }

    /**
     * Returns the difficulty level for a sentence, or 0 if it is longer than 20 chars.
     */
    public static int levelOf(String sentence) {
        int length = sentence.length();
        if (length <= 8) {
            return EASY;
        }
        if (length <= 15) {
            return MEDIUM;
        }
        if (length <= 20) {
            return HARD;
        }
        return 0;
    }

    /**
     * Returns the sentences of the given level (empty list if the level is unknown).
     */
    public List<String> getSentences(int level) {
        return sentencesByLevel.getOrDefault(level, new ArrayList<>());
    }

    /**
     * Picks a random sentence of the given level by drawing a random index.
     * Returns null if there are no sentences for this level.
     */
    public String getRandomSentence(int level) {
        List<String> sentences = getSentences(level);
        if (sentences.isEmpty()) {
            return null;
        }
        int index = random.nextInt(sentences.size());
        return sentences.get(index);
    }
}
