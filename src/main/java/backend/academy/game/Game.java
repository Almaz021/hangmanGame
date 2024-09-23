package backend.academy.game;

import backend.academy.entity.Word;
import backend.academy.enums.Category;
import backend.academy.enums.Difficulty;
import backend.academy.settings.GameSettings;
import backend.academy.storage.WordStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Game {
    @Getter private GameSession gameSession;
    private final WordStorage wordStorage;
    @Getter private GameInterface gameInterface;
    private final BufferedReader reader;
    private final SecureRandom random;

    public Game(
        WordStorage wordStorage,
        GameInterface gameInterface,
        BufferedReader reader,
        SecureRandom random,
        boolean initNow
    ) {
        this.wordStorage = wordStorage;
        this.gameInterface = gameInterface;
        this.reader = reader;
        this.random = random;

        if (initNow) {
            try {
                initGame();
            } catch (Exception e) {
                log.error("Error during game initialization", e);
            }
        }
    }

    public final void initGame() {
        Category category;
        Difficulty difficulty;
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

        } catch (IOException | RuntimeException e) {
            log.error(e.getMessage());
        }

    }

    public void initGameSession(Word word, Set<Character> wordSet) {
        if (word.word().length() > GameSettings.MAX_WORD_LENGTH) {
            throw new RuntimeException("too long word");
        }
        gameSession = new GameSession(word, wordSet);
    }

    public int startGame() {
        try {
            while (isGameActive()) {
                gameInterface.guessLetter();
                String letter = reader.readLine();

                if (!isValidInput(letter)) {
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

                if (gameSession.currAttemptsCount() + 1 == GameSettings.MAX_ATTEMPTS_COUNT) {
                    gameInterface.showHint(gameSession.word());
                }
            }
            if (checkGameOver(true)) {
                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 1;
    }

    private boolean isGameActive() {
        return GameSettings.MAX_ATTEMPTS_COUNT > gameSession.currAttemptsCount() && !(checkGameOver(false));
    }

    public Word chooseWord(Difficulty difficulty, Category category) {
        List<Word> filteredWords = wordStorage.getWords().stream()
            .filter(word -> word.difficulty() == difficulty && word.category() == category)
            .toList();

        return filteredWords.get(random.nextInt(filteredWords.size()));
    }

    public boolean isValidInput(String letter) {
        if (letter == null || !letter.matches("^[a-zA-Z]+$")) {
            gameInterface.incorrectSymbols();
            return false;
        }
        if (letter.length() > 1) {
            gameInterface.incorrectInputLength();
            return false;
        }
        return true;
    }

    private void handleGuess(char letter) {
        if (gameSession.answer().contains(letter)) {
            gameInterface.goodAttempt();
        } else {
            gameInterface.badAttempt(gameSession().currAttemptsCount());
        }
    }

    private boolean checkGameOver(boolean print) {
        if ((gameSession.getCurrLetters().containsAll(gameSession.answer()))) {
            if (print) {
                gameInterface.winGame();
            }
            return true;
        } else {
            if (print) {
                gameInterface.loseGame();
            }
            return false;
        }
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
