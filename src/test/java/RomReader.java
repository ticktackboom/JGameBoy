import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class RomReader {
    public static void main(String[] args) throws Exception {
        URL url = RomReader.class.getClassLoader().getResource("DMG_ROM.bin");
        File rom = new File(url.toURI());

        RandomAccessFile raf = new RandomAccessFile(rom, "r");
        raf.seek(4);
        byte[] buffer = new byte[3];
        raf.read(buffer);
        System.out.printf("OPCODE:   %d\n", Byte.toUnsignedInt(buffer[0]));
        System.out.printf("OPERAND1: %d\n", Byte.toUnsignedInt(buffer[2]));
        System.out.printf("OPERAND2: %d\n", Byte.toUnsignedInt(buffer[1]));
        System.out.println();

        int value = 0x9fff;
        print(value);
        value = 256;
        print(value);
        value = 513;
        print(value);
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
