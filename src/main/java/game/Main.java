package game;

import java.util.Scanner;

public class Main {

    private static final String SENTENCES_FILE = "sentences.txt";
    private static final String RECORD_FILE = "record.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        SentenceRepository repository = new SentenceRepository();
        repository.load(SENTENCES_FILE);

        RecordManager recordManager = new RecordManager(RECORD_FILE);

        System.out.println("=== Guess the Sentence ===");

        int level = readLevel(scanner);
        String sentence = repository.getRandomSentence(level);
        if (sentence == null) {
            System.out.println("No sentences available for this level.");
            scanner.close();
            return;
        }

        Game game = new Game(sentence);
        while (!game.isSolved()) {
            System.out.println();
            System.out.println(game.getMaskedSentence());
            char letter = readLetter(scanner);

            if (game.wasGuessed(letter)) {
                System.out.println("You already guessed '" + letter + "'.");
                continue;
            }

            if (game.guess(letter)) {
                System.out.println("Good guess!");
            } else {
                System.out.println("The letter '" + letter + "' is not in the sentence.");
            }
        }

        System.out.println();
        System.out.println(game.getSentence());
        System.out.println("You won! Number of attempts: " + game.getAttempts());

        if (recordManager.updateIfRecord(game.getAttempts())) {
            System.out.println("This is the lowest number of attempts so far - new record!");
        } else {
            System.out.println("Current record: " + recordManager.readRecord() + " attempts.");
        }

        scanner.close();
    }

    /**
     * Asks the user for a difficulty level until a valid one (1-3) is entered.
     */
    private static int readLevel(Scanner scanner) {
        while (true) {
            System.out.print("Choose difficulty (1 = easy, 2 = medium, 3 = hard): ");
            String input = scanner.nextLine().trim();
            try {
                int level = Integer.parseInt(input);
                if (level >= SentenceRepository.EASY && level <= SentenceRepository.HARD) {
                    return level;
                }
            } catch (NumberFormatException e) {
                // not a number - fall through to the error message
            }
            System.out.println("Please enter 1, 2 or 3.");
        }
    }

    /**
     * Asks the user for a single letter until a valid one is entered.
     */
    private static char readLetter(Scanner scanner) {
        while (true) {
            System.out.print("Enter a letter: ");
            String input = scanner.nextLine().trim();
            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                return input.charAt(0);
            }
            System.out.println("Please enter exactly one letter.");
        }
    }
}
