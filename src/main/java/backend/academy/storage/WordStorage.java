package backend.academy.storage;

import backend.academy.Word;
import backend.academy.enums.Category;
import backend.academy.enums.Difficulty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordStorage {

    private final List<Word> words;

    public WordStorage() {
        words = new ArrayList<>();
        initializeWords();
    }

    private void initializeWords() {
        words.add(new Word(
            "football",
            "A popular team sport played with a round ball",
            Difficulty.EASY,
            Category.SPORTS));
        words.add(new Word(
            "badminton",
            "A racket sport played with a shuttlecock",
            Difficulty.MEDIUM,
            Category.SPORTS));
        words.add(new Word(
            "kayaking",
            "A water sport that involves paddling a small boat",
            Difficulty.HARD,
            Category.SPORTS));

        words.add(new Word(
            "apple",
            "A common red or green fruit that keeps the doctor away",
            Difficulty.EASY,
            Category.FRUITS));
        words.add(new Word(
            "pineapple",
            "A tropical fruit with spiky skin and sweet yellow flesh",
            Difficulty.MEDIUM,
            Category.FRUITS));
        words.add(new Word(
            "blackcurrant",
            "A small dark berry used to make jams and juices",
            Difficulty.HARD,
            Category.FRUITS));

        words.add(new Word(
            "horse",
            "A large, domesticated animal used for riding",
            Difficulty.EASY,
            Category.ANIMALS));
        words.add(new Word(
            "elephant",
            "The largest land animal, known for its trunk",
            Difficulty.MEDIUM,
            Category.ANIMALS));
        words.add(new Word(
            "porcupine",
            "A spiny rodent that can defend itself with sharp quills",
            Difficulty.HARD,
            Category.ANIMALS));

        words.add(new Word(
            "russia",
            "The largest country in the world by area",
            Difficulty.EASY,
            Category.COUNTRIES));
        words.add(new Word(
            "switzerland",
            "A European country known for its neutrality and the Alps",
            Difficulty.MEDIUM,
            Category.COUNTRIES));
        words.add(new Word(
            "bangladesh",
            "A South Asian country known for its rivers and high population density",
            Difficulty.HARD,
            Category.COUNTRIES));
    }

    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }
}
