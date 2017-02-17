import com.meadowsapps.jgameboy.core.CoreFactory;
import com.meadowsapps.jgameboy.core.CoreType;
import com.meadowsapps.jgameboy.core.EmulatorCore;

import java.io.File;
import java.net.URL;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class RomReader {
    public static void main(String[] args) throws Exception {
        URL url = RomReader.class.getClassLoader().getResource("gbc/Tetris (World).gb");
        File rom = new File(url.toURI());

        EmulatorCore core = CoreFactory.getFactory().getCore(CoreType.GAMEBOY);
        core.cartridge().load(rom);
        core.start();

        while (core.isRunning()) {
            // wait
        }
    }

    static void print(int value) {
        String binary = Integer.toBinaryString(value);
        binary = String.format("%16s", binary).replace(' ', '0');
        StringBuilder builder = new StringBuilder(binary);
        for (int i = builder.length() - 4; i > 0; i -= 4) {
            builder.insert(i, ' ');
        }

        System.out.println(builder.toString());
        int hi = value >> 8;
        System.out.println(hi);   // hi
        int lo = value & 0xFF;
        System.out.println(lo); // lo
        System.out.println((hi << 8) + lo);
    }
}
