package com.meadowsapps.jgameboy.gbc.core.cartridge.mbc;

import com.meadowsapps.jgameboy.gbc.core.cartridge.GbcCartridge;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Created by dmeadows on 2/3/2017.
 */
public class GbcMbc5 extends AbstractGbcMbc {

    private int romSize;

    private int[] romBank0;

    private int[][] romBanks;

    public GbcMbc5(GbcCartridge cartridge) {
        super(cartridge);
    }

    @Override
    public void initialize(byte[] contents) {
        romSize = cartridge().getHeader().getRomSize();
        int romBankCount = cartridge().getHeader().getRomBankCount();
        romBank0 = new int[0x4000];
        romBanks = new int[romBankCount][0x4000];

        int bank = 0;
        for (int i = 0; i < contents.length; i += 0x4000) {
            // store buffer as int[] to proper rom bank
            byte[] current = Arrays.copyOfRange(contents, i, i + 0x4000);
            IntBuffer intBuf = ByteBuffer.wrap(current).asIntBuffer();
            int[] buffer = new int[intBuf.remaining()];
            intBuf.get(buffer);
            if (bank == 0) {
                romBank0 = buffer;
            } else {
                romBanks[bank] = buffer;
            }
            bank++;
        }
    }

    @Override
    public int read(int addr) {
        return 0;
    }

    @Override
    public void write(int value, int addr) {
    }
}
