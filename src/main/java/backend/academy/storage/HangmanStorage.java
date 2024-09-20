package backend.academy.storage;

import java.util.List;

public final class HangmanStorage {

    private static final List<String> HANGMAN_IMAGES = List.of(
        """
            ____________________
            |
            |
            |
            |
            |
            |
            |
            |""",
        """
            ____________________
            |         |
            |
            |
            |
            |
            |
            |
            |""",
        """
            ____________________
            |         |
            |         |
            |
            |
            |
            |
            |
            |""",
        """
            ____________________
            |         |
            |         |
            |         |
            |
            |
            |
            |
            |""",
        """
            ____________________
            |         |
            |         |
            |         |
            |         O
            |
            |
            |
            |
            """,
        """
            ____________________
            |         |
            |         |
            |         |
            |         O
            |         |
            |
            |
            |""",
        """
            ____________________
            |         |
            |         |
            |         |
            |         O
            |        /|
            |
            |
            |""",
        """
            ____________________
            |         |
            |         |
            |         |
            |         O
            |        /|\\
            |
            |
            |""",
        """
            ____________________
            |         |
            |         |
            |         |
            |         O
            |        /|\\
            |        /
            |
            |""",
        """
            ____________________
            |         |
            |         |
            |         |
            |         O
            |        /|\\
            |        / \\
            |
            |"""
    );

    private HangmanStorage() {
    }

    public static List<String> getHangmanImages() {
        return HANGMAN_IMAGES;
    }
}
