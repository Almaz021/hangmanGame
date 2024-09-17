package backend.academy;

import backend.academy.settings.GameSettings;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameInterface {
    private final List<String> hangman;
    private static final PrintWriter WRITER = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

    public void helloMessage() {
        printMessage(
            "Hello, Player! Welcome to the Hangman game! Choose the category and the difficulty of the word.");
    }

    public void chooseCategory() {
        printMessage("""
            Category:
            1. Sports
            2. Fruits
            3. Animals
            4. Countries
            Type number of category you want to choose or other symbol to choose random""");

    }

    public void chooseDifficulty() {
        printMessage("""
            Difficulty:
            1. Easy
            2. Medium
            3. Hard
            Type number of difficulty you want to choose or other symbol to choose random""");
    }

    public void chosenWord() {
        printMessage("Word is chosen! Game starts now! So, guess a letter:");
    }

    public void incorrectAttempt() {
        printMessage("You need to use only english letters! Try again!");
    }

    public void goodAttempt() {
        printMessage("You're Right! Here is the updated word");
    }

    public void badAttempt() {
        printMessage("Oh, no! Do you really want him to hang?!");
    }

    public void guessLetter() {
        printMessage("Guess next letter: ");
    }

    public void showHint(Word word) {
        printMessage("Your last chance! Here is the hint to this word: " + word.hint());
    }

    public void winGame() {
        printMessage("YOU WON! GOOD GAME!");
    }

    public void loseGame() {
        printMessage("YOU LOST! Bye, Hangman!");
    }

    public void drawHangman(int attemptsCount) {
        int stage =
            (int) Math.floor((double) GameSettings.IMAGES_COUNT / GameSettings.MAX_ATTEMPTS_COUNT * attemptsCount);
        printMessage(hangman.get(stage));
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
        String currentWord = String.join(" ", list);

        printMessage(currentWord);
    }

    private void printMessage(String msg) {
        WRITER.println(msg);
    }

}
