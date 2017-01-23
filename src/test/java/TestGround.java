import com.meadowsapps.jgameboy.core.Register8Bit;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class TestGround {
    public static void main(String[] args) {
        bitTest2();
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

    static void bitTest1() {
        int value = 15;
        String binary = toBinary(value, 8, false);
        for (int i = 0; i < binary.length(); i++) {
            int tmp = value & ~(1 << i);
            System.out.println(toBinary(tmp, 8, false));
        }
    }

    static String toBinary(int value, int length, boolean space) {
        String binary = String.format("%" + length + "s", Integer.toBinaryString(value));
        binary = binary.replace(" ", "0");
        if (space) {
            StringBuilder builder = new StringBuilder(binary);
            for (int i = builder.length() - 1; i >= 0; i -= 4) {
                builder.insert(i, " ");
            }
            binary = builder.toString();
        }
        return binary;
    }

    static void bitTest2() {
        int value = Integer.parseInt("10010101", 2);
        System.out.println(toBinary(value, 8, false));
        int hi = value >> 4;
        int lo = value & 0xF;
        System.out.println(hi);
        System.out.println(lo);
        value = (lo << 4) + hi;
        System.out.println(toBinary(value, 8, false));
    }
}
