package game;

import java.util.HashSet;
import java.util.Set;

/**
 * State of a single game: the hidden sentence, the letters guessed so far and the attempt count.
 */
public class Game {

    private final String sentence;
    private final Set<Character> guessedLetters = new HashSet<>();
    private int attempts;

    public Game(String sentence) {
        this.sentence = sentence;
    }

    /**
     * Returns the sentence where every letter not yet guessed is shown as '_'.
     * Characters are separated by a space so the lines are easy to count,
     * and a gap between words is shown as a wider space.
     * Punctuation is shown as it is.
     */
    public String getMaskedSentence() {
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (c == ' ') {
                masked.append("  ");
            } else if (Character.isLetter(c) && !wasGuessed(c)) {
                masked.append("_ ");
            } else {
                masked.append(c).append(' ');
            }
        }
        return masked.toString().trim();
    }

    /**
     * Registers a guess (case-insensitive) and counts it as an attempt.
     * Returns true if the letter appears in the sentence.
     */
    public boolean guess(char letter) {
        char lower = Character.toLowerCase(letter);
        guessedLetters.add(lower);
        attempts++;
        return sentence.toLowerCase().indexOf(lower) >= 0;
    }

    /**
     * Returns true when every letter of the sentence has been revealed.
     */
    public boolean isSolved() {
        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (Character.isLetter(c) && !wasGuessed(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean wasGuessed(char letter) {
        return guessedLetters.contains(Character.toLowerCase(letter));
    }

    public int getAttempts() {
        return attempts;
    }

    public String getSentence() {
        return sentence;
    }
}
