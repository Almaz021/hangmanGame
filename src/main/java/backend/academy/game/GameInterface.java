package backend.academy.game;

import backend.academy.entity.Word;
import backend.academy.settings.GameSettings;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameInterface {
    private final List<String> hangman;
    @Getter private String currMessage;
    private static final PrintWriter WRITER = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

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

    public void incorrectAttempt() {
        currMessage = "You need to use only english letters! Try again!";
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

    public void badAttempt() {
        currMessage = "Oh, no! Do you really want him to hang?!";
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
        int stage =
            (int) Math.floor((double) GameSettings.IMAGES_COUNT / GameSettings.MAX_ATTEMPTS_COUNT * attemptsCount);
        currMessage = hangman.get(Math.max(0, stage - 1));
        printMessage(currMessage);
    }

    public void word(Word word, Set<Character> letters) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < word.word().length(); i++) {
            if (letters.contains(word.word().charAt(i))) {
                list.add(String.valueOf(word.word().charAt(i)));
            } else {
                list.add("_");
            }
        }
        currMessage = String.join(" ", list);
        printMessage(currMessage);
    }

    private void printMessage(String msg) {
        WRITER.println(msg);
    }

}
