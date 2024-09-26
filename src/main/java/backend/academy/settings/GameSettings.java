package backend.academy.settings;

import java.math.BigDecimal;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameSettings {
    public static final BigDecimal MAX_ATTEMPTS_COUNT = new BigDecimal("10");
    public static final BigDecimal IMAGES_COUNT = new BigDecimal("10");
    public static final int MAX_WORD_LENGTH = 15;
    public static final int NUMBER_SCALE = 3;
}
