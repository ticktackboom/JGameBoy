import com.meadowsapps.jgameboy.core.uint.uint8;

/**
 * Created by Dylan on 2/6/17.
 */
public class UIntTest {
    public static void main(String[] args) {
        uint8 register = new uint8();
        System.out.println(register);

        for (int i = 0; i < uint8.MAX_VALUE + 1; i++) {
            register = register.dec();
            System.out.println(register);
        }
    }
}
