import com.meadowsapps.jgameboy.core.CoreFactory;
import com.meadowsapps.jgameboy.core.CoreType;
import com.meadowsapps.jgameboy.core.Cpu;
import com.meadowsapps.jgameboy.core.EmulatorCore;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * Created by dmeadows on 1/13/2017.
 */
public class RomReader {
    public static void main(String[] args) throws Exception {
        URL url = RomReader.class.getClassLoader().getResource("gbc/DMG_ROM.bin");
        File rom = new File(url.toURI());

        EmulatorCore core = CoreFactory.getFactory().getCore(CoreType.GAMEBOY);
        Cpu cpu = core.getCpu();
        byte[] buffer = new byte[3];
        RandomAccessFile raf = new RandomAccessFile(rom, "r");
        long start = System.currentTimeMillis();
        try {
            for (int i = 0; i <= raf.length(); ) {
                raf.seek(i);
                raf.read(buffer);

                int opcode = Byte.toUnsignedInt(buffer[0]);
                int operand1 = Byte.toUnsignedInt(buffer[1]);
                int operand2 = Byte.toUnsignedInt(buffer[2]);

                if (opcode == 0xDD) {
                    System.exit(-1);
                }

                String hex = String.format("%2s", Integer.toHexString(opcode));
                hex = "0x" + ((hex.replace(" ", "0")).toUpperCase());
                System.out.printf("OFFSET:   %d\n", i);
                System.out.printf("OPCODE:   %s\n", hex);
                System.out.printf("OPERAND1: %d\n", operand1);
                System.out.printf("OPERAND2: %d\n", operand2);
                System.out.println();

                int length = cpu.execute(opcode, operand1, operand2);
                i += length;
            }
            System.out.println("completed");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long stop = System.currentTimeMillis();
            System.out.println(stop - start);
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
