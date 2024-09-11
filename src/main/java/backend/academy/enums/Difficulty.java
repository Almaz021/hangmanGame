package backend.academy.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Difficulty {
    EASY("Easy"), MEDIUM("Medium"), HARD("Hard");
    private final String difficulty;

    @Override
    public String toString() {
        return difficulty;
    }
}
