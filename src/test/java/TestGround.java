import com.meadowsapps.jgameboy.core.Register8Bit;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class TestGround {
    public static void main(String[] args) {
        bitTest();
    }

    static void bitFlip() {
        Register8Bit r = new Register8Bit();
        System.out.println(r.read());
        r.flip(0);
        System.out.println(r.read());
        r.flip(4);
        System.out.println(r.read());
    }

    static void bitShift() {
        int value = 65535;

        int left = value;
        int right = value;
        for (int i = 0; i < 20; i++) {
            left = (left << 1) & 65535;
            System.out.printf("LEFT: %d\n", left);
            right = (right >> 1) & 65535;
            System.out.printf("RIGHT: %d\n", right);
            System.out.println();
        }
    }

    static void add() {
        int value1 = 65520;
        int value2 = 35;
        int sum = value1 + value2;
        System.out.println(sum & 0xFFFF);
    }

    static void subtract() {
        int value1 = 15;
        int value2 = 20;
        int difference = value1 - value2;
        System.out.println();
    }

    static void bitTest() {
        Register8Bit r = new Register8Bit();
        r.set(0, 1);
        System.out.println(r.read());
        r.set(1, 1);
        System.out.println(r.read());
        r.set(5, 1);
        System.out.println(r.read());
        r.set(1, 0);
        System.out.println(r.read());
        r.set(1, 0);
        System.out.println(r.read());
        r.set(0, 1);
        System.out.println(r.read());
        r.set(12, 1);
        System.out.println(r.read());
    }
}
