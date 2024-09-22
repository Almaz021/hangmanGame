package backend.academy;

import backend.academy.game.Game;
import backend.academy.game.GameInterface;
import backend.academy.storage.HangmanStorage;
import backend.academy.storage.WordStorage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    private static Game game;

    public static void main(String[] args) {
        init();
        start();
    }

    private void init() {
        game = new Game(
            new WordStorage(),
            new GameInterface(HangmanStorage.getHangmanImages(),
                new PrintWriter(System.out, true, StandardCharsets.UTF_8)),
            new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)),
            new SecureRandom());
    }

    private void start() {
        game.initGame();
        game.startGame();
    }
}
