package backend.academy;

import java.io.IOException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    private static Game game;

    public static void main(String[] args) throws IOException {
        init();
        start();
    }

    private void init() {
        game = new Game();
    }

    private void start() throws IOException {
        game.initGame();
        game.startGame();
    }
}
