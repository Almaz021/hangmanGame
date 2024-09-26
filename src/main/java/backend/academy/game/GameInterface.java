package backend.academy.game;

import backend.academy.entity.Word;
import backend.academy.settings.GameSettings;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameInterface {
    private final List<String> hangman;
    @Getter private String currMessage;
    private final PrintWriter writer;

    public void helloMessage() {
        currMessage = "Hello, Player! Welcome to the Hangman game! Choose the category and the difficulty of the word.";
        printMessage(currMessage);
    }

    public void chooseCategory() {
        currMessage = """
            Category:
            1. Sports
            2. Fruits
            3. Animals
            4. Countries
            Type number of category you want to choose or other symbol to choose random""";
        printMessage(currMessage);

    }

    public void chooseDifficulty() {
        currMessage = """
            Difficulty:
            1. Easy
            2. Medium
            3. Hard
            Type number of difficulty you want to choose or other symbol to choose random""";
        printMessage(currMessage);
    }

    public void chosenWord() {
        currMessage = "Word is chosen! Game starts now! So, guess a letter:";
        printMessage(currMessage);
    }

    public void incorrectSymbols() {
        currMessage = "You need to use only english letters! Try again!";
        printMessage(currMessage);
    }

    public void incorrectInputLength() {
        currMessage = "You need to use only one english letter! Try again!";
        printMessage(currMessage);
    }

    public void sameLetter() {
        currMessage = "This letter is already used. Try again!";
        printMessage(currMessage);
    }

    public void showMaxAttemptsCount() {
        currMessage = "You have " + GameSettings.MAX_ATTEMPTS_COUNT + " attempts";
        printMessage(currMessage);
    }

    public void goodAttempt() {
        currMessage = "You're Right! Here is the updated word";
        printMessage(currMessage);
    }

    public void badAttempt(int attemptsCount) {
        BigDecimal currentAttempts = new BigDecimal(attemptsCount);
        currMessage = """
            Oh, no! Do you really want him to hang?!
            Current attempts:\s""" + (GameSettings.MAX_ATTEMPTS_COUNT.subtract(currentAttempts));
        printMessage(currMessage);
    }

    public void guessLetter() {
        currMessage = "Guess next letter: ";
        printMessage(currMessage);
    }

    public void showHint(Word word) {
        currMessage = "Your last chance! Here is the hint to this word: " + word.hint();
        printMessage(currMessage);
    }

    public void winGame() {
        currMessage = "YOU WON! GOOD GAME!";
        printMessage(currMessage);
    }

    public void loseGame() {
        currMessage = "YOU LOST! Bye, Hangman!";
        printMessage(currMessage);
    }

    public void drawHangman(int attemptsCount) {
        BigDecimal currentAttempts = new BigDecimal(attemptsCount);

        BigDecimal stage =
            GameSettings.IMAGES_COUNT.divide(GameSettings.MAX_ATTEMPTS_COUNT, GameSettings.NUMBER_SCALE,
                RoundingMode.UP).multiply(currentAttempts);
        BigDecimal stageIndex = stage.max(BigDecimal.ONE).subtract(BigDecimal.ONE);

        currMessage = hangman.get(stageIndex.intValue());
        printMessage(currMessage);
    }

    public void word(Word word, Set<Character> letters) {
        currMessage = word.word()
            .chars()
            .mapToObj(i -> letters.contains((char) i) ? String.valueOf((char) i) : "_")
            .collect(Collectors.joining(" "));
        printMessage(currMessage);
    }

    private void printMessage(String msg) {
        writer.println(msg);
    }

}
