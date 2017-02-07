import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;
import com.meadowsapps.jgameboy.gbc.core.GbcCartridgeHeader;
import com.meadowsapps.jgameboy.gbc.core.GbcCore;

import java.io.File;
import java.net.URL;

/**
 * Created by dmeadows on 2/2/2017.
 */
public class CartridgeTest {
    public static void main(String[] args) throws Exception {
        URL resource = CartridgeTest.class.getClassLoader().getResource("gbc/Tetris (World).gb");
        File rom = new File(resource.toURI());

        GbcCartridge cartridge = new GbcCartridge(new GbcCore());
        cartridge.load(rom);
        System.out.println();
    }
}