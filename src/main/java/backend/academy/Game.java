package backend.academy;

import backend.academy.enums.Category;
import backend.academy.enums.Difficulty;
import backend.academy.settings.GameSettings;
import backend.academy.storage.HangmanStorage;
import backend.academy.storage.WordStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private GameSession gameSession;
    private final WordStorage wordStorage = new WordStorage();
    private GameInterface gameInterface;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    private final SecureRandom random = new SecureRandom();

    public void initGame() {
        Category category;
        Difficulty difficulty;
        initGameInterface();
        gameInterface.helloMessage();

        try {
            gameInterface.chooseCategory();
            String ctgr = reader.readLine();
            if (!(ctgr == null)) {
                category = switch (ctgr) {
                    case "1" -> Category.SPORTS;
                    case "2" -> Category.FRUITS;
                    case "3" -> Category.ANIMALS;
                    case "4" -> Category.COUNTRIES;
                    default -> randomCategory();
                };
            } else {
                category = randomCategory();
            }

            gameInterface.chooseDifficulty();
            String dfclt = reader.readLine();
            if (!(dfclt == null)) {
                difficulty = switch (dfclt) {
                    case "1" -> Difficulty.EASY;
                    case "2" -> Difficulty.MEDIUM;
                    case "3" -> Difficulty.HARD;
                    default -> randomDifficulty();

                };
            } else {
                difficulty = randomDifficulty();
            }

            List<Word> filteredWords = wordStorage.getWords().stream()
                .filter(word -> word.difficulty() == difficulty && word.category() == category)
                .toList();

            Word word = filteredWords.get(random.nextInt(filteredWords.size()));

            Set<Character> uniqueCharacters = new HashSet<>(word.word().length());
            for (char c : word.word().toCharArray()) {
                uniqueCharacters.add(c);
            }

            initGameSession(word, uniqueCharacters);
            gameInterface.chosenWord();
            gameInterface.showMaxAttemptsCount();
            gameInterface.word(gameSession.word(), gameSession.getCurrLetters());

        } catch (IOException e) {
            throw new RuntimeException("Unexpected error occurred while reading input", e);
        }

    }

    private void initGameSession(Word word, Set<Character> wordSet) {
        gameSession = new GameSession(word, wordSet);
    }

    private void initGameInterface() {
        gameInterface = new GameInterface(HangmanStorage.getHangmanImages());
    }

    public void startGame() throws IOException {
        while (GameSettings.MAX_ATTEMPTS_COUNT > gameSession.currAttemptsCount()) {
            gameInterface.guessLetter();
            String letter = reader.readLine();

            if (!isValidInput(letter)) {
                gameInterface.incorrectAttempt();
                continue;
            }

            letter = letter.toLowerCase();
            char guessedChar = letter.charAt(0);

            if (gameSession.updateLetters(guessedChar)) {
                handleGuess(guessedChar);
                if (gameSession.currAttemptsCount() != 0) {
                    gameInterface.drawHangman(gameSession.currAttemptsCount());
                }
                gameInterface.word(gameSession.word(), gameSession.getCurrLetters());
            } else {
                gameInterface.sameLetter();
            }

            if (checkGameOver()) {
                return;
            }

            if (gameSession.currAttemptsCount() + 1 == GameSettings.MAX_ATTEMPTS_COUNT) {
                gameInterface.showHint(gameSession.word());
            }
        }
        gameInterface.loseGame();
    }

    private boolean isValidInput(String letter) {
        return letter != null && checkIsLetter(letter.toLowerCase());
    }

    private void handleGuess(char letter) {
        if (gameSession.answer().contains(letter)) {
            gameInterface.goodAttempt();
        } else {
            gameInterface.badAttempt();
        }
    }

    private boolean checkGameOver() {
        if ((gameSession.getCurrLetters().containsAll(gameSession.answer()))) {
            gameInterface.winGame();

            return true;
        }
        return false;
    }

    private boolean checkIsLetter(String l) {
        return l.matches("[a-z]");
    }

    private Category randomCategory() {
        return new Category[] {
            Category.SPORTS,
            Category.FRUITS,
            Category.ANIMALS,
            Category.COUNTRIES}[random.nextInt(Category.values().length)];
    }

    private Difficulty randomDifficulty() {
        return new Difficulty[] {
            Difficulty.EASY,
            Difficulty.MEDIUM,
            Difficulty.HARD}[random.nextInt(Difficulty.values().length)];
    }
}
