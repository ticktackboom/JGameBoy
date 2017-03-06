import com.meadowsapps.jgameboy.core.util.Register16Bit
import com.meadowsapps.jgameboy.core.util.Register8Bit
import org.junit.Test

/**
 * Created by dmeadows on 3/5/17.
 */
class RegisterTest {

    @Test
    void testRegister8Bit() {

    }

    @Test
    void testRegister16Bit() {
        Register16Bit r = new Register16Bit()
        r.word = 0xFF70
        assert r.word.intValue() == 0xFF70
        assert r.hi.intValue() == 0xFF
        assert r.lo.intValue() == 0x70
        r.hi++
        assert r.word.intValue() == 0x0070
        assert r.hi.intValue() == 0x00
        assert r.lo.intValue() == 0x70
    }
}
