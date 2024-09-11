package backend.academy.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
    SPORTS("Sports"), FRUITS("Fruits"), ANIMALS("Animals"), COUNTRIES("Countries");
    private final String category;

    @Override
    public String toString() {
        return category;
    }
}
