import com.meadowsapps.jgameboy.core.util.UInt16
import com.meadowsapps.jgameboy.core.util.UInt8
import com.meadowsapps.jgameboy.gbc.GbcMmu
import org.junit.Test

/**
 * Created by dmeadows on 3/5/17.
 */
class MmuTest {
    @Test
    void testMmu() {
        GbcMmu mmu = new GbcMmu();
        mmu.writeByte(new UInt16(0xFF70), new UInt8(0x58))
        assert mmu.readByte(new UInt16(0xFF70)).intValue() == 0x58
    }
}
