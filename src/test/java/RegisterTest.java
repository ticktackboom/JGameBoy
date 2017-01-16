import com.meadowsapps.jgameboy.Register;
import com.meadowsapps.jgameboy.Register16Bit;
import com.meadowsapps.jgameboy.Register8Bit;
import com.meadowsapps.jgameboy.RegisterSizeException;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class RegisterTest {
    public static void main(String[] args) {
        Register B = new Register8Bit();
        Register C = new Register8Bit();
        Register SP = new Register16Bit();

        System.out.printf("B:7 = %d\n", B.get(7));
        System.out.printf("B = %d\n", B.read());
        B.set(7, 1);
        System.out.printf("B:7 = %d\n", B.get(7));
        System.out.printf("B = %d\n\n", B.read());

        System.out.printf("B:7 = %d\n", B.get(7));
        System.out.printf("B = %d\n", B.read());
        B.set(7, 0);
        System.out.printf("B:7 = %d\n", B.get(7));
        System.out.printf("B = %d\n\n", B.read());

        C.set(0, 1);
        System.out.printf("C = %d\n", C.read());
        for (int i = 0; i < 7; i++) {
            System.out.printf("Shifting LEFT by %d...\n", 1);
            C.shift(Register.LEFT, 1);
            System.out.printf("C = %d\n", C.read());
        }

        throw new RegisterSizeException(RegisterSizeException.BIT);
    }

    static String toBinary(int value) {
        String binary = Integer.toBinaryString(value);
        binary = String.format("%8s", binary).replace(' ', '0');
        StringBuilder builder = new StringBuilder(binary);
        for (int i = builder.length() - 4; i > 0; i -= 4) {
            builder.insert(i, ' ');
        }
        return builder.toString();
    }
}
