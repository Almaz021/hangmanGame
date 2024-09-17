package backend.academy.entity;

import backend.academy.enums.Category;
import backend.academy.enums.Difficulty;

public record Word(String word, String hint, Difficulty difficulty, Category category) {
}
