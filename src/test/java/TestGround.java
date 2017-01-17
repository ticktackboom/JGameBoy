import com.meadowsapps.jgameboy.core.Register8Bit;

/**
 * Created by dmeadows on 1/17/2017.
 */
public class TestGround {
    public static void main(String[] args) {
        bitFlip();
    }

    static void bitFlip() {
        Register8Bit r = new Register8Bit();
        System.out.println(r.read());
        r.flip(0);
        System.out.println(r.read());
        r.flip(4);
        System.out.println(r.read());
    }
}
