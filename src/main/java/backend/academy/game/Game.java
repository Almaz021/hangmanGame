package backend.academy.game;

import backend.academy.entity.Word;
import backend.academy.enums.Category;
import backend.academy.enums.Difficulty;
import backend.academy.settings.GameSettings;
import backend.academy.storage.HangmanStorage;
import backend.academy.storage.WordStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

public class Game {
    @Getter private GameSession gameSession;
    private final WordStorage wordStorage;
    @Getter private GameInterface gameInterface;
    private final BufferedReader reader;
    private final SecureRandom random;

    public Game(WordStorage wordStorage, GameInterface gameInterface, BufferedReader reader, SecureRandom random) {
        this.wordStorage = wordStorage;
        this.gameInterface = gameInterface;
        this.reader = reader;
        this.random = random;
    }

    public void initGame() {
        Category category;
        Difficulty difficulty;
        initGameInterface();
        gameInterface.helloMessage();

        try {
            gameInterface.chooseCategory();
            String ctgr = reader.readLine();
            category = selectCategory(ctgr);

            gameInterface.chooseDifficulty();
            String dfclt = reader.readLine();
            difficulty = selectDifficulty(dfclt);

            Word word = chooseWord(difficulty, category);
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

    public void initGameSession(Word word, Set<Character> wordSet) {
        if (word.word().length() > GameSettings.MAX_WORD_LENGTH) {
            throw new RuntimeException("too long word");
        }
        gameSession = new GameSession(word, wordSet);
    }

    public void initGameInterface() {
        gameInterface = new GameInterface(HangmanStorage.getHangmanImages(),
            new PrintWriter(System.out, true, StandardCharsets.UTF_8));
    }

    public int startGame() throws IOException {
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
                return 0;
            }

            if (gameSession.currAttemptsCount() + 1 == GameSettings.MAX_ATTEMPTS_COUNT) {
                gameInterface.showHint(gameSession.word());
            }
        }
        gameInterface.loseGame();
        return -1;
    }

    public Word chooseWord(Difficulty difficulty, Category category) {
        List<Word> filteredWords = wordStorage.getWords().stream()
            .filter(word -> word.difficulty() == difficulty && word.category() == category)
            .toList();

        return filteredWords.get(random.nextInt(filteredWords.size()));
    }

    public boolean isValidInput(String letter) {
        return letter != null && checkIsLetter(letter.toLowerCase());
    }

    private void handleGuess(char letter) {
        if (gameSession.answer().contains(letter)) {
            gameInterface.goodAttempt();
        } else {
            gameInterface.badAttempt(gameSession().currAttemptsCount());
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

    public Category selectCategory(String ctgr) {
        if (ctgr == null) {
            return randomCategory();
        }

        return switch (ctgr) {
            case "1" -> Category.SPORTS;
            case "2" -> Category.FRUITS;
            case "3" -> Category.ANIMALS;
            case "4" -> Category.COUNTRIES;
            default -> randomCategory();
        };
    }

    public Difficulty selectDifficulty(String dfclt) {
        if (!(dfclt == null)) {
            return switch (dfclt) {
                case "1" -> Difficulty.EASY;
                case "2" -> Difficulty.MEDIUM;
                case "3" -> Difficulty.HARD;
                default -> randomDifficulty();

            };
        } else {
            return randomDifficulty();
        }
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
