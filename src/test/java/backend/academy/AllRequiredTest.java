package backend.academy;

import backend.academy.entity.Word;
import backend.academy.enums.Category;
import backend.academy.enums.Difficulty;
import backend.academy.game.Game;
import backend.academy.game.GameInterface;
import backend.academy.storage.HangmanStorage;
import backend.academy.storage.WordStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllRequiredTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        WordStorage wordStorage = new WordStorage();
        GameInterface gameInterface = new GameInterface(HangmanStorage.getHangmanImages());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        SecureRandom random = new SecureRandom();
        game = new Game(wordStorage, gameInterface, reader, random);
    }

    // Напишите тесты для проверки правильности выбора слова из списка.
    @Test
    public void checkCorrectWordChoosing() {
        Category category = Category.SPORTS;
        Difficulty difficulty = Difficulty.EASY;

        Word word = game.chooseWord(difficulty, category);

        assertEquals(category, word.category());
        assertEquals(difficulty, word.difficulty());
    }

    // Проверьте корректность отображения состояния игры после каждого ввода пользователя.
    @Test
    public void checkGameCategorySelection() {
        Category category = game.selectCategory("1");
        Category category1 = game.selectCategory("2");
        Category category2 = game.selectCategory("3");
        Category category3 = game.selectCategory("4");
        Category category4 = game.selectCategory("a");
        Category category5 = game.selectCategory(null);

        assertEquals(Category.SPORTS, category);
        assertEquals(Category.FRUITS, category1);
        assertEquals(Category.ANIMALS, category2);
        assertEquals(Category.COUNTRIES, category3);
        assertThat(category4).isIn(Category.SPORTS, Category.ANIMALS, Category.FRUITS, Category.COUNTRIES);
        assertThat(category5).isIn(Category.SPORTS, Category.ANIMALS, Category.FRUITS, Category.COUNTRIES);

    }

    @Test
    public void checkGameDifficultySelection() {
        Difficulty difficulty = game.selectDifficulty("1");
        Difficulty difficulty1 = game.selectDifficulty("2");
        Difficulty difficulty2 = game.selectDifficulty("3");
        Difficulty difficulty3 = game.selectDifficulty("a");
        Difficulty difficulty4 = game.selectDifficulty(null);

        assertEquals(Difficulty.EASY, difficulty);
        assertEquals(Difficulty.MEDIUM, difficulty1);
        assertEquals(Difficulty.HARD, difficulty2);
        assertThat(difficulty3).isIn(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD);
        assertThat(difficulty4).isIn(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD);

    }

    @Test
    public void testGoodGameStates() {
        Word word = new Word(
            "apple",
            "A common red or green fruit that keeps the doctor away",
            Difficulty.EASY,
            Category.FRUITS);

        game.initGameSession(word, Set.of('a', 'p', 'l', 'e'));
        game.initGameInterface();

        game.gameSession().updateLetters('a');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('a'), game.gameSession().getCurrLetters());
        assertEquals(0, game.gameSession().currAttemptsCount());
        assertEquals("a _ _ _ _", game.gameInterface().currMessage());

        game.gameSession().updateLetters('p');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('a', 'p'), game.gameSession().getCurrLetters());
        assertEquals(0, game.gameSession().currAttemptsCount());
        assertEquals("a p p _ _", game.gameInterface().currMessage());

        game.gameSession().updateLetters('l');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('a', 'p', 'l'), game.gameSession().getCurrLetters());
        assertEquals(0, game.gameSession().currAttemptsCount());
        assertEquals("a p p l _", game.gameInterface().currMessage());

        game.gameSession().updateLetters('e');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('a', 'p', 'l', 'e'), game.gameSession().getCurrLetters());
        assertEquals(0, game.gameSession().currAttemptsCount());
        assertEquals("a p p l e", game.gameInterface().currMessage());

    }

    @Test
    public void testBadGameStates() {
        Word word = new Word(
            "apple",
            "A common red or green fruit that keeps the doctor away",
            Difficulty.EASY,
            Category.FRUITS);
        game.initGameSession(word, Set.of('a', 'p', 'l', 'e'));
        game.initGameInterface();

        game.gameSession().updateLetters('q');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q'), game.gameSession().getCurrLetters());
        assertEquals(1, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().getFirst(), game.gameInterface().currMessage());

        game.gameSession().updateLetters('w');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q', 'w'), game.gameSession().getCurrLetters());
        assertEquals(2, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().get(1), game.gameInterface().currMessage());

        game.gameSession().updateLetters('r');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q', 'w', 'r'), game.gameSession().getCurrLetters());
        assertEquals(3, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().get(2), game.gameInterface().currMessage());

        game.gameSession().updateLetters('t');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q', 'w', 'r', 't'), game.gameSession().getCurrLetters());
        assertEquals(4, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().get(3), game.gameInterface().currMessage());

        game.gameSession().updateLetters('y');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q', 'w', 'r', 't', 'y'), game.gameSession().getCurrLetters());
        assertEquals(5, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().get(4), game.gameInterface().currMessage());

        game.gameSession().updateLetters('u');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q', 'w', 'r', 't', 'y', 'u'), game.gameSession().getCurrLetters());
        assertEquals(6, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().get(5), game.gameInterface().currMessage());

        game.gameSession().updateLetters('i');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q', 'w', 'r', 't', 'y', 'u', 'i'), game.gameSession().getCurrLetters());
        assertEquals(7, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().get(6), game.gameInterface().currMessage());

        game.gameSession().updateLetters('o');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q', 'w', 'r', 't', 'y', 'u', 'i', 'o'), game.gameSession().getCurrLetters());
        assertEquals(8, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().get(7), game.gameInterface().currMessage());

        game.gameSession().updateLetters('s');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q', 'w', 'r', 't', 'y', 'u', 'i', 'o', 's'), game.gameSession().getCurrLetters());
        assertEquals(9, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().get(8), game.gameInterface().currMessage());

        game.gameSession().updateLetters('d');
        game.gameInterface().word(game.gameSession().word(), game.gameSession().getCurrLetters());
        assertEquals(Set.of('q', 'w', 'r', 't', 'y', 'u', 'i', 'o', 's', 'd'), game.gameSession().getCurrLetters());
        assertEquals(10, game.gameSession().currAttemptsCount());
        assertEquals("_ _ _ _ _", game.gameInterface().currMessage());

        game.gameInterface().drawHangman(game.gameSession().currAttemptsCount());
        assertEquals(HangmanStorage.getHangmanImages().get(9), game.gameInterface().currMessage());

    }

    // Убедитесь, что введенные буквы корректно обрабатываются вне зависимости от их регистра.
    @Test
    public void checkCaseInsensitive() {
        boolean result1 = game.isValidInput("F");
        boolean result2 = game.isValidInput("f");

        assertTrue(result1);
        assertTrue(result2);
    }

    // Игра не запускается, если загадываемое слово имеет некорректную длину.
    @Test
    public void tooLongWord() {
        Word word = new Word("abcdefghijklmnop", "", Difficulty.EASY, Category.SPORTS);

        Throwable exception = assertThrows(RuntimeException.class, () -> game.initGameSession(word, Set.of('c')));
        assertEquals("too long word", exception.getMessage());
    }

    @Test
    public void correctLengthWord() {
        Word word = new Word("abcdef", "", Difficulty.EASY, Category.SPORTS);

        assertDoesNotThrow(() -> game.initGameSession(word, Set.of('c')));
    }

    // После превышения заданного количества попыток игра всегда возвращает поражение.
    @Test
    public void maxAttemptsTest() throws IOException {
        Word word = new Word(
            "apple",
            "A common red or green fruit that keeps the doctor away",
            Difficulty.EASY,
            Category.FRUITS);

        game.initGameSession(word, Set.of('a', 'p', 'l', 'e'));
        game.initGameInterface();

        game.gameSession().updateLetters('q');
        game.gameSession().updateLetters('w');
        game.gameSession().updateLetters('r');
        game.gameSession().updateLetters('t');
        game.gameSession().updateLetters('y');
        game.gameSession().updateLetters('u');
        game.gameSession().updateLetters('i');
        game.gameSession().updateLetters('o');
        game.gameSession().updateLetters('s');
        game.gameSession().updateLetters('d');

        assertEquals(-1, game.startGame());

    }

    // Состояние игры корректно изменяется при угадывании/не угадывании.
    @Test
    public void correctStateTest() {
        Word word = new Word(
            "apple",
            "A common red or green fruit that keeps the doctor away",
            Difficulty.EASY,
            Category.FRUITS);

        game.initGameSession(word, Set.of('a', 'p', 'l', 'e'));
        game.initGameInterface();

        game.gameSession().updateLetters('a');
        assertEquals(0, game.gameSession().currAttemptsCount());
        assertEquals(Set.of('a'), game.gameSession().getCurrLetters());

        game.gameSession().updateLetters('a');
        assertEquals(0, game.gameSession().currAttemptsCount());
        assertEquals(Set.of('a'), game.gameSession().getCurrLetters());

        game.gameSession().updateLetters('c');
        assertEquals(1, game.gameSession().currAttemptsCount());
        assertEquals(Set.of('a', 'c'), game.gameSession().getCurrLetters());

    }

    // Проверка, что при отгадывании ввод строки длиной больше чем 1 (опечатка) приводит к повторному вводу, без изменения состояния.
    @Test
    public void checkCorrectInput() {
        boolean result1 = game.isValidInput("d");
        boolean result2 = game.isValidInput("2");
        boolean result3 = game.isValidInput("");
        boolean result4 = game.isValidInput(null);

        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertFalse(result4);
    }

    @Test
    public void checkTooLongInput() {
        boolean result1 = game.isValidInput("dd");
        boolean result2 = game.isValidInput("DD");

        assertFalse(result1);
        assertFalse(result2);
    }
}
