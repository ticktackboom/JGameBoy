package com.meadowsapps.jgameboy.gbc.core;

import com.meadowsapps.jgameboy.core.AbstractCartridge;
import com.meadowsapps.jgameboy.gbc.core.mbc.GbcMbcFactory;
import com.meadowsapps.jgameboy.gbc.core.mbc.MemoryBankController;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by dmeadows on 2/2/2017.
 */
public class GbcCartridge extends AbstractCartridge {

    private GbcCartridgeHeader header;

    private MemoryBankController mbc;

    private byte[] contents;

    public GbcCartridge(GbcCore core) {
        super(core);
    }

    @Override
    public void load(File file) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        contents = new byte[(int) raf.length()];
        raf.readFully(contents);

        this.header = new GbcCartridgeHeader(contents);
        GbcMbcFactory factory = GbcMbcFactory.getFactory();
        int type = header.getCartridgeType();
        int romSize = header.getRomSize();
        int ramSize = header.getRamSize();
        this.mbc = factory.getMbc(type, romSize, ramSize);
        this.mbc.setCartridge(this);
    }

    @Override
    public int read(int addr) {
        return mbc.read(addr);
    }

    @Override
    public void write(int value, int addr) {
        mbc.write(value, addr);
    }

    byte[] getContents() {
        return contents;
    }
}
