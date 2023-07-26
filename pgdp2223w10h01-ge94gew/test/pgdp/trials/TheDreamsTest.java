package pgdp.trials;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.function.Function;

public class TheDreamsTest {
    //private final byte[] key = new byte[]{0x01, (byte)0x7F, (byte)0x00, 0x00};
    private final byte[] key = new byte[]{0x00, 0x10, 0xF};
    private final Function<byte[], Boolean> lock = key -> Arrays.equals(key, this.key);
    @Test
    @DisplayName("Test Lock 1")
    public void test01() {
        byte[] result = TrialOfTheDreams.lockPick(lock, 3);

        System.out.println("result: " + Arrays.toString(result));

        assertArrayEquals(this.key, result);
    }
    @Test
    @DisplayName("Test Lock 2")
    public void test02() {
        byte[] result = TrialOfTheDreams.lockPick(lock, 4);

        System.out.println("result: " + Arrays.toString(result));

        assertArrayEquals(this.key, result);
    }
    @Test
    @DisplayName("Test null")
    public void test03() {
        byte[] result = TrialOfTheDreams.lockPick(lock, 2);

        System.out.println("result: " + Arrays.toString(result));

        assertArrayEquals(null, result);
    }
}
