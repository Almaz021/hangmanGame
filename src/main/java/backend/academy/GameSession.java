package backend.academy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public class GameSession {
    @Getter private final Word word;
    @Getter private final Set<Character> answer;
    private Set<Character> currLetters;
    @Getter private int currAttemptsCount;

    public GameSession(Word word, Set<Character> answer) {
        this.word = word;
        this.answer = Collections.unmodifiableSet(answer);
        initGameSession();
    }

    private void initGameSession() {
        currLetters = new HashSet<>();
        currAttemptsCount = 0;
    }

    public boolean updateLetters(Character c) {
        if (currLetters.contains(c)) {
            return false;
        }
        currLetters.add(c);
        incrementCurrAttemptsCount();
        return true;
    }

    public Set<Character> getCurrLetters() {
        return Collections.unmodifiableSet(currLetters);
    }

    private void incrementCurrAttemptsCount() {
        currAttemptsCount++;
    }
}
