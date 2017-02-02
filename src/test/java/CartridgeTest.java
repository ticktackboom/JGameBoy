import com.meadowsapps.jgameboy.gbc.core.GbcCartridge;

import java.io.File;
import java.net.URL;

/**
 * Created by dmeadows on 2/2/2017.
 */
public class CartridgeTest {
    public static void main(String[] args) throws Exception {
        URL resource = CartridgeTest.class.getClassLoader().getResource("gbc/Pokemon Silver.gbc");
        File file = new File(resource.toURI());
        GbcCartridge.load(file);
    }
}
